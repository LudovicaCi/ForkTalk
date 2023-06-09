package it.unipi.inginf.lsdb.group15.forktalk.dto;

import java.util.Date;

public class ReviewDTO {
    private int id;
    private Date timestamp;
    private int rating;
    private String title;
    private String content;

    //------------------------------------- -------------------------------------
    //METODI GET

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

    //------------------------------------- -------------------------------------
    //METODI SET

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

    //------------------------------------- -------------------------------------

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "restaurantID='" + id + '\'' +
                ", rate=" + rating +
                ", review='" + content + '\'' +
                '}';
    }
}
