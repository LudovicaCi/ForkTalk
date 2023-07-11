package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RestaurantBarController implements Initializable {
    public Text nameField;
    public Button deleteButton;

    String username;
    String restaurantId;
    String title;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteButton.setOnAction(this::deleteRestaurantFromList);
    }

    private void deleteRestaurantFromList(ActionEvent event) {
        if (UserDAO.removeRestaurantFromList(Objects.requireNonNull(UserDAO.getUserByUsername(username)), title, restaurantId)){
            Session.getListPageController().setListPageInfo(title, username);
            Session.getListPageController().refreshLists();
        } else {
            Utils.showAlert("Something went wrong! please try again.");
        }
    }
}
