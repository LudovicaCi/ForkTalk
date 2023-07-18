package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import com.mongodb.internal.client.model.Util;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
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
    public Button deleteButton;

    public String restaurantId = "";
    public String currentPage = "";
    public String username = "";
    public String author = " ";

    FXMLLoader loader;
    Parent root;
    ListPageController listPageController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        restListButton.setOnAction(this::OpenListPage);
        deleteButton.setOnAction(this::deleteList);
        nFollowers.setText("0");
    }

    private void deleteList(ActionEvent event) {
        if(Session.loggedUser.getUsername().equals(author)) {
            if (UserDAO.deleteRestaurantListFromUser(Session.loggedUser.getUsername(), titleField.getText())) {
                if (Neo4jUserDAO.deleteRestaurantList(Session.loggedUser.getUsername(), titleField.getText())){
                    if(!currentPage.equals("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FindListsBar.fxml")) {
                        Session.loggedUser.setRestaurantLists(UserDAO.getRestaurantListsByUser(Session.getLoggedUser()));
                        Session.getRestaurantsListController().refreshLists();
                    }else {
                        Session.getFindListsBarController().refreshLists();
                    }
                } else {
                    UserDAO.recoverRestaurantList(Session.loggedUser.getUsername(), Session.loggedUser.getRestaurantLists());
                    Utils.showAlert("Something went wrong! Please try again.");
                }
            } else {
                Utils.showAlert("Something went wrong! Please try again.");
            }
        }else if(!Session.loggedUser.getUsername().equals(author) && Session.loggedUser.getRole() == 2){
            if (UserDAO.deleteRestaurantListFromUser(author, titleField.getText())) {
                if (Neo4jUserDAO.deleteRestaurantList(author, titleField.getText())) {
                    if(!currentPage.equals("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FindListsBar.fxml")) {
                        Session.getRestaurantsListController().refreshLists();
                    }else {
                        Session.getFindListsBarController().refreshLists();
                    }
                } else {
                    UserDAO.recoverRestaurantList(Session.loggedUser.getUsername(), Session.loggedUser.getRestaurantLists());
                    Utils.showAlert("Something went wrong! Please try again.");
                }
            } else {
                Utils.showAlert("Something went wrong! Please try again.");
            }
        }
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
            Utils.showAlert("Something went wrong! please try again.");
    }

    public void setList(String title, String author) throws IOException {
        this.author = author;
        titleField.setText(title);
        nFollowers.setText(String.valueOf(Neo4jUserDAO.getNumFollowersRestaurantList(author, title)));

        loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/ListPage.fxml"));
        root = loader.load();
        listPageController = loader.getController();

        listPageController.setListPageInfo(titleField.getText(), this.username.equals("") ? Session.getLoggedUser().getUsername() : this.username);

        listPageController.showRestaurants();
    }
}
