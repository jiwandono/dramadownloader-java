package com.dramadownloader.scraper.title;

import com.dramadownloader.scraper.AbstractScraper;

import java.io.IOException;

public abstract class TitleScraper extends AbstractScraper<TitleScrapeResult> {
  @Override
  public TitleScrapeResult scrape(String url) throws IOException {
    if(!isSupported(url))
      return new TitleScrapeResult(TitleScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(url);
  }

  protected abstract TitleScrapeResult scrapeInternal(String url) throws IOException;
}
