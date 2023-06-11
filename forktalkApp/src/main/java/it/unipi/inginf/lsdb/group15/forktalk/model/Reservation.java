package it.unipi.inginf.lsdb.group15.forktalk.model;

import it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.MongoDBUserDAO;

import java.util.Date;

public class Reservation {
    private String restaurantName;
    private String clientUsername;
    private String restaurantUsername;
    private Date date;
    private int people;

    public Reservation(MongoDBUserDAO mongoDBUserDAO, String restaurantName, String clientUsername, String restaurantUsername, Date date, int people) {
        this.restaurantName = restaurantName;
        this.clientUsername = clientUsername;
        this.restaurantUsername = restaurantUsername;
        this.date = date;
        this.people = people;
    }

    //GET

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public String getRestaurantUsername() {
        return restaurantUsername;
    }

    public Date getDate() {
        return date;
    }

    public int getPeople() {
        return people;
    }

    //SET

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public void setRestaurantUsername(String restaurantUsername) {
        this.restaurantUsername = restaurantUsername;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
