package com.dramadownloader.test.scraper.episode;

import com.dramadownloader.scraper.episode.EpisodeScrapeResult;
import com.dramadownloader.scraper.episode.EpisodeScraper;
import org.junit.Assert;

public abstract class EpisodeScraperTestBase {
  protected void printFetchResult(EpisodeScrapeResult result) {
    System.out.println(result.getStatus());
    for(EpisodeScrapeResult.Episode episode : result.getEpisodes()) {
      System.out.println(episode.getTitle() + " - " + episode.getUrl());
    }
  }

  protected void testFetchUrl(EpisodeScraper scraper, String url) throws Exception {
    System.out.println(url);
    EpisodeScrapeResult result = scraper.scrape(url);
    Assert.assertEquals(EpisodeScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }
}
