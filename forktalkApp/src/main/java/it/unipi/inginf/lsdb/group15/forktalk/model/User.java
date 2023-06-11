package it.unipi.inginf.lsdb.group15.forktalk.model;

import java.util.ArrayList;

public class User extends GeneralUser{
    private String nome;
    private String cognome;
    private String origin;
    private int role;


    // COSTRUTTORE
    public User(int id,String nome, String cognome, String email, String username, String password, String origin, ArrayList<Restaurant> restaurantList, ArrayList<Reservation> reservations, int role) {
        super (email, username, password);
        this.nome = nome;
        this.cognome = cognome;
        this.origin = origin;
        this.role = role;
    }

    //GET
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getOrigin() {
        return origin;
    }

    public int getRole() {
        return role;
    }

    //SET

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
