package com.dramadownloader.scraper.stream;

import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.scraper.util.HttpUtil;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GooddramaStreamScraper implements StreamScraper {
  private static final Logger log = Logger.getLogger(GooddramaStreamScraper.class);

  private final ScheduledExecutorService _scheduledExecutorService;

  public GooddramaStreamScraper(ScheduledExecutorService scheduledExecutorService) {
    super();
    _scheduledExecutorService = scheduledExecutorService;
  }

  @Override
  public StreamScrapeResult scrape(StreamScrapeRequest request) throws IOException {
    String url = request.getUrl();

    StreamScrapeResult result = new StreamScrapeResult(StreamScrapeResult.Status.FAILED);

    Document doc = HttpUtil.getDocument(url);
    Elements iframes = doc.select("#streams iframe[src]");

    List<Integer> goodPlaylists = new ArrayList<>();
    List<Integer> badPlaylists = new ArrayList<>();

    int playlistIdx;
    for(playlistIdx = 0; playlistIdx < iframes.size(); playlistIdx++) {
      String src = iframes.get(playlistIdx).attr("src");
      String hostname = HttpUtil.getHostname(src);
      assert hostname != null;
      if(hostname.contains("easyvideo.me")) {
        goodPlaylists.add(playlistIdx);
      } else {
        badPlaylists.add(playlistIdx);
      }
    }

    final Integer chosenPlaylist;
    if(goodPlaylists.size() > 0) {
      chosenPlaylist = goodPlaylists.get(0);
    } else if(badPlaylists.size() > 0) {
      chosenPlaylist = badPlaylists.get(0);
    } else {
      chosenPlaylist = null;
    }

    if(chosenPlaylist != null) {
      Elements parts = doc.select("#streams .vmargin").eq(chosenPlaylist).select(".part_list a[href]");
      if(parts.size() == 0) {
        Element iframe = iframes.get(chosenPlaylist);
        String streamUrl = iframe.attr("src");
        result.getStreams().add(new StreamScrapeResult.Stream("Server Standard", streamUrl));
      } else {
        CountDownLatch latch = new CountDownLatch(parts.size());
        Map<Integer, StreamScrapeResult.Stream> streams = new TreeMap<>();
        int seqNo = 1;
        for(Element part : parts) {
          Integer localSeqNo = seqNo;
          String pageUrl = part.attr("href");
          _scheduledExecutorService.submit(() -> {
            try {
              Document partDoc = HttpUtil.getDocument(pageUrl);
              Element iframe = partDoc.select("#streams iframe[src]").get(chosenPlaylist);
              String streamUrl = iframe.attr("src");
              streams.put(localSeqNo, new StreamScrapeResult.Stream("Part " + localSeqNo, streamUrl));
            } catch (IOException e) {
              log.error(e.getMessage(), e);
            } finally {
              latch.countDown();
            }
          });
          seqNo++;
        }

        try {
          latch.await(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          log.error(e.getMessage(), e);
        }

        result.getStreams().addAll(streams.values());
      }
    }

    String title = doc.select("#top_block h1").first().text().trim();
    if(!StringUtil.isNullOrEmpty(title)) {
      result.setTitle(title);
    }

    if(result.getStreams().size() > 0) {
      result.setStatus(StreamScrapeResult.Status.OK);
    }

    return result;
  }
}
