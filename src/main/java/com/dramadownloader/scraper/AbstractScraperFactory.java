package com.dramadownloader.scraper;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractScraperFactory<T extends Scraper> implements ScraperFactory<T> {
  private final Set<T> scrapers;

  public AbstractScraperFactory() {
    scrapers = new HashSet<>();
  }

  @Override
  public T getScraper(String url) {
    for(T scraper : scrapers)
      if(scraper.isSupported(url))
        return scraper;

    return null;
  }

  public void register(T scraper) {
    scrapers.add(scraper);
  }
}
