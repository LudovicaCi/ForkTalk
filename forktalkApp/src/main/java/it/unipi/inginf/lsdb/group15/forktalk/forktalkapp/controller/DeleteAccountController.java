package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jRestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteAccountController implements Initializable {
    public Button cancelButton;
    public Button deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelButton.setOnAction(this::cancelDeleteProcess);
        if(Session.loggedUser != null)
            deleteButton.setOnAction(this::deleteUser);
        else
            deleteButton.setOnAction(this::deleteRestaurant);
    }

    private void deleteRestaurant(ActionEvent event) {
        if(RestaurantDAO.deleteRestaurant(Session.loggedRestaurant)){
            if(Neo4jRestaurantDAO.deleteRestaurant(Session.loggedRestaurant)) {
                Session.setLoggedUser(null);
                Session.setLoggedRestaurant(null);
                Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
            }else{
                RestaurantDAO.recoverRestaurant(Session.loggedRestaurant);
                Utils.showAlert("Delete User unsuccessful! Please try again.");
            }
        }else{
            Utils.showAlert("Delete User unsuccessful! Please try again.");
        }
    }

    private void cancelDeleteProcess(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        if(UserDAO.deleteUser(Session.getLoggedUser().getUsername())){
            if(Neo4jUserDAO.deleteUser(Session.loggedUser.getUsername())) {
                Session.setLoggedUser(null);
                Session.setLoggedRestaurant(null);
                Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
            }else{
                UserDAO.recoverUser(Session.loggedUser);
                Utils.showAlert("Delete User unsuccessful! Please try again.");
            }
        }else{
            Utils.showAlert("Delete User unsuccessful! Please try again.");
        }
    }
}
