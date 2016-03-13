package com.dramadownloader.scraper.file;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.ScrapeResult;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EmbeddriveFileScraper extends FileScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("cdn.dramacool.to");
    DOMAINS.add("cdn.dramanice.to");
  }

  @Override
  protected FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(ScrapeResult.Status.FAILED);

    // Direct links are available under <video> tag.

    Document doc = getDocument(url);
    Elements sources = doc.getElementsByTag("source");
    for(Element source : sources) {
      String fileUrl = source.attr("src");
      String label = source.attr("label");

      FileScrapeResult.File file = new FileScrapeResult.File(fileUrl, true, StringUtil.isNullOrEmpty(label) ? null : label);
      result.getFiles().add(file);
    }

    if(result.getFiles().size() > 0) {
      result.setStatus(ScrapeResult.Status.OK);
    }

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
