package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.DramacoolsioStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramacoolsioFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new DramacoolsioStreamScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("https://dramacools.io/iron-ladies-episode-1.html");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
