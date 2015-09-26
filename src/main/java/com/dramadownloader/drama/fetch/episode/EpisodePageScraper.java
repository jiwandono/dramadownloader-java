package com.dramadownloader.drama.fetch.episode;

import com.dramadownloader.drama.fetch.AbstractPageScraper;

import java.io.IOException;

public abstract class EpisodePageScraper extends AbstractPageScraper<EpisodeScrapeResult> {
  @Override
  public final EpisodeScrapeResult scrape(String url) throws IOException {
    if(!isSupported(url))
      return new EpisodeScrapeResult(EpisodeScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(url);
  }

  protected abstract EpisodeScrapeResult scrapeInternal(String url) throws IOException;
}
