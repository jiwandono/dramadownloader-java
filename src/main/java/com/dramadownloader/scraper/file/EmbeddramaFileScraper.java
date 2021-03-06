package com.dramadownloader.scraper.file;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class EmbeddramaFileScraper implements FileScraper {
  @Override
  public FileScrapeResult scrape(FileScrapeRequest request) throws IOException {
    // Video download URL is usually embedded in the link, base64-encoded in the GET url query.
    // Decode the encoded part to get download link.

    String url = request.getUrl();

    FileScrapeResult result = new FileScrapeResult(FileScrapeResult.Status.FAILED);

    List<NameValuePair> pairs;
    try {
      pairs = URLEncodedUtils.parse(new URI(url), "UTF-8");
    } catch (URISyntaxException e) {
      return result;
    }

    for(NameValuePair pair : pairs) {
      if(pair.getName().equals("id")) {
        String value = pair.getValue();
        String downloadUrl = new String(Base64.decodeBase64(value));

        result.setStatus(FileScrapeResult.Status.OK);
        result.getFiles().add(new FileScrapeResult.File(downloadUrl, true));

        return result;
      }
    }

    return result;
  }
}
