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

    // Instance registration

    _fileScraperFactory = new FileScraperFactory();
    _fileScraperFactory.register(EmbeddriveFileScraper.class, new EmbeddriveFileScraper());
    _fileScraperFactory.register(DramauploadFileScraper.class, new DramauploadFileScraper());
    _fileScraperFactory.register(EasyvideoFileScraper.class, new EasyvideoFileScraper());
    _fileScraperFactory.register(EmbeddramaFileScraper.class, new EmbeddramaFileScraper());
    _fileScraperFactory.register(GooglevideoFileScraper.class, new GooglevideoFileScraper());
    _fileScraperFactory.register(Mp4UploadFileScraper.class, new Mp4UploadFileScraper());
    _fileScraperFactory.register(StoragestreamingFileScraper.class, new StoragestreamingFileScraper());
    _fileScraperFactory.register(TiveeFileScraper.class, new TiveeFileScraper(commonComponent.getObjectMapper()));
    _fileScraperFactory.register(VideouploadusFileScraper.class, new VideouploadusFileScraper());
    _fileScraperFactory.register(ZippyshareFileScraper.class, new ZippyshareFileScraper());

    _streamScraperFactory = new StreamScraperFactory();
    _streamScraperFactory.register(AnimestvStreamScraper.class, new AnimestvStreamScraper());
    _streamScraperFactory.register(DramacoolcomStreamScraper.class, new DramacoolcomStreamScraper());
    _streamScraperFactory.register(DramafirecomStreamScraper.class, new DramafirecomStreamScraper());
    _streamScraperFactory.register(DramaniceStreamScraper.class, new DramaniceStreamScraper());
    _streamScraperFactory.register(DramatvStreamScraper.class, new DramatvStreamScraper());
    _streamScraperFactory.register(GooddramaStreamScraper.class, new GooddramaStreamScraper(_scheduledExecutorService));
    _streamScraperFactory.register(TiveeStreamScraper.class, new TiveeStreamScraper());

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
    _fileScraperFactory.register("cdn.dramacool.to", EmbeddriveFileScraper.class);
    _fileScraperFactory.register("cdn.dramanice.to", EmbeddriveFileScraper.class);
    _fileScraperFactory.register("dramaupload.com", DramauploadFileScraper.class);
    _fileScraperFactory.register("easyvideo.me", EasyvideoFileScraper.class);
    _fileScraperFactory.register("mp4upload.com", Mp4UploadFileScraper.class);
    _fileScraperFactory.register("storagestreaming.com", StoragestreamingFileScraper.class);
    _fileScraperFactory.register("tivee.com", TiveeFileScraper.class);
    _fileScraperFactory.register("videoupload.biz", VideouploadusFileScraper.class);
    _fileScraperFactory.register("videoupload.space", VideouploadusFileScraper.class);
    _fileScraperFactory.register("videoupload.us", VideouploadusFileScraper.class);
    _fileScraperFactory.register("zippyshare.com", ZippyshareFileScraper.class);

    _streamScraperFactory.register("anime4you.net", AnimestvStreamScraper.class);
    _streamScraperFactory.register("animestv.eu", AnimestvStreamScraper.class);
    _streamScraperFactory.register("animestv.us", AnimestvStreamScraper.class);
    _streamScraperFactory.register("animetv.in", AnimestvStreamScraper.class);
    _streamScraperFactory.register("animetv.one", AnimestvStreamScraper.class);
    _streamScraperFactory.register("animetv.online", AnimestvStreamScraper.class);
    _streamScraperFactory.register("animetv.top", AnimestvStreamScraper.class);
    _streamScraperFactory.register("drama.to", DramatvStreamScraper.class);
    _streamScraperFactory.register("dramacool.cc", DramacoolcomStreamScraper.class);
    _streamScraperFactory.register("dramacool.com", DramacoolcomStreamScraper.class);
    _streamScraperFactory.register("dramacool.me", DramacoolcomStreamScraper.class);
    _streamScraperFactory.register("dramacool.one", DramacoolcomStreamScraper.class);
    _streamScraperFactory.register("dramacool.to", DramacoolcomStreamScraper.class);
    _streamScraperFactory.register("dramacool.tv", DramatvStreamScraper.class);
    _streamScraperFactory.register("dramafire.com", DramafirecomStreamScraper.class);
    _streamScraperFactory.register("dramagalaxy.eu", GooddramaStreamScraper.class);
    _streamScraperFactory.register("dramagalaxy.tv", GooddramaStreamScraper.class);
    _streamScraperFactory.register("dramago.com", GooddramaStreamScraper.class);
    _streamScraperFactory.register("dramanice.com", DramaniceStreamScraper.class);
    _streamScraperFactory.register("dramanice.eu", DramaniceStreamScraper.class);
    _streamScraperFactory.register("dramanice.net", DramaniceStreamScraper.class);
    _streamScraperFactory.register("dramanice.to", DramaniceStreamScraper.class);
    _streamScraperFactory.register("dramanice.us", DramaniceStreamScraper.class);
    _streamScraperFactory.register("dramatv.co", DramatvStreamScraper.class);
    _streamScraperFactory.register("dramatv.eu", DramatvStreamScraper.class);
    _streamScraperFactory.register("dramatv.tv", DramatvStreamScraper.class);
    _streamScraperFactory.register("dramatv.us", DramatvStreamScraper.class);
    _streamScraperFactory.register("gooddrama.net", GooddramaStreamScraper.class);
    _streamScraperFactory.register("tiveee.com", TiveeStreamScraper.class);

    _episodeScraperFactory.register("dramacool.cc", DramacoolcomEpisodeScraper.class);
    _episodeScraperFactory.register("dramacool.com", DramacoolcomEpisodeScraper.class);
    _episodeScraperFactory.register("dramacool.one", DramacoolcomEpisodeScraper.class);
    _episodeScraperFactory.register("dramagalaxy.eu", GooddramaEpisodeScraper.class);
    _episodeScraperFactory.register("dramago.com", GooddramaEpisodeScraper.class);
    _episodeScraperFactory.register("gooddrama.net", GooddramaEpisodeScraper.class);

    _titleScraperFactory.register("dramacool.cc", DramacoolcomTitleScraper.class);
    _titleScraperFactory.register("dramacool.com", DramacoolcomTitleScraper.class);
    _titleScraperFactory.register("dramacool.one", DramacoolcomTitleScraper.class);
    _titleScraperFactory.register("dramagalaxy.eu", GooddramaTitleScraper.class);
    _titleScraperFactory.register("dramago.com", GooddramaTitleScraper.class);
    _titleScraperFactory.register("gooddrama.net", GooddramaTitleScraper.class);
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
