package it.unipi.inginf.lsdb.group15.forktalk.dto;

public class GeneralUserDTO {
    private String username;
    private String password;
    private String email;

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
}
