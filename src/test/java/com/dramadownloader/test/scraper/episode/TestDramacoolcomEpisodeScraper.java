package com.dramadownloader.test.scraper.episode;

import com.dramadownloader.scraper.episode.DramacoolcomEpisodeScraper;
import com.dramadownloader.scraper.episode.EpisodeScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramacoolcomEpisodeScraper extends EpisodeScraperTestBase {
  private static final EpisodeScraper scraper = new DramacoolcomEpisodeScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.thedramacool.com/drama-detail/we-got-married-s1-.html");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
