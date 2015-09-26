package com.dramadownloader.drama.fetch.episode;

import com.dramadownloader.drama.fetch.PageScraperFactory;

import java.util.HashSet;
import java.util.Set;

public class EpisodePageScraperFactory implements PageScraperFactory<EpisodePageScraper> {
  private final Set<EpisodePageScraper> scrapers;

  public EpisodePageScraperFactory() {
    scrapers = new HashSet<>();
  }

  @Override
  public EpisodePageScraper getPageScraper(String url) {
    for(EpisodePageScraper scraper : scrapers)
      if(scraper.isSupported(url))
        return scraper;

    return null;
  }

  public void registerPageScraper(EpisodePageScraper scraper) {
    scrapers.add(scraper);
  }
}
