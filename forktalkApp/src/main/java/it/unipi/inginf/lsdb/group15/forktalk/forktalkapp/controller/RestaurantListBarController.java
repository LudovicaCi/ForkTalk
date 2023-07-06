package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import com.mongodb.internal.client.model.Util;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class RestaurantListBarController implements Initializable {
    public Text titleField;
    public Text nFollowers;
    public Button restListButton;

    public String restaurantId = "";
    public String currentPage = "";
    public String username = "";

    FXMLLoader loader;
    Parent root;
    ListPageController listPageController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restListButton.setOnAction(this::OpenListPage);
    }

    public void OpenListPage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void addToThisList() {
        RestaurantDTO restToAdd = RestaurantDAO.getRestaurantById(this.restaurantId);
        boolean result = UserDAO.addRestaurantToList(Session.loggedUser, titleField.getText(), restToAdd);

        if(!result)
            Utils.showAlert("Somenthing went wrong! please try again.");
    }

    public void setList(RestaurantsListDTO list) throws IOException {
        titleField.setText(list.getTitle());
        nFollowers.setText("0");

        loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/ListPage.fxml"));
        root = loader.load();
        listPageController = loader.getController();

        listPageController.setListPageInfo(titleField.getText(), this.username.equals("") ? Session.getLoggedUser().getUsername() : this.username);
    }
}
