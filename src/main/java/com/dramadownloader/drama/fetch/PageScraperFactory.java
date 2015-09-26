package com.dramadownloader.drama.fetch;

public interface PageScraperFactory<T extends PageScraper> {
  T getPageScraper(String url);
}
