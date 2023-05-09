package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.Date;

public class Review {
    private int id;
    private Date timestamp;
    private String author_name;
    private double rating;
    private Float rating_value;
    private Float rating_service;
    private Float rating_atmosphere;
    private Float rating_food;
    private String title;
    private String title_clean;
    private String content;
    private String content_clean;

    public Review(int id, Date timestamp, String author_name, double rating, Float rating_value, Float rating_service, Float rating_atmosphere, Float rating_food, String title, String title_clean, String content, String content_clean) {
        this.id = id;
        this.timestamp = timestamp;
        this.author_name = author_name;
        this.rating = rating;
        this.rating_value = rating_value;
        this.rating_service = rating_service;
        this.rating_atmosphere = rating_atmosphere;
        this.rating_food = rating_food;
        this.title = title;
        this.title_clean = title_clean;
        this.content = content;
        this.content_clean = content_clean;
    }

    public int getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return author_name;
    }

    public double getRating() {
        return rating;
    }

    public Float getRating_value() {
        return rating_value;
    }

    public Float getRating_service() {
        return rating_service;
    }

    public Float getRating_atmosphere() {
        return rating_atmosphere;
    }

    public Float getRating_food() {
        return rating_food;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_clean() {
        return title_clean;
    }

    public String getContent() {
        return content;
    }

    public String getContent_clean() {
        return content_clean;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setUsername(String username) {
        this.author_name = username;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRating_value(Float rating_value) {
        this.rating_value = rating_value;
    }

    public void setRating_service(Float rating_service) {
        this.rating_service = rating_service;
    }

    public void setRating_atmosphere(Float rating_atmosphere) {
        this.rating_atmosphere = rating_atmosphere;
    }

    public void setRating_food(Float rating_food) {
        this.rating_food = rating_food;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle_clean(String title_clean) {
        this.title_clean = title_clean;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent_clean(String content_clean) {
        this.content_clean = content_clean;
    }
}