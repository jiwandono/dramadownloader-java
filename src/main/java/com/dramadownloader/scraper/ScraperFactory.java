package com.dramadownloader.scraper;

public interface ScraperFactory<T extends Scraper> {
  T getScraper(String url);
}
