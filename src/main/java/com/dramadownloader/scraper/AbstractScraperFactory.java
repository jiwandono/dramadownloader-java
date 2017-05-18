package com.dramadownloader.scraper;

import com.dramadownloader.scraper.util.HttpUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractScraperFactory<T extends Scraper> implements ScraperFactory<T> {
  private final Map<Class<? extends T>, T> _classMap;
  private final Map<String, T> _domainMap;

  public AbstractScraperFactory() {
    _classMap = new HashMap<>();
    _domainMap = new HashMap<>();
  }

  @Override
  public T getScraper(String url) {
    String hostname  = HttpUtil.getHostname(url);

    // Look for exact match
    T instance = _domainMap.get(hostname);
    if(instance != null) {
      return instance;
    }

    // Look for suffix match
    for(String domain : _domainMap.keySet()) {
      if(hostname.endsWith(domain)) {
        return _domainMap.get(domain);
      }
    }

    return null;
  }

  public void register(Class<? extends T> clazz, T instance) {
    _classMap.put(clazz, instance);
  }

  public void register(String domain, Class<? extends T> clazz) {
    _domainMap.put(domain, _classMap.get(clazz));
  }
}
