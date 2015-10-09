package com.dramadownloader.scraper;

import java.io.IOException;

public interface Scraper<T extends ScrapeResult> {
  T scrape(String url) throws IOException;

  boolean isSupported(String url);
}
