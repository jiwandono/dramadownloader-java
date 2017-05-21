package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramacoolcomFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new DramacoolcomStreamScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.dramacool.su/tunnel-korean-drama-episode-15.html");
    urls.add("http://www.dramacool.su/moon-river-episode-22.html");
    urls.add("http://www.dramacool.su/my-love-from-the-star-episode-3.html");
    urls.add("http://www.dramacool.su/bloody-monday-season-1-episode-1.html");
    urls.add("http://www.dramacool.su/iris-episode-1.html");
    urls.add("http://www.dramacool.su/running-man-episode-91.html");
    urls.add("http://www.dramacool.su/i-summon-you-gold-episode-1.html");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
