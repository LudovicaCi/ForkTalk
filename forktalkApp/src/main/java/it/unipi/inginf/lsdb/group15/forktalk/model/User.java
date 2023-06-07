package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String origin;

    public User(int id, String username, String password, String origin, ArrayList<Restaurant> restaurantList, ArrayList<Reservation> reservations) {
        this.username = username;
        this.password = password;
        this.origin = origin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getOrigin() {
        return origin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
