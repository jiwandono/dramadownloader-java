package com.dramadownloader.app.api.v1;

import java.util.ArrayList;
import java.util.List;

public class FetchStreamsResponse {
  private Status status;
  private List<Link> links = new ArrayList<>();
  private String title;

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public static enum Status {
    OK,
    FAILED,
    UNSUPPORTED
  }

  public static class Link {
    private String name;
    private String url;
    private boolean isDirectLink;

    public Link() {

    }

    public Link(String name, String url, boolean isDirectLink) {
      this.name = name;
      this.url = url;
      this.isDirectLink = isDirectLink;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public boolean isDirectLink() {
      return isDirectLink;
    }

    public void setIsDirectLink(boolean isDirectLink) {
      this.isDirectLink = isDirectLink;
    }
  }
}
