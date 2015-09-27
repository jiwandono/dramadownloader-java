package com.dramadownloader.drama.fetch.episode;

import com.dramadownloader.drama.fetch.hosting.HostingScrapeResult;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DramafirecomEpisodePageScraper extends EpisodePageScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramafire.com");
    DOMAINS.add("www.dramafire.com");
  }

  @Override
  protected EpisodeScrapeResult scrapeInternal(String url) throws IOException {
    EpisodeScrapeResult result = new EpisodeScrapeResult(EpisodeScrapeResult.Status.FAILED);

    Document doc = getDocument(url);
    Elements candidates = new Elements();
    candidates.addAll(doc.select("#main-content .post iframe[src]"));
    candidates.addAll(doc.select("#main-content .post video source[src]"));

    int i = 1;
    for(Element candidate : candidates) {
      String name = "Server " + i++;
      String streamUrl = candidate.attr("src");

      result.getStreams().add(new EpisodeScrapeResult.Stream(name, streamUrl));
    }

    Elements scripts = doc.getElementsByTag("script");
    for(Element script : scripts) {
      for(DataNode dataNode : script.dataNodes()) {
        String actualScript = dataNode.getWholeData();
        String nospaces = actualScript.replace(" ", "");
        int pos1 = nospaces.indexOf("file:");
        int pos2 = nospaces.indexOf("'", pos1 + 6);
        if(pos1 != -1 && pos2 != -1 && pos2 > pos1) {
          String fileUrl = nospaces.substring(pos1 + 6, pos2);
          result.getStreams().add(new EpisodeScrapeResult.Stream("Server " + i++, fileUrl));
        }
      }
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(EpisodeScrapeResult.Status.OK);
    }

    return result;
  }

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
  }
}
