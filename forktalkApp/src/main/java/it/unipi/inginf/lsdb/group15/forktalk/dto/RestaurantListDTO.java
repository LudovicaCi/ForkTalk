package it.unipi.inginf.lsdb.group15.forktalk.dto;

import it.unipi.inginf.lsdb.group15.forktalk.model.Restaurant;

import java.util.ArrayList;

public class RestaurantListDTO {
    private String title;
    private ArrayList<Restaurant> restaurants;

    //METODI GET

    public String getTitle() {
        return title;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    //METODI SET

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
