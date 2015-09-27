package com.dramadownloader.app.api.v1.monitor;

import org.mongodb.morphia.annotations.Id;

public class LegacyMonitorEntry {
  @Id
  private String id; // Unused, set by mongo
  private String url;
  private long timestamp;
  private boolean success;

  public LegacyMonitorEntry() {

  }

  public LegacyMonitorEntry(String url, long timestamp, boolean success) {
    this.url = url;
    this.timestamp = timestamp;
    this.success = success;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
