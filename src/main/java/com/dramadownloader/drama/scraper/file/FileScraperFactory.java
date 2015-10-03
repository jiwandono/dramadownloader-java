package com.dramadownloader.drama.scraper.file;

import com.dramadownloader.drama.scraper.ScraperFactory;

import java.util.HashSet;
import java.util.Set;

public class FileScraperFactory implements ScraperFactory<FileScraper> {
  private final Set<FileScraper> scrapers;

  public FileScraperFactory() {
    scrapers = new HashSet<>();
  }

  @Override
  public FileScraper getScraper(String url) {
    for(FileScraper scraper : scrapers)
      if(scraper.isSupported(url))
        return scraper;

    return null;
  }

  public void register(FileScraper scraper) {
    scrapers.add(scraper);
  }
}
