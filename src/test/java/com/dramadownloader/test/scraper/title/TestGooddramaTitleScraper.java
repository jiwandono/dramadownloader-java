package com.dramadownloader.test.scraper.title;

import com.dramadownloader.scraper.title.GooddramaTitleScraper;
import com.dramadownloader.scraper.title.TitleScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestGooddramaTitleScraper extends TitleScraperTestBase {
  private static final TitleScraper scraper = new GooddramaTitleScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://www.gooddrama.net/drama-list");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }

  @Test
  public void populateData() throws Exception {
    for(String url : urls) populateData(scraper, url);
  }
}
