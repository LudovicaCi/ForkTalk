package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model;

import java.util.Date;

public class Reservation {
    //    -------------------------------------
    private String clientUsername;
    private String clientName;
    private String clientSurname;
    private String restaurantID;
    private String restaurantName;
    private String restaurantUsername;
    private String restaurantCity;
    private String restaurantAddress;
    private Date date;
    private int people;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public Reservation(String clientUsername, String clientName, String clientSurname, String restaurantID, String restaurantName, String restaurantUsername, String restaurantCity, String restaurantAddress, Date date, int people) {
        this.clientUsername = clientUsername;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantUsername = restaurantUsername;
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

    public String getRestaurantUsername() {
        return restaurantUsername;
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

    public void setRestaurantUsername(String restaurantUsername) {
        this.restaurantUsername = restaurantUsername;
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
}
