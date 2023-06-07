package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class Restaurant {
    private String restaurant_id;
    private String password;
    private String name;
    private ArrayList<Double> coordinates;
    private String country;
    private String county;
    private String district;
    private String city;
    private String address;
    private String street_number;
    private String postcode;
    private double price;

    public Restaurant(String restaurant_id, String password, String name, ArrayList<Double> coordinates, String location, String country, String county, String district, String city, String address, String street_number, String postcode, double price, ArrayList<String> restaurant_tag, ArrayList<Review> reviews) {
        this.restaurant_id = restaurant_id;
        this.password = password;
        this.name = name;
        this.coordinates = coordinates;
        this.country = country;
        this.county = county;
        this.district = district;
        this.city = city;
        this.address = address;
        this.street_number = street_number;
        this.postcode = postcode;
        this.price = price;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
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

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRestaurant_id() {
        return restaurant_id;
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

    public String getStreet_number() {
        return street_number;
    }

    public String getPostcode() {
        return postcode;
    }

    public double getPrice() {
        return price;
    }
}
