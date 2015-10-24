package com.dramadownloader.test.scraper.file;

import com.dramadownloader.scraper.file.FileScrapeRequest;
import com.dramadownloader.scraper.file.FileScraper;
import com.dramadownloader.scraper.file.FileScrapeResult;
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
    FileScrapeResult result = scraper.scrape(new FileScrapeRequest(url));
    Assert.assertEquals(FileScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }
}
