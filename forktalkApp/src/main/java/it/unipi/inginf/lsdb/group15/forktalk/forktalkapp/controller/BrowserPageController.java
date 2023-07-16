package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
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
    public AnchorPane dynamicPane;
    public MenuItem topKRestButton;
    public MenuItem activeUserButton;
    public MenuItem lifespanButton;
    public MenuItem followedUsers;
    public MenuItem likedRestaurants;
    public MenuItem followedLists;
    public MenuItem suggestedUsers;
    public MenuItem suggestedRestaurants;
    public MenuItem suggestedLists;

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
        topKRestButton.setOnAction(event -> {
            try {
                openTopKAnalyticsPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        activeUserButton.setOnAction(event -> {
            try {
                openMostActiveAnalyticsPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        lifespanButton.setOnAction(event -> {
            try {
                openHighestLifeSpanRestPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        followedUsers.setOnAction(event -> {
            try {
                openTopKFollowedUsersPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        likedRestaurants.setOnAction(event -> {
            try {
                openTopKLikedRestaurantPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        suggestedRestaurants.setOnAction(event -> {
            try {
                openRestaurantsSuggestedPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        suggestedUsers.setOnAction(event -> {
            try {
                openUsersSuggestedPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        followedLists.setOnAction(event -> {
            try {
                openTopKFollowedRestaurantListsPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        suggestedLists.setOnAction(event -> {
            try {
                openRestaurantsListsSuggestedPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void openTopKAnalyticsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchTopKRatedRestaurantsByCuisine());

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openTopKFollowedUsersPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchMostFollowedUsers());
        analyticsPaneController.entity = 0;
        analyticsPaneController.cuisineField.setVisible(false);

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openTopKFollowedRestaurantListsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchMostFollowedRestaurantLists());
        analyticsPaneController.loadMoreButton.setOnAction(event -> analyticsPaneController.loadMoreRestaurantsLists());
        analyticsPaneController.cuisineField.setVisible(false);

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openTopKLikedRestaurantPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchMostLikedRestaurants());
        analyticsPaneController.cuisineField.setVisible(false);

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openUsersSuggestedPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchSuggestedUsers());
        analyticsPaneController.entity = 0;
        analyticsPaneController.cuisineField.setVisible(false);

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openRestaurantsSuggestedPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.suggestedRestaurants());
        analyticsPaneController.cuisineField.setVisible(false);

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openRestaurantsListsSuggestedPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchSuggestedRestaurantLists());
        analyticsPaneController.loadMoreButton.setOnAction(event -> analyticsPaneController.loadMoreRestaurantsLists());
        analyticsPaneController.cuisineField.setVisible(false);

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openMostActiveAnalyticsPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.cuisineField.setVisible(false);
        analyticsPaneController.entity = 0;
        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchKMostActiveUsers());


        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
    }

    private void openHighestLifeSpanRestPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AnalyticsPane.fxml"));
        AnalyticsPaneController analyticsPaneController = new AnalyticsPaneController();
        loader.setController(analyticsPaneController);
        Parent findTopRoot = loader.load();

        analyticsPaneController.cuisineField.setVisible(false);
        analyticsPaneController.searchButton.setOnAction(event -> analyticsPaneController.searchKHighestLifespanRestaurants());

        Region findTopRegion = (Region) findTopRoot;

        findTopRegion.setPrefWidth(searchBarPane.getWidth());
        findTopRegion.setPrefHeight(searchBarPane.getHeight());

        AnchorPane.setTopAnchor(findTopRoot, 0.0);
        AnchorPane.setRightAnchor(findTopRoot, 0.0);
        AnchorPane.setBottomAnchor(findTopRoot, 0.0);
        AnchorPane.setLeftAnchor(findTopRoot, 0.0);

        searchBarPane.getChildren().setAll(findTopRoot);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FindRestaurantPage.fxml"));
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
