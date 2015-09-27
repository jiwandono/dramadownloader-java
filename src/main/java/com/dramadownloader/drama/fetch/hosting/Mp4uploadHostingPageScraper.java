package com.dramadownloader.drama.fetch.hosting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Mp4uploadHostingPageScraper extends HostingPageScraper {
  private static final Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("mp4upload.com");
    DOMAINS.add("www.mp4upload.com");
  }

  @Override
  protected HostingScrapeResult scrapeInternal(String url) throws IOException {
    // URL is usually in http://www.mp4upload.com/embed-cyyg0nztqbyh.html format.
    // Just remove the embed- part then it will show a page with download link.
    HostingScrapeResult result = new HostingScrapeResult(HostingScrapeResult.Status.FAILED);

    if(url.contains("mp4upload.com/embed-")) {
      String downloadUrl = url.replace("/embed-", "/");

      result.setStatus(HostingScrapeResult.Status.OK);
      result.getDownloadables().add(new HostingScrapeResult.Downloadable(downloadUrl, false));
    }

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
