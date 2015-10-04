package com.dramadownloader.core;

import com.dramadownloader.core.model.Title;

import java.io.IOException;
import java.util.List;

public interface TitleAccessor {
  Title getTitle(String id) throws IOException;

  List<Title> getTitlesByPrefix(String prefix) throws IOException;

  void insertTitle(Title title) throws IOException;

  void upsertTitle(Title title) throws IOException;
}
