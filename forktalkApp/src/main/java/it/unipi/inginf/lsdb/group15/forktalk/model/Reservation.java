package it.unipi.inginf.lsdb.group15.forktalk.model;

import it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBUser;

import java.util.Date;

public class Reservation {
    private String restaurantName;
    private String clientUsername;
    private String restaurantUsername;
    private Date date;
    private int people;

    public Reservation(MongoDBUser mongoDBUser, String restaurantName, String clientUsername, String restaurantUsername, Date date, int people) {
        this.restaurantName = restaurantName;
        this.clientUsername = clientUsername;
        this.restaurantUsername = restaurantUsername;
        this.date = date;
        this.people = people;
    }
}
