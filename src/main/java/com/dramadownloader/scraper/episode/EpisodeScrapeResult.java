package com.dramadownloader.scraper.episode;

import com.dramadownloader.scraper.ScrapeResult;

import java.util.ArrayList;
import java.util.List;

public class EpisodeScrapeResult extends ScrapeResult {
  private List<Episode> episodes = new ArrayList<>();

  public EpisodeScrapeResult() {

  }

  public EpisodeScrapeResult(Status status) {
    this.status = status;
  }

  public List<Episode> getEpisodes() {
    return episodes;
  }

  public void setEpisodes(List<Episode> episodes) {
    this.episodes = episodes;
  }

  public static class Episode {
    private String title;
    private String url;

    public Episode() {

    }

    public Episode(String title, String url) {
      this.title = title;
      this.url = url;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
