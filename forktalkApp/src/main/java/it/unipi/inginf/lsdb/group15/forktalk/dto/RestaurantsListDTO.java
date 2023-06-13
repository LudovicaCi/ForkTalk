package it.unipi.inginf.lsdb.group15.forktalk.dto;

import it.unipi.inginf.lsdb.group15.forktalk.model.Restaurant;

import java.util.ArrayList;

public class RestaurantsListDTO {
    //    -------------------------------------
    private String title;
    private ArrayList<Restaurant> restaurants;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public RestaurantsListDTO(String title, ArrayList<Restaurant> restaurants) {
        this.title = title;
        this.restaurants = restaurants;
    }

    public RestaurantsListDTO(String title) {
        this.title = title;
        this.restaurants = new ArrayList<>();
    }

    public RestaurantsListDTO() {
        this.restaurants = new ArrayList<>();
    }

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

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "RestaurantsListDTO{" +
                "title='" + title + '\'' +
                ", restaurants=" + restaurants +
                '}';
    }
}
