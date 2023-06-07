package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class RestaurantsList {
    private String title;
    private ArrayList<Restaurant> restaurants;

    public RestaurantsList(String title, ArrayList<Restaurant> restaurants) {
        this.title = title;
        this.restaurants = restaurants;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

}
