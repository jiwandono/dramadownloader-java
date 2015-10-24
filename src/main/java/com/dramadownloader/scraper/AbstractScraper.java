package com.dramadownloader.scraper;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractScraper<TRequest extends ScrapeRequest, TResult extends ScrapeResult> implements Scraper<TRequest, TResult> {
  private static final Logger log = Logger.getLogger(AbstractScraper.class);

  private static final int CONNECTION_TIMEOUT_MSEC = 30000;
  private static final int MAX_BODY_SIZE = 1024 * 1024 * 8;
  private static final List<String> USER_AGENTS;

  static {
    USER_AGENTS = new ArrayList<>();
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 5.1; rv:8.0; en_us) Gecko/20100101 Firefox/8.0");
    USER_AGENTS.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:9.0) Gecko/20100101 Firefox/9.0");
    USER_AGENTS.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1309.0 Safari/537.17");
    USER_AGENTS.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.13 (KHTML, like Gecko) Chrome/24.0.1290.1 Safari/537.13");
    USER_AGENTS.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.6 Safari/537.11");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.15 (KHTML, like Gecko) Chrome/24.0.1295.0 Safari/537.15");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.14 (KHTML, like Gecko) Chrome/24.0.1292.0 Safari/537.14");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.13 (KHTML, like Gecko) Chrome/24.0.1290.1 Safari/537.13");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.2; rv:9.0.1) Gecko/20100101 Firefox/9.0.1");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; U;WOW64; de;rv:11.0) Gecko Firefox/11.0");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko Firefox/11.0");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 5.1; rv:11.0) Gecko Firefox/11.0");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/20120403211507 Firefox/14.0.1");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; rv:12.0) Gecko/ 20120405 Firefox/14.0.1");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.0; rv:14.0) Gecko/20100101 Firefox/14.0.1");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:15.0) Gecko/20120910144328 Firefox/15.0.2");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20120427 Firefox/15.0a1");
    USER_AGENTS.add("Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; rv:15.0) Gecko/20120716 Firefox/15.0a2");
    USER_AGENTS.add("Mozilla/5.0 (U; Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");
    USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; Media Center PC 6.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C)");
    USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Zune 3.0)");
    USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0)");
    USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729)");
    USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; Media Center PC 6.0; Zune 3.0)");
    USER_AGENTS.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0; chromeframe/11.0.696.57)");
    USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
    USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
    USER_AGENTS.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24");
    USER_AGENTS.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.24 (KHTML, like Gecko) RockMelt/0.9.58.494 Chrome/11.0.696.71 Safari/534.24");
    USER_AGENTS.add("Opera/12.80 (Windows NT 5.1; U; en) Presto/2.10.289 Version/12.02");
    USER_AGENTS.add("Opera/12.0(Windows NT 5.2;U;en)Presto/22.9.168 Version/12.00");
    USER_AGENTS.add("Opera/9.80 (Windows NT 6.1; WOW64; U; pt) Presto/2.10.229 Version/11.62");
    USER_AGENTS.add("Opera/9.80 (X11; Linux x86_64; U; fr) Presto/2.9.168 Version/11.50");
    USER_AGENTS.add("Opera/9.80 (Windows NT 6.0; U; en) Presto/2.8.99 Version/11.10");
  }

  private static String getRandomUserAgent() {
    int max = USER_AGENTS.size();
    int index = ThreadLocalRandom.current().nextInt(max);
    return USER_AGENTS.get(index);
  }

  protected static Document getDocument(String url) throws IOException {
    return getDocument(url, new HashMap<>());
  }

  protected static Document getDocument(String url, Map<String, String> cookies) throws IOException{
    URL urlObject = createUrl(url);
    if(urlObject == null)
      return new Document("");

    Connection connection = Jsoup.connect(urlObject.toString())
        .userAgent(getRandomUserAgent())
        .timeout(CONNECTION_TIMEOUT_MSEC)
        .maxBodySize(MAX_BODY_SIZE)
        .cookies(cookies);

    return connection.get();
  }

  protected static String getHostname(String urlString) {
    URL url = createUrl(urlString);
    if(url != null)
      return url.getHost().toLowerCase();

    return null;
  }

  protected static String getFinalUrl(String url) throws IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
    con.setInstanceFollowRedirects(false);
    con.connect();
    con.getInputStream();

    if(con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
      String redirectUrl = con.getHeaderField("Location");
      return getFinalUrl(redirectUrl);
    }

    return url;
  }

  protected static Map<String, List<String>> getQueryParams(String url) throws IOException {
    Map<String, List<String>> params = new HashMap<>();
    String[] urlParts = url.split("\\?");
    if (urlParts.length > 1) {
      String query = urlParts[1];
      for (String param : query.split("&")) {
        String[] pair = param.split("=");
        String key = URLDecoder.decode(pair[0], "UTF-8");
        String value = "";
        if (pair.length > 1) {
          value = URLDecoder.decode(pair[1], "UTF-8");
        }

        List<String> values = params.get(key);
        if (values == null) {
          values = new ArrayList<>();
          params.put(key, values);
        }
        values.add(value);
      }
    }

    return params;
  }

  private static URL createUrl(String urlString) {
    try {
      return new URL(urlString);
    } catch (MalformedURLException e) {
      if(e.getMessage().contains("no protocol")) {
        return createUrl("http://" + urlString);
      } else {
        log.warn("Unable to create URL: " + e.getMessage());
      }
    }

    return null;
  }
}
