package com.dramadownloader.test.scraper.file;

import com.dramadownloader.drama.scraper.file.FileScraper;
import com.dramadownloader.drama.scraper.file.FileScrapeResult;
import org.junit.Assert;

public abstract class FileScraperTestBase {
  protected void printFetchResult(FileScrapeResult result) {
    System.out.println(result.getStatus());
    for(FileScrapeResult.File file : result.getFiles()) {
      System.out.println(file.getDownloadUrl());
    }
  }

  protected void testFetchUrl(FileScraper scraper, String url) throws Exception {
    System.out.println(url);
    FileScrapeResult result = scraper.scrape(url);
    Assert.assertEquals(FileScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }
}
