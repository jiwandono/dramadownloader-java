package com.dramadownloader.scraper.episode;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DramacoolcomEpisodeScraper extends EpisodeScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramacool.com");
    DOMAINS.add("www.dramacool.com");
    DOMAINS.add("dramacool.one");
    DOMAINS.add("www.dramacool.one");
    DOMAINS.add("dramacool.cc");
    DOMAINS.add("www.dramacool.cc");
  }

  @Override
  protected EpisodeScrapeResult scrapeInternal(EpisodeScrapeRequest request) throws IOException {
    String url = request.getUrl();

    EpisodeScrapeResult result = new EpisodeScrapeResult(EpisodeScrapeResult.Status.FAILED);

    Document doc = getDocument(url);
    Elements anchors = doc.select("#view-detail a[href]");

    for(Element a : anchors) {
      a.select(".timeago").html("");
      String episodeTitle = a.text().trim();
      String episodeUrl = a.attr("href");

      result.getEpisodes().add(new EpisodeScrapeResult.Episode(episodeTitle, episodeUrl));
    }

    if(result.getEpisodes().size() > 0)
      result.setStatus(EpisodeScrapeResult.Status.OK);

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
