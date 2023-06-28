package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonalPageController implements Initializable {
    public Button backButton;
    public Button logoutButton;
    public Label nameField;
    public Label usernameField;
    public Button searchButton;
    public Button editButton;
    public Button deleteButton;

    @FXML


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        logoutButton.setOnAction(this::logout);
        nameField.setText(Session.getLoggedUser().getName() + " " + Session.getLoggedUser().getSurname());
        usernameField.setText(Session.getLoggedUser().getUsername());
        searchButton.setOnAction(this::openSearchPage);
        editButton.setOnAction(this::handleModifyUser);
        deleteButton.setOnAction(this::handleDeleteUser);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml",event);
    }

    private void openSearchPage(ActionEvent event){
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml",event);
    }

    public void logout(ActionEvent event) {
        Session.setLoggedUser(null);
        Session.setLoggedRestaurant(null);
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    @FXML
    private void handleModifyUser(ActionEvent event) {
        // Logic for handling "Modify User" action
        // Replace this with your own implementation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modify User");
        alert.setHeaderText(null);
        alert.setContentText("Modify User selected!");
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        if(UserDAO.deleteUser(Session.getLoggedUser().getUsername())){
            Session.setLoggedUser(null);
            Session.setLoggedRestaurant(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete User");
            alert.setHeaderText(null);
            alert.setContentText("Delete User successful!");
            alert.showAndWait();
            Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete User");
            alert.setHeaderText(null);
            alert.setContentText("Delete User unsuccessful! Please try again.");
            alert.showAndWait();
        }
    }
}
