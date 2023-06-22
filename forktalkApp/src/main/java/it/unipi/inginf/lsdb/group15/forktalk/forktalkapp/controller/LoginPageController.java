package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {
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
    }
}
