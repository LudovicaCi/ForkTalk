package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model;

import java.util.ArrayList;

public class RestaurantsList {
    //    -------------------------------------
    private String title;
    private ArrayList<Restaurant> restaurants;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public RestaurantsList(String title, ArrayList<Restaurant> restaurants) {
        this.title = title;
        this.restaurants = restaurants;
    }

    public RestaurantsList(){}

    /* ********* GET METHOD ********* */

    public String getTitle() {
        return title;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    /* ********* SET METHOD ********* */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
