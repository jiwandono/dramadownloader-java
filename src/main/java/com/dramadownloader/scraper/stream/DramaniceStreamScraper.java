package com.dramadownloader.scraper.stream;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class DramaniceStreamScraper implements StreamScraper {
  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
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

    String title = doc.select("#main-content h1").first().text().trim();
    if(!StringUtil.isNullOrEmpty(title)) {
      result.setTitle(title);
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(StreamScrapeResult.Status.OK);
    }

    return result;
  }
}
