package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.ScrapeRequest;

public class StreamScrapeRequest extends ScrapeRequest {
  public StreamScrapeRequest() {

  }

  public StreamScrapeRequest(String url) {
    this.url = url;
  }
}
