package com.dramadownloader.scraper.title;

import com.dramadownloader.core.model.Title;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GooddramaTitleScraper implements TitleScraper {
  @Override
  public TitleScrapeResult scrape(TitleScrapeRequest request) throws IOException {
    String url = request.getUrl();
    String host = HttpUtil.getHostname(url);
    String providerId;
    if(host.contains("gooddrama")) {
      providerId = "gooddrama";
    } else if(host.contains("dramagalaxy")) {
      providerId = "dramagalaxy";
    } else if(host.contains("dramago")) {
      providerId = "dramago";
    } else {
      throw new IllegalStateException();
    }

    TitleScrapeResult result = new TitleScrapeResult(TitleScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    Elements anchors = doc.select(".series_index a[href]");

    for(Element a : anchors) {
      Title title = new Title();
      title.setTitle(a.text().trim());
      title.setUrl(a.attr("abs:href"));
      title.setType(Title.Type.DRAMA);
      title.setProviderId(providerId);

      result.getTitles().add(title);
    }

    if(result.getTitles().size() > 0) {
      result.setStatus(TitleScrapeResult.Status.OK);
    } else {
      result.setStatus(TitleScrapeResult.Status.FAILED);
    }

    return result;
  }
}
