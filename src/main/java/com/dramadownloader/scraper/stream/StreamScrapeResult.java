package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.ScrapeResult;

import java.util.ArrayList;
import java.util.List;

public class StreamScrapeResult extends ScrapeResult {
  private List<Stream> streams = new ArrayList<>();
  private String title;

  public StreamScrapeResult() {

  }

  public StreamScrapeResult(Status status) {
    this.status = status;
  }

  public List<Stream> getStreams() {
    return streams;
  }

  public void setStreams(List<Stream> streams) {
    this.streams = streams;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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
