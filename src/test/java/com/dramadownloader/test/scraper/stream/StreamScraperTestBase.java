package com.dramadownloader.test.scraper.stream;

import com.dramadownloader.scraper.stream.StreamScraper;
import com.dramadownloader.scraper.stream.StreamScrapeResult;
import org.junit.Assert;

public abstract class StreamScraperTestBase {
  protected void printFetchResult(StreamScrapeResult result) {
    System.out.println(result.getStatus());
    for(StreamScrapeResult.Stream stream : result.getStreams()) {
      System.out.println(stream.getStreamName() + ": " + stream.getStreamUrl());
    }
  }

  protected void testFetchUrl(StreamScraper scraper, String url) throws Exception {
    System.out.println(url);
    StreamScrapeResult result = scraper.scrape(url);
    Assert.assertEquals(StreamScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }
}
