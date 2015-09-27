package com.dramadownloader.app.api.v1;

import com.dramadownloader.app.api.v1.monitor.LegacyMongoMonitor;
import com.dramadownloader.app.api.v1.monitor.LegacyMonitor;
import com.dramadownloader.drama.fetch.episode.DramacoolcomEpisodePageScraper;
import com.dramadownloader.drama.fetch.episode.DramafirecomEpisodePageScraper;
import com.dramadownloader.drama.fetch.episode.EpisodePageScraper;
import com.dramadownloader.drama.fetch.episode.EpisodePageScraperFactory;
import com.dramadownloader.drama.fetch.episode.EpisodeScrapeResult;
import com.dramadownloader.drama.fetch.hosting.DramauploadHostingPageScraper;
import com.dramadownloader.drama.fetch.hosting.EmbeddramaHostingPageScraper;
import com.dramadownloader.drama.fetch.hosting.HostingPageScraper;
import com.dramadownloader.drama.fetch.hosting.HostingPageScraperFactory;
import com.dramadownloader.drama.fetch.hosting.HostingScrapeResult;
import com.dramadownloader.drama.fetch.hosting.Mp4uploadHostingPageScraper;
import com.dramadownloader.drama.fetch.hosting.StoragestreamingHostingPageScraper;
import com.dramadownloader.drama.fetch.hosting.VideouploadusHostingPageScraper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Morphia;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

public class HelloSpark {
  private static final Logger LOGGER = Logger.getLogger(HelloSpark.class);

  private static long FETCH_TIMEOUT_MSEC = 30000L;

  private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(16);
  private static final EpisodePageScraperFactory EPISODE_PAGE_SCRAPER_FACTORY = new EpisodePageScraperFactory();
  private static final HostingPageScraperFactory HOSTING_PAGE_SCRAPER_FACTORY = new HostingPageScraperFactory();

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final MongoClient MONGO_CLIENT = new MongoClient( "localhost" );
  private static final DB DB_MONITOR = MONGO_CLIENT.getDB("dramadownloader-monitor");
  private static final Morphia MORPHIA = new Morphia();

  private static final LegacyMonitor LEGACY_MONITOR = new LegacyMongoMonitor(DB_MONITOR, MORPHIA);

  private static MemcachedClient MEMCACHED_CLIENT;

  public static void main(String[] args) {
    initEpisodePageScraperFactory();
    initHostingPageScraperFactory();
    initObjectMapper();
    initMemcachedClient();

    helloWorld();
    fetchStreams();
    stats();
    passthru();
  }

  public static void helloWorld() {
    get("/", (request, response) -> "Hello World!");
  }

  public static void fetchStreams() {
    post("/v1/drama/fetchstreams", (request, response) -> {
      response.type("application/json");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Vary", "Origin");

      FetchStreamsRequest apiRequest = OBJECT_MAPPER.readValue(request.body(), FetchStreamsRequest.class);
      FetchStreamsResponse apiResponse = new FetchStreamsResponse();

      String episodeUrl = apiRequest.getUrl();

      // Check cache
      Object cachedObject = MEMCACHED_CLIENT.get("fetchstreams_" + episodeUrl);
      if (cachedObject != null) {
        LOGGER.info("Found cached response for " + episodeUrl);
        FetchStreamsResponse cachedApiResponse = OBJECT_MAPPER.readValue((String) cachedObject, FetchStreamsResponse.class);
        LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), cachedApiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return OBJECT_MAPPER.writeValueAsString(cachedApiResponse);
      }

      EpisodePageScraper episodePageScraper = EPISODE_PAGE_SCRAPER_FACTORY.getPageScraper(episodeUrl);
      if (episodePageScraper == null) {
        apiResponse.setStatus(FetchStreamsResponse.Status.UNSUPPORTED);
        LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return OBJECT_MAPPER.writeValueAsString(apiResponse);
      }

      EpisodeScrapeResult episodeScrapeResult = episodePageScraper.scrape(episodeUrl);
      LOGGER.info("URL " + episodeUrl + " processed with status " + episodeScrapeResult.getStatus());

      CountDownLatch countDownLatch = new CountDownLatch(episodeScrapeResult.getStreams().size());
      for (EpisodeScrapeResult.Stream stream : episodeScrapeResult.getStreams()) {
        final String streamName = stream.getStreamName();
        final String streamUrl = stream.getStreamUrl();
        LOGGER.info("Processing stream " + streamName + " - " + stream.getStreamUrl());

        final HostingPageScraper hostingPageScraper = HOSTING_PAGE_SCRAPER_FACTORY.getPageScraper(streamUrl);
        if (hostingPageScraper == null) {
          countDownLatch.countDown();
          continue;
        }

        SCHEDULED_EXECUTOR_SERVICE.submit(() -> {
          try {
            HostingScrapeResult hostingScrapeResult = hostingPageScraper.scrape(streamUrl);
            if (hostingScrapeResult.getStatus().equals(HostingScrapeResult.Status.OK)) {
              HostingScrapeResult.Downloadable downloadable = hostingScrapeResult.getDownloadables().get(0);
              synchronized (apiResponse.getLinks()) {
                FetchStreamsResponse.Link link = new FetchStreamsResponse.Link(streamName, downloadable.getDownloadUrl(), downloadable.isDirectLink());
                apiResponse.getLinks().add(link);
              }
            }
          } catch (Exception e) {
            // no-op
          } finally {
            countDownLatch.countDown();
          }
        });
      }
      countDownLatch.await(FETCH_TIMEOUT_MSEC, TimeUnit.MILLISECONDS);

      if (apiResponse.getLinks().size() > 0) {
        apiResponse.setStatus(FetchStreamsResponse.Status.OK);

        // Filter fetch.dramadownloader.com links
        List<FetchStreamsResponse.Link> firstPriorityLinks = new ArrayList<>();
        List<FetchStreamsResponse.Link> secondPriorityLinks = new ArrayList<>();
        for(FetchStreamsResponse.Link link : apiResponse.getLinks()) {
          if(link.getUrl().contains("fetch.dramadownloader.com")) {
            secondPriorityLinks.add(link);
          } else {
            firstPriorityLinks.add(link);
          }
        }

        if(firstPriorityLinks.size() > 0) {
          apiResponse.setLinks(firstPriorityLinks);
        } else {
          apiResponse.setLinks(secondPriorityLinks);
        }
      } else {
        apiResponse.setStatus(FetchStreamsResponse.Status.FAILED);
      }

      // Cache result
      if (apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK)) {
        MEMCACHED_CLIENT.set("fetchstreams_" + episodeUrl, 3600, OBJECT_MAPPER.writeValueAsString(apiResponse));
      }

      LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));

      return OBJECT_MAPPER.writeValueAsString(apiResponse);
    });
  }

  private static void stats() {
    get("/v1/monitor/stats", (request, response) -> {
      response.type("application/json");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Vary", "Origin");

      long minTimestamp = System.currentTimeMillis() - 3600_000L;
      return OBJECT_MAPPER.writeValueAsString(LEGACY_MONITOR.getEntries(minTimestamp));
    });
  }

  private static void passthru() {
    get("/passthru/:data", (request, response) -> {
      String data = request.params(":data");
      String targetUrl = new String(Base64.decodeBase64(data));
      URL url = new URL(targetUrl);
      String[] pathParts = url.getPath().split("/");
      String filename = pathParts[pathParts.length -1];

      response.type("application/octet-stream");
      response.header("Content-Disposition", "attachment; filename=" + filename);

      InputStream inputStream = url.openStream();
      OutputStream outputStream = response.raw().getOutputStream();
      byte[] buffer = new byte[4096];
      int n = -1;
      while ((n = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, n);
      }
      inputStream.close();

      return response.raw();
    });
  }

  // ==========

  private static void initEpisodePageScraperFactory() {
    DramacoolcomEpisodePageScraper dramacoolcomEpisodePageScraper = new DramacoolcomEpisodePageScraper();
    DramafirecomEpisodePageScraper dramafirecomEpisodePageScraper = new DramafirecomEpisodePageScraper();

    EPISODE_PAGE_SCRAPER_FACTORY.registerPageScraper(dramacoolcomEpisodePageScraper);
    EPISODE_PAGE_SCRAPER_FACTORY.registerPageScraper(dramafirecomEpisodePageScraper);
  }

  private static void initHostingPageScraperFactory() {
    DramauploadHostingPageScraper dramauploadHostingPageScraper = new DramauploadHostingPageScraper();
    EmbeddramaHostingPageScraper embeddramaHostingPageScraper = new EmbeddramaHostingPageScraper();
    Mp4uploadHostingPageScraper mp4uploadHostingPageScraper = new Mp4uploadHostingPageScraper();
    StoragestreamingHostingPageScraper storagestreamingHostingPageScraper = new StoragestreamingHostingPageScraper();
    VideouploadusHostingPageScraper videouploadusHostingPageScraper = new VideouploadusHostingPageScraper();

    HOSTING_PAGE_SCRAPER_FACTORY.registerScraper(dramauploadHostingPageScraper);
    HOSTING_PAGE_SCRAPER_FACTORY.registerScraper(embeddramaHostingPageScraper);
    HOSTING_PAGE_SCRAPER_FACTORY.registerScraper(mp4uploadHostingPageScraper);
    HOSTING_PAGE_SCRAPER_FACTORY.registerScraper(storagestreamingHostingPageScraper);
    HOSTING_PAGE_SCRAPER_FACTORY.registerScraper(videouploadusHostingPageScraper);
  }

  private static void initObjectMapper() {
    OBJECT_MAPPER
        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  private static void initMemcachedClient() {
    try {
      MEMCACHED_CLIENT = new MemcachedClient(
          new BinaryConnectionFactory(),
          AddrUtil.getAddresses("localhost:11211")
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
