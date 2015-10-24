package com.dramadownloader.scraper.file;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class EasyvideoFileScraper extends FileScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("easyvideo.me");
    DOMAINS.add("www.easyvideo.me");
  }

  @Override
  protected FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.FAILED);

    // Direct link is usually in "url: 'http://blahblah/blah/blah.mp4'" format, inside script tag.

    String origFilename = getQueryParams(url).get("file").get(0);
    String origFileBasename = origFilename.substring(0, origFilename.lastIndexOf('.'));

    Document doc = getDocument(url);
    Elements scripts = doc.getElementsByTag("script");
    for(Element script : scripts) {
      for(DataNode dataNode : script.dataNodes()) {
        String actualScript = dataNode.getWholeData();
        String nospaces = actualScript.replace(" ", "");
        int pos1 = nospaces.indexOf("url:");
        int pos2 = nospaces.indexOf("'", pos1 + 5);
        if(pos1 != -1 && pos2 != -1 && pos2 > pos1) {
          String fileUrl = nospaces.substring(pos1 + 5, pos2);

          if(fileUrl.contains("googleusercontent.com/")) {
            fileUrl = getFinalUrl(fileUrl);
          }

          if(fileUrl.contains("googlevideo.com/")) {
            fileUrl += "&title=" + URLEncoder.encode(origFileBasename, "UTF-8");
          }

          result.setStatus(FileScrapeResult.Status.OK);
          result.getFiles().add(new FileScrapeResult.File(fileUrl, true));

          return result;
        }
      }
    }

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
