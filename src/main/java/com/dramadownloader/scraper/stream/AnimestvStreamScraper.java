package com.dramadownloader.scraper.stream;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.util.HttpUtil;
import org.apache.log4j.Logger;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AnimestvStreamScraper implements StreamScraper {
  private static final Logger log = Logger.getLogger(AnimestvStreamScraper.class);

  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
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
            String nospaces = actualScript.replace(" ", "");
            if(actualScript.contains(target)) {
              int pos1 = nospaces.indexOf("src='");
              int pos2 = nospaces.indexOf("'", pos1 + 5);
              if(pos1 != -1 && pos2 != -1 && pos2 > pos1) {
                streamUrl = nospaces.substring(pos1 + 5, pos2);
              }
            }

            if(streamUrl == null) {
              int pos1 = nospaces.indexOf("file:'");
              int pos2 = nospaces.indexOf("'", pos1 + 6);
              if(pos1 != -1 && pos2 != -1 && pos2 > pos1) {
                streamUrl = nospaces.substring(pos1 + 6, pos2);
              }
            }
          }
        }
      }

      if(streamUrl != null) {
        result.setStatus(StreamScrapeResult.Status.OK);
        result.getStreams().add(new StreamScrapeResult.Stream(name, streamUrl));
      }
    }

    String title = doc.select(".title-detail-ep-film h1").first().text().trim();
    if(!StringUtil.isNullOrEmpty(title)) {
      result.setTitle(title);
    }

    return result;
  }
}
