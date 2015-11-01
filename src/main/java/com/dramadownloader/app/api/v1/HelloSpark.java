package com.dramadownloader.app.api.v1;

import com.dramadownloader.app.api.v1.monitor.LegacyMongoMonitor;
import com.dramadownloader.app.api.v1.monitor.LegacyMonitor;
import com.dramadownloader.app.api.v1.monitor.LegacyMonitorEntry;
import com.dramadownloader.common.component.CommonComponent;
import com.dramadownloader.common.component.MemcachedComponent;
import com.dramadownloader.common.component.VelocityComponent;
import com.dramadownloader.common.template.VelocityTemplateEngine;
import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.core.TitleAccessor;
import com.dramadownloader.core.TitleMongoAccessor;
import com.dramadownloader.core.model.Title;
import com.dramadownloader.scraper.component.ScraperComponent;
import com.dramadownloader.scraper.episode.EpisodeScrapeRequest;
import com.dramadownloader.scraper.episode.EpisodeScrapeResult;
import com.dramadownloader.scraper.episode.EpisodeScraper;
import com.dramadownloader.scraper.file.FileScrapeRequest;
import com.dramadownloader.scraper.stream.StreamScrapeRequest;
import com.dramadownloader.scraper.stream.StreamScraper;
import com.dramadownloader.scraper.stream.StreamScrapeResult;
import com.dramadownloader.scraper.file.FileScraper;
import com.dramadownloader.scraper.file.FileScrapeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.EscapeTool;
import spark.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class HelloSpark {
  private static final Logger LOGGER = Logger.getLogger(HelloSpark.class);

  private static long FETCH_TIMEOUT_MSEC = 30000L;

  private static final CommonComponent commonComponent = new CommonComponent();
  private static final MemcachedComponent memcachedComponent = new MemcachedComponent();
  private static final ScraperComponent scraperComponent = new ScraperComponent();
  private static final VelocityComponent velocityComponent = new VelocityComponent();

  private static final MemcachedClient memcachedClient = memcachedComponent.getMemcachedClient();
  private static final ObjectMapper objectMapper = commonComponent.getObjectMapper();
  private static final VelocityTemplateEngine templateEngine = velocityComponent.getVelocityTemplateEngine();

  private static final LegacyMonitor legacyMonitor = new LegacyMongoMonitor(commonComponent.getDbMonitor(), commonComponent.getMorphia());

  private static final TitleAccessor titleAccessor = new TitleMongoAccessor(commonComponent.getDbData(), commonComponent.getMorphia());

  public static void main(String[] args) {
    staticFileLocation("/public");

    index();
    list();
    detail();
    fetchStreams();
    fetchEpisodes();
    stats();
    passthru();
  }

  public static void index() {
    get("/", (request, response) -> {
      String renderResult;
      Object cachedResponse = memcachedClient.get("page_index");
      if(cachedResponse == null) {
        Map<String, Object> context = new HashMap<>();
        context.put("requestUrl", request.url());
        context.put("esc", new EscapeTool());
        context.put("topUrls", getTopN(5));
        renderResult = templateEngine.render(new ModelAndView(context, "view/index.vm"));
        memcachedClient.set("page_index", 60, renderResult);
      } else {
        renderResult = (String) cachedResponse;
      }

      return renderResult;
    });
  }

  public static void list() {
    get("/list/:prefix", (request, response) -> {
      String prefix = request.params(":prefix");
      if(prefix.length() != 1) {
        response.status(404);
        return "404 Not found";
      }

      char first = prefix.charAt(0);
      boolean queryOk = (first >= 'a' && first <= 'z') || first == '0';
      if(!queryOk) {
        response.status(404);
        return "404 Not found";
      }

      String renderResult;
      Object cachedResponse = memcachedClient.get("page_list_" + first);
      if(cachedResponse == null) {
        Map<String, Object> context = new HashMap<>();
        context.put("requestUrl", request.url());
        context.put("esc", new EscapeTool());
        context.put("stringUtil", StringUtil.class);
        context.put("title", "List of Titles - " + String.valueOf(first).toUpperCase() + " - DramaDownloader.com");
        context.put("dramaTitles", titleAccessor.getTitlesByPrefix(prefix));
        renderResult = templateEngine.render(new ModelAndView(context, "view/list.vm"));
        memcachedClient.set("page_list_" + first, 7 * 86400, renderResult);
      } else {
        renderResult = (String) cachedResponse;
      }

      return renderResult;
    });
  }

  public static void detail() {
    get("/detail/:id/:slug", (request, response) -> {
      String id = request.params(":id");
      String slug = request.params(":slug");

      Title title = titleAccessor.getTitle(id);
      boolean found = true;
      if(title == null) {
        found = false;
      } else if(!slug.equals(StringUtil.urlize(title.getTitle()))) {
        found = false;
      }

      if(!found) {
        response.status(404);
        return "404 Not found";
      }

      String renderResult;
      Object cachedResponse = memcachedClient.get("page_detail_" + id);
      if(cachedResponse == null) {
        Map<String, Object> context = new HashMap<>();
        context.put("requestUrl", request.url());
        context.put("esc", new EscapeTool());
        context.put("stringUtil", StringUtil.class);
        context.put("title", title.getTitle() + " - Source: " + title.getProviderId() + " - DramaDownloader.com");
        context.put("dramaTitle", title);
        renderResult = templateEngine.render(new ModelAndView(context, "view/detail.vm"));
        memcachedClient.set("page_detail_" + id, 7 * 86400, renderResult);
      } else {
        renderResult = (String) cachedResponse;
      }

      return renderResult;
    });
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
        LOGGER.info("Found cached fetchstreams response for " + episodeUrl);
        FetchStreamsResponse cachedApiResponse = objectMapper.readValue((String) cachedObject, FetchStreamsResponse.class);
        legacyMonitor.onRequestProcessed(episodeUrl, System.currentTimeMillis(), cachedApiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return objectMapper.writeValueAsString(cachedApiResponse);
      }

      StreamScraper streamScraper = scraperComponent.getStreamScraperFactory().getScraper(episodeUrl);
      if (streamScraper == null) {
        apiResponse.setStatus(FetchStreamsResponse.Status.UNSUPPORTED);
        legacyMonitor.onRequestProcessed(episodeUrl, System.currentTimeMillis(), apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));
        return objectMapper.writeValueAsString(apiResponse);
      }

      StreamScrapeResult streamScrapeResult = streamScraper.scrape(new StreamScrapeRequest(episodeUrl));
      LOGGER.info("URL " + episodeUrl + " processed with status " + streamScrapeResult.getStatus());

      CountDownLatch countDownLatch = new CountDownLatch(streamScrapeResult.getStreams().size());
      int seqNo = 1;
      Map<Integer, FetchStreamsResponse.Link> links = new TreeMap<>();
      for (StreamScrapeResult.Stream stream : streamScrapeResult.getStreams()) {
        final String streamName = stream.getStreamName();
        final String streamUrl = stream.getStreamUrl();
        final String streamTitle = streamScrapeResult.getTitle();
        LOGGER.info("Processing stream " + streamName + " - " + stream.getStreamUrl());

        final FileScraper fileScraper = scraperComponent.getFileScraperFactory().getScraper(streamUrl);
        if (fileScraper == null) {
          countDownLatch.countDown();
          continue;
        }

        Integer localSeqNo = seqNo;
        commonComponent.getScheduledExecutorService().submit(() -> {
          try {
            FileScrapeResult fileScrapeResult = fileScraper.scrape(new FileScrapeRequest(streamUrl, streamTitle));
            if (fileScrapeResult.getFiles().size() > 0) {
              FileScrapeResult.File file = fileScrapeResult.getFiles().get(0);
              synchronized (apiResponse.getLinks()) {
                FetchStreamsResponse.Link link = new FetchStreamsResponse.Link(streamName, file.getDownloadUrl(), file.isDirectLink());
                links.put(localSeqNo, link);
              }
            }
          } catch (Exception e) {
            // no-op
          } finally {
            countDownLatch.countDown();
          }
        });
        seqNo++;
      }
      countDownLatch.await(FETCH_TIMEOUT_MSEC, TimeUnit.MILLISECONDS);
      apiResponse.getLinks().addAll(links.values());
      apiResponse.setTitle(streamScrapeResult.getTitle());

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

      legacyMonitor.onRequestProcessed(episodeUrl, System.currentTimeMillis(), apiResponse.getStatus().equals(FetchStreamsResponse.Status.OK));

      return objectMapper.writeValueAsString(apiResponse);
    });
  }

  private static void fetchEpisodes() {
    post("/v1/drama/fetchepisodes", (request, response) -> {
      response.type("application/json");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Vary", "Origin");

      FetchEpisodesRequest apiRequest = objectMapper.readValue(request.body(), FetchEpisodesRequest.class);
      FetchEpisodesResponse apiResponse = new FetchEpisodesResponse();

      String titleUrl = apiRequest.getUrl().trim();

      // Check cache
      Object cachedObject = memcachedClient.get("fetchepisodes_" + titleUrl);
      if (cachedObject != null) {
        LOGGER.info("Found cached fetchepisodes response for " + titleUrl);
        FetchEpisodesResponse cachedApiResponse = objectMapper.readValue((String) cachedObject, FetchEpisodesResponse.class);
        return objectMapper.writeValueAsString(cachedApiResponse);
      }

      EpisodeScraper episodeScraper = scraperComponent.getEpisodeScraperFactory().getScraper(titleUrl);
      if (episodeScraper == null) {
        apiResponse.setStatus(FetchEpisodesResponse.Status.UNSUPPORTED);
        return objectMapper.writeValueAsString(apiResponse);
      }

      EpisodeScrapeResult episodeScrapeResult = episodeScraper.scrape(new EpisodeScrapeRequest(titleUrl));
      LOGGER.info("Title URL " + titleUrl + " processed with status " + episodeScrapeResult.getStatus());

      for (EpisodeScrapeResult.Episode episode : episodeScrapeResult.getEpisodes()) {
        FetchEpisodesResponse.EpisodeDisplay episodeDisplay = new FetchEpisodesResponse.EpisodeDisplay(episode.getTitle(), episode.getUrl());
        apiResponse.getEpisodes().add(episodeDisplay);
      }

      if (apiResponse.getEpisodes().size() > 0) {
        apiResponse.setStatus(FetchEpisodesResponse.Status.OK);
        memcachedClient.set("fetchepisodes_" + titleUrl, 14400, objectMapper.writeValueAsString(apiResponse));
      } else {
        apiResponse.setStatus(FetchEpisodesResponse.Status.FAILED);
      }

      return objectMapper.writeValueAsString(apiResponse);
    });
  }

  private static void stats() {
    get("/v1/monitor/stats", (request, response) -> {
      response.type("application/json");
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Vary", "Origin");

      long minTimestamp = System.currentTimeMillis() - 3600_000L;
      return objectMapper.writeValueAsString(legacyMonitor.getEntries(minTimestamp));
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

  private static List<String> getTopN(int n) throws IOException {
    List<LegacyMonitorEntry> entries = legacyMonitor.getEntries(System.currentTimeMillis() - 3600_000);
    Map<String, Integer> urlCountMap = new HashMap<>();
    entries.stream().filter(LegacyMonitorEntry::isSuccess).forEach(entry -> {
      if (urlCountMap.get(entry.getUrl()) == null) {
        urlCountMap.put(entry.getUrl(), 1);
      } else {
        urlCountMap.put(entry.getUrl(), urlCountMap.get(entry.getUrl()) + 1);
      }
    });

    List<UrlCount> urlCounts = urlCountMap.keySet()
        .stream()
        .map(key -> new UrlCount(key, urlCountMap.get(key)))
        .collect(Collectors.toList());

    Collections.sort(urlCounts, (o1, o2) -> -Integer.compare(o1.getCount(), o2.getCount()));

    List<String> sortedUrls = urlCounts
        .stream()
        .map(UrlCount::getUrl)
        .limit(n)
        .collect(Collectors.toList());

    return sortedUrls;
  }

  private static class UrlCount {
    private String url;
    private int count;

    public UrlCount() {

    }

    public UrlCount(String url, int count) {
      this.url = url;
      this.count = count;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }
  }
}
