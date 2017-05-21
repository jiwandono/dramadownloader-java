package com.dramadownloader.test.scraper.file;

import com.dramadownloader.scraper.file.FileScraper;
import com.dramadownloader.scraper.file.VideothedramacoolFileScraper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestVideodramacoolFetcher extends FileScraperTestBase {
  private static final FileScraper scraper = new VideothedramacoolFileScraper();

  private static final List<String> urls = Arrays.asList(
    "http://video.thedramacool.com/embed/?type=jwplayer&url=aHR0cHM6Ly93d3cuYW1hem9uLmNvbS9jbG91ZGRyaXZlL3NoYXJlL2Q0bmwxMDVld0xxQTB3SmdDV0FNWnNyUko3eWZvcDBBOG9DVGZ4QU9wZXY%2FcmVmXz1jZF9waF9zaGFyZV9saW5rX2NvcHkjYW1hem9u&sub=Bad"
  );

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) {
      testFetchUrl(scraper, url);
    }
  }
}
