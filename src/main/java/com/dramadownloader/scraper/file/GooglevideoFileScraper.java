package com.dramadownloader.scraper.file;

import java.io.IOException;
import java.net.URLEncoder;

public class GooglevideoFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
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
}
