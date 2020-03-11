package com.google.sps.data;

public final class Comment {

  private final String email;
  private final String text;
  private final long time;

  public Comment(String email, String text, long time) {
    this.email = email;
    this.text = text;
    this.time = time;
  }
}