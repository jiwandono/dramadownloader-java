package com.dramadownloader.scraper.episode;

import com.dramadownloader.scraper.Scraper;

import java.io.IOException;

public abstract class EpisodeScraper implements Scraper<EpisodeScrapeRequest, EpisodeScrapeResult> {
  @Override
  public EpisodeScrapeResult scrape(EpisodeScrapeRequest request) throws IOException {
    String url = request.getUrl();

    if(!isSupported(url))
      return new EpisodeScrapeResult(EpisodeScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(request);
  }

  protected abstract EpisodeScrapeResult scrapeInternal(EpisodeScrapeRequest request) throws IOException;
}
