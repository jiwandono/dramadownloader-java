package com.dramadownloader.scraper.title;

import com.dramadownloader.core.model.Title;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GooddramaTitleScraper extends TitleScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("gooddrama.net");
    DOMAINS.add("www.gooddrama.net");
    DOMAINS.add("dramagalaxy.eu");
    DOMAINS.add("www.dramagalaxy.eu");
    DOMAINS.add("dramago.com");
    DOMAINS.add("www.dramago.com");
  }

  @Override
  protected TitleScrapeResult scrapeInternal(TitleScrapeRequest request) throws IOException {
    String url = request.getUrl();
    String host = getHostname(url);
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

    Document doc = getDocument(url);
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

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
