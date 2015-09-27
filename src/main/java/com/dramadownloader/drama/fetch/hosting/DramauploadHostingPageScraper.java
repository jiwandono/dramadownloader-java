package com.dramadownloader.drama.fetch.hosting;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DramauploadHostingPageScraper extends HostingPageScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramaupload.com");
    DOMAINS.add("www.dramaupload.com");
  }

  @Override
  protected HostingScrapeResult scrapeInternal(String url) throws IOException {
    HostingScrapeResult result = new HostingScrapeResult(HostingScrapeResult.Status.FAILED);

    // Direct link is usually in "file: 'http://blahblah/blah/blah.mp4'" format, inside script tag.

    Document doc = getDocument(url);
    Elements scripts = doc.getElementsByTag("script");
    for(Element script : scripts) {
      for(DataNode dataNode : script.dataNodes()) {
        String actualScript = dataNode.getWholeData();
        String nospaces = actualScript.replace(" ", "");
        int pos1 = nospaces.indexOf("file:");
        int pos2 = nospaces.indexOf("'", pos1 + 6);
        if(pos1 != -1 && pos2 != -1 && pos2 > pos1) {
          String fileUrl = nospaces.substring(pos1 + 6, pos2);
          String downloadUrl = "http://fetch.dramadownloader.com/passthru/" + Base64.encodeBase64String(fileUrl.getBytes());

          result.setStatus(HostingScrapeResult.Status.OK);
          result.getDownloadables().add(new HostingScrapeResult.Downloadable(downloadUrl, true));

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