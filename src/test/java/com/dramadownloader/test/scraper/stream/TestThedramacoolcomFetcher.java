package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.ThedramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestThedramacoolcomFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new ThedramacoolcomStreamScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.thedramacool.com/watch-video-man-man-episode-10.html");
    urls.add("http://www.thedramacool.com/watch-video-bad-thief-good-thief-episode-3.html");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
