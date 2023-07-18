package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PersonalPageController implements Initializable {
    public Button backButton;
    public Button logoutButton;
    public Label nameField;
    public Label usernameField;
    public Button searchButton;
    public Button editButton;
    public Button deleteButton;
    public AnchorPane dynamicPane;
    public Button bookingButton;
    public Button showLists;
    public Text nListPosted;

    public VBox pageContainer;
    public Text followersNumber;
    public Text followingNumbers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        logoutButton.setOnAction(this::logout);
        nameField.setText(Session.getLoggedUser().getName() + " " + Session.getLoggedUser().getSurname());
        usernameField.setText(Session.getLoggedUser().getUsername());
        searchButton.setOnAction(this::openSearchPage);
        followersNumber.setText(String.valueOf(Neo4jUserDAO.getNumFollowersUser(Session.loggedUser.getUsername())));
        followingNumbers.setText(String.valueOf(Neo4jUserDAO.getNumFollowingUsers(Session.loggedUser.getUsername())));
        nListPosted.setText(String.valueOf(Session.loggedUser.getRestaurantLists().size()));
        pageContainer = new VBox();
        editButton.setOnAction(event -> {
            try {
                handleModifyUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        deleteButton.setOnAction(event -> {
            try {
                handleDeleteUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        bookingButton.setOnAction(this::createReservationGridPane);
        showLists.setOnAction(event -> {
            try {
                showListPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Session.setPersonalPageController(this);
    }

    private void showListPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantsListPane.fxml"));
        Parent restListsRoot = loader.load();
        RestaurantsListController listRestController = loader.getController();
        listRestController.showLists();

        Region restListsRegion = (Region) restListsRoot;

        restListsRegion.setPrefWidth(dynamicPane.getWidth());
        restListsRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(restListsRoot, 0.0);
        AnchorPane.setRightAnchor(restListsRoot, 0.0);
        AnchorPane.setBottomAnchor(restListsRoot, 0.0);
        AnchorPane.setLeftAnchor(restListsRoot, 0.0);

        dynamicPane.getChildren().setAll(restListsRoot);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml",event);
    }

    private void openSearchPage(ActionEvent event){
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml",event);
    }

    public void logout(ActionEvent event) {
        Session.setLoggedUser(null);
        Session.setLoggedRestaurant(null);
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    @FXML
    private void handleModifyUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/EditProfilePage.fxml"));
        Parent editProfileRoot = loader.load();

        Region editProfileRegion = (Region) editProfileRoot;

        editProfileRegion.setPrefWidth(dynamicPane.getWidth());
        editProfileRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(editProfileRoot, 0.0);
        AnchorPane.setRightAnchor(editProfileRoot, 0.0);
        AnchorPane.setBottomAnchor(editProfileRoot, 0.0);
        AnchorPane.setLeftAnchor(editProfileRoot, 0.0);

        dynamicPane.getChildren().setAll(editProfileRoot);
    }

    @FXML
    private void handleDeleteUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/DeleteAccount.fxml"));
        Parent deleteProfileRoot = loader.load();

        Region deleteProfileRegion = (Region) deleteProfileRoot;

        deleteProfileRegion.setPrefWidth(dynamicPane.getWidth());
        deleteProfileRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(deleteProfileRoot, 0.0);
        AnchorPane.setRightAnchor(deleteProfileRoot, 0.0);
        AnchorPane.setBottomAnchor(deleteProfileRoot, 0.0);
        AnchorPane.setLeftAnchor(deleteProfileRoot, 0.0);

        dynamicPane.getChildren().setAll(deleteProfileRoot);
    }

    @FXML
    public void createReservationGridPane(ActionEvent event) {
        GridPane reservationGridPane = new GridPane();
        reservationGridPane.setPadding(new Insets(10));
        reservationGridPane.setVgap(10);
        reservationGridPane.setStyle("-fx-background-color: transparent;");

        List<ReservationDTO> reservationList = Session.getLoggedUser().getReservations();

        int row = 0;
        for (ReservationDTO reservation : reservationList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/ReservationWidget.fxml"));
                ReservationWidgetController widgetController = new ReservationWidgetController();
                fxmlLoader.setController(widgetController);
                VBox reservationWidget = fxmlLoader.load();

                widgetController.setReservation(reservation);

                reservationGridPane.add(reservationWidget, 0, row);

                row++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ScrollPane scrollPane = new ScrollPane(reservationGridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setStyle("-fx-background-color: transparent;");


        dynamicPane.getChildren().clear();

        dynamicPane.setStyle("-fx-background-color: #F0F0F0;");

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        dynamicPane.getChildren().add(scrollPane);
    }
}
