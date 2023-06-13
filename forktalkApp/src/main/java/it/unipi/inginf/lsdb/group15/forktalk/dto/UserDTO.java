package it.unipi.inginf.lsdb.group15.forktalk.dto;

import java.util.ArrayList;

public class UserDTO {
    //    -------------------------------------
    private String email;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String origin;
    private int suspended; //0 if the account is not suspended 1 otherwise
    private int role; //1 if normal user and 2 if admin
    private ArrayList<ReservationDTO> reservations;
    private ArrayList<RestaurantsListDTO> restaurantLists;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public UserDTO() {
        this.reservations = new ArrayList<>();
        this.restaurantLists = new ArrayList<>();
    }

    public UserDTO(String email, String username, String password, String name, String surname, String origin) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.origin = origin;
        this.suspended = 0;
        this.role = 1;
        this.reservations = null;
        this.restaurantLists = null;
    }

    public UserDTO(String email, String username, String password, String name, String surname, String origin, ArrayList<ReservationDTO> reservations, ArrayList<RestaurantsListDTO> restaurantLists) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.origin = origin;
        this.suspended = 0;
        this.role = 1;
        this.reservations = reservations;
        this.restaurantLists = restaurantLists;
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

    public ArrayList<ReservationDTO> getReservations() {
        return reservations;
    }

    public ArrayList<RestaurantsListDTO> getRestaurantLists() {
        return restaurantLists;
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

    public void setReservations(ArrayList<ReservationDTO> reservations) {
        this.reservations = reservations;
    }

    public void setRestaurantLists(ArrayList<RestaurantsListDTO> restaurantLists) {
        this.restaurantLists = restaurantLists;
    }

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", origin='" + origin + '\'' +
                ", suspended=" + suspended +
                ", role=" + role +
                ", reservations=" + reservations +
                ", restaurantLists=" + restaurantLists +
                '}';
    }
}
