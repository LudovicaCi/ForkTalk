package it.unipi.inginf.lsdb.group15.forktalk.dto;

public class GeneralUserDTO {
    private static String username;
    private String password;
    private static String email;

    // METODI GET
    public static String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    public static String getEmail() {

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
