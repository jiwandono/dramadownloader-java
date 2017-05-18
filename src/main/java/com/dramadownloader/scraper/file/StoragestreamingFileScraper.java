package com.dramadownloader.scraper.file;

import java.io.IOException;

public class StoragestreamingFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.OK);
    result.getFiles().add(new FileScrapeResult.File(url, true));

    return result;
  }
}
