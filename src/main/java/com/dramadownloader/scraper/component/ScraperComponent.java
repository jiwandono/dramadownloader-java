package com.dramadownloader.scraper.component;

import com.dramadownloader.common.component.CommonComponent;
import com.dramadownloader.scraper.episode.DramacoolcomEpisodeScraper;
import com.dramadownloader.scraper.episode.EpisodeScraperFactory;
import com.dramadownloader.scraper.episode.GooddramaEpisodeScraper;
import com.dramadownloader.scraper.file.*;
import com.dramadownloader.scraper.stream.AnimestvStreamScraper;
import com.dramadownloader.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.DramafirecomStreamScraper;
import com.dramadownloader.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.scraper.stream.DramatvStreamScraper;
import com.dramadownloader.scraper.stream.GooddramaStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraperFactory;
import com.dramadownloader.scraper.stream.TiveeStreamScraper;
import com.dramadownloader.scraper.title.DramacoolcomTitleScraper;
import com.dramadownloader.scraper.title.GooddramaTitleScraper;
import com.dramadownloader.scraper.title.TitleScraperFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScraperComponent {
  private final ScheduledExecutorService _scheduledExecutorService;

  private final FileScraperFactory _fileScraperFactory;
  private final StreamScraperFactory _streamScraperFactory;
  private final EpisodeScraperFactory _episodeScraperFactory;
  private final TitleScraperFactory _titleScraperFactory;

  public ScraperComponent(CommonComponent commonComponent) {
    _scheduledExecutorService = Executors.newScheduledThreadPool(16);

    _fileScraperFactory = new FileScraperFactory();
    _fileScraperFactory.register(new EmbeddriveFileScraper());
    _fileScraperFactory.register(new DramauploadFileScraper());
    _fileScraperFactory.register(new EasyvideoFileScraper());
    _fileScraperFactory.register(new EmbeddramaFileScraper());
    _fileScraperFactory.register(new GooglevideoFileScraper());
    _fileScraperFactory.register(new Mp4UploadFileScraper());
    _fileScraperFactory.register(new StoragestreamingFileScraper());
    _fileScraperFactory.register(new TiveeFileScraper(commonComponent.getObjectMapper()));
    _fileScraperFactory.register(new VideouploadusFileScraper());
    _fileScraperFactory.register(new ZippyshareFileScraper());

    _streamScraperFactory = new StreamScraperFactory();
    _streamScraperFactory.register(new AnimestvStreamScraper());
    _streamScraperFactory.register(new DramacoolcomStreamScraper());
    _streamScraperFactory.register(new DramafirecomStreamScraper());
    _streamScraperFactory.register(new DramaniceStreamScraper());
    _streamScraperFactory.register(new DramatvStreamScraper());
    _streamScraperFactory.register(new GooddramaStreamScraper(_scheduledExecutorService));
    _streamScraperFactory.register(new TiveeStreamScraper());

    _episodeScraperFactory = new EpisodeScraperFactory();
    _episodeScraperFactory.register(new DramacoolcomEpisodeScraper());
    _episodeScraperFactory.register(new GooddramaEpisodeScraper(_scheduledExecutorService));

    _titleScraperFactory = new TitleScraperFactory();
    _titleScraperFactory.register(new DramacoolcomTitleScraper());
    _titleScraperFactory.register(new GooddramaTitleScraper());
  }

  public FileScraperFactory getFileScraperFactory() {
    return _fileScraperFactory;
  }

  public StreamScraperFactory getStreamScraperFactory() {
    return _streamScraperFactory;
  }

  public EpisodeScraperFactory getEpisodeScraperFactory() {
    return _episodeScraperFactory;
  }

  public TitleScraperFactory getTitleScraperFactory() {
    return _titleScraperFactory;
  }
}
