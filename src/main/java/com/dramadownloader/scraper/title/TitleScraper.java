package com.dramadownloader.scraper.title;

import com.dramadownloader.scraper.AbstractScraper;

import java.io.IOException;

public abstract class TitleScraper extends AbstractScraper<TitleScrapeRequest, TitleScrapeResult> {
  @Override
  public TitleScrapeResult scrape(TitleScrapeRequest request) throws IOException {
    String url = request.getUrl();

    if(!isSupported(url))
      return new TitleScrapeResult(TitleScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(request);
  }

  protected abstract TitleScrapeResult scrapeInternal(TitleScrapeRequest request) throws IOException;
}
