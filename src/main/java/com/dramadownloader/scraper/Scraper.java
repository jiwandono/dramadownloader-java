package com.dramadownloader.scraper;

import java.io.IOException;

public interface Scraper<TRequest extends ScrapeRequest, TResult extends ScrapeResult> {
  TResult scrape(TRequest request) throws IOException;

  boolean isSupported(String url);
}
