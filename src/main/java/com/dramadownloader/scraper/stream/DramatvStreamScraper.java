package com.dramadownloader.scraper.stream;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DramatvStreamScraper implements StreamScraper {
  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
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

    String title = doc.select(".movie-detail h3.title").first().text().trim();
    if(!StringUtil.isNullOrEmpty(title)) {
      result.setTitle(title);
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(StreamScrapeResult.Status.OK);
    }

    return result;
  }
}
