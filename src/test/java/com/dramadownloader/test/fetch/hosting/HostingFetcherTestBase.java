package com.dramadownloader.test.fetch.hosting;

import com.dramadownloader.drama.fetch.hosting.HostingPageScraper;
import com.dramadownloader.drama.fetch.hosting.HostingScrapeResult;
import org.junit.Assert;

public abstract class HostingFetcherTestBase {
  protected void printFetchResult(HostingScrapeResult result) {
    System.out.println(result.getStatus());
    for(HostingScrapeResult.Downloadable downloadable : result.getDownloadables()) {
      System.out.println(downloadable.getDownloadUrl());
    }
  }

  protected void testFetchUrl(HostingPageScraper scraper, String url) throws Exception {
    System.out.println(url);
    HostingScrapeResult result = scraper.scrape(url);
    Assert.assertEquals(HostingScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }
}
