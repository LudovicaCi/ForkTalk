package it.unipi.inginf.lsdb.group15.forktalk.model;

import javax.management.QueryExp;
import javax.swing.text.ElementIterator;

public abstract class GeneralUser {
    //    -------------------------------------
    private String email;
    private String username;
    private String password;
    private boolean isRestaurant;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public GeneralUser(String email, String username, String password, boolean isRestaurant) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isRestaurant = isRestaurant;
    }

    public GeneralUser(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public GeneralUser() {

    }

    /* ********* GET METHOD ********* */

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRestaurant() {
        return isRestaurant;
    }

    /* ********* SET METHOD ********* */

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRestaurant(boolean restaurant) {
        isRestaurant = restaurant;
    }

    /* ********* TO STRING METHOD ********* */
    @Override
    public String toString() {
        return "User{" +
                "username=" + username +
                ", password='" + password +
                ", email='"+ email;
    }
}
