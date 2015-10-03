package com.dramadownloader.test.scraper.file;

import com.dramadownloader.scraper.file.DramauploadFileScraper;
import com.dramadownloader.scraper.file.FileScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDramauploadFetcher extends FileScraperTestBase {
  private static final FileScraper scraper = new DramauploadFileScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://dramaupload.com/jw7.php?file=Twenty.again.e08.150919.hdtv.xvid-with-1.mp4-55fe17ebb49de.avi");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
