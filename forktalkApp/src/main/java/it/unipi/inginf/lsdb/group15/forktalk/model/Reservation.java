package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.Date;

public class Reservation {
    private String restaurant_name;
    private String client_username;
    private String restaurant_username;
    private Date date;

    public Reservation(String restaurant_name, String client_username, String restaurant_username, Date date) {
        this.restaurant_name = restaurant_name;
        this.client_username = client_username;
        this.restaurant_username = restaurant_username;
        this.date = date;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setClient_username(String client_username) {
        this.client_username = client_username;
    }

    public void setRestaurant_username(String restaurant_username) {
        this.restaurant_username = restaurant_username;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getClient_username() {
        return client_username;
    }

    public String getRestaurant_username() {
        return restaurant_username;
    }

    public Date getDate() {
        return date;
    }
}
