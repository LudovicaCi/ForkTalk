package it.unipi.inginf.lsdb.group15.forktalk.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private String origin;
    private int number_review;
    private ArrayList<Restaurant> restaurantList;
    private ArrayList<User> following;
    private ArrayList<Review> reviews;

    public User(int id, String username, String password, String origin, int number_review, ArrayList<Restaurant> restaurantList, ArrayList<User> following, ArrayList<Review> reviews) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.origin = origin;
        this.number_review = number_review;
        this.restaurantList = restaurantList;
        this.following = following;
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getOrigin() {
        return origin;
    }

    public int getNumber_review() {
        return number_review;
    }

    public ArrayList<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public List<User> getFollowing() {
        return following;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setNumber_review(int number_review) {
        this.number_review = number_review;
    }

    public void setRestaurantList(ArrayList<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public void setFollowing(ArrayList<User> following) {
        this.following = following;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
