package com.dramadownloader.test.scraper.file;

import com.dramadownloader.scraper.file.EmbeddramacoolFileScraper;
import com.dramadownloader.scraper.file.FileScraper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestEmbeddramacoolFetcher extends FileScraperTestBase {
  private static final FileScraper scraper = new EmbeddramacoolFileScraper();

  private static final List<String> urls = Arrays.asList(
      "https://embed.dramacool.su/embed.php?id=MTE0MTcy&title=Tunnel+%28Korean+Drama%29+episode+15&typesub=SUB&sub=L3R1bm5lbC1rb3JlYW4tZHJhbWEtZXBpc29kZS0xNS90dW5uZWwta29yZWFuLWRyYW1hLWVwaXNvZGUtMTUudnR0",
      "https://embed.dramacool.su/bk/embed.php?id=MTE0MTcy&title=Tunnel+%28Korean+Drama%29+episode+15&typesub=SUB&sub=L3R1bm5lbC1rb3JlYW4tZHJhbWEtZXBpc29kZS0xNS90dW5uZWwta29yZWFuLWRyYW1hLWVwaXNvZGUtMTUudnR0"
  );

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) {
      testFetchUrl(scraper, url);
    }
  }
}
