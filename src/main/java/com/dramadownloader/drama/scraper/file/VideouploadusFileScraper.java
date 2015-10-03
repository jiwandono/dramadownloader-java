package com.dramadownloader.drama.scraper.file;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VideouploadusFileScraper extends FileScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("videoupload.us");
    DOMAINS.add("www.videoupload.us");
  }

  @Override
  protected FileScrapeResult scrapeInternal(String url) throws IOException {
    // URL is usually in http://videoupload.us/drama/embed-MTU5NDI=.html format.
    // Just change embed- to video- then it will show a page with download link.
    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.FAILED);

    if(url.contains("videoupload.us/drama/video")) {
      // Leave as is.
      result.setStatus(FileScrapeResult.Status.OK);
      result.getFiles().add(new FileScrapeResult.File(url, false));
    } else if(url.contains("videoupload.us/drama/embed-")) {
      String downloadUrl = url.replace("/embed-", "/video-");

      result.setStatus(FileScrapeResult.Status.OK);
      result.getFiles().add(new FileScrapeResult.File(downloadUrl, false));
    } else if(url.contains("videoupload.us/embed/drama-")) {
      String downloadUrl = url.replace("/embed/drama-", "/drama/video-");

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
