package com.example.api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    private Integer userId;
    private Integer id;
    private String title;
    private String body;

    public Post() {}

    public Post(Integer userId, String title, String body) {
        this.userId = userId;
        this.title  = title;
        this.body   = body;
    }

    public Integer getUserId() { return userId; }
    public Integer getId()     { return id; }
    public String  getTitle()  { return title; }
    public String  getBody()   { return body; }

    public Post setUserId(Integer userId) { this.userId = userId; return this; }
    public Post setId(Integer id)         { this.id = id; return this; }
    public Post setTitle(String title)    { this.title = title; return this; }
    public Post setBody(String body)      { this.body = body; return this; }

    @Override public String toString() {
        return "Post{userId=%s, id=%s, title='%s', body='%s'}".formatted(userId, id, title, body);
    }
}
