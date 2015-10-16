package com.dramadownloader.scraper.file;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ZippyshareFileScraper extends FileScraper {
  private static Set<String> SEQUENCES;

  static {
    SEQUENCES = new HashSet<>();
    SEQUENCES.add("zippyshare.com/");
  }

  @Override
  protected FileScrapeResult scrapeInternal(String url) throws IOException {
    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.OK);
    result.getFiles().add(new FileScrapeResult.File(url, false));

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
