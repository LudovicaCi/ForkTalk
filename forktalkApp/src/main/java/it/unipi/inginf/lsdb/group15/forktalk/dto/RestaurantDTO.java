package it.unipi.inginf.lsdb.group15.forktalk.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class RestaurantDTO{
    //    -------------------------------------
    @SerializedName("rest_id")
    private String id;

    private String email;
    private String username;
    private String password;

    @SerializedName("restaurant_name")
    private String name;

    private ArrayList<String> coordinates;
    private ArrayList<String> location;
    private String country;
    private String county;
    private String district;
    private String city;
    private String address;

    @SerializedName("street_number")
    private String streetNumber;
    private String postCode;
    private int price;

    @SerializedName("tag")
    private ArrayList<String> features;
    private ArrayList<ReservationDTO> reservations;

    @SerializedName("rest_rating")
    private int rating;
    private ArrayList<ReviewDTO> reviews;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public RestaurantDTO(String id, String email, String username, String password, String name, ArrayList<String> coordinates, ArrayList<String> location, String country, String county, String district, String city, String address, String streetNumber, String postCode, int price, ArrayList<String> features, ArrayList<ReservationDTO> reservations, int rating, ArrayList<ReviewDTO> reviews) {
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
        this.reservations = reservations;
        this.rating = rating;
        this.reviews = reviews;
    }

    public RestaurantDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public RestaurantDTO(){
        this.coordinates = new ArrayList<>();
        this.location = new ArrayList<>();
        this.features = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.reservations = new ArrayList<>();
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

    public ArrayList<String> getCoordinates() {
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

    public int getRating() {
        return rating;
    }

    public ArrayList<ReviewDTO> getReviews() {
        return reviews;
    }

    public ArrayList<ReservationDTO> getReservations() {
        return reservations;
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

    public void setCoordinates(ArrayList<String> coordinates) {
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

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReviews(ArrayList<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public void setReservations(ArrayList<ReservationDTO> reservations) {
        this.reservations = reservations;
    }

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RestaurantDTO{").append('\n');
        sb.append("id='").append(id).append('\'').append(',').append('\n');
        sb.append("email='").append(email).append('\'').append(',').append('\n');
        sb.append("username='").append(username).append('\'').append(',').append('\n');
        sb.append("password='").append(password).append('\'').append(',').append('\n');
        sb.append("name='").append(name).append('\'').append(',').append('\n');
        sb.append("coordinates=").append(coordinates).append(',').append('\n');
        sb.append("location=").append(location).append(',').append('\n');
        sb.append("country='").append(country).append('\'').append(',').append('\n');
        sb.append("county='").append(county).append('\'').append(',').append('\n');
        sb.append("district='").append(district).append('\'').append(',').append('\n');
        sb.append("city='").append(city).append('\'').append(',').append('\n');
        sb.append("address='").append(address).append('\'').append(',').append('\n');
        sb.append("streetNumber='").append(streetNumber).append('\'').append(',').append('\n');
        sb.append("postCode='").append(postCode).append('\'').append(',').append('\n');
        sb.append("price=").append(price).append(',').append('\n');
        sb.append("features=").append(features).append(',').append('\n');
        sb.append("reservations=").append(reservations).append(',').append('\n');
        sb.append("rating=").append(rating).append(',').append('\n');
        sb.append("reviews=").append(reviews).append('\n');
        sb.append('}');

        return sb.toString();
    }
}
