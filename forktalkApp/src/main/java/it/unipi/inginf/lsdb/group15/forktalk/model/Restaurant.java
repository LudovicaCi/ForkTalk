package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class Restaurant extends GeneralUser{
    private ArrayList<Reservation> restaurantReservations;
    private ArrayList<String> restaurantTag;
    private ArrayList<String> restaurantReviews;
    private String restaurantId;
    private String password;
    private String name;
    private ArrayList<Double> coordinates;
    private String country;
    private String county;
    private String district;
    private String city;
    private String address;
    private String streetNumber;
    private String postcode;
    private double price;

    public Restaurant(String username, String email, String restaurantId, String password, String name, ArrayList<Double> coordinates, String location, String country, String county, String district, String city, String address, String streetNumber, String postcode, double price,ArrayList<Reservation> restaurantReservations, ArrayList<String> restaurantTag, ArrayList<String> reviews) {
        super (email, username, password);
        this.restaurantId = restaurantId;
        this.name = name;
        this.coordinates = coordinates;
        this.country = country;
        this.county = county;
        this.district = district;
        this.city = city;
        this.address = address;
        this.streetNumber = streetNumber;
        this.postcode = postcode;
        this.price = price;
        this.restaurantReservations = restaurantReservations;
        this.restaurantTag= restaurantTag;
        this.restaurantReviews= reviews;
    }


    /*public static void addReservation(Reservation reservations) {
        reservations.add(reservations);
        // Perform any additional logic related to the reservation
    }

     */
}
