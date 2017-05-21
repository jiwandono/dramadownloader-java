package com.dramadownloader.scraper.file;

import java.io.IOException;

public class OpenloadFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.OK);

    String pageUrl = request.getUrl().replace("/embed/", "/f/");
    result.getFiles().add(new FileScrapeResult.File(pageUrl, false));

    return result;
  }
}
