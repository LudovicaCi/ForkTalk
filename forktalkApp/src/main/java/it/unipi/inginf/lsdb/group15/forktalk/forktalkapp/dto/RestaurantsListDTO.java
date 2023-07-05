package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Restaurant;

import java.util.ArrayList;

public class RestaurantsListDTO {
    //    -------------------------------------
    private String title;
    private ArrayList<RestaurantDTO> restaurants;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public RestaurantsListDTO(String title, ArrayList<RestaurantDTO> restaurants) {
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

    public ArrayList<RestaurantDTO> getRestaurants() {
        return restaurants;
    }

    /* ********* SET METHOD ********* */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRestaurants(ArrayList<RestaurantDTO> restaurants) {
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
