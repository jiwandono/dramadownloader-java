package com.dramadownloader.core;

import com.dramadownloader.common.util.HashUtil;
import com.dramadownloader.core.model.Title;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import org.mongodb.morphia.Morphia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TitleMongoAccessor implements TitleAccessor {
  private final DBCollection _coll;
  private final Morphia _morphia;

  public TitleMongoAccessor(DB db, Morphia morphia) {
    _coll = db.getCollection("title.title");
    _morphia = morphia.map(Title.class);
  }

  @Override
  public Title getTitle(String id) throws IOException {
    DBObject condition = new BasicDBObject().append("_id", id);
    DBObject object = _coll.findOne(condition);
    Title title = _morphia.fromDBObject(Title.class, object);

    return title;
  }

  @Override
  public List<Title> getTitlesByPrefix(String prefix) throws IOException {
    char letter = prefix.charAt(0);
    DBObject condition = new BasicDBObject();

    if(letter >= 'a' && letter <= 'z') {
      condition.put("title", Pattern.compile("^" + letter, Pattern.CASE_INSENSITIVE));
    } else if (letter == '0') {
      condition.put("title", new BasicDBObject().append("$not", Pattern.compile("^[a-z]", Pattern.CASE_INSENSITIVE)));
    } else {
      return new ArrayList<>();
    }

    List<Title> titles = new ArrayList<>();
    try (DBCursor cursor = _coll.find(condition)) {
      while (cursor.hasNext()) {
        DBObject object = cursor.next();
        Title title = _morphia.fromDBObject(Title.class, object);
        titles.add(title);
      }
    }

    return titles;
  }

  @Override
  public void upsertTitle(Title title) throws IOException {
    title.setId(generateId(title));

    DBObject condition = new BasicDBObject().append("_id", title.getId());
    DBObject object = _morphia.toDBObject(title);
    _coll.update(condition, object, true, false, WriteConcern.JOURNAL_SAFE);
  }

  private static String generateId(Title title) {
    return HashUtil.sha256(title.getProviderId() + "|" + title.getTitle()).substring(0, 10);
  }
}
