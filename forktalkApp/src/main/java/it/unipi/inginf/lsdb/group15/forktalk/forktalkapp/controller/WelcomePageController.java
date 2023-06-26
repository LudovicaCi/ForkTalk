package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;
import javafx.event.ActionEvent;
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
        signUpButton.setOnAction(this::openSignUpPage);
        loginButton.setOnAction(this::openLoginPage);
    }

    private void openSignUpPage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/SelectSignUpPage.fxml"));
            Parent signUpRoot = fxmlLoader.load();
            Scene signUpScene = new Scene(signUpRoot);

            Stage stage = (Stage) signUpButton.getScene().getWindow();
            double windowWidth = stage.getWidth();
            double windowHeight = stage.getHeight();
            stage.setScene(signUpScene);
            stage.setWidth(windowWidth);
            stage.setHeight(windowHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLoginPage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/LoginPage.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Scene loginScene = new Scene(loginRoot);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            double windowWidth = stage.getWidth();
            double windowHeight = stage.getHeight();
            stage.setScene(loginScene);
            stage.setWidth(windowWidth);
            stage.setHeight(windowHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



