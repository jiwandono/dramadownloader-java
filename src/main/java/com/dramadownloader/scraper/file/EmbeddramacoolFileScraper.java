package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.ScrapeResult;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class EmbeddramacoolFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    FileScrapeResult result = new FileScrapeResult(ScrapeResult.Status.FAILED);

    Document document = HttpUtil.getDocument(request.getUrl());

    Elements elements = document.select("video source[src]");
    for(Element element : elements) {
      String fileUrl = element.attr("src");
      String label = element.attr("label");
      result.getFiles().add(new FileScrapeResult.File(fileUrl, true, label));
    }

    if(result.getFiles().size() > 0) {
      result.setStatus(FileScrapeResult.Status.OK);
    }

    return result;
  }
}
