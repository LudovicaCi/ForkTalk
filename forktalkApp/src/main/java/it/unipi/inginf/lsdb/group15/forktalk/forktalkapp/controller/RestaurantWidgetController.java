package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RestaurantWidgetController implements Initializable {
    public Text rating;
    public Text location;
    public Text reviews;
    public Button showButton;
    public Text restName;

    FXMLLoader loader;
    Parent root;
    RestaurantPageController restPageController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showButton.setOnAction(this::openRestaurantPage);
    }

    public void setRestaurant(Document restaurant) throws IOException {
        restName.setText(restaurant.getString("restaurant_name"));
        location.setText(restaurant.getString("address").trim() + ", " + restaurant.getString("city"));
        if(String.valueOf(restaurant.get("rest_rating")).equals("null"))
            rating.setText("N/A");
        else
            rating.setText(String.valueOf(restaurant.get("rest_rating")));
        reviews.setText(String.valueOf(restaurant.get("reviews", ArrayList.class).size()));

        List<Document> reviewsDocuments = restaurant.getList("reviews", Document.class);


        loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantPage.fxml"));
        root = loader.load();
        restPageController = loader.getController();

        // Passa l'ID dell'utente al controller della nuova pagina
        restPageController.setRestaurantInfo(restaurant.getString("restaurant_name"), restaurant.getString("rest_id"), String.valueOf(restaurant.get("rest_rating")), String.valueOf(restaurant.get("reviews", ArrayList.class).size()),
                restaurant.getString("address").trim() + ", " + restaurant.getString("city") + ", " + restaurant.getString("country"),
                String.valueOf(restaurant.get("tag")), reviewsDocuments, String.valueOf(restaurant.get("price")));

        restPageController.updateStarImages();
    }

    private void openRestaurantPage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
