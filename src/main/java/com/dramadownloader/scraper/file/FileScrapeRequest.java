package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.ScrapeRequest;

public class FileScrapeRequest extends ScrapeRequest {
  public FileScrapeRequest() {

  }

  public FileScrapeRequest(String url) {
    this.url = url;
  }
}
