package it.unipi.inginf.lsdb.group15.forktalk.dto;

import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class ReservationDTO {
    //    -------------------------------------
    @SerializedName("client_username")
    private String clientUsername;

    @SerializedName("client_name")
    private String clientName;

    @SerializedName("client_surname")
    private String clientSurname;

    @SerializedName("restaurant_id")
    private String restaurantID;

    @SerializedName("restaurant_name")
    private String restaurantName;

    @SerializedName("restaurant_city")
    private String restaurantCity;

    @SerializedName("restaurant_address")
    private String restaurantAddress;

    private Date date;

    @SerializedName("number of person")
    private int people;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public ReservationDTO(String clientUsername, String clientName, String clientSurname, String restaurantID, String restaurantName, String restaurantUsername, String restaurantCity, String restaurantAddress, Date date, int people) {
        this.clientUsername = clientUsername;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantCity = restaurantCity;
        this.restaurantAddress = restaurantAddress;
        this.date = date;
        this.people = people;
    }

    /* ********* GET METHOD ********* */

    public String getClientUsername() {
        return clientUsername;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantCity() {
        return restaurantCity;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public Date getDate() {
        return date;
    }

    public int getPeople() {
        return people;
    }

    /* ********* SET METHOD ********* */

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantCity(String restaurantCity) {
        this.restaurantCity = restaurantCity;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "clientUsername='" + clientUsername + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientSurname='" + clientSurname + '\'' +
                ", restaurantID='" + restaurantID + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantCity='" + restaurantCity + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", date=" + date +
                ", people=" + people +
                '}';
    }
}
