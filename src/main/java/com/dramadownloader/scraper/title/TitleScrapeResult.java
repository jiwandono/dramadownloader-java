package com.dramadownloader.scraper.title;

import com.dramadownloader.core.model.Title;

import java.util.ArrayList;
import java.util.List;

public class TitleScrapeResult {
  private Status status;
  private List<Title> titles = new ArrayList<>();

  public TitleScrapeResult() {

  }

  public TitleScrapeResult(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<Title> getTitles() {
    return titles;
  }

  public void setTitles(List<Title> titles) {
    this.titles = titles;
  }

  public static enum Status {
    OK,
    FAILED,
    UNSUPPORTED
  }
}
