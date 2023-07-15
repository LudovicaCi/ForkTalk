package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditRestaurantLoggedPageController implements Initializable {
    public TextField name;
    public TextField emailField;
    public TextField username;
    public PasswordField password;
    public TextField country;
    public TextField county;
    public TextField district;
    public TextField city;
    public TextField address;
    public TextField street_number;
    public TextField postcode;
    public TextField price;
    public TextField feature;
    public Button updateButton;
    public TextField maxClient;
    public Button setButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setOnAction(event -> {
            try {
                updateProfileInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        setButton.setOnAction(event -> {
            try {
                setAvailability();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setAvailability() throws IOException {
        if(maxClient.getText().isEmpty())
            Utils.showAlert("Please, insert an integer number for setting\n the new availability.");

        String input = maxClient.getText();
        if (input.matches("\\d+")) {
            int maxClients = Integer.parseInt(input);
            if(RestaurantDAO.setMaxClient(maxClients, Session.loggedRestaurant)){
                Session.setLoggedRestaurant(RestaurantDAO.getRestaurantById(Session.getLoggedRestaurant().getId()));
                Session.getRestaurantLoggedPageController().handleModifyRestaurant();
            }else {
                Utils.showAlert("Something went wrong! Please try again.");
            }
        } else {
            Utils.showAlert("Please, insert a valid integer number for setting\n the new availability.");
        }
    }

    private void updateProfileInfo() throws IOException {
        RestaurantDTO updatedRestInfo = new RestaurantDTO();

        updatedRestInfo.setId(Session.loggedRestaurant.getId());

        if (!name.getText().isEmpty()) {
            updatedRestInfo.setName(name.getText());
        }

        if (!emailField.getText().isEmpty()) {
            updatedRestInfo.setEmail(emailField.getText());
        }

        if (!username.getText().isEmpty()) {
            updatedRestInfo.setUsername(username.getText());
        }

        if (!password.getText().isEmpty()) {
            updatedRestInfo.setPassword(password.getText());
        }

        if (!country.getText().isEmpty()) {
            updatedRestInfo.setCountry(country.getText());
        }

        if (!county.getText().isEmpty()) {
            updatedRestInfo.setCounty(county.getText());
        }

        if (!district.getText().isEmpty()) {
            updatedRestInfo.setDistrict(district.getText());
        }

        if (!city.getText().isEmpty()) {
            updatedRestInfo.setCity(city.getText());
        }

        if (!address.getText().isEmpty()) {
            updatedRestInfo.setAddress(address.getText());
        }

        if (!street_number.getText().isEmpty()) {
            updatedRestInfo.setStreetNumber(street_number.getText());
        }

        if (!postcode.getText().isEmpty()) {
            updatedRestInfo.setPostCode(postcode.getText());
        }

        if (!price.getText().isEmpty()) {
            try {
                int priceValue = Integer.parseInt(price.getText());
                updatedRestInfo.setPrice(priceValue);
            } catch (NumberFormatException e) {
                Utils.showAlert("Insert a integer number for setting the price");
            }
        }

        if (!feature.getText().isEmpty()) {
            String featureText = feature.getText();
            if (featureText.matches("^\\b\\w+(,\\s*\\w+)*\\b$")) {
                String[] featureArray = featureText.split(",");
                ArrayList<String> newFeatures = new ArrayList<>();

                for (String featureValue : featureArray) {
                    String newWord = featureValue.trim();
                    newFeatures.add(newWord);
                }

                updatedRestInfo.setFeatures(newFeatures);
            } else {
                Utils.showAlert("Insert the new features in the correct format.");
            }
        }

        ArrayList<String> newLocation = new ArrayList<>();
        newLocation.add(updatedRestInfo.getCounty());
        newLocation.add(updatedRestInfo.getDistrict());
        newLocation.add(updatedRestInfo.getCity());
        updatedRestInfo.setLocation(newLocation);


        if(RestaurantDAO.updateRestaurant(updatedRestInfo)){
            Session.setLoggedRestaurant(RestaurantDAO.getRestaurantById(Session.getLoggedRestaurant().getId()));
            Session.getRestaurantLoggedPageController().handleModifyRestaurant();
            Session.getRestaurantLoggedPageController().nameField.setText(Session.loggedRestaurant.getName());
            Session.getRestaurantLoggedPageController().usernameField.setText(Session.loggedRestaurant.getUsername());
        }else{
            Utils.showAlert("Something went wrong! Please try again.");
        }
    }

}
