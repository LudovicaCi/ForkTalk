package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantsListDTO;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Scene scene = restListButton.getScene();
        if (scene != null && scene.getRoot().getId().equals("restaurantPage")) {
            restListButton.setText("Add");
            restListButton.setOnAction(event -> addToThisList());
        } else if (scene != null && scene.getRoot().getId().equals("personalPage")) {
            restListButton.setText("Show List");
            restListButton.setOnAction(event -> OpenListPage());
        }
    }

    private void OpenListPage() {
    }

    private void addToThisList() {
    }

    public void setList(RestaurantsListDTO list){
        titleField.setText(list.getTitle());
        nFollowers.setText("0");
    }
}
