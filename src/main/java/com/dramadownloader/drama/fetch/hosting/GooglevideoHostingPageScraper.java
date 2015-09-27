package com.dramadownloader.drama.fetch.hosting;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GooglevideoHostingPageScraper extends HostingPageScraper {
  private static Set<String> SEQUENCES;

  static {
    SEQUENCES = new HashSet<>();
    SEQUENCES.add("googlevideo.com/");
  }

  @Override
  protected HostingScrapeResult scrapeInternal(String url) throws IOException {
    HostingScrapeResult result = new HostingScrapeResult(HostingScrapeResult.Status.OK);
    result.getDownloadables().add(new HostingScrapeResult.Downloadable(url, true));

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    for(String sequence : SEQUENCES)
      if(url.contains(sequence))
        return true;

    return false;
  }
}
