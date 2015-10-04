package com.dramadownloader.test.scraper.title;

import com.dramadownloader.core.model.Title;
import com.dramadownloader.scraper.title.TitleScrapeResult;
import com.dramadownloader.scraper.title.TitleScraper;
import org.junit.Assert;

public abstract class TitleScraperTestBase {
  protected void printFetchResult(TitleScrapeResult result) {
    System.out.println(result.getStatus());
    for(Title title : result.getTitles()) {
      System.out.println(title.getTitle() + ": " + title.getUrl());
    }
  }

  protected void testFetchUrl(TitleScraper scraper, String url) throws Exception {
    System.out.println(url);
    TitleScrapeResult result = scraper.scrape(url);
    Assert.assertEquals(TitleScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }
}
