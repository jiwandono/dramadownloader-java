package com.dramadownloader.scraper;

public abstract class ScrapeResult {
  protected Status status;

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public enum Status {
    OK,
    FAILED,
    UNSUPPORTED
  }
}
