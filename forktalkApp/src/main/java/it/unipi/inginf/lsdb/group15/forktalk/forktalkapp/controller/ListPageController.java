package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ListPageController implements Initializable {
    public Button backButton;
    public Button profileButton;
    public Button browseButton;
    public Button logoutButton;
    public Label titleField;
    public Label authorField;
    public Label numberRest;
    public Label follower;
    public Button followButton;


    public VBox pageContainer;
    public AnchorPane dynamicPane;

    private ArrayList<RestaurantDTO> restaurantOfList;
    private int currentIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        profileButton.setOnAction(this::openProfilePage);
        browseButton.setOnAction(this::openBrowserPage);
        logoutButton.setOnAction(this::logout);
    }

    private void logout(ActionEvent event) {
        Session.setLoggedUser(null);
        Session.setLoggedRestaurant(null);
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    private void openBrowserPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml", event);
    }

    private void openProfilePage(ActionEvent event) {
        Utils.changeScene(" it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml", event);
    }

    public void setListPageInfo(String title, String author){
        titleField.setText(title);
        authorField.setText(author);
        follower.setText("0");
        if(authorField.getText().equals(Session.getLoggedUser().getUsername())) {
            restaurantOfList = Objects.requireNonNull(UserDAO.getRestaurantsFromLists(Session.loggedUser, titleField.getText())).getRestaurants();
            followButton.setVisible(false);
        }else{
            UserDTO currentUser = UserDAO.getUserByUsername(authorField.getText());
            assert currentUser != null;
            restaurantOfList = Objects.requireNonNull(UserDAO.getRestaurantsFromLists(currentUser, titleField.getText())).getRestaurants();
        }
        numberRest.setText(String.valueOf(restaurantOfList.size()));
    }

}
