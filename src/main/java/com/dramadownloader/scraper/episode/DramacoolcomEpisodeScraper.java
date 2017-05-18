package com.dramadownloader.scraper.episode;

import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DramacoolcomEpisodeScraper implements EpisodeScraper {
  @Override
  public EpisodeScrapeResult scrape(EpisodeScrapeRequest request) throws IOException {
    String url = request.getUrl();

    EpisodeScrapeResult result = new EpisodeScrapeResult(EpisodeScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
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
}
