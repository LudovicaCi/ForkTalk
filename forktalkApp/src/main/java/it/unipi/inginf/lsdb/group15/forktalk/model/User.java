package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class User extends GeneralUser{
    private int id;
    private String nome;
    private String cognome;

    private String origin;

    private int role;


    // COSTRUTTORE
    public User(int id,String nome, String cognome, String email, String username, String password, String origin, ArrayList<Restaurant> restaurantList, ArrayList<Reservation> reservations, int role) {
        super (email, username, password);
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.origin = origin;
        this.role = role;
    }

}
