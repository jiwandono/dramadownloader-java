package com.dramadownloader.drama.fetch.episode;

import com.dramadownloader.drama.fetch.PageScraperFactory;

import java.util.HashSet;
import java.util.Set;

public class EpisodePageScraperFactory implements PageScraperFactory<EpisodePageScraper> {
  private final Set<EpisodePageScraper> episodePageScrapers;

  public EpisodePageScraperFactory() {
    episodePageScrapers = new HashSet<>();
  }

  @Override
  public EpisodePageScraper getPageScraper(String url) {
    for(EpisodePageScraper scraper : episodePageScrapers)
      if(scraper.isSupported(url))
        return scraper;

    return null;
  }

  public void registerPageScraper(EpisodePageScraper scraper) {
    episodePageScrapers.add(scraper);
  }
}
