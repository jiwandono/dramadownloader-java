package com.dramadownloader.scraper.title;

import com.dramadownloader.core.model.Title;
import com.dramadownloader.scraper.util.HttpUtil;
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
    DOMAINS.add("dramacool.one");
    DOMAINS.add("www.dramacool.one");
    DOMAINS.add("dramacool.cc");
    DOMAINS.add("www.dramacool.cc");
  }

  @Override
  protected TitleScrapeResult scrapeInternal(TitleScrapeRequest request) throws IOException {
    String url = request.getUrl();

    TitleScrapeResult result = new TitleScrapeResult(TitleScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    Elements entires = doc.select(".listdramacool");

    for(Element entry : entires) {
      Element a = entry.select("a[href]").first();

      Title title = new Title();
      title.setTitle(a.text().trim());
      title.setUrl(a.attr("abs:href"));
      title.setType(Title.Type.DRAMA);
      title.setProviderId("dramacool");

      int classSeq = 1;
      for(String className : entry.classNames()) {
        if(classSeq == 6) {
          int year = Integer.parseInt(className, 10);
          if(year > 1900)
            title.setYear(year);
        } else if(classSeq >= 8 && classSeq < entry.classNames().size()) {
          String genre = className.trim().toLowerCase();
          title.getGenres().add(genre);
        }

        classSeq++;
      }

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
    String hostname = HttpUtil.getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
