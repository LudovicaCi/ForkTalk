package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils.showAlert;

public class LoginUserPageController implements Initializable {
    @FXML
    public Button backButton;

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
    public TextField usernameLoginField;

    @FXML
    public PasswordField passwordLoginField;

    @FXML
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        signupButton.setOnAction(this::registerUser);
        loginButton.setOnAction(this::login);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
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
                Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml", event);
            } else {
                showAlert("Registration failed. Please try again.");
            }
        }
    }

    private void login(ActionEvent event){
        try {
            //login user
            UserDTO loggedUser = UserDAO.loginUser(usernameLoginField.getText(), passwordLoginField.getText());

            if (loggedUser != null) {
                Session.setLoggedUser(loggedUser);
                Session.setLoggedRestaurant(null);
                Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml", event);
            } else {
                showAlert("Login failed. Please try again.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
