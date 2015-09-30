package com.dramadownloader.drama.fetch.episode;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class DramaniceEpisodePageScraper extends EpisodePageScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramanice.net");
    DOMAINS.add("www.dramanice.net");
  }

  @Override
  protected EpisodeScrapeResult scrapeInternal(String url) throws IOException {
    EpisodeScrapeResult result = new EpisodeScrapeResult(EpisodeScrapeResult.Status.FAILED);

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
      if(streamUrl.contains("itag=18")) {
        result.getStreams().add(new EpisodeScrapeResult.Stream("Server SD", streamUrl));
      } else if(streamUrl.contains("itag=22")) {
        result.getStreams().add(new EpisodeScrapeResult.Stream("Server HD", streamUrl));
      } else {
        result.getStreams().add(new EpisodeScrapeResult.Stream("Server " + i++, streamUrl));
      }
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(EpisodeScrapeResult.Status.OK);
    }

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
