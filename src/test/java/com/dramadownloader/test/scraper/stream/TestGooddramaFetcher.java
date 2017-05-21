package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.GooddramaStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TestGooddramaFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new GooddramaStreamScraper(Executors.newScheduledThreadPool(8));

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.dramagalaxy.tv/chicago-typewriter-episode-12");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl (scraper, url);
  }
}
