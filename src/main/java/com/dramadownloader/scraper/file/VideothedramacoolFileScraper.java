package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class VideothedramacoolFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.FAILED);

    // Look in the script tag. Actually there are multiple streams, but maybe later...
    Document document = HttpUtil.getDocument(request.getUrl());

    Elements scripts = document.getElementsByTag("script");
    for(Element script : scripts) {
      for(DataNode dataNode : script.dataNodes()) {
        String actualScript = dataNode.getWholeData();
        String nospaces = actualScript.replace(" ", "");
        int pos1 = nospaces.indexOf("\"file\":\"");
        int pos2 = nospaces.indexOf("\"", pos1 + 8);
        if(pos1 != -1 && pos2 != -1 && pos2 > pos1) {
          String downloadUrl = nospaces.substring(pos1 + 8, pos2);
          result.getFiles().add(new FileScrapeResult.File(downloadUrl, true));
        }
      }
    }

    if(result.getFiles().size() > 0) {
      result.setStatus(FileScrapeResult.Status.OK);
    }

    return result;
  }
}
