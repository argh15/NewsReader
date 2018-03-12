package com.teksystems.newsreader.Models;

/**
 * Created by Arghadeep on 05-03-2018.
 */

public class News {
  String title;
  String author;
  String description;
  String url;
  String urlToImage;
  String publishedAt;
  String sourceName;

  public News() {
  }

  public News(String title, String author, String description, String url, String urlToImage, String publishedAt, String sourceName) {
    this.title = title;
    this.author = author;
    this.description = description;
    this.url = url;
    this.urlToImage = urlToImage;
    this.publishedAt = publishedAt;
    this.sourceName = sourceName;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public String getDescription() {
    return description;
  }

  public String getUrl() {
    return url;
  }

  public String getUrlToImage() {
    return urlToImage;
  }

  public String getPublishedAt() {
    return publishedAt;
  }

  public String getSourceName() {
    return sourceName;
  }
}
