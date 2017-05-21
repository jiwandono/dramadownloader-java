package com.dramadownloader.scraper.stream;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.util.HttpUtil;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ThedramacoolcomStreamScraper implements StreamScraper {
  private static final Logger log = Logger.getLogger(ThedramacoolcomStreamScraper.class);

  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);

    // Look for something with url.
    Elements candidates = new Elements();
    candidates.addAll(doc.select(".entry iframe[src]"));
    candidates.addAll(doc.select(".entry video source[src]"));
    int i = 1;
    for(Element candidate : candidates) {
      String streamUrl = candidate.attr("src");

      result.getStreams().add(new StreamScrapeResult.Stream("Server " + i++, streamUrl));
    }

    String title = doc.select(".entry-title ").first().text().trim();
    if(!StringUtil.isNullOrEmpty(title)) {
      result.setTitle(title);
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(StreamScrapeResult.Status.OK);
    }

    return result;
  }
}
