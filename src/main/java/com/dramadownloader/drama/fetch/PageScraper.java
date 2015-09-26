package com.dramadownloader.drama.fetch;

import java.io.IOException;

public interface PageScraper<T> {
  T scrape(String url) throws IOException;

  boolean isSupported(String url);
}
