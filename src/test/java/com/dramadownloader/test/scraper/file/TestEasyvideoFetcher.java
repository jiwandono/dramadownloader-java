package com.dramadownloader.test.scraper.file;

import com.dramadownloader.scraper.file.EasyvideoFileScraper;
import com.dramadownloader.scraper.file.FileScraper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyvideoFetcher extends FileScraperTestBase {
  private static final FileScraper scraper = new EasyvideoFileScraper();

  private static final List<String> urls = new ArrayList<>();
  static {
    urls.add("http://easyvideo.me/gogo/?w=790&h=480&file=i_am_sam_-_01_part_1.flv&sv=1");
    urls.add("http://easyvideo.me/gogo/?w=790&h=480&file=i_am_sam_-_01_part_2.flv&sv=1");
    urls.add("http://easyvideo.me/gogo/?w=790&h=480&file=the_merchant_gaekju_-_05_clip3.mp4&sv=1");
  }

  @Test
  public void testFetch() throws Exception {
    for(String url : urls) testFetchUrl(scraper, url);
  }
}
