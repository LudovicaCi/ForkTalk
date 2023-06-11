package it.unipi.inginf.lsdb.group15.forktalk.model;

import javax.management.QueryExp;
import javax.swing.text.ElementIterator;

public abstract class GeneralUser {
    private String username;
    private String password;
    private String email;
    private boolean isRestaurant;

    //TO STRING
    @Override
    public String toString() {
        return "RegisteredUser{" +
                "username=" + username +
                ", password='" + password +
                ", email='"+ email;
    }
    //COSTRUTTORE
    public GeneralUser(String username, String password, String email, boolean isRestaurant) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isRestaurant = isRestaurant;
    }


    //GET


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isRestaurant() {
        return isRestaurant;
    }

    //SET

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRestaurant(boolean restaurant) {
        isRestaurant = restaurant;
    }
}
