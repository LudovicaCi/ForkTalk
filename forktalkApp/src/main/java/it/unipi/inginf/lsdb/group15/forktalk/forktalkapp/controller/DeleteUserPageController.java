package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteUserPageController implements Initializable {
    public Button cancelButton;
    public Button deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelButton.setOnAction(this::cancelDeleteProcess);
        deleteButton.setOnAction(this::handleDeleteUser);
    }

    private void cancelDeleteProcess(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        if(UserDAO.deleteUser(Session.getLoggedUser().getUsername())){
            Session.setLoggedUser(null);
            Session.setLoggedRestaurant(null);
            Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
        }else{
            Utils.showAlert("Delete User unsuccessful! Please try again.");
        }
    }
}
