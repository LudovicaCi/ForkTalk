package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller.ListPageController;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller.RestaurantPageController;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller.RestaurantsListController;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;

public class Session {
    //    -------------------------------------
    public static UserDTO loggedUser = null;
    public static RestaurantDTO loggedRestaurant = null;
    public static RestaurantPageController restaurantPageController;
    public static RestaurantsListController restaurantsListController;
    public static ListPageController listPageController;
    //    -------------------------------------

    /* ********* GET METHOD ********* */

    public static UserDTO getLoggedUser() {
        return loggedUser;
    }

    public static RestaurantDTO getLoggedRestaurant() {
        return loggedRestaurant;
    }

    public static RestaurantsListController getRestaurantsListController() {
        return restaurantsListController;
    }

    public static RestaurantPageController getRestaurantPageController() {
        return restaurantPageController;
    }

    public static ListPageController getListPageController(){ return listPageController; }

    /* ********* SET METHOD ********* */

    public static void setLoggedUser(UserDTO loggedUser) {
        Session.loggedUser = loggedUser;
    }

    public static void setLoggedRestaurant(RestaurantDTO loggedRestaurant) {
        Session.loggedRestaurant = loggedRestaurant;
    }

    public static void setRestaurantPageController(RestaurantPageController restaurantPageController) {
        Session.restaurantPageController = restaurantPageController;
    }

    public static void setRestaurantsListController(RestaurantsListController restaurantsListController) {
        Session.restaurantsListController = restaurantsListController;
    }

    public static void setListPageController(ListPageController listPageController) {
        Session.listPageController = listPageController;
    }
}
