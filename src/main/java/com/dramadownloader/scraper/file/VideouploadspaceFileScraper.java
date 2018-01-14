package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.ScrapeResult;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class VideouploadspaceFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    FileScrapeResult result = new FileScrapeResult(ScrapeResult.Status.FAILED);

    Document document = HttpUtil.getDocument(request.getUrl());
    Element anchor = document.select(".dowload a").first(); // Intentional typo
    String href = anchor.attr("href");

    result.getFiles().add(new FileScrapeResult.File(href, true));
    result.setStatus(ScrapeResult.Status.OK);

    return result;
  }
}