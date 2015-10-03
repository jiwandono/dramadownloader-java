package com.dramadownloader.scraper.episode;

import com.dramadownloader.scraper.AbstractScraper;

import java.io.IOException;

public abstract class EpisodeScraper extends AbstractScraper<EpisodeScrapeResult> {
  @Override
  public EpisodeScrapeResult scrape(String url) throws IOException {
    if(!isSupported(url))
      return new EpisodeScrapeResult(EpisodeScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(url);
  }

  protected abstract EpisodeScrapeResult scrapeInternal(String url) throws IOException;
}
