package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpPageRestaurantController {
    @FXML
    private TextField emailField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        backButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/WelcomePage.fxml"));
                Parent welcomeRoot = fxmlLoader.load();
                Scene welcomeScene = new Scene(welcomeRoot);

                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(welcomeScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        signUpButton.setOnAction(event -> registerRestaurant());

    }

    private void registerRestaurant(){

    }
}
