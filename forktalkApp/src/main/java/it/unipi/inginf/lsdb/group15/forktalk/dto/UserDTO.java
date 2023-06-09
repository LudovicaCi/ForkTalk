package it.unipi.inginf.lsdb.group15.forktalk.dto;

public class UserDTO extends GeneralUserDTO{
    private int id;
    private String nome;
    private String cognome;

    private String origin;
    private int role;



    // METODI GET
    public int getId() {
        return id;
    }

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
    // METODI SET
    public void setUsername(String username) {

        super.setUsername(username);
    }
    public void setPassword(String password) {

        super.setPassword(password);
    }

    public void setId(int id) {
        this.id = id;
    }

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
