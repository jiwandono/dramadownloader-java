package com.dramadownloader.app.api.v1;

import java.util.ArrayList;
import java.util.List;

public class FetchEpisodesResponse {
  private Status status;
  private List<EpisodeDisplay> episodes = new ArrayList<>();

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<EpisodeDisplay> getEpisodes() {
    return episodes;
  }

  public void setEpisodes(List<EpisodeDisplay> episodes) {
    this.episodes = episodes;
  }

  public static enum Status {
    OK,
    FAILED,
    UNSUPPORTED
  }

  public static class EpisodeDisplay {
    private String displayTitle;
    private String url;

    public EpisodeDisplay() {

    }

    public EpisodeDisplay(String displayTitle, String url) {
      this.displayTitle = displayTitle;
      this.url = url;
    }

    public String getDisplayTitle() {
      return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
      this.displayTitle = displayTitle;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
