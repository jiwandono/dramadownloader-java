package com.dramadownloader.scraper.episode;

import com.dramadownloader.scraper.ScrapeRequest;

public class EpisodeScrapeRequest extends ScrapeRequest {
  public EpisodeScrapeRequest() {

  }

  public EpisodeScrapeRequest(String url) {
    this.url = url;
  }
}
