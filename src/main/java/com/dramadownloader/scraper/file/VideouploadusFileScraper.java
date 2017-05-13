package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
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
    DOMAINS.add("videoupload.biz");
    DOMAINS.add("www.videoupload.biz");
    DOMAINS.add("videoupload.space");
    DOMAINS.add("www.videoupload.space");
  }

  @Override
  protected FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException {
    // URL is usually in http://videoupload.us/drama/embed-MTU5NDI=.html format.
    // Just change embed- to video- then it will show a page with download link.

    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.FAILED);

    if(url.contains("/drama/video")) {
      // Leave as is.
      result.getFiles().add(new FileScrapeResult.File(url, false));
    } else if(url.contains("/video/drama/embed-")) {
      Set<String> fileSet = new HashSet<>();
      Document document = HttpUtil.getDocument(url);

      Elements sources = document.getElementsByTag("source");
      for(Element source : sources) {
        String downloadUrl = source.attr("src");
        fileSet.add(downloadUrl);
        break; // For now get only the first one.
      }

      for(String downloadUrl : fileSet) {
        result.getFiles().add(new FileScrapeResult.File(downloadUrl, true));
      }
    } else if(url.contains("/drama/embed-")) {
      String downloadUrl = url.replace("/embed-", "/video-");
      result.getFiles().add(new FileScrapeResult.File(downloadUrl, false));
    } else if(url.contains("/embed/drama-")) {
      String downloadUrl = url.replace("/embed/drama-", "/drama/video-");
      result.getFiles().add(new FileScrapeResult.File(downloadUrl, false));
    } else {
      // Look in the script tag. Actually there are multiple streams, but maybe later...
      Set<String> fileSet = new HashSet<>();
      Document document = HttpUtil.getDocument(url);

      Elements scripts = document.getElementsByTag("script");
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
    String hostname = HttpUtil.getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
