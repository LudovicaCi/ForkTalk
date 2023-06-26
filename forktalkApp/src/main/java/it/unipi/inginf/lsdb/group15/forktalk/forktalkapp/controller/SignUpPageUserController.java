package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpPageUserController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField originField;

    @FXML
    private Button signupButton;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setOnAction(this::handleBack);
        signupButton.setOnAction(this::registerUser);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/SelectSignUpPage.fxml"));
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

    @FXML
    private void registerUser(ActionEvent event) {
        if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || originField.getText().isEmpty()) {
            showAlert("All fields are required.");
        } else {
            UserDTO newUser = new UserDTO();
            newUser.setName(nameField.getText());
            newUser.setSurname(surnameField.getText());
            newUser.setEmail(emailField.getText());
            newUser.setUsername(usernameField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setOrigin(originField.getText());
            newUser.setSuspended(0);
            newUser.setRole(1);

            boolean success = UserDAO.registerUser(newUser);
            if (success) {
                showAlert("Registration successful.");
                Session.setLoggedUser(newUser);
                Session.setLoggedRestaurant(null);
            } else {
                showAlert("Registration failed. Please try again.");
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
