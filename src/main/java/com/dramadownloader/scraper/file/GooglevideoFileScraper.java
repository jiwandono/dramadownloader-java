package com.dramadownloader.scraper.file;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GooglevideoFileScraper extends FileScraper {
  private static Set<String> SEQUENCES;

  static {
    SEQUENCES = new HashSet<>();
    SEQUENCES.add("googlevideo.com/");
    SEQUENCES.add("googleusercontent.com/");
  }

  @Override
  protected FileScrapeResult scrapeInternal(String url) throws IOException {
    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.OK);
    result.getFiles().add(new FileScrapeResult.File(url, true));

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
