package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.DramafireinfoStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramafireinfoFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new DramafireinfoStreamScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.dramafire.info/money-flower-episode-18/");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
