package com.dramadownloader.drama.fetch.hosting;

import com.dramadownloader.drama.fetch.PageScraperFactory;

import java.util.HashSet;
import java.util.Set;

public class HostingPageScraperFactory implements PageScraperFactory<HostingPageScraper> {
  private final Set<HostingPageScraper> scrapers;

  public HostingPageScraperFactory() {
    scrapers = new HashSet<>();
  }

  @Override
  public HostingPageScraper getPageScraper(String url) {
    for(HostingPageScraper scraper : scrapers)
      if(scraper.isSupported(url))
        return scraper;

    return null;
  }

  public void registerScraper(HostingPageScraper scraper) {
    scrapers.add(scraper);
  }
}
