package com.dramadownloader.scraper.file;

import com.dramadownloader.scraper.ScrapeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TiveeFileScraper extends FileScraper {
  private static Set<String> DOMAINS;

  static {
    DOMAINS = new HashSet<>();
    DOMAINS.add("tiveee.com");
    DOMAINS.add("www.tiveee.com");
  }

  private final ObjectMapper _objectMapper;

  public TiveeFileScraper(ObjectMapper objectMapper) {
    super();
    _objectMapper = objectMapper;
  }

  @Override
  protected FileScrapeResult scrapeInternal(FileScrapeRequest request) throws IOException {
    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(ScrapeResult.Status.FAILED);

    String titleLinkName = null;
    String epNumber = null;

    // Look for titleLinkName
    URL urlObject = createUrl(url);
    if(urlObject != null) {
      String urlPath = StringUtils.strip(urlObject.getPath(), "/");
      String[] pathParts = urlPath.split("/");
      titleLinkName = pathParts[pathParts.length -1];
    }

    // Look for epNumber
    Map<String, List<String>> queryParams = getQueryParams(url);
    if(queryParams.get("ep") != null) {
      epNumber = queryParams.get("ep").get(0);
    }

    if(titleLinkName != null && epNumber != null) {
      String itUrl = getHostname(url) + "/cloudfs/" + titleLinkName + "/" + titleLinkName + "_ep" + epNumber + ".itp";
      String json = get(itUrl);

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

  @Override
  public boolean isSupported(String url) {
    String hostname = getHostname(url);
    return DOMAINS.contains(hostname);
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
