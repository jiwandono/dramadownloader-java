package com.dramadownloader.test.scraper.title;

import com.dramadownloader.common.component.CommonComponent;
import com.dramadownloader.common.util.StringUtil;
import com.dramadownloader.core.TitleAccessor;
import com.dramadownloader.core.TitleMongoAccessor;
import com.dramadownloader.core.model.Title;
import com.dramadownloader.scraper.title.TitleScrapeRequest;
import com.dramadownloader.scraper.title.TitleScrapeResult;
import com.dramadownloader.scraper.title.TitleScraper;
import com.mongodb.DuplicateKeyException;
import org.junit.Assert;

public abstract class TitleScraperTestBase {
  private final CommonComponent _commonComponent = new CommonComponent();
  private final TitleAccessor _titleAccessor = new TitleMongoAccessor(_commonComponent.getDbData(), _commonComponent.getMorphia());

  protected void printFetchResult(TitleScrapeResult result) {
    System.out.println(result.getStatus());
    for(Title title : result.getTitles()) {
      System.out.println(title.getTitle() + ": " + title.getUrl());
    }
  }

  protected void testFetchUrl(TitleScraper scraper, String url) throws Exception {
    System.out.println(url);
    TitleScrapeResult result = scraper.scrape(new TitleScrapeRequest(url));
    Assert.assertEquals(TitleScrapeResult.Status.OK, result.getStatus());
    printFetchResult(result);
  }

  protected void populateData(TitleScraper scraper, String url) throws Exception {
    System.out.println(url);
    TitleScrapeResult result = scraper.scrape(new TitleScrapeRequest(url));
    for(Title title : result.getTitles()) {
      try {
        _titleAccessor.upsertTitle(title);
        System.out.println("Inserting entry: " + title.getId() + " - " + title.getUrl() + " - " + title.getId() + "/" + StringUtil.toPrettyUrl(title.getTitle()));
      } catch (DuplicateKeyException e) {
        System.out.println("Duplicate entry: " + title.getId() + " - " + title.getUrl() + " - " + title.getId() + "/" + StringUtil.toPrettyUrl(title.getTitle()));
      }
    }
  }
}
