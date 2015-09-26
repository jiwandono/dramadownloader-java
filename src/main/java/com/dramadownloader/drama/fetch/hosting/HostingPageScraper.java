package com.dramadownloader.drama.fetch.hosting;

import com.dramadownloader.drama.fetch.AbstractPageScraper;

import java.io.IOException;

public abstract class HostingPageScraper extends AbstractPageScraper<HostingScrapeResult> {
  @Override
  public final HostingScrapeResult scrape(String url) throws IOException {
    if(!isSupported(url))
      return new HostingScrapeResult(HostingScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(url);
  }

  protected abstract HostingScrapeResult scrapeInternal(String url) throws IOException;
}
