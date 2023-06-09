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
}