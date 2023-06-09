package it.unipi.inginf.lsdb.group15.forktalk.dto;

public class UserDTO extends GeneralUserDTO{
    private int id;
    private String nome;
    private String cognome;

    private String origin;
    private int role;

    private int nfollowers;


    //------------------------------------- ------------------------------------- -------------------------------------
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

    public int getNfollowers() {return nfollowers;}

    //------------------------------------- ------------------------------------- -------------------------------------
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

    public void setNfollowers(int nfollowers) {this.nfollowers = nfollowers;}

    //------------------------------------- ------------------------------------- -------------------------------------
    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", username='" + GeneralUserDTO.getUsername() + '\'' +
                ", firstName='" + nome + '\'' +
                ", lastName='" + cognome + '\'' +
                ", email='" + GeneralUserDTO.getEmail() + '\'' +
                ", phoneNumber='" + origin + '\'' +
                ", address='" + role + '\'' +
                ", nFollowers='" + nfollowers + '\'' +
                '}';

    }
}
