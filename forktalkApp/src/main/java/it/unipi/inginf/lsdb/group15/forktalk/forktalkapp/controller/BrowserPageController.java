package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BrowserPageController implements Initializable {

    public Button userButton;
    public Button logoutButton;
    public Button findRestButton;
    public HBox searchBarPane;
    public Button findUserButton;
    public Button findListsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userButton.setOnAction(this::goToProfilePage);
        logoutButton.setOnAction(this::logout);
        findRestButton.setOnAction(event -> {
            try {
                openRestBar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        findUserButton.setOnAction(event -> {
            try {
                openUserBar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        findListsButton.setOnAction(event -> {
            try {
                openListsBar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void openListsBar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FindListsBar.fxml"));
        Parent findListsRoot = loader.load();

        Region findListsRegion = (Region) findListsRoot;

        findListsRegion.setPrefWidth(searchBarPane.getWidth());
        findListsRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findListsRoot, 0.0);
        AnchorPane.setRightAnchor(findListsRoot, 0.0);
        AnchorPane.setBottomAnchor(findListsRoot, 0.0);
        AnchorPane.setLeftAnchor(findListsRoot, 0.0);

        searchBarPane.getChildren().setAll(findListsRoot);
    }

    private void openUserBar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FindUserBar.fxml"));
        Parent findUserRoot = loader.load();

        Region findUserRegion = (Region) findUserRoot;

        findUserRegion.setPrefWidth(searchBarPane.getWidth());
        findUserRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findUserRoot, 0.0);
        AnchorPane.setRightAnchor(findUserRoot, 0.0);
        AnchorPane.setBottomAnchor(findUserRoot, 0.0);
        AnchorPane.setLeftAnchor(findUserRoot, 0.0);

        searchBarPane.getChildren().setAll(findUserRoot);
    }

    private void openRestBar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FindRestaurantBar.fxml"));
        Parent findRestRoot = loader.load();

        Region findRestRegion = (Region) findRestRoot;

        findRestRegion.setPrefWidth(searchBarPane.getWidth());
        findRestRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findRestRoot, 0.0);
        AnchorPane.setRightAnchor(findRestRoot, 0.0);
        AnchorPane.setBottomAnchor(findRestRoot, 0.0);
        AnchorPane.setLeftAnchor(findRestRoot, 0.0);

        searchBarPane.getChildren().setAll(findRestRoot);
    }

    @FXML
    void goToProfilePage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
    }

    public void logout(ActionEvent event) {
        Session.setLoggedUser(null);
        Session.setLoggedRestaurant(null);
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }
}
