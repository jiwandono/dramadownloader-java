package com.dramadownloader.test.fetch.episode;

import com.dramadownloader.drama.fetch.episode.EpisodePageScraper;
import com.dramadownloader.drama.fetch.episode.EpisodeScrapeResult;
import org.junit.Assert;

public abstract class EpisodeFetcherTestBase {
  protected void printFetchResult(EpisodeScrapeResult result) {
    System.out.println(result.getStatus());
    for(EpisodeScrapeResult.Stream stream : result.getStreams()) {
      System.out.println(stream.getStreamName() + ": " + stream.getStreamUrl());
    }
  }

  protected void testFetchUrl(EpisodePageScraper scraper, String url) throws Exception {
    System.out.println(url);
    EpisodeScrapeResult result = scraper.scrape(url);
    Assert.assertEquals(EpisodeScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }
}
