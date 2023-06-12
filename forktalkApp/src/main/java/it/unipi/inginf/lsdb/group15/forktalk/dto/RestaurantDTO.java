package it.unipi.inginf.lsdb.group15.forktalk.dto;

import java.util.ArrayList;

public class RestaurantDTO{
    //    -------------------------------------
    private String id;
    private String email;
    private String username;
    private String password;
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
    private ArrayList<ReviewDTO> reviews;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public RestaurantDTO(String id, String email, String username, String password, String name, ArrayList<Double> coordinates, ArrayList<String> location, String country, String county, String district, String city, String address, String streetNumber, String postCode, int price, ArrayList<String> features, double rating, ArrayList<ReviewDTO> reviews) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
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

    public RestaurantDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /* ********* GET METHOD ********* */

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
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

    public ArrayList<ReviewDTO> getReviews() {
        return reviews;
    }

    /* ********* SET METHOD ********* */

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setReviews(ArrayList<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", location=" + location +
                ", country='" + country + '\'' +
                ", county='" + county + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", postCode='" + postCode + '\'' +
                ", price=" + price +
                ", features=" + features +
                ", rating=" + rating +
                ", reviews=" + reviews +
                '}';
    }
}
