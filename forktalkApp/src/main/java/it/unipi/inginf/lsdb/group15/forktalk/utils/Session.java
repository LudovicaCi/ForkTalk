package it.unipi.inginf.lsdb.group15.forktalk.utils;

import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.UserDTO;

public class Session {
    //    -------------------------------------
    public static UserDTO loggedUser = null;
    public static RestaurantDTO loggedRestaurant = null;
    //    -------------------------------------

    /* ********* GET METHOD ********* */

    public static UserDTO getLoggedUser() {
        return loggedUser;
    }

    public static RestaurantDTO getLoggedRestaurant() {
        return loggedRestaurant;
    }

    /* ********* SET METHOD ********* */

    public static void setLoggedUser(UserDTO loggedUser) {
        Session.loggedUser = loggedUser;
    }

    public static void setLoggedRestaurant(RestaurantDTO loggedRestaurant) {
        Session.loggedRestaurant = loggedRestaurant;
    }
}
