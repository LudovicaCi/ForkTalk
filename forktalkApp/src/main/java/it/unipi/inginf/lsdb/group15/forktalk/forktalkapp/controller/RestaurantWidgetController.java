package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jRestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class RestaurantWidgetController implements Initializable {
    public Text rating;
    public Text location;
    public Text reviews;
    public Button showButton;
    public Text restName;
    public Button deleteButton;

    FXMLLoader loader;
    Document restaurant;
    Parent root;
    RestaurantPageController restPageController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showButton.setOnAction(this::openRestaurantPage);
        if(Session.loggedUser.getRole() != 2) {
            deleteButton.setVisible(false);
        }
        deleteButton.setOnAction(this::deleteRestaurant);

    }

    private void deleteRestaurant(ActionEvent event) {
        if(RestaurantDAO.deleteRestaurant(Utility.unpackRestaurant(restaurant))){
            if(Neo4jRestaurantDAO.deleteRestaurant(Utility.unpackRestaurant(restaurant))) {
                Utils.showAlert("OK!");
                Session.getFindRestaurantBarController().allRestaurants.remove(restaurant);
                Session.getFindRestaurantBarController().resetView();
                Session.getFindRestaurantBarController().loadNextBatch();
            }else{
                RestaurantDAO.recoverRestaurant(Utility.unpackRestaurant(restaurant));
                Utils.showAlert("Something went wrong! please try again.");
            }
        }else{
            Utils.showAlert("Something went wrong! please try again.");
        }
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
        this.restaurant = restaurant;
    }

    private void openRestaurantPage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
