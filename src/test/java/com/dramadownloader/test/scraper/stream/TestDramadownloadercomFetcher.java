package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramadownloadercomFetcher extends StreamScraperTestBase {
  private static final StreamScraper scraper = new DramacoolcomStreamScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.dramacool.com/i-summon-you-gold-episode-1.html");
    urls.add("www.dramacool.com/dr-mo-clinic-dr-ian--episode-6.html");
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
