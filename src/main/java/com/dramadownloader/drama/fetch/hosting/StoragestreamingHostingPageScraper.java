package com.dramadownloader.drama.fetch.hosting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StoragestreamingHostingPageScraper extends HostingPageScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("www.storagestreaming.com");
  }

  @Override
  protected HostingScrapeResult scrapeInternal(String url) throws IOException {
    HostingScrapeResult result = new HostingScrapeResult(HostingScrapeResult.Status.OK);
    result.getDownloadables().add(new HostingScrapeResult.Downloadable(url, true));

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
