package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;
import java.util.List;

public class Administrator {
    private ArrayList<Restaurant> restaurants;

    private ArrayList<Review> reviews;

    public Administrator(ArrayList<Restaurant> restaurants, ArrayList<Review> reviews) {
        this.restaurants = restaurants;
        this.reviews = reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);
    }

    public void deleteReview(Review review_to_delete){
        for(Review r: reviews){
            if(r.equals(review_to_delete))
                reviews.remove(r);
        }
    }
}