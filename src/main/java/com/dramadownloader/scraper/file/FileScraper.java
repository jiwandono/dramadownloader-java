package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.AbstractScraper;

import java.io.IOException;

public abstract class FileScraper extends AbstractScraper<FileScrapeRequest, FileScrapeResult> {
  @Override
  public final FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    if(!isSupported(url))
      return new FileScrapeResult(FileScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(request);
  }

  protected abstract FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException;
}
