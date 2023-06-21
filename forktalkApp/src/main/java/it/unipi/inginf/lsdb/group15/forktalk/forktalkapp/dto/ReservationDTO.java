package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto;

public class ReservationDTO {
    //    -------------------------------------
    private String clientUsername;
    private String clientName;
    private String clientSurname;
    private String restaurantID;
    private String restaurantName;
    private String restaurantCity;
    private String restaurantAddress;
    private String date;
    private int people;
    //    -------------------------------------

    /* ********* CONSTRUCTOR ********* */

    public ReservationDTO(String clientUsername, String clientName, String clientSurname, String restaurantID, String restaurantName, String restaurantCity, String restaurantAddress, String date, int people) {
        this.clientUsername = clientUsername;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantCity = restaurantCity;
        this.restaurantAddress = restaurantAddress;
        this.date = date;
        this.people = people;
    }

    public ReservationDTO(String date, String restaurantId, String restaurantName, String restaurantCity, String restaurantAddress, int people) {
        this.restaurantID = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantCity = restaurantCity;
        this.restaurantAddress = restaurantAddress;
        this.date = date;
        this.people = people;
    }

    public ReservationDTO(String date, String clientName, String clientUsername, String clientSurname, int people) {
        this.clientUsername = clientUsername;
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.date = date;
        this.people = people;
    }

    public ReservationDTO(String date) {
        this.date = date;
        this.clientUsername = null;
        this.clientName = null;
        this.clientSurname = null;
        this.restaurantID = null;
        this.restaurantName = null;
        this.restaurantCity = null;
        this.restaurantAddress = null;
        this.people = 0;
    }

    /* ********* GET METHOD ********* */

    public String getClientUsername() {
        return clientUsername;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantCity() {
        return restaurantCity;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getDate() {
        return date;
    }

    public int getPeople() {
        return people;
    }

    /* ********* SET METHOD ********* */

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setRestaurantCity(String restaurantCity) {
        this.restaurantCity = restaurantCity;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    /* ********* TO STRING METHOD ********* */

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "clientUsername='" + clientUsername + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientSurname='" + clientSurname + '\'' +
                ", restaurantID='" + restaurantID + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantCity='" + restaurantCity + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", date=" + date +
                ", people=" + people +
                '}';
    }
}
