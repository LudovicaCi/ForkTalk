package it.unipi.inginf.lsdb.group15.forktalk.data_structures;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private String origin;
    private int number_review;
    private List<Restaurant> restaurantList;
    private List<User> following;

    public User(int id, String username, String password, String origin, int number_review) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.origin = origin;
        this.number_review = number_review;
        restaurantList = new ArrayList<>();
        following = new ArrayList<>();
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

    public int getNumber_review() { return number_review; }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void addRestaurantToList(Restaurant restaurant) {
        restaurantList.add(restaurant);
    }

    public List<User> getFollowing() {
        return following;
    }

    public void followUser(User user) {
        following.add(user);
    }
}
