package com.dramadownloader.drama.fetch.episode;

import org.apache.log4j.Logger;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DramacoolcomEpisodePageScraper extends EpisodePageScraper {
  private static final Logger log = Logger.getLogger(DramacoolcomEpisodePageScraper.class);

  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("dramacool.com");
    DOMAINS.add("www.dramacool.com");
  }

  @Override
  protected EpisodeScrapeResult scrapeInternal(String url) throws IOException {
    EpisodeScrapeResult result = new EpisodeScrapeResult(EpisodeScrapeResult.Status.FAILED);

    Document doc = getDocument(url);
    Elements anchors = doc.select(".detail-ep-film .nav a");

    for(Element a : anchors) {
      String target = a.attr("href"); // Points to video container eg. #servercool, #server1, etc.
      Element targetEl = doc.select(target).first();

      String name = a.text();
      String streamUrl = null;

      // Look for something with url.
      Elements candidates = new Elements();
      candidates.addAll(targetEl.select("iframe[src]"));
      candidates.addAll(targetEl.select("video source[src]"));
      if(!candidates.isEmpty()) {
        streamUrl = candidates.first().attr("src");
      } else {
        // Most probably using jwplayer. Look in the script tag.
        Elements scripts = targetEl.getElementsByTag("script");
        for(Element script : scripts) {
          for(DataNode dataNode : script.dataNodes()) {
            String actualScript = dataNode.getWholeData();
            if(actualScript.contains(target)) {
              // Split the script by whitespaces, look for source url.
              String[] tokens = actualScript.split(" ");
              for(String token : tokens) {
                if(token.startsWith("src=")) {
                  try {
                    streamUrl = token.substring(5, token.length() - 1);
                  } catch (Exception e) {
                    log.error("Caught exception while parsing " + token, e);
                  }
                }
              }
            }
          }
        }
      }

      if(streamUrl != null) {
        result.setStatus(EpisodeScrapeResult.Status.OK);
        result.getStreams().add(new EpisodeScrapeResult.Stream(name, streamUrl));
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
