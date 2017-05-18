package com.dramadownloader.scraper.title;

import com.dramadownloader.scraper.Scraper;

import java.io.IOException;

public interface TitleScraper extends Scraper<TitleScrapeRequest, TitleScrapeResult> {
  public TitleScrapeResult scrape(TitleScrapeRequest request) throws IOException;
}
