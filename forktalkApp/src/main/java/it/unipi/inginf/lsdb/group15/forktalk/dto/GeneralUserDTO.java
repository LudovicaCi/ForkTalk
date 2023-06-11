package it.unipi.inginf.lsdb.group15.forktalk.dto;

public class GeneralUserDTO {
    private static String username;
    private static String password;
    private static String email;
    private static boolean isRestaurant;

    // METODI GET

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getEmail() {
        return email;
    }

    public static boolean isIsRestaurant() {
        return isRestaurant;
    }

    // METODI SET

    public static void setUsername(String username) {
        GeneralUserDTO.username = username;
    }

    public static void setPassword(String password) {
        GeneralUserDTO.password = password;
    }

    public static void setEmail(String email) {
        GeneralUserDTO.email = email;
    }

    public static void setIsRestaurant(boolean isRestaurant) {
        GeneralUserDTO.isRestaurant = isRestaurant;
    }
}
