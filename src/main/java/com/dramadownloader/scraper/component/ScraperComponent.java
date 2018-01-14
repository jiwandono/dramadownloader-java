package com.dramadownloader.scraper.component;

import com.dramadownloader.scraper.episode.DramacoolcomEpisodeScraper;
import com.dramadownloader.scraper.episode.EpisodeScraperFactory;
import com.dramadownloader.scraper.episode.GooddramaEpisodeScraper;
import com.dramadownloader.scraper.file.DramauploadFileScraper;
import com.dramadownloader.scraper.file.EasyvideoFileScraper;
import com.dramadownloader.scraper.file.EmbeddramaFileScraper;
import com.dramadownloader.scraper.file.EmbeddriveFileScraper;
import com.dramadownloader.scraper.file.FileScraperFactory;
import com.dramadownloader.scraper.file.GooglevideoFileScraper;
import com.dramadownloader.scraper.file.Mp4UploadFileScraper;
import com.dramadownloader.scraper.file.OpenloadFileScraper;
import com.dramadownloader.scraper.file.StoragestreamingFileScraper;
import com.dramadownloader.scraper.file.VideothedramacoolFileScraper;
import com.dramadownloader.scraper.file.VideouploadspaceFileScraper;
import com.dramadownloader.scraper.file.VideouploadusFileScraper;
import com.dramadownloader.scraper.file.ZippyshareFileScraper;
import com.dramadownloader.scraper.stream.AnimestvStreamScraper;
import com.dramadownloader.scraper.stream.DramacoolcomStreamScraper;
import com.dramadownloader.scraper.stream.DramacoolsioStreamScraper;
import com.dramadownloader.scraper.stream.DramafirecomStreamScraper;
import com.dramadownloader.scraper.stream.DramafireinfoStreamScraper;
import com.dramadownloader.scraper.stream.DramaniceStreamScraper;
import com.dramadownloader.scraper.stream.DramatvStreamScraper;
import com.dramadownloader.scraper.stream.GooddramaStreamScraper;
import com.dramadownloader.scraper.stream.StreamScraperFactory;
import com.dramadownloader.scraper.stream.ThedramacoolcomStreamScraper;
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

  public ScraperComponent() {
    _scheduledExecutorService = Executors.newScheduledThreadPool(16);

    // Instance registration

    _fileScraperFactory = new FileScraperFactory();
    _fileScraperFactory.register(EmbeddriveFileScraper.class, new EmbeddriveFileScraper());
    _fileScraperFactory.register(DramauploadFileScraper.class, new DramauploadFileScraper());
    _fileScraperFactory.register(EasyvideoFileScraper.class, new EasyvideoFileScraper());
    _fileScraperFactory.register(EmbeddramaFileScraper.class, new EmbeddramaFileScraper());
    _fileScraperFactory.register(GooglevideoFileScraper.class, new GooglevideoFileScraper());
    _fileScraperFactory.register(Mp4UploadFileScraper.class, new Mp4UploadFileScraper());
    _fileScraperFactory.register(OpenloadFileScraper.class, new OpenloadFileScraper());
    _fileScraperFactory.register(StoragestreamingFileScraper.class, new StoragestreamingFileScraper());
    _fileScraperFactory.register(VideothedramacoolFileScraper.class, new VideothedramacoolFileScraper());
    _fileScraperFactory.register(VideouploadspaceFileScraper.class, new VideouploadspaceFileScraper());
    _fileScraperFactory.register(VideouploadusFileScraper.class, new VideouploadusFileScraper());
    _fileScraperFactory.register(ZippyshareFileScraper.class, new ZippyshareFileScraper());

    _streamScraperFactory = new StreamScraperFactory();
    _streamScraperFactory.register(AnimestvStreamScraper.class, new AnimestvStreamScraper());
    _streamScraperFactory.register(DramacoolcomStreamScraper.class, new DramacoolcomStreamScraper());
    _streamScraperFactory.register(DramacoolsioStreamScraper.class, new DramacoolsioStreamScraper());
    _streamScraperFactory.register(ThedramacoolcomStreamScraper.class, new ThedramacoolcomStreamScraper());
    _streamScraperFactory.register(DramafirecomStreamScraper.class, new DramafirecomStreamScraper());
    _streamScraperFactory.register(DramafireinfoStreamScraper.class, new DramafireinfoStreamScraper());
    _streamScraperFactory.register(DramaniceStreamScraper.class, new DramaniceStreamScraper());
    _streamScraperFactory.register(DramatvStreamScraper.class, new DramatvStreamScraper());
    _streamScraperFactory.register(GooddramaStreamScraper.class, new GooddramaStreamScraper(_scheduledExecutorService));

    _episodeScraperFactory = new EpisodeScraperFactory();
    _episodeScraperFactory.register(DramacoolcomEpisodeScraper.class, new DramacoolcomEpisodeScraper());
    _episodeScraperFactory.register(GooddramaEpisodeScraper.class, new GooddramaEpisodeScraper(_scheduledExecutorService));

    _titleScraperFactory = new TitleScraperFactory();
    _titleScraperFactory.register(DramacoolcomTitleScraper.class, new DramacoolcomTitleScraper());
    _titleScraperFactory.register(GooddramaTitleScraper.class, new GooddramaTitleScraper());

    // Domain registration

    _fileScraperFactory.register("blogspot.com", GooglevideoFileScraper.class);
    _fileScraperFactory.register("googleusercontent.com", GooglevideoFileScraper.class);
    _fileScraperFactory.register("googlevideo.com", GooglevideoFileScraper.class);
    _fileScraperFactory.register("easyvideo.me", EasyvideoFileScraper.class);
    _fileScraperFactory.register("mp4upload.com", Mp4UploadFileScraper.class);
    _fileScraperFactory.register("openload.co", OpenloadFileScraper.class);
    _fileScraperFactory.register("storagestreaming.com", StoragestreamingFileScraper.class);
    _fileScraperFactory.register("thedramacool.com", VideothedramacoolFileScraper.class);
    _fileScraperFactory.register("videoupload.space", VideouploadspaceFileScraper.class);
    _fileScraperFactory.register("videoupload.us", VideouploadusFileScraper.class);
    _fileScraperFactory.register("zippyshare.com", ZippyshareFileScraper.class);

    _streamScraperFactory.register("animetv.to", AnimestvStreamScraper.class);
    _streamScraperFactory.register("dramacool.su", DramacoolcomStreamScraper.class);
    _streamScraperFactory.register("dramacools.io", DramacoolsioStreamScraper.class);
    _streamScraperFactory.register("dramafire.com", DramafirecomStreamScraper.class);
    _streamScraperFactory.register("dramafire.info", DramafireinfoStreamScraper.class);
    _streamScraperFactory.register("dramagalaxy.tv", GooddramaStreamScraper.class);
    _streamScraperFactory.register("dramago.com", GooddramaStreamScraper.class);
    _streamScraperFactory.register("dramanice.es", DramaniceStreamScraper.class);
    _streamScraperFactory.register("gooddrama.to", GooddramaStreamScraper.class);
    _streamScraperFactory.register("thedramacool.com", ThedramacoolcomStreamScraper.class);
    _streamScraperFactory.register("watchasian.co", DramacoolsioStreamScraper.class);

    _episodeScraperFactory.register("dramagalaxy.tv", GooddramaEpisodeScraper.class);
    _episodeScraperFactory.register("dramago.com", GooddramaEpisodeScraper.class);
    _episodeScraperFactory.register("gooddrama.to", GooddramaEpisodeScraper.class);

    _titleScraperFactory.register("dramagalaxy.eu", GooddramaTitleScraper.class);
    _titleScraperFactory.register("dramago.com", GooddramaTitleScraper.class);
    _titleScraperFactory.register("gooddrama.net", GooddramaTitleScraper.class);
    _titleScraperFactory.register("thedramacool.com", DramacoolcomTitleScraper.class);
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
