package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestDramaniceFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new DramaniceStreamScraper();

  private static final List<String> urls = Arrays.asList(
    "https://dramanice.es/king-of-mask-singer/watch-king-of-mask-singer-episode-113-online"
  );

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) {
      testFetchUrl(scraper, url);
    }
  }
}
