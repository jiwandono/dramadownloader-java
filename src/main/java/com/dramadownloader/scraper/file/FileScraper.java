package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.Scraper;

import java.io.IOException;

public abstract class FileScraper implements Scraper<FileScrapeRequest, FileScrapeResult> {
  @Override
  public final FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    if(!isSupported(url))
      return new FileScrapeResult(FileScrapeResult.Status.UNSUPPORTED);

    return scrapeInternal(request);
  }

  protected abstract FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException;
}
