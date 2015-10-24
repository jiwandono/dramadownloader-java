package com.dramadownloader.scraper.file;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StoragestreamingFileScraper extends FileScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("www.storagestreaming.com");
  }

  @Override
  protected FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.OK);
    result.getFiles().add(new FileScrapeResult.File(url, true));

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
