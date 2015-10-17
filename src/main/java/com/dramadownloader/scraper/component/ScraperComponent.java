package com.dramadownloader.scraper.component;

import com.dramadownloader.scraper.episode.DramacoolcomEpisodeScraper;
import com.dramadownloader.scraper.episode.EpisodeScraperFactory;
import com.dramadownloader.scraper.file.DramauploadFileScraper;
import com.dramadownloader.scraper.file.EasyvideoFileScraper;
import com.dramadownloader.scraper.file.EmbeddramaFileScraper;
import com.dramadownloader.scraper.file.FileScraperFactory;
import com.dramadownloader.scraper.file.GooglevideoFileScraper;
import com.dramadownloader.scraper.file.Mp4UploadFileScraper;
import com.dramadownloader.scraper.file.StoragestreamingFileScraper;
import com.dramadownloader.scraper.file.VideouploadusFileScraper;
import com.dramadownloader.scraper.file.ZippyshareFileScraper;
import com.dramadownloader.scraper.stream.AnimestvStreamScraper;
import com.dramadownloader.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.DramafirecomStreamScraper;
import com.dramadownloader.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.scraper.stream.DramatvStreamScraper;
import com.dramadownloader.scraper.stream.GooddramaStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraperFactory;
import com.dramadownloader.scraper.title.TitleScraperFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScraperComponent {
  private final ScheduledExecutorService _scheduledExecutorService;

  private final FileScraperFactory _fileScraperFactory;
  private final StreamScraperFactory _streamScraperFactory;
  private final EpisodeScraperFactory _episodeScraperFactory;
  private final TitleScraperFactory _titleScraperFactory;

  public ScraperComponent() {
    _scheduledExecutorService = Executors.newScheduledThreadPool(16);

    _fileScraperFactory = new FileScraperFactory();
    _fileScraperFactory.register(new DramauploadFileScraper());
    _fileScraperFactory.register(new EasyvideoFileScraper());
    _fileScraperFactory.register(new EmbeddramaFileScraper());
    _fileScraperFactory.register(new GooglevideoFileScraper());
    _fileScraperFactory.register(new Mp4UploadFileScraper());
    _fileScraperFactory.register(new StoragestreamingFileScraper());
    _fileScraperFactory.register(new VideouploadusFileScraper());
    _fileScraperFactory.register(new ZippyshareFileScraper());

    _streamScraperFactory = new StreamScraperFactory();
    _streamScraperFactory.register(new AnimestvStreamScraper());
    _streamScraperFactory.register(new DramacoolcomStreamScraper());
    _streamScraperFactory.register(new DramafirecomStreamScraper());
    _streamScraperFactory.register(new DramaniceStreamScraper());
    _streamScraperFactory.register(new DramatvStreamScraper());
    _streamScraperFactory.register(new GooddramaStreamScraper(_scheduledExecutorService));

    _episodeScraperFactory = new EpisodeScraperFactory();
    _episodeScraperFactory.register(new DramacoolcomEpisodeScraper());

    _titleScraperFactory = new TitleScraperFactory();
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
