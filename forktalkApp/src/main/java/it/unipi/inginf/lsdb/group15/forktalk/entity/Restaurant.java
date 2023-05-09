package it.unipi.inginf.lsdb.group15.forktalk.entity;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private String location;
    private String country;
    private String region;
    private String province;
    private String city;
    private String address;
    private String awards;
    private String price_level;
    private double priceRange;
    private String cuisine;
    private double rating_excellence;
    private double rating_very_good;
    private double rating_neutral;
    private double rating_poor;
    private double rating_terrible;
    private String url_menu;
    private double rest_rating;
    private int number_review;
    private ArrayList<Review> reviews;

    public Restaurant(int id, String name, String location, String country, String region, String province, String city, String address, String awards, String price_level, double priceRange, String cuisine, double rating_excellence, double rating_very_good, double rating_neutral, double rating_poor, double rating_terrible, String url_menu, double rest_rating, int number_review, ArrayList<Review> reviews) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.country = country;
        this.region = region;
        this.province = province;
        this.city = city;
        this.address = address;
        this.awards = awards;
        this.price_level = price_level;
        this.priceRange = priceRange;
        this.cuisine = cuisine;
        this.rating_excellence = rating_excellence;
        this.rating_very_good = rating_very_good;
        this.rating_neutral = rating_neutral;
        this.rating_poor = rating_poor;
        this.rating_terrible = rating_terrible;
        this.url_menu = url_menu;
        this.rest_rating = rest_rating;
        this.number_review = number_review;
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public String getName() {return name;}

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getAwards() {
        return awards;
    }

    public String getPriceLevel() {
        return price_level;
    }

    public Double getPriceRange() { return priceRange; }

    public String getCuisine() {
        return cuisine;
    }

    public double getRating_excellence() {
        return rating_excellence;
    }

    public double getRating_very_good() {
        return rating_very_good;
    }

    public double getRating_neutral() {
        return rating_neutral;
    }

    public double getRating_poor() {
        return rating_poor;
    }

    public double getRating_terrible() {
        return rating_terrible;
    }

    public String getUrlMenu() {
        return url_menu;
    }

    public double getRestRating() {
        return rest_rating;
    }

    public int getNumberReview() {return number_review; }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setPrice_level(String price_level) {
        this.price_level = price_level;
    }

    public void setPriceRange(double priceRange) {
        this.priceRange = priceRange;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setRating_excellence(double rating_excellence) {
        this.rating_excellence = rating_excellence;
    }

    public void setRating_very_good(double rating_very_good) {
        this.rating_very_good = rating_very_good;
    }

    public void setRating_neutral(double rating_neutral) {
        this.rating_neutral = rating_neutral;
    }

    public void setRating_poor(double rating_poor) {
        this.rating_poor = rating_poor;
    }

    public void setRating_terrible(double rating_terrible) {
        this.rating_terrible = rating_terrible;
    }

    public void setUrl_menu(String url_menu) {
        this.url_menu = url_menu;
    }

    public void setRest_rating(double rest_rating) {
        this.rest_rating = rest_rating;
    }

    public void setNumber_review(int number_review) {
        this.number_review = number_review;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }
}
