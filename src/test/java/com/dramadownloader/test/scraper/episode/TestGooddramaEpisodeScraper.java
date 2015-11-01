package com.dramadownloader.test.scraper.episode;

import com.dramadownloader.scraper.episode.EpisodeScraper;
import com.dramadownloader.scraper.episode.GooddramaEpisodeScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TestGooddramaEpisodeScraper extends EpisodeScraperTestBase {
  private static final EpisodeScraper scraper = new GooddramaEpisodeScraper(Executors.newScheduledThreadPool(16));

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.gooddrama.net/korean-drama/running-man");
    urls.add("http://www.gooddrama.net/chinese-drama/happy-love-forever");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
