package it.unipi.inginf.lsdb.group15.forktalk.dto;

import java.util.Date;

public class ReviewDTO {
    //    -------------------------------------
    private String id;
    private String timestamp;
    private int rating;
    private String content;
    private String reviewer;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public ReviewDTO(String id, String timestamp, int rating, String content, String reviewer) {
        this.id = id;
        this.timestamp = timestamp;
        this.rating = rating;
        this.content = content;
        this.reviewer = reviewer;
    }

    public ReviewDTO() {

    }

    /* ********* GET METHOD ********* */

    public String getId() {
        return id;
    }

    public String getTimestamp() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
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

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", reviewer='" + reviewer + '\'' +
                '}';
    }
}
