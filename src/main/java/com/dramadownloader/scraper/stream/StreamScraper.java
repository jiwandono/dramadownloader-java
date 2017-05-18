package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.Scraper;

import java.io.IOException;

public interface StreamScraper extends Scraper<StreamScrapeRequest, StreamScrapeResult> {
  StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException;
}
