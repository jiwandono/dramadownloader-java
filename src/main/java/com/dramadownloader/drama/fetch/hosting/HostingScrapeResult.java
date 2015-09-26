package com.dramadownloader.drama.fetch.hosting;

import java.util.ArrayList;
import java.util.List;

public class HostingScrapeResult {
  private Status status;
  private List<Downloadable> downloadables = new ArrayList<>();

  public HostingScrapeResult() {

  }

  public HostingScrapeResult(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<Downloadable> getDownloadables() {
    return downloadables;
  }

  public void setDownloadables(List<Downloadable> downloadables) {
    this.downloadables = downloadables;
  }

  public static enum Status {
    OK,
    FAILED,
    UNSUPPORTED
  }

  public static class Downloadable {
    private String downloadUrl;
    private boolean isDirectLink;

    public Downloadable() {

    }

    public Downloadable(String downloadUrl, boolean isDirectLink) {
      this.downloadUrl = downloadUrl;
      this.isDirectLink = isDirectLink;
    }

    public String getDownloadUrl() {
      return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
      this.downloadUrl = downloadUrl;
    }

    public boolean isDirectLink() {
      return isDirectLink;
    }

    public void setDirectLink(boolean directLink) {
      this.isDirectLink = directLink;
    }
  }
}
