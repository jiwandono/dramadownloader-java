package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.ScrapeRequest;

public class FileScrapeRequest extends ScrapeRequest {
  private String name;

  public FileScrapeRequest() {

  }

  public FileScrapeRequest(String url) {
    this.url = url;
  }

  public FileScrapeRequest(String url, String name) {
    this.url = url;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
