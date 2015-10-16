package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramaniceFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new DramaniceStreamScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.dramanice.us/running-man/watch-running-man-episode-126-online");
    urls.add("http://dramanice.net/playful-kiss/watch-playful-kiss-episode-12-online");
    urls.add("http://www.dramanice.net/omotesando-koukou-gasshoubu-/watch-omotesando-koukou-gasshoubu--episode-10-online");
    urls.add("http://dramanice.net/bird-that-doesnt-cry/watch-bird-that-doesnt-cry-episode-87-online");
    urls.add("http://www.dramanice.net/nevertheless-i-love-you/watch-nevertheless-i-love-you-episode-1-online");
    urls.add("http://www.dramanice.net/moon-river/watch-moon-river-episode-12-online");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
