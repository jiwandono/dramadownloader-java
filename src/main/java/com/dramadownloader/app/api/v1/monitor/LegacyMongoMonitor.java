package com.dramadownloader.app.api.v1.monitor;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.BsonDateTime;
import org.mongodb.morphia.Morphia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LegacyMongoMonitor implements LegacyMonitor {
  private final DBCollection _coll;
  private final Morphia _morphia;

  public LegacyMongoMonitor(DB db, Morphia morphia) {
    _coll = db.getCollection("monitor.legacy");
    _morphia = morphia.map(LegacyMonitorEntry.class);
  }

  @Override
  public void onRequestProcessed(String url, long timestamp, boolean success) throws IOException {
    LegacyMonitorEntry entry = new LegacyMonitorEntry();
    entry.setUrl(url);
    entry.setTimestamp(timestamp);
    entry.setSuccess(success);

    DBObject object = _morphia.toDBObject(entry);
    object.put("_createdAt", new BsonDateTime(timestamp));

    _coll.insert(object);
  }

  @Override
  public List<LegacyMonitorEntry> getEntries(long minTimestamp) throws IOException {
    List<LegacyMonitorEntry> entries = new ArrayList<>();
    DBObject condition = new BasicDBObject().append("timestamp", new BasicDBObject().append("$gte", minTimestamp));

    try (DBCursor cursor = _coll.find(condition)) {
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        LegacyMonitorEntry entry = _morphia.fromDBObject(LegacyMonitorEntry.class, object);
        entries.add(entry);
      }
    }

    return entries;
  }
}
