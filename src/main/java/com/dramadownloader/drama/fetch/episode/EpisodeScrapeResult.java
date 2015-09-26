package com.dramadownloader.drama.fetch.episode;

import java.util.ArrayList;
import java.util.List;

public class EpisodeScrapeResult {
  private Status status;
  private List<Stream> streams = new ArrayList<>();

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<Stream> getStreams() {
    return streams;
  }

  public void setStreams(List<Stream> streams) {
    this.streams = streams;
  }

  public static enum Status {
    OK,
    FAILED,
    UNSUPPORTED
  }

  public static class Stream {
    private String streamName;
    private String streamUrl;

    public Stream() {

    }

    public Stream(String streamName, String streamUrl) {
      this.streamName = streamName;
      this.streamUrl = streamUrl;
    }

    public String getStreamName() {
      return streamName;
    }

    public void setStreamName(String streamName) {
      this.streamName = streamName;
    }

    public String getStreamUrl() {
      return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
      this.streamUrl = streamUrl;
    }
  }
}
