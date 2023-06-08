package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class User extends GeneralUser{

    private String origin;

    private int role = 1;

    public User(int id, String username, String password, String origin, ArrayList<Restaurant> restaurantList, ArrayList<Reservation> reservations) {

        this.origin = origin;
    }





    public String getOrigin() {

        return origin;
    }





    public void setOrigin(String origin) {

        this.origin = origin;
    }
}
