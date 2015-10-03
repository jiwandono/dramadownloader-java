package com.dramadownloader.drama.scraper;

public interface ScraperFactory<T extends Scraper> {
  T getScraper(String url);
}
