package com.dramadownloader.scraper.title;

import com.dramadownloader.core.model.Title;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DramacoolcomTitleScraper extends TitleScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramacool.com");
    DOMAINS.add("www.dramacool.com");
  }

  @Override
  protected TitleScrapeResult scrapeInternal(TitleScrapeRequest request) throws IOException {
    String url = request.getUrl();

    TitleScrapeResult result = new TitleScrapeResult(TitleScrapeResult.Status.FAILED);

    Document doc = getDocument(url);
    Elements anchors = doc.select(".listdramacool a[href]");

    for(Element a : anchors) {
      Title title = new Title();
      title.setTitle(a.text().trim());
      title.setUrl(a.attr("abs:href"));
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
