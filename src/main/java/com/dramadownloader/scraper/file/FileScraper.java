package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.Scraper;

import java.io.IOException;

public interface FileScraper extends Scraper<FileScrapeRequest, FileScrapeResult> {
  FileScrapeResult scrape(FileScrapeRequest request) throws IOException;
}
