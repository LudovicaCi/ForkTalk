package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.IOException;
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
    public AnchorPane dynamicPane;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        logoutButton.setOnAction(this::logout);
        nameField.setText(Session.getLoggedUser().getName() + " " + Session.getLoggedUser().getSurname());
        usernameField.setText(Session.getLoggedUser().getUsername());
        searchButton.setOnAction(this::openSearchPage);
        editButton.setOnAction(event -> {
            try {
                handleModifyUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deleteButton.setOnAction(event -> {
            try {
                handleDeleteUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
    private void handleModifyUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/EditProfilePage.fxml"));
        Parent editProfileRoot = loader.load();

        Region editProfileRegion = (Region) editProfileRoot;

        editProfileRegion.setPrefWidth(dynamicPane.getWidth());
        editProfileRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(editProfileRoot, 0.0);
        AnchorPane.setRightAnchor(editProfileRoot, 0.0);
        AnchorPane.setBottomAnchor(editProfileRoot, 0.0);
        AnchorPane.setLeftAnchor(editProfileRoot, 0.0);

        dynamicPane.getChildren().setAll(editProfileRoot);
    }

    @FXML
    private void handleDeleteUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/DeleteUserPage.fxml"));
        Parent deleteProfileRoot = loader.load();

        Region deleteProfileRegion = (Region) deleteProfileRoot;

        deleteProfileRegion.setPrefWidth(dynamicPane.getWidth());
        deleteProfileRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(deleteProfileRoot, 0.0);
        AnchorPane.setRightAnchor(deleteProfileRoot, 0.0);
        AnchorPane.setBottomAnchor(deleteProfileRoot, 0.0);
        AnchorPane.setLeftAnchor(deleteProfileRoot, 0.0);

        dynamicPane.getChildren().setAll(deleteProfileRoot);
    }
}
