package com.dramadownloader.scraper.episode;

import com.dramadownloader.scraper.util.HttpUtil;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GooddramaEpisodeScraper implements EpisodeScraper {
  private static final Logger log = Logger.getLogger(GooddramaEpisodeScraper.class);

  private final ScheduledExecutorService _scheduledExecutorService;

  public GooddramaEpisodeScraper(ScheduledExecutorService scheduledExecutorService) {
    _scheduledExecutorService = scheduledExecutorService;
  }

  @Override
  public EpisodeScrapeResult scrape(EpisodeScrapeRequest request) throws IOException {
    String url = request.getUrl();

    EpisodeScrapeResult result = new EpisodeScrapeResult(EpisodeScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    Elements pages = doc.select(".pagination li button[href]");

    Map<Integer, List<EpisodeScrapeResult.Episode>> listMap = new HashMap<>();
    listMap.put(0, processPage(doc));

    CountDownLatch latch = new CountDownLatch(pages.size());
    for(int i = 0; i < pages.size(); i++) {
      Integer localIdx = i;
      _scheduledExecutorService.submit(() -> {
        try {
          String pageUrl = pages.get(localIdx).attr("href");
          Document pageDoc = HttpUtil.getDocument(pageUrl);
          listMap.put(localIdx+1, processPage(pageDoc));
          latch.countDown();
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      });
    }

    try {
      latch.await(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
    }

    for(int i : listMap.keySet()) {
      result.getEpisodes().addAll(listMap.get(i));
    }

    if(result.getEpisodes().size() > 0)
      result.setStatus(EpisodeScrapeResult.Status.OK);

    return result;
  }

  private List<EpisodeScrapeResult.Episode> processPage(Document doc) {
    List<EpisodeScrapeResult.Episode> episodes = new ArrayList<>();

    Elements anchors = doc.select("#videos li a");
    for(Element a : anchors) {
      String episodeTitle = a.text();
      String episodeUrl = a.attr("href");

      episodes.add(new EpisodeScrapeResult.Episode(episodeTitle, episodeUrl));
    }

    return episodes;
  }
}
