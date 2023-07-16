package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller.*;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;

public class Session {
    //    -------------------------------------
    public static UserDTO loggedUser = null;
    public static RestaurantDTO loggedRestaurant = null;
    public static RestaurantPageController restaurantPageController;
    public static RestaurantsListController restaurantsListController;
    public static ListPageController listPageController;
    public static PersonalPageController personalPageController;
    public static RestaurantLoggedPageController restaurantLoggedPageController;
    public static FindRestaurantBarController findRestaurantBarController;
    public static FindUserBarController findUserBarController;
    public static FindListsBarController findListsBarController;
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

    public static PersonalPageController getPersonalPageController() {
        return personalPageController;
    }

    public static RestaurantLoggedPageController getRestaurantLoggedPageController() {
        return restaurantLoggedPageController;
    }

    public static FindRestaurantBarController getFindRestaurantBarController() {
        return findRestaurantBarController;
    }

    public static FindUserBarController getFindUserBarController() {
        return findUserBarController;
    }

    public static FindListsBarController getFindListsBarController(){return findListsBarController;}

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

    public static void setPersonalPageController(PersonalPageController personalPageController) {
        Session.personalPageController = personalPageController;
    }

    public static void setRestaurantLoggedPageController(RestaurantLoggedPageController restaurantLoggedPageController) {
        Session.restaurantLoggedPageController = restaurantLoggedPageController;
    }

    public static void setFindRestaurantBarController(FindRestaurantBarController findRestaurantBarController) {
        Session.findRestaurantBarController = findRestaurantBarController;
    }

    public static void setFindUserBarController(FindUserBarController findUserBarController) {
        Session.findUserBarController = findUserBarController;
    }

    public static void setFindListsBarController(FindListsBarController findListsBarController) {
        Session.findListsBarController = findListsBarController;
    }
}
