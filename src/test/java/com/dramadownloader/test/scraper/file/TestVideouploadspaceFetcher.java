package com.dramadownloader.test.scraper.file;

import com.dramadownloader.scraper.file.FileScraper;
import com.dramadownloader.scraper.file.VideouploadspaceFileScraper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestVideouploadspaceFetcher extends FileScraperTestBase {
  private static final FileScraper scraper = new VideouploadspaceFileScraper();

  private static final List<String> urls = Arrays.asList(
      "https://videoupload.space/video.php?id=MTM1MDMx&typesub=dramacool-RAW&title=Iron+Ladies+Episode+1"
  );

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) {
      testFetchUrl(scraper, url);
    }
  }
}