package com.dramadownloader.core.model;

import java.util.List;

public class DramaEpisode {
  private long episodeId;
  private long dramaId;
  private long episodeNumber;
  private String url;

  public long getEpisodeId() {
    return episodeId;
  }

  public void setEpisodeId(long episodeId) {
    this.episodeId = episodeId;
  }

  public long getDramaId() {
    return dramaId;
  }

  public void setDramaId(long dramaId) {
    this.dramaId = dramaId;
  }

  public long getEpisodeNumber() {
    return episodeNumber;
  }

  public void setEpisodeNumber(long episodeNumber) {
    this.episodeNumber = episodeNumber;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
