package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.ScrapeResult;

import java.util.ArrayList;
import java.util.List;

public class FileScrapeResult extends ScrapeResult {
  private List<File> files = new ArrayList<>();

  public FileScrapeResult() {

  }

  public FileScrapeResult(Status status) {
    this.status = status;
  }

  public List<File> getFiles() {
    return files;
  }

  public void setFiles(List<File> files) {
    this.files = files;
  }

  public static class File {
    private String downloadUrl;
    private boolean isDirectLink;

    public File() {

    }

    public File(String downloadUrl, boolean isDirectLink) {
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
