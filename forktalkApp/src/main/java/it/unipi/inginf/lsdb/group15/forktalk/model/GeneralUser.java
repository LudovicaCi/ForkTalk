package it.unipi.inginf.lsdb.group15.forktalk.model;

import javax.management.QueryExp;
import javax.swing.text.ElementIterator;

public abstract class GeneralUser {

    private String username;
    private String password;
    private String email;

    //TO STRING
    @Override
    public String toString() {
        return "RegisteredUser{" +
                "username=" + username +
                ", password='" + password +
                ", email='"+ email;
    }
    //COSTRUTTORE
    public GeneralUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
