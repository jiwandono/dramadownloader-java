package com.dramadownloader.core.model;

import java.util.List;

public class DramaSummary {
  private Drama drama;
  private List<DramaEpisode> episodes;


  public Drama getDrama() {
    return drama;
  }

  public void setDrama(Drama drama) {
    this.drama = drama;
  }

  public List<DramaEpisode> getEpisodes() {
    return episodes;
  }

  public void setEpisodes(List<DramaEpisode> episodes) {
    this.episodes = episodes;
  }
}
