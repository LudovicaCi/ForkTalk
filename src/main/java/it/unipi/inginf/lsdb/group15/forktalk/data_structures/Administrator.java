package it.unipi.inginf.lsdb.group15.forktalk.data_structures;

import java.util.ArrayList;
import java.util.List;

public class Administrator {
    private List<Restaurant> restaurants;

    public Administrator() {
        restaurants = new ArrayList<>();
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }
}