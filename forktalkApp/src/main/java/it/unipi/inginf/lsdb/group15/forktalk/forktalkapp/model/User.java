package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model;

import java.util.ArrayList;

public class User extends GeneralUser{
    //    -------------------------------------
    private String name;
    private String surname;
    private String origin;
    private int suspended; //0 if the account is not suspended 1 otherwise
    private int role; //1 if normal user and 2 if admin
    private ArrayList<Reservation> reservations;
    private ArrayList<RestaurantsList> restaurantLists;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public User(String email, String username, String password, String name, String surname, String origin) {
        super(email, username, password);
        this.name = name;
        this.surname = surname;
        this.origin = origin;
        this.suspended = 0;
        this.role = 1;
        this.reservations = null;
        this.restaurantLists = null;
    }

    public User(String username, String password, String email, boolean isRestaurant, String name, String surname, String origin, int suspended, int role, ArrayList<Reservation> reservations, ArrayList<RestaurantsList> restaurantLists) {
        super(username, password, email, isRestaurant);
        this.name = name;
        this.surname = surname;
        this.origin = origin;
        this.suspended = suspended;
        this.role = role;
        this.reservations = reservations;
        this.restaurantLists = restaurantLists;
    }

    /* ********* GET METHOD ********* */

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getOrigin() {
        return origin;
    }

    public int getSuspended() {
        return suspended;
    }

    public int getRole() {
        return role;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public ArrayList<RestaurantsList> getRestaurantLists() {
        return restaurantLists;
    }

    /* ********* SET METHOD ********* */

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setSuspended(int suspended) {
        this.suspended = suspended;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setRestaurantLists(ArrayList<RestaurantsList> restaurantLists) {
        this.restaurantLists = restaurantLists;
    }

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", origin='" + origin + '\'' +
                ", suspended=" + suspended +
                ", role=" + role +
                ", reservations=" + reservations +
                ", restaurantLists=" + restaurantLists +
                ", super=" + super.toString() +
                '}';
    }


}
