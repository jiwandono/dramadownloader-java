package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.ScrapeResult;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TiveeStreamScraper implements StreamScraper {
  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(ScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    String title = doc.getElementsByClass("mtitle").first().text();

    result.getStreams().add(new StreamScrapeResult.Stream("Tiveee", url));

    if(result.getStreams().size() > 0) {
      result.setTitle(title);
      result.setStatus(StreamScrapeResult.Status.OK);
    }

    return result;
  }
}
