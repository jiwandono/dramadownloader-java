package com.dramadownloader.scraper.stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DramatvStreamScraper extends StreamScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramatv.tv");
    DOMAINS.add("www.dramatv.tv");
    DOMAINS.add("dramatv.us");
    DOMAINS.add("www.dramatv.us");
    DOMAINS.add("dramatv.co");
    DOMAINS.add("www.dramatv.co");
    DOMAINS.add("dramacool.tv");
    DOMAINS.add("www.dramacool.tv");
  }

  @Override
  protected StreamScrapeResult scrapeInternal(String url) throws IOException {
    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = getDocument(url);
    Elements candidates = new Elements();
    candidates.addAll(doc.select(".movie-detail iframe[src]"));
    candidates.addAll(doc.select(".movie-detail video source[src]"));

    int i = 1;
    for(Element candidate : candidates) {
      String name = "Server " + i++;
      String streamUrl = candidate.attr("src");

      result.getStreams().add(new StreamScrapeResult.Stream(name, streamUrl));
    }

    Elements paragraphs = doc.select(".movie-detail p[data-content]");
    for(Element p : paragraphs) {
      Document inline = Jsoup.parse(p.attr("data-content"));
      String src = inline.select("[src]").attr("src");
      if(!src.isEmpty()) {
        result.getStreams().add(new StreamScrapeResult.Stream("Server " + i++, src));
      }
    }

    if(result.getStreams().size() > 0) {
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
