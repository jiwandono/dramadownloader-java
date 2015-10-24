package com.dramadownloader.scraper.file;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Mp4UploadFileScraper extends FileScraper {
  private static final Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("mp4upload.com");
    DOMAINS.add("www.mp4upload.com");
  }

  @Override
  protected FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException {
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

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
