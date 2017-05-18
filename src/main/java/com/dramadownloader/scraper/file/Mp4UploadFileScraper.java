package com.dramadownloader.scraper.file;

import java.io.IOException;

public class Mp4UploadFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    // URL is usually in http://www.mp4upload.com/embed-cyyg0nztqbyh.html format.
    // Just remove the embed- part then it will show a page with download link.

    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.FAILED);

    if(url.contains("mp4upload.com/embed-")) {
      String downloadUrl = url.replace("/embed-", "/");

      result.setStatus(FileScrapeResult.Status.OK);
      result.getFiles().add(new FileScrapeResult.File(downloadUrl, false));
    }

    return result;
  }
}
