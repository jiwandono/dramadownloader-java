package com.dramadownloader.test.fetch.episode;

import com.dramadownloader.drama.fetch.episode.DramafirecomEpisodePageScraper;
import com.dramadownloader.drama.fetch.episode.EpisodePageScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramafirecomFetcher extends EpisodeFetcherTestBase {
  private static final EpisodePageScraper scraper = new DramafirecomEpisodePageScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://dramafire.com/yong-pal-episode-15/l");
    urls.add("http://dramafire.com/my-daughter-geum-sa-wol-episode-6/");
    urls.add("http://dramafire.com/assembly-episode-19/");
    urls.add("http://dramafire.com/running-man-episode-261/");
    urls.add("http://dramafire.com/i-give-my-first-love-to-you-2009/");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
