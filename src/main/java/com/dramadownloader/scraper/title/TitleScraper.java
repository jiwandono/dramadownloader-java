package com.dramadownloader.scraper.title;

import com.dramadownloader.scraper.AbstractScraper;

import java.io.IOException;

public abstract class TitleScraper extends AbstractScraper<TitleScrapeResult> {
  @Override
  public TitleScrapeResult scrape(String url) throws IOException {
    return null;
  }

  protected abstract TitleScrapeResult scrapeInternal(String url) throws IOException;
}
