package it.unipi.inginf.lsdb.group15.forktalk.dto;

import it.unipi.inginf.lsdb.group15.forktalk.model.Reservation;

import java.util.ArrayList;

public class RestaurantDTO extends GeneralUserDTO{
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

    //METODI SET
    public void restaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {

        this.coordinates = coordinates;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRestaurantReservations(ArrayList<Reservation> restaurantReservations) {
        this.restaurantReservations = restaurantReservations;
    }

    public void setRestaurantTag(ArrayList<String> restaurantTag) {
        this.restaurantTag = restaurantTag;
    }
    public void setRestaurantReviews(ArrayList<String> restaurantReviews) {
        this.restaurantReviews = restaurantReviews;
    }

    //METODI GET
    public String getRestaurantId() {
        return restaurantId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public String getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public double getPrice() {
        return price;
    }

    public ArrayList<Reservation> getRestaurantReservations() {
        return restaurantReservations;
    }

    public ArrayList<String> getRestaurantTag() {
        return restaurantTag;
    }

    public ArrayList<String> getRestaurantReviews() {
        return restaurantReviews;
    }

}
