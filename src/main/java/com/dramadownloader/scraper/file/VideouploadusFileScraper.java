package com.dramadownloader.scraper.file;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class VideouploadusFileScraper extends FileScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("videoupload.us");
    DOMAINS.add("www.videoupload.us");
  }

  @Override
  protected FileScrapeResult scrapeInternal(String url) throws IOException {
    // URL is usually in http://videoupload.us/drama/embed-MTU5NDI=.html format.
    // Just change embed- to video- then it will show a page with download link.
    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.FAILED);

    if(url.contains("videoupload.us/drama/video")) {
      // Leave as is.
      result.getFiles().add(new FileScrapeResult.File(url, false));
    } else if(url.contains("videoupload.us/drama/embed-")) {
      String downloadUrl = url.replace("/embed-", "/video-");
      result.getFiles().add(new FileScrapeResult.File(downloadUrl, false));
    } else if(url.contains("videoupload.us/embed/drama-")) {
      String downloadUrl = url.replace("/embed/drama-", "/drama/video-");
      result.getFiles().add(new FileScrapeResult.File(downloadUrl, false));
    } else {
      // Look in the script tag. Actually there are multiple streams, but maybe later...
      Set<String> fileSet = new HashSet<>();
      Elements scripts = getDocument(url).getElementsByTag("script");
      for(Element script : scripts) {
        for(DataNode dataNode : script.dataNodes()) {
          String actualScript = dataNode.getWholeData();
          String nospaces = actualScript.replace(" ", "");
          int pos1 = nospaces.indexOf("file:'");
          int pos2 = nospaces.indexOf("'", pos1 + 6);
          if(pos1 != -1 && pos2 != -1 && pos2 > pos1) {
            String downloadUrl = nospaces.substring(pos1 + 6, pos2);
            fileSet.add(downloadUrl);
          }
        }
      }

      for(String downloadUrl : fileSet) {
        result.getFiles().add(new FileScrapeResult.File(downloadUrl, true));
      }
    }

    if(result.getFiles().size() > 0) {
      result.setStatus(FileScrapeResult.Status.OK);
    }

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
