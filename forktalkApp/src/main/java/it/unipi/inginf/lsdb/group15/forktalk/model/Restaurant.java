package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class Restaurant extends GeneralUser{
    //    -------------------------------------
    private String id;
    private String name;
    private ArrayList<Double> coordinates;
    private ArrayList<String> location;
    private String country;
    private String county;
    private String district;
    private String city;
    private String address;
    private String streetNumber;
    private String postCode;
    private int price;
    private ArrayList<String> features;
    private double rating;
    private ArrayList<Review> reviews;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public Restaurant(String username, String password, String email, boolean isRestaurant, String id, String name, ArrayList<Double> coordinates, ArrayList<String> location, String country, String county, String district, String city, String address, String streetNumber, String postCode, int price, ArrayList<String> features, double rating, ArrayList<Review> reviews) {
        super(username, password, email, isRestaurant);
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.location = location;
        this.country = country;
        this.county = county;
        this.district = district;
        this.city = city;
        this.address = address;
        this.streetNumber = streetNumber;
        this.postCode = postCode;
        this.price = price;
        this.features = features;
        this.rating = rating;
        this.reviews = reviews;
    }

    public Restaurant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /* ********* GET METHOD ********* */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public ArrayList<String> getLocation() {
        return location;
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

    public String getPostCode() {
        return postCode;
    }

    public int getPrice() {
        return price;
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    public double getRating() {
        return rating;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /* ********* SET METHOD ********* */

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
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

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setFeatures(ArrayList<String> features) {
        this.features = features;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
