package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SignUpPageRestaurantController {
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
    private Button backButton;


    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBack);

        signUpButton.setOnAction(this::registerRestaurant);

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

            // Generazione casuale delle coordinate
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double generateLatitude() {
        // Limiti delle coordinate di latitudine in Inghilterra
        double minLatitude = 49.823809;  // Latitudine minima
        double maxLatitude = 58.785744;  // Latitudine massima

        Random random = new Random();
        double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();

        return Math.round(latitude * 1e6) / 1e6; // Arrotonda alle 6 cifre decimali
    }

    private double generateLongitude() {
        // Limiti delle coordinate di longitudine in Inghilterra
        double minLongitude = -6.417628;  // Longitudine minima
        double maxLongitude = 1.768926;   // Longitudine massima

        Random random = new Random();
        double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

        return Math.round(longitude * 1e6) / 1e6; // Arrotonda alle 6 cifre decimali
    }
}
