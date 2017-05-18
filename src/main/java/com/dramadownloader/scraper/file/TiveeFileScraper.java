package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.ScrapeResult;
import com.dramadownloader.scraper.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiveeFileScraper implements FileScraper {
  private final ObjectMapper _objectMapper;

  public TiveeFileScraper(ObjectMapper objectMapper) {
    super();
    _objectMapper = objectMapper;
  }

  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(ScrapeResult.Status.FAILED);

    String titleLinkName = null;
    String epNumber = null;

    // Look for titleLinkName
    URL urlObject = HttpUtil.createUrl(url);
    if(urlObject != null) {
      String urlPath = StringUtils.strip(urlObject.getPath(), "/");
      String[] pathParts = urlPath.split("/");
      titleLinkName = pathParts[pathParts.length -1];
    }

    // Look for epNumber
    Map<String, List<String>> queryParams = HttpUtil.getQueryParams(url);
    if(queryParams.get("ep") != null) {
      epNumber = queryParams.get("ep").get(0);
    }

    if(titleLinkName != null && epNumber != null) {
      String itUrl = HttpUtil.getHostname(url) + "/cloudfs/" + titleLinkName + "/" + titleLinkName + "_ep" + epNumber + ".itp";
      String json = HttpUtil.get(itUrl);

      Itp itp = _objectMapper.readValue(json, Itp.class);
      for(ItpPic itpPic : itp.picHolder.values()) {
        result.getFiles().add(new FileScrapeResult.File(itpPic.urlLink, true, "Quality " + itpPic.quality));
      }
    }

    if(result.getFiles().size() > 0) {
      result.setStatus(ScrapeResult.Status.OK);
    }

    return result;
  }

  private static class Itp {
    Map<String, ItpPic> picHolder = new HashMap<>();
  }

  private static class ItpPic {
    int quality;
    String urlLink;
    List<String> urlLinks = new ArrayList<>();
  }
}
