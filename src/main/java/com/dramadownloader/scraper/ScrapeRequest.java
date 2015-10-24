package com.dramadownloader.scraper;

public abstract class ScrapeRequest {
  protected String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
