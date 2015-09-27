package com.dramadownloader.test.fetch.episode;

import com.dramadownloader.drama.fetch.episode.DramacoolcomEpisodePageScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramadownloadercomFetcher extends EpisodeFetcherTestBase {
  private static final DramacoolcomEpisodePageScraper scraper = new DramacoolcomEpisodePageScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("www.dramacool.com/my-girlfriend-is-a-gumiho-episode-11.html");
    urls.add("dramacool.com/-empresses-in-the-palace-episode-71.html");
    urls.add("http://dramacool.com/noble-my-love-episode-20.html");
    urls.add("http://www.dramacool.com/second-time-twenty-years-old-episode-9.html");
    urls.add("http://dramacool.com/yong-pal-episode-16.html");
    urls.add("http://dramacool.com/on-the-wings-of-love-episode-26.html");
    urls.add("http://dramacool.com/second-time-twenty-years-old-episode-8.html");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
