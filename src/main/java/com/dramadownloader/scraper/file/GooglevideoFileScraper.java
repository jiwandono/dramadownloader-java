package com.dramadownloader.scraper.file;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class GooglevideoFileScraper extends FileScraper {
  private static Set<String> SEQUENCES;

  static {
    SEQUENCES = new HashSet<>();
    SEQUENCES.add("googlevideo.com/");
    SEQUENCES.add("googleusercontent.com/");
    SEQUENCES.add("blogspot.com/");
  }

  @Override
  protected FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    // This won't work because the final URL is IP-restricted.
    // if(url.contains("googleusercontent.com/")) {
    //   url = getFinalUrl(url);
    // }

    if(url.contains("googlevideo.com/") && request.getName() != null) {
      url += "&title=" + URLEncoder.encode(request.getName(), "UTF-8");
    }

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
