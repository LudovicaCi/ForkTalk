package it.unipi.inginf.lsdb.group15.forktalk.model;

import javax.management.QueryExp;
import javax.swing.text.ElementIterator;

public abstract class GeneralUser {

    private String username;
    private String password;
    private String email;

    public GeneralUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // METODI GET
    public String getUsername() {

        return username;
    }
    public String getPassword() {

        return password;
    }
    public String getEmail() {

        return email;
    }
    // METODI SET
    public void setUsername(String username) {

        this.username = username;
    }
    public void setPassword(String password) {

        this.password = password;
    }
    public void setEmail(String email) {

        this.email = email;
    }
    @Override
    public String toString() {
        return "RegisteredUser{" +
                "username=" + username +
                ", password='" + password +
                ", email='"+ email;
    }
}
