package com.dramadownloader.drama.scraper.stream;

import com.dramadownloader.drama.scraper.AbstractScraper;

import java.io.IOException;

public abstract class StreamScraper extends AbstractScraper<StreamScrapeResult> {
  @Override
  public final StreamScrapeResult scrape(String url) throws IOException {
    if(!isSupported(url))
      return new StreamScrapeResult(StreamScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(url);
  }

  protected abstract StreamScrapeResult scrapeInternal(String url) throws IOException;
}
