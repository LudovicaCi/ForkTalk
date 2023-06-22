package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectSignUpPageController implements Initializable{
    @FXML
    public Button restaurantButton;

    @FXML
    public Button userButton;

    @FXML
    public Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(event -> goBack());
        userButton.setOnAction(event -> goSignUpUser());
        restaurantButton.setOnAction(event -> goSignUpRestaurant());
    }

    private void goSignUpRestaurant() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/SignUpPageRestaurant.fxml"));
            Parent welcomeRoot = fxmlLoader.load();
            Scene welcomeScene = new Scene(welcomeRoot);

            Stage stage = (Stage) backButton.getScene().getWindow();
            double windowWidth = stage.getWidth();
            double windowHeight = stage.getHeight();
            stage.setScene(welcomeScene);
            stage.setWidth(windowWidth);
            stage.setHeight(windowHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goSignUpUser() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/SignUpPageUser.fxml"));
            Parent welcomeRoot = fxmlLoader.load();
            Scene welcomeScene = new Scene(welcomeRoot);

            Stage stage = (Stage) backButton.getScene().getWindow();
            double windowWidth = stage.getWidth();
            double windowHeight = stage.getHeight();
            stage.setScene(welcomeScene);
            stage.setWidth(windowWidth);
            stage.setHeight(windowHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/WelcomePage.fxml"));
            Parent welcomeRoot = fxmlLoader.load();
            Scene welcomeScene = new Scene(welcomeRoot);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(welcomeScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
