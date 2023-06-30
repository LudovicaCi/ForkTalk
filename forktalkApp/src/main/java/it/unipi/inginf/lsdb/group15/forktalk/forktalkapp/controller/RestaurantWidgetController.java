package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RestaurantWidgetController implements Initializable {
    public Text rating;
    public Text location;
    public Text reviews;
    public Button showButton;
    public Text restName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setRestaurant(Document restaurant){
        restName.setText(restaurant.getString("restaurant_name"));
        location.setText(restaurant.getString("address").trim() + ", " + restaurant.getString("city"));
        rating.setText(String.valueOf(restaurant.get("rest_rating")));
        reviews.setText(String.valueOf(restaurant.get("reviews", ArrayList.class).size()));
    }

    private void openRestaurantPage(ActionEvent event) {
    }
}
