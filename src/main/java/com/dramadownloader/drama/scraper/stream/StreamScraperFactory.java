package com.dramadownloader.drama.scraper.stream;

import com.dramadownloader.drama.scraper.ScraperFactory;

import java.util.HashSet;
import java.util.Set;

public class StreamScraperFactory implements ScraperFactory<StreamScraper> {
  private final Set<StreamScraper> scrapers;

  public StreamScraperFactory() {
    scrapers = new HashSet<>();
  }

  @Override
  public StreamScraper getScraper(String url) {
    for(StreamScraper scraper : scrapers)
      if(scraper.isSupported(url))
        return scraper;

    return null;
  }

  public void register(StreamScraper scraper) {
    scrapers.add(scraper);
  }
}
