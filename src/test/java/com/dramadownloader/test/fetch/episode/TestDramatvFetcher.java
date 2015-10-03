package com.dramadownloader.test.fetch.episode;

import com.dramadownloader.drama.fetch.episode.DramatvEpisodePageScraper;
import com.dramadownloader.drama.fetch.episode.EpisodePageScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramatvFetcher extends EpisodeFetcherTestBase {
  private static final EpisodePageScraper scraper = new DramatvEpisodePageScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://dramatv.tv/watch-drama/the-heirs-ep-17-english-sub.html");
    urls.add("http://www.dramatv.tv/watch-drama/king-geunchogo-ep-43-english-sub.html");
    urls.add("http://dramatv.co/watch-drama/kinkyori-renai-season-zero-ep-1-english-sub.html");
    urls.add("http://www.dramatv.co/watch-drama/oh-my-ghost-ep-12-english-sub.html");
    urls.add("http://www.dramatv.tv/watch-drama/running-man-ep-185-english-sub.html");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
