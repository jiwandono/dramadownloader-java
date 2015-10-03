package com.dramadownloader.drama.scraper;

import java.io.IOException;

public interface Scraper<T> {
  T scrape(String url) throws IOException;

  boolean isSupported(String url);
}
