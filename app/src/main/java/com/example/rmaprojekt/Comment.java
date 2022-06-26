package com.example.rmaprojekt;

public class Comment {
    private String text;
    private String likeCount;
    private String dislikeCount;
    private String author;
    private String published;

    public Comment(String text, String likeCount, String dislikeCount, String author, String published) {
        this.text = text;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.author = author;
        this.published = published;
    }

    public String getText() {
        return text;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }
}
