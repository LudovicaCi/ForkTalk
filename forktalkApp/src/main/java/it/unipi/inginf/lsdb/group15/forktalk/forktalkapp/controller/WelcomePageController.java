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

public class WelcomePageController implements Initializable {

    @FXML
    private Button signUpButton;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signUpButton.setOnAction(event -> openSignUpPage());
        loginButton.setOnAction(event -> openLoginPage());
    }

    private void openSignUpPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/SelectSignUpPage.fxml"));
            Parent signUpRoot = fxmlLoader.load();
            Scene signUpScene = new Scene(signUpRoot);

            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(signUpScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLoginPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/LoginPage.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Scene loginScene = new Scene(loginRoot);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



