package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.ScrapeResult;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TiveeStreamScraper extends StreamScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("tiveee.com");
    DOMAINS.add("www.tiveee.com");
  }

  @Override
  protected StreamScrapeResult scrapeInternal(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(ScrapeResult.Status.FAILED);

    Document doc = getDocument(url);
    String title = doc.getElementsByClass("mtitle").first().text();

    result.getStreams().add(new StreamScrapeResult.Stream("Tiveee", url));

    if(result.getStreams().size() > 0) {
      result.setTitle(title);
      result.setStatus(StreamScrapeResult.Status.OK);
    }

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
