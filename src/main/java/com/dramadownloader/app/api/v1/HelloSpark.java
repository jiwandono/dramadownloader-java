package com.dramadownloader.app.api.v1;

import com.dramadownloader.app.api.v1.monitor.LegacyMongoMonitor;
import com.dramadownloader.app.api.v1.monitor.LegacyMonitor;
import com.dramadownloader.drama.scraper.stream.AnimestvStreamScraper;
import com.dramadownloader.drama.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.drama.scraper.stream.DramafirecomStreamScraper;
import com.dramadownloader.drama.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.drama.scraper.stream.DramatvStreamScraper;
import com.dramadownloader.drama.scraper.stream.StreamScraper;
import com.dramadownloader.drama.scraper.stream.StreamScraperFactory;
import com.dramadownloader.drama.scraper.stream.StreamScrapeResult;
import com.dramadownloader.drama.scraper.file.DramauploadFileScraper;
import com.dramadownloader.drama.scraper.file.EmbeddramaFileScraper;
import com.dramadownloader.drama.scraper.file.GooglevideoFileScraper;
import com.dramadownloader.drama.scraper.file.FileScraper;
import com.dramadownloader.drama.scraper.file.FileScraperFactory;
import com.dramadownloader.drama.scraper.file.FileScrapeResult;
import com.dramadownloader.drama.scraper.file.Mp4UploadFileScraper;
import com.dramadownloader.drama.scraper.file.StoragestreamingFileScraper;
import com.dramadownloader.drama.scraper.file.VideouploadusFileScraper;
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
  private static final StreamScraperFactory STREAM_SCRAPER_FACTORY = new StreamScraperFactory();
  private static final FileScraperFactory FILE_SCRAPER_FACTORY = new FileScraperFactory();

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final MongoClient MONGO_CLIENT = new MongoClient( "localhost" );
  private static final DB DB_MONITOR = MONGO_CLIENT.getDB("dramadownloader-monitor");
  private static final Morphia MORPHIA = new Morphia();

  private static final LegacyMonitor LEGACY_MONITOR = new LegacyMongoMonitor(DB_MONITOR, MORPHIA);

  private static MemcachedClient MEMCACHED_CLIENT;

  public static void main(String[] args) {
    initStreamScraperFactory();
    initFileScraperFactory();
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

      String episodeUrl = apiRequest.getUrl().trim();

      // Check cache
      Object cachedObject = MEMCACHED_CLIENT.get("fetchstreams_" + episodeUrl);
      if (cachedObject != null) {
        LOGGER.info("Found cached response for " + episodeUrl);
        FetchStreamsResponse cachedApiResponse = OBJECT_MAPPER.readValue((String) cachedObject, FetchStreamsResponse.class);
        LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), cachedApiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return OBJECT_MAPPER.writeValueAsString(cachedApiResponse);
      }

      StreamScraper streamScraper = STREAM_SCRAPER_FACTORY.getScraper(episodeUrl);
      if (streamScraper == null) {
        apiResponse.setStatus(FetchStreamsResponse.Status.UNSUPPORTED);
        LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return OBJECT_MAPPER.writeValueAsString(apiResponse);
      }

      StreamScrapeResult streamScrapeResult = streamScraper.scrape(episodeUrl);
      LOGGER.info("URL " + episodeUrl + " processed with status " + streamScrapeResult.getStatus());

      CountDownLatch countDownLatch = new CountDownLatch(streamScrapeResult.getStreams().size());
      for (StreamScrapeResult.Stream stream : streamScrapeResult.getStreams()) {
        final String streamName = stream.getStreamName();
        final String streamUrl = stream.getStreamUrl();
        LOGGER.info("Processing stream " + streamName + " - " + stream.getStreamUrl());

        final FileScraper fileScraper = FILE_SCRAPER_FACTORY.getScraper(streamUrl);
        if (fileScraper == null) {
          countDownLatch.countDown();
          continue;
        }

        SCHEDULED_EXECUTOR_SERVICE.submit(() -> {
          try {
            FileScrapeResult fileScrapeResult = fileScraper.scrape(streamUrl);
            if (fileScrapeResult.getStatus().equals(FileScrapeResult.Status.OK)) {
              FileScrapeResult.File file = fileScrapeResult.getFiles().get(0);
              synchronized (apiResponse.getLinks()) {
                FetchStreamsResponse.Link link = new FetchStreamsResponse.Link(streamName, file.getDownloadUrl(), file.isDirectLink());
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

  private static void initStreamScraperFactory() {
    AnimestvStreamScraper animestvStreamScraper = new AnimestvStreamScraper();
    DramacoolcomStreamScraper dramacoolcomStreamScraper = new DramacoolcomStreamScraper();
    DramafirecomStreamScraper dramafirecomStreamScraper = new DramafirecomStreamScraper();
    DramaniceStreamScraper dramaniceStreamScraper = new DramaniceStreamScraper();
    DramatvStreamScraper dramatvStreamScraper = new DramatvStreamScraper();

    STREAM_SCRAPER_FACTORY.register(animestvStreamScraper);
    STREAM_SCRAPER_FACTORY.register(dramacoolcomStreamScraper);
    STREAM_SCRAPER_FACTORY.register(dramafirecomStreamScraper);
    STREAM_SCRAPER_FACTORY.register(dramaniceStreamScraper);
    STREAM_SCRAPER_FACTORY.register(dramatvStreamScraper);
  }

  private static void initFileScraperFactory() {
    DramauploadFileScraper dramauploadFileScraper = new DramauploadFileScraper();
    EmbeddramaFileScraper embeddramaFileScraper = new EmbeddramaFileScraper();
    GooglevideoFileScraper googlevideoFileScraper = new GooglevideoFileScraper();
    Mp4UploadFileScraper mp4uploadFileScraper = new Mp4UploadFileScraper();
    StoragestreamingFileScraper storagestreamingFileScraper = new StoragestreamingFileScraper();
    VideouploadusFileScraper videouploadusFileScraper = new VideouploadusFileScraper();

    FILE_SCRAPER_FACTORY.register(dramauploadFileScraper);
    FILE_SCRAPER_FACTORY.register(embeddramaFileScraper);
    FILE_SCRAPER_FACTORY.register(googlevideoFileScraper);
    FILE_SCRAPER_FACTORY.register(mp4uploadFileScraper);
    FILE_SCRAPER_FACTORY.register(storagestreamingFileScraper);
    FILE_SCRAPER_FACTORY.register(videouploadusFileScraper);
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
