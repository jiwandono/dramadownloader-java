package com.dramadownloader.scraper.title;

import com.dramadownloader.core.model.Title;
import com.dramadownloader.scraper.ScrapeResult;

import java.util.ArrayList;
import java.util.List;

public class TitleScrapeResult extends ScrapeResult {
  private List<Title> titles = new ArrayList<>();

  public TitleScrapeResult() {

  }

  public TitleScrapeResult(Status status) {
    this.status = status;
  }

  public List<Title> getTitles() {
    return titles;
  }

  public void setTitles(List<Title> titles) {
    this.titles = titles;
  }
}
