package com.dramadownloader.core.model;

public class Drama {
  private long dramaId;
  private String providerId;
  private String title;
  private String url;

  public long getDramaId() {
    return dramaId;
  }

  public void setDramaId(long dramaId) {
    this.dramaId = dramaId;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
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
