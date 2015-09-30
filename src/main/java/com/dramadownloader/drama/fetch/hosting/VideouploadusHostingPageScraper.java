package com.dramadownloader.drama.fetch.hosting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VideouploadusHostingPageScraper extends HostingPageScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("videoupload.us");
    DOMAINS.add("www.videoupload.us");
  }

  @Override
  protected HostingScrapeResult scrapeInternal(String url) throws IOException {
    // URL is usually in http://videoupload.us/drama/embed-MTU5NDI=.html format.
    // Just change embed- to video- then it will show a page with download link.
    HostingScrapeResult result = new HostingScrapeResult(HostingScrapeResult.Status.FAILED);

    if(url.contains("videoupload.us/drama/embed-")) {
      String downloadUrl = url.replace("/embed-", "/video-");

      result.setStatus(HostingScrapeResult.Status.OK);
      result.getDownloadables().add(new HostingScrapeResult.Downloadable(downloadUrl, false));
    } else if(url.contains("videoupload.us/embed/drama-")) {
      String downloadUrl = url.replace("/embed/drama-", "/drama/video-");

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
