package com.dramadownloader.scraper.stream;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.util.HttpUtil;
import org.apache.log4j.Logger;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DramacoolcomStreamScraper implements StreamScraper {
  private static final Logger log = Logger.getLogger(DramacoolcomStreamScraper.class);

  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    Elements tabs = doc.select(".tab-content-video");

    int tabIndex = 1;
    for(Element tab : tabs) {
      String streamUrl = null;

      // Look for something with url.
      Elements candidates = new Elements();
      candidates.addAll(tab.select("iframe[src]"));
      candidates.addAll(tab.select("video source[src]"));
      if(!candidates.isEmpty()) {
        streamUrl = candidates.first().attr("src");
        if(StringUtil.isNullOrEmpty(streamUrl)) {
          streamUrl = candidates.first().attr("data-src");
        }
      }

      if(streamUrl != null) {
        result.getStreams().add(new StreamScrapeResult.Stream("Server " + tabIndex++, streamUrl));
      }
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(StreamScrapeResult.Status.OK);

      String title = doc.select("h1").first().text().trim();
      if(!StringUtil.isNullOrEmpty(title)) {
        result.setTitle(title);
      }
    }

    return result;
  }
}
