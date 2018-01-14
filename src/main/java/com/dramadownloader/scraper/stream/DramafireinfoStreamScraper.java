package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.ScrapeResult;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DramafireinfoStreamScraper implements StreamScraper {
  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    StreamScrapeResult result = new StreamScrapeResult(ScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(request.getUrl());
    Element currentEpisodeAnchor = doc.select(".episode-nav a.current").first();
    if(currentEpisodeAnchor == null) {
      return result;
    }

    int currentEpisode = Integer.parseInt(currentEpisodeAnchor.text().trim());
    String dramaPageUrl = doc.select(".episode-nav a.drama-name").first().attr("href");
    Document dramaPageDoc = HttpUtil.getDocument(dramaPageUrl);
    Elements anchors = dramaPageDoc.select("[id^=download-links] a");
    String streamUrl = anchors.get(currentEpisode-1).attr("href");

    result.getStreams().add(new StreamScrapeResult.Stream("Server 1", streamUrl));
    result.setStatus(ScrapeResult.Status.OK);

    return result;
  }
}
