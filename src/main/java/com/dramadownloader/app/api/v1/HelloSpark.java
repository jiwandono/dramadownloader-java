package com.dramadownloader.app.api.v1;

import com.dramadownloader.app.api.v1.monitor.LegacyMongoMonitor;
import com.dramadownloader.app.api.v1.monitor.LegacyMonitor;
import com.dramadownloader.common.component.CommonComponent;
import com.dramadownloader.common.component.MemcachedComponent;
import com.dramadownloader.scraper.component.ScraperComponent;
import com.dramadownloader.scraper.stream.AnimestvStreamScraper;
import com.dramadownloader.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.DramafirecomStreamScraper;
import com.dramadownloader.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.scraper.stream.DramatvStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import com.dramadownloader.scraper.stream.StreamScraperFactory;
import com.dramadownloader.scraper.stream.StreamScrapeResult;
import com.dramadownloader.scraper.file.DramauploadFileScraper;
import com.dramadownloader.scraper.file.EmbeddramaFileScraper;
import com.dramadownloader.scraper.file.GooglevideoFileScraper;
import com.dramadownloader.scraper.file.FileScraper;
import com.dramadownloader.scraper.file.FileScraperFactory;
import com.dramadownloader.scraper.file.FileScrapeResult;
import com.dramadownloader.scraper.file.Mp4UploadFileScraper;
import com.dramadownloader.scraper.file.StoragestreamingFileScraper;
import com.dramadownloader.scraper.file.VideouploadusFileScraper;
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

  private static final CommonComponent commonComponent = new CommonComponent();
  private static final MemcachedComponent memcachedComponent = new MemcachedComponent();
  private static final ScraperComponent scraperComponent = new ScraperComponent();

  private static final MemcachedClient memcachedClient = memcachedComponent.getMemcachedClient();
  private static final ObjectMapper objectMapper = commonComponent.getObjectMapper();

  private static final LegacyMonitor LEGACY_MONITOR = new LegacyMongoMonitor(commonComponent.getDbMonitor(), commonComponent.getMorphia());

  public static void main(String[] args) {
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

      FetchStreamsRequest apiRequest = objectMapper.readValue(request.body(), FetchStreamsRequest.class);
      FetchStreamsResponse apiResponse = new FetchStreamsResponse();

      String episodeUrl = apiRequest.getUrl().trim();

      // Check cache
      Object cachedObject = memcachedClient.get("fetchstreams_" + episodeUrl);
      if (cachedObject != null) {
        LOGGER.info("Found cached response for " + episodeUrl);
        FetchStreamsResponse cachedApiResponse = objectMapper.readValue((String) cachedObject, FetchStreamsResponse.class);
        LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), cachedApiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return objectMapper.writeValueAsString(cachedApiResponse);
      }

      StreamScraper streamScraper = scraperComponent.getStreamScraperFactory().getScraper(episodeUrl);
      if (streamScraper == null) {
        apiResponse.setStatus(FetchStreamsResponse.Status.UNSUPPORTED);
        LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return objectMapper.writeValueAsString(apiResponse);
      }

      StreamScrapeResult streamScrapeResult = streamScraper.scrape(episodeUrl);
      LOGGER.info("URL " + episodeUrl + " processed with status " + streamScrapeResult.getStatus());

      CountDownLatch countDownLatch = new CountDownLatch(streamScrapeResult.getStreams().size());
      for (StreamScrapeResult.Stream stream : streamScrapeResult.getStreams()) {
        final String streamName = stream.getStreamName();
        final String streamUrl = stream.getStreamUrl();
        LOGGER.info("Processing stream " + streamName + " - " + stream.getStreamUrl());

        final FileScraper fileScraper = scraperComponent.getFileScraperFactory().getScraper(streamUrl);
        if (fileScraper == null) {
          countDownLatch.countDown();
          continue;
        }

        commonComponent.getScheduledExecutorService().submit(() -> {
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
        for (FetchStreamsResponse.Link link : apiResponse.getLinks()) {
          if (link.getUrl().contains("fetch.dramadownloader.com")) {
            secondPriorityLinks.add(link);
          } else {
            firstPriorityLinks.add(link);
          }
        }

        if (firstPriorityLinks.size() > 0) {
          apiResponse.setLinks(firstPriorityLinks);
        } else {
          apiResponse.setLinks(secondPriorityLinks);
        }
      } else {
        apiResponse.setStatus(FetchStreamsResponse.Status.FAILED);
      }

      // Cache result
      if (apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK)) {
        memcachedClient.set("fetchstreams_" + episodeUrl, 3600, objectMapper.writeValueAsString(apiResponse));
      }

      LEGACY_MONITOR.onRequestProcessed(episodeUrl, System.currentTimeMillis(), apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));

      return objectMapper.writeValueAsString(apiResponse);
    });
  }

  private static void stats() {
    get("/v1/monitor/stats", (request, response) -> {
      response.type("application/json");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Vary", "Origin");

      long minTimestamp = System.currentTimeMillis() - 3600_000L;
      return objectMapper.writeValueAsString(LEGACY_MONITOR.getEntries(minTimestamp));
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
}
