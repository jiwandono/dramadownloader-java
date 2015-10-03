package com.dramadownloader.drama.scraper.file;

import com.dramadownloader.drama.scraper.AbstractScraper;

import java.io.IOException;

public abstract class FileScraper extends AbstractScraper<FileScrapeResult> {
  @Override
  public final FileScrapeResult scrape(String url) throws IOException {
    if(!isSupported(url))
      return new FileScrapeResult(FileScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(url);
  }

  protected abstract FileScrapeResult scrapeInternal(String url) throws IOException;
}
