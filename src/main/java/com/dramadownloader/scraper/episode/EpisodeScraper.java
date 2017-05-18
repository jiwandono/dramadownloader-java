package com.dramadownloader.scraper.episode;

import com.dramadownloader.scraper.Scraper;

import java.io.IOException;

public interface EpisodeScraper extends Scraper<EpisodeScrapeRequest, EpisodeScrapeResult> {
  EpisodeScrapeResult scrape(EpisodeScrapeRequest request) throws IOException;
}
