package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginRestaurantPageController implements Initializable {
    @FXML
    public TextField name;

    @FXML
    public TextField country;

    @FXML
    public TextField county;

    @FXML
    public TextField district;

    @FXML
    public TextField city;

    @FXML
    public TextField address;

    @FXML
    public TextField postcode;

    @FXML
    public TextField street_number;

    @FXML
    private TextField emailField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    public Button backButton;

    @FXML
    public TextField usernameLoginField;

    @FXML
    public PasswordField passwordLoginField;

    @FXML
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        loginButton.setOnAction(this::login);
        signUpButton.setOnAction(this::registerRestaurant);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    private void login(ActionEvent event) {
        try {
            //login restaurant
            RestaurantDTO loggedRest = RestaurantDAO.loginRestaurant(usernameLoginField.getText(), passwordLoginField.getText());

            if (loggedRest != null) {
                showAlert("login successful.");
                Session.setLoggedUser(null);
                Session.setLoggedRestaurant(loggedRest);
            } else {
                showAlert("Login failed. Please try again.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void registerRestaurant(ActionEvent event) {
        if (name.getText().isEmpty() || country.getText().isEmpty() ||
                county.getText().isEmpty() || district.getText().isEmpty() || city.getText().isEmpty() ||
                address.getText().isEmpty() || postcode.getText().isEmpty() || emailField.getText().isEmpty() ||
                signUpButton.getText().isEmpty() || street_number.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert("All fields are required.");
        } else {
            RestaurantDTO newRest = new RestaurantDTO();
            newRest.setName(name.getText());
            newRest.setCountry(country.getText());
            newRest.setCounty(county.getText());
            newRest.setDistrict(district.getText());
            newRest.setCity(city.getText());
            newRest.setAddress(address.getText());
            newRest.setStreetNumber(street_number.getText());
            newRest.setPostCode(postcode.getText());
            newRest.setEmail(emailField.getText());
            newRest.setUsername(username.getText());
            newRest.setPassword(password.getText());
            newRest.setReviews(new ArrayList<>());
            newRest.setReservations(new ArrayList<>());
            newRest.setFeatures(new ArrayList<>());
            ArrayList<String> location = new ArrayList<>();
            location.add(county.getText().toLowerCase());
            location.add(district.getText().toLowerCase());
            location.add(city.getText().toLowerCase());
            newRest.setLocation(location);

            double latitude = generateLatitude();
            double longitude = generateLongitude();
            ArrayList<String> coordinates = new ArrayList<>();
            coordinates.add(String.valueOf(longitude));
            coordinates.add(String.valueOf(latitude));
            newRest.setCoordinates(coordinates);

            boolean success = RestaurantDAO.addRestaurant(newRest);
            if (success) {
                showAlert("Registration successful.");
                Session.setLoggedUser(null);
                Session.setLoggedRestaurant(newRest);
            } else {
                showAlert("Registration failed. Please try again.");
            }
        }
    }

    private double generateLatitude() {
        double minLatitude = 49.823809;
        double maxLatitude = 58.785744;

        Random random = new Random();
        double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();

        return Math.round(latitude * 1e6) / 1e6;
    }

    private double generateLongitude() {
        double minLongitude = -6.417628;
        double maxLongitude = 1.768926;

        Random random = new Random();
        double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

        return Math.round(longitude * 1e6) / 1e6;
    }
}
