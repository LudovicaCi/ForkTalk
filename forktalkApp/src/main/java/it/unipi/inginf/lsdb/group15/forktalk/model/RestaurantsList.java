package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class RestaurantsList {
    String Title;
    ArrayList<Restaurant> Restaurants;
    String User_Creator;

    public RestaurantsList(String title, ArrayList<Restaurant> restaurants, String user_Creator) {
        Title = title;
        Restaurants = restaurants;
        User_Creator = user_Creator;
    }

    public String getTitle() {
        return Title;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return Restaurants;
    }

    public String getUser_Creator() {
        return User_Creator;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        Restaurants = restaurants;
    }

    public void setUser_Creator(String user_Creator) {
        User_Creator = user_Creator;
    }
}
