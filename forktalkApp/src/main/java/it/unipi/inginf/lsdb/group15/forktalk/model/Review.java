package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.Date;

public class Review {
    private int id;
    private Date timestamp;
    private int rating;
    private String title;
    private String content;

    public Review(int id, Date timestamp, int rating, String title, String content) {
        this.id = id;
        this.timestamp = timestamp;
        this.rating = rating;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

}