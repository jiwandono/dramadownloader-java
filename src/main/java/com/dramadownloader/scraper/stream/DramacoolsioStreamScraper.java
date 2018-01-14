package com.dramadownloader.scraper.stream;

import com.dramadownloader.scraper.ScrapeResult;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class DramacoolsioStreamScraper implements StreamScraper {
  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(ScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    String title = doc.select(".watch-drama h1").first().text();
    Element anchor = doc.select(".download a").first();
    String href = anchor.attr("href");
    result.getStreams().add(new StreamScrapeResult.Stream("Server 1", href));
    result.setTitle(title);
    result.setStatus(ScrapeResult.Status.OK);

    return result;
  }
}
