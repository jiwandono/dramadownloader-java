package com.dramadownloader.scraper.title;

import com.dramadownloader.scraper.ScrapeRequest;

public class TitleScrapeRequest extends ScrapeRequest {
  public TitleScrapeRequest() {

  }

  public TitleScrapeRequest(String url) {
    this.url = url;
  }
}
