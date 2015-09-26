package com.dramadownloader.drama.fetch.episode;

import org.jsoup.nodes.Document;

import java.io.IOException;

public class DramacoolcomEpisodePageScraper extends EpisodePageScraper {
  @Override
  public EpisodeScrapeResult scrape(String url) throws IOException {
    Document document = getDocument("asdF");
    return null;  // TODO impl
  }

  @Override
  public boolean isSupported(String url) {
    return false;  // TODO impl
  }
}
