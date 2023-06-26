package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model;

import java.util.Date;

public class Review {
    //    -------------------------------------
    private int id;
    private Date timestamp;
    private int rating;
    private String content;
    private String reviewer;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public Review(int id, Date timestamp, int rating, String content, String reviewer) {
        this.id = id;
        this.timestamp = timestamp;
        this.rating = rating;
        this.content = content;
        this.reviewer = reviewer;
    }

    /* ********* GET METHOD ********* */

    public int getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public String getReviewer() {
        return reviewer;
    }

    /* ********* SET METHOD ********* */

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
}