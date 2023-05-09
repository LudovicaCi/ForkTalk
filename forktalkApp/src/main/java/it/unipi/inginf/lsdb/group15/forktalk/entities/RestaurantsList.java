package it.unipi.inginf.lsdb.group15.forktalk.entities;

import java.util.ArrayList;

public class RestaurantsList {
    private String title;
    private ArrayList<Restaurant> restaurants;
    private String username;

    public RestaurantsList(String title, ArrayList<Restaurant> restaurants, String user_Creator) {
        this.title = title;
        this.restaurants = restaurants;
        username = user_Creator;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public String getUsername() {
        return username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
