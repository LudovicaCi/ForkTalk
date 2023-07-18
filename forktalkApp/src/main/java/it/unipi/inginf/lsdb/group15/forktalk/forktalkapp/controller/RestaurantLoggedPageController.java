package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jRestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RestaurantLoggedPageController implements Initializable {
    public Button backButton;
    public Button logoutButton;
    public Label nameField;
    public Label usernameField;
    public Button editButton;
    public Button deleteButton;
    public Button reviewButton;
    public Button loadMoreButton;
    public AnchorPane dynamicPane;

    public VBox pageContainer;
    public Button reservationButton;
    public Button addSlotsButton;
    public Button statisticsButton;
    public Text numberLikes;
    private int currentIndex = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        logoutButton.setOnAction(this::logout);
        nameField.setText(Session.getLoggedRestaurant().getName());
        usernameField.setText(Session.getLoggedRestaurant().getUsername());
        loadMoreButton.setVisible(false);
        numberLikes.setText(String.valueOf(Neo4jRestaurantDAO.getNumLikesRestaurant(Session.getLoggedRestaurant().getId())));
        loadMoreButton.setOnAction(this::loadMoreReviews);
        reviewButton.setOnAction(this::showReviews);
        pageContainer = new VBox();
        reservationButton.setOnAction(this::createReservationGridPane);
        Session.setRestaurantLoggedPageController(this);
        addSlotsButton.setOnAction(event -> {
            try {
                openAddSlotsPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        statisticsButton.setOnAction(event -> {
            try {
                openStatisticsPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deleteButton.setOnAction(event -> {
            try {
                handleDeleteRestaurant();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        editButton.setOnAction(event -> {
            try{
                handleModifyRestaurant();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    public void logout(ActionEvent event) {
        Session.setLoggedUser(null);
        Session.setLoggedRestaurant(null);
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    public void openStatisticsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantStatistics.fxml"));
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

    public void handleDeleteRestaurant() throws IOException {
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

    public void openAddSlotsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/AddReservationSlots.fxml"));
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

    public void handleModifyRestaurant() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/EditRestaurantLoggedPage.fxml"));
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

    public void showReviews(ActionEvent event) {
        dynamicPane.getChildren().clear();
        currentIndex = 0;
        pageContainer.getChildren().clear();
        loadNextBatch();
    }

    public void showReviews() {
        dynamicPane.getChildren().clear();
        currentIndex = 0;
        pageContainer.getChildren().clear();
        loadNextBatch();
    }

    public void loadMoreReviews(ActionEvent event) {
        loadNextBatch();
    }

    private void loadNextBatch() {
        int batchSize = 5;
        if(Session.loggedRestaurant.getReviews().size() == 0) {
            Text noListText = new Text("No Reviews Yet");
            noListText.setFill(Paint.valueOf("#00000080"));
            noListText.setStrokeType(StrokeType.OUTSIDE);
            noListText.setStrokeWidth(0.0);
            noListText.setTextAlignment(TextAlignment.CENTER);
            noListText.setWrappingWidth(300);
            noListText.setFont(new Font(24.0));
            VBox newBox = new VBox();
            newBox.getChildren().setAll(noListText);
            newBox.setAlignment(Pos.CENTER);

            AnchorPane.setTopAnchor(newBox, 0.0);
            AnchorPane.setBottomAnchor(newBox, 0.0);
            AnchorPane.setLeftAnchor(newBox, 0.0);
            AnchorPane.setRightAnchor(newBox, 0.0);

            dynamicPane.getChildren().add(newBox);
            return;
        }
        int endIndex = Math.min(currentIndex + batchSize, Session.loggedRestaurant.getReviews().size());
        List<ReviewDTO> nextBatch = Session.loggedRestaurant.getReviews().subList(currentIndex, endIndex);

        for (ReviewDTO review : nextBatch) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/ReviewWidget.fxml"));
                ReviewWidgetController widgetController = new ReviewWidgetController();
                fxmlLoader.setController(widgetController);
                VBox reviewWidget = fxmlLoader.load();

                widgetController.setReview(review);
                widgetController.updateStarImages();

                pageContainer.getChildren().add(reviewWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        loadMoreButton.setVisible(currentIndex < Session.loggedRestaurant.getReviews().size());

        setupReviewView();
    }

    private void resetView() {
        pageContainer.getChildren().clear();
        currentIndex = 0;
        loadMoreButton.setVisible(false);
    }

    private void setupReviewView() {
        ScrollPane scrollPane = new ScrollPane(pageContainer);
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

    public void refreshReviews() {
        resetView();
        Session.loggedRestaurant.setReviews(RestaurantDAO.getReviews(Session.loggedUser.getUsername()));
        showReviews();
    }

    public void createReservationGridPane(ActionEvent event) {
        pageContainer.getChildren().clear();

        List<ReservationDTO> reservationList = Session.getLoggedRestaurant().getReservations();

        for (ReservationDTO reservation : reservationList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/ReservationWidget.fxml"));
                ReservationWidgetController widgetController = new ReservationWidgetController();
                fxmlLoader.setController(widgetController);
                VBox reservationWidget = fxmlLoader.load();

                widgetController.setReservation(reservation);

                pageContainer.getChildren().add(reservationWidget);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ScrollPane scrollPane = new ScrollPane(pageContainer);
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
