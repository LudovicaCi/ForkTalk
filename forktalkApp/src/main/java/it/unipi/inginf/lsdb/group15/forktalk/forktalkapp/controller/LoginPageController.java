package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {
    public TextField usernameField;
    public PasswordField passwordField;
    public RadioButton userRadioButton;
    public RadioButton restaurantRadioButton;
    public Button loginButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBack);
        loginButton.setOnAction(this::login);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/WelcomePage.fxml"));
            Parent welcomeRoot = fxmlLoader.load();
            Scene welcomeScene = new Scene(welcomeRoot);

            Stage stage = (Stage) backButton.getScene().getWindow();
            double windowWidth = stage.getWidth();
            double windowHeight = stage.getHeight();
            stage.setScene(welcomeScene);
            stage.setWidth(windowWidth);
            stage.setHeight(windowHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(ActionEvent event){
        if(userRadioButton.isSelected()){
            //login user
            UserDTO loggedUser = UserDAO.loginUser(usernameField.getText(), passwordField.getText());

            if(loggedUser != null){
                showAlert("login successful.");
                Session.setLoggedUser(loggedUser);
                Session.setLoggedRestaurant(null);
            }else{
                showAlert("Login failed. Please try again.");
            }
        }else if(restaurantRadioButton.isSelected()){
            //login restaurant
            RestaurantDTO loggedRest = RestaurantDAO.loginRestaurant(usernameField.getText(), passwordField.getText());

            if(loggedRest != null){
                showAlert("login successful.");
                Session.setLoggedUser(null);
                Session.setLoggedRestaurant(loggedRest);
            }else{
                showAlert("Login failed. Please try again.");
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
