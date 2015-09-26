package com.dramadownloader.drama.fetch;

import org.jsoup.nodes.Document;

public abstract class AbstractPageScraper<T> implements PageScraper<T> {
  protected Document getDocument(String url) {
    return null;
  }
}
