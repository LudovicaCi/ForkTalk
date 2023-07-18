package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {
    public Label nameField;
    public Button backButton;
    public Button profileButton;
    public Button browseButton;
    public Button logoutButton;
    public Label usernameField;
    public AnchorPane dynamicPane;
    public Text followersNumber;
    public Text followingNumber;
    public Text listPostedNumber;
    public Button followButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        profileButton.setOnAction(this::openProfilePage);
        browseButton.setOnAction(this::openBrowserPage);
        logoutButton.setOnAction(this::logout);
        followingNumber.setText("0");
        followersNumber.setText("0");
        listPostedNumber.setText("0");
        followButton.setOnAction(this::followUser);
    }

    private void followUser(ActionEvent event) {
        if(followButton.getText().equals("Follow")){
            Neo4jUserDAO.userAFollowsUserB(Session.loggedUser.getUsername(), usernameField.getText());
            if(Neo4jUserDAO.isUserAFollowingUserB(Session.loggedUser.getUsername(), usernameField.getText())){
                followButton.setText("Unfollow");
                followersNumber.setText(String.valueOf(Neo4jUserDAO.getNumFollowersUser(usernameField.getText())));
            }else{
                Utils.showAlert("Something went wrong! Please try again.");
                followersNumber.setText(String.valueOf(Neo4jUserDAO.getNumFollowersUser(usernameField.getText())));
            }
        }else if(followButton.getText().equals("Unfollow")){
            Neo4jUserDAO.userAUnfollowUserB(Session.loggedUser.getUsername(), usernameField.getText());
            if(!Neo4jUserDAO.isUserAFollowingUserB(Session.loggedUser.getUsername(), usernameField.getText())){
                followButton.setText("Follow");
                followersNumber.setText(String.valueOf(Neo4jUserDAO.getNumFollowersUser(usernameField.getText())));
            }else{
                Utils.showAlert("Something went wrong! Please try again.");
                followersNumber.setText(String.valueOf(Neo4jUserDAO.getNumFollowersUser(usernameField.getText())));
            }
        }
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
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml", event);
    }

    public void setUserInfo(String username, String nameSurname, String listSize) throws IOException {
        this.nameField.setText(nameSurname);
        this.usernameField.setText(username);
        followersNumber.setText(String.valueOf(Neo4jUserDAO.getNumFollowersUser(username)));
        followingNumber.setText(String.valueOf(Neo4jUserDAO.getNumFollowingUsers(username)));
        listPostedNumber.setText(listSize);
        if(Neo4jUserDAO.isUserAFollowingUserB(Session.loggedUser.getUsername(), username))
            followButton.setText("Unfollow");
        showListPane();
    }

    private void showListPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantsListPane.fxml"));
        Parent restListsRoot = loader.load();
        RestaurantsListController listRestController = loader.getController();
        listRestController.username = usernameField.getText();
        listRestController.showLists();
        BorderPane borderPaneTop = (BorderPane) listRestController.topBox.getParent();
        borderPaneTop.getChildren().remove(listRestController.topBox);

        Region restListsRegion = (Region) restListsRoot;

        restListsRegion.setPrefWidth(dynamicPane.getWidth());
        restListsRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(restListsRoot, 0.0);
        AnchorPane.setRightAnchor(restListsRoot, 0.0);
        AnchorPane.setBottomAnchor(restListsRoot, 0.0);
        AnchorPane.setLeftAnchor(restListsRoot, 0.0);

        dynamicPane.getChildren().setAll(restListsRoot);
    }
}
