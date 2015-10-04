package com.dramadownloader.common.component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CommonComponent {
  private static final int THREAD_POOL_SIZE = 16;
  private static final String MONGO_ADDRESS = "localhost";

  private final ScheduledExecutorService _scheduledExecutorService;
  private final ObjectMapper _objectMapper;
  private final Morphia _morphia;
  private final DB _dbMonitor;
  private final DB _dbData;

  public CommonComponent() {
    _scheduledExecutorService = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
    _objectMapper = new ObjectMapper()
        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    _morphia = new Morphia();

    MongoClient mongoClient = new MongoClient(MONGO_ADDRESS);
    _dbMonitor = mongoClient.getDB("dramadownloader-monitor");
    _dbData = mongoClient.getDB("dramadownloader-data");
  }

  public ScheduledExecutorService getScheduledExecutorService() {
    return _scheduledExecutorService;
  }

  public ObjectMapper getObjectMapper() {
    return _objectMapper;
  }

  public Morphia getMorphia() {
    return _morphia;
  }

  public DB getDbMonitor() {
    return _dbMonitor;
  }

  public DB getDbData() {
    return _dbData;
  }
}
