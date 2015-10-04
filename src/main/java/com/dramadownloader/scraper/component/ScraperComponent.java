package com.dramadownloader.scraper.component;

import com.dramadownloader.scraper.episode.DramacoolcomEpisodeScraper;
import com.dramadownloader.scraper.episode.EpisodeScraperFactory;
import com.dramadownloader.scraper.file.DramauploadFileScraper;
import com.dramadownloader.scraper.file.EmbeddramaFileScraper;
import com.dramadownloader.scraper.file.FileScraperFactory;
import com.dramadownloader.scraper.file.GooglevideoFileScraper;
import com.dramadownloader.scraper.file.Mp4UploadFileScraper;
import com.dramadownloader.scraper.file.StoragestreamingFileScraper;
import com.dramadownloader.scraper.file.VideouploadusFileScraper;
import com.dramadownloader.scraper.stream.AnimestvStreamScraper;
import com.dramadownloader.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.DramafirecomStreamScraper;
import com.dramadownloader.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.scraper.stream.DramatvStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraperFactory;
import com.dramadownloader.scraper.title.TitleScraperFactory;

public class ScraperComponent {
  private final FileScraperFactory _fileScraperFactory;
  private final StreamScraperFactory _streamScraperFactory;
  private final EpisodeScraperFactory _episodeScraperFactory;
  private final TitleScraperFactory _titleScraperFactory;

  public ScraperComponent() {
    _fileScraperFactory = new FileScraperFactory();
    _fileScraperFactory.register(new DramauploadFileScraper());
    _fileScraperFactory.register(new EmbeddramaFileScraper());
    _fileScraperFactory.register(new GooglevideoFileScraper());
    _fileScraperFactory.register(new Mp4UploadFileScraper());
    _fileScraperFactory.register(new StoragestreamingFileScraper());
    _fileScraperFactory.register(new VideouploadusFileScraper());

    _streamScraperFactory = new StreamScraperFactory();
    _streamScraperFactory.register(new AnimestvStreamScraper());
    _streamScraperFactory.register(new DramacoolcomStreamScraper());
    _streamScraperFactory.register(new DramafirecomStreamScraper());
    _streamScraperFactory.register(new DramaniceStreamScraper());
    _streamScraperFactory.register(new DramatvStreamScraper());

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