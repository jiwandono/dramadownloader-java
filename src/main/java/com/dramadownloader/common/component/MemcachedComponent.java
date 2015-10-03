package com.dramadownloader.common.component;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;

public class MemcachedComponent {
  private static final String ADDRESS = "localhost:11211";

  private final MemcachedClient _memcachedClient;

  public MemcachedComponent() {
    try {
      _memcachedClient = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(ADDRESS));
    } catch (IOException e) {
      throw new IllegalStateException("Failed to intialize memcached client.", e);
    }
  }

  public MemcachedClient getMemcachedClient() {
    return _memcachedClient;
  }
}
