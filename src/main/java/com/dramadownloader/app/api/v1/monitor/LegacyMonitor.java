package com.dramadownloader.app.api.v1.monitor;

import java.io.IOException;
import java.util.List;

public interface LegacyMonitor {
  void onRequestProcessed(String url, long timestamp, boolean success) throws IOException;

  List<LegacyMonitorEntry> getEntries(long minTimestamp) throws IOException;
}
