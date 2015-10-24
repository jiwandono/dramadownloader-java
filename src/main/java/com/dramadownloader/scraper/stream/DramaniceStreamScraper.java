package com.dramadownloader.scraper.stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class DramaniceStreamScraper extends StreamScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramanice.net");
    DOMAINS.add("www.dramanice.net");
    DOMAINS.add("dramanice.us");
    DOMAINS.add("www.dramanice.us");
    DOMAINS.add("dramanice.com");
    DOMAINS.add("www.dramanice.com");
  }

  @Override
  protected StreamScrapeResult scrapeInternal(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = getDocument(url);
    Set<String> streamUrls = new LinkedHashSet<>();

    // Look for something with url.
    Elements candidates = new Elements();
    candidates.addAll(doc.select(".desc-detail-ep-film iframe[src]"));
    candidates.addAll(doc.select(".desc-detail-ep-film video source[src]"));
    for(Element el : candidates) {
      streamUrls.add(el.attr("src"));
    }

    // The already available download button.
    Element anchor = doc.getElementById("download_link");
    if(anchor != null)
      streamUrls.add(anchor.attr("href"));

    int i = 1;
    for(String streamUrl : streamUrls) {
      if(streamUrl.contains("itag=18") || streamUrl.contains("=m18")) {
        result.getStreams().add(new StreamScrapeResult.Stream("Server SD", streamUrl));
      } else if(streamUrl.contains("itag=22") || streamUrl.contains("=m22")) {
        result.getStreams().add(new StreamScrapeResult.Stream("Server HD", streamUrl));
      } else {
        result.getStreams().add(new StreamScrapeResult.Stream("Server " + i++, streamUrl));
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
