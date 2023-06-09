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

    /*public static void addReservation(Reservation reservations) {
        reservations.add(reservations);
        // Perform any additional logic related to the reservation
    }

     */
}
