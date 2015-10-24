package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.AbstractScraper;

import java.io.IOException;

public abstract class StreamScraper extends AbstractScraper<StreamScrapeRequest, StreamScrapeResult> {
  @Override
  public final StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    if(!isSupported(url))
      return new StreamScrapeResult(StreamScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(request);
  }

  protected abstract StreamScrapeResult scrapeInternal(StreamScrapeRequest request) throws IOException;
}
