package com.dramadownloader.core.model;

import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;
import java.util.Set;

public class Title {
  @Id
  private String id;
  private String providerId;
  private Type type;
  private String title;
  private String url;
  private Long year;
  private Origin origin;
  private Set<String> genres = new HashSet<>();
  private CompletionStatus completionStatus;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Long getYear() {
    return year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public Origin getOrigin() {
    return origin;
  }

  public void setOrigin(Origin origin) {
    this.origin = origin;
  }

  public Set<String> getGenres() {
    return genres;
  }

  public void setGenres(Set<String> genres) {
    this.genres = genres;
  }

  public CompletionStatus getCompletionStatus() {
    return completionStatus;
  }

  public void setCompletionStatus(CompletionStatus completionStatus) {
    this.completionStatus = completionStatus;
  }

  public static enum Type {
    ANIME,
    DRAMA
  }

  public static enum Origin {
    JAPAN,
    SOUTH_KOREA,
    CHINESE,
    TAIWAN,
    HONG_KONG,
    MALAYSIA,
    INDONESIA,
    SINGAPORE,
    THAILAND
  }

  public static enum CompletionStatus {
    ONGOING,
    COMPLETED
  }
}
