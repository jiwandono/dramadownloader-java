package com.dramadownloader.scraper.stream;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.util.HttpUtil;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DramafirecomStreamScraper implements StreamScraper {

  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    Elements candidates = new Elements();
    candidates.addAll(doc.select("#main-content .post iframe[src]"));
    candidates.addAll(doc.select("#main-content .post video source[src]"));

    int i = 1;
    for(Element candidate : candidates) {
      String name = "Server " + i++;
      String streamUrl = candidate.attr("src");

      result.getStreams().add(new StreamScrapeResult.Stream(name, streamUrl));
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
          result.getStreams().add(new StreamScrapeResult.Stream("Server " + i++, fileUrl));
        }
      }
    }

    Elements embeds = doc.select("#main-content .post embed[flashvars]");
    for(Element embed : embeds) {
      String flashvars = embed.attr("flashvars");
      String[] flashvarsSplit = flashvars.split("&");
      for(String flashvar : flashvarsSplit) {
        if(flashvar.startsWith("proxy.link=")) {
          String fileUrl = flashvar.replace("proxy.link=", "");
          result.getStreams().add(new StreamScrapeResult.Stream("Server " + i++, fileUrl));
        }
      }
    }

    String title = doc.select("h1.title").first().text().trim();
    if(!StringUtil.isNullOrEmpty(title)) {
      result.setTitle(title);
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(StreamScrapeResult.Status.OK);
    }

    return result;
  }
}
