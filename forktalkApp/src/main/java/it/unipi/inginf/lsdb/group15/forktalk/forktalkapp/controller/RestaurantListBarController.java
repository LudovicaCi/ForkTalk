package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import com.mongodb.internal.client.model.Util;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RestaurantListBarController implements Initializable {
    public Text titleField;
    public Text nFollowers;
    public Button restListButton;

    public String restaurantId = "";
    public String currentPage ="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restListButton.setOnAction(event -> OpenListPage());
    }

    public void OpenListPage() {
    }

    public void addToThisList() {
        RestaurantDTO restToAdd = RestaurantDAO.getRestaurantById(this.restaurantId);
        boolean result = UserDAO.addRestaurantToList(Session.loggedUser, titleField.getText(), restToAdd);

        if(!result)
            Utils.showAlert("Somenthing went wrong! please try again.");
    }

    public void setList(RestaurantsListDTO list){
        titleField.setText(list.getTitle());
        nFollowers.setText("0");
    }
}
