package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RestaurantPageController implements Initializable {
    public Button backButton;
    public Button profileButton;
    public Button browseButton;
    public Button logoutButton;
    public Text restName;
    public Button writeReview;
    public Button addToList;
    public Text totalReviews;
    public Text tagField;
    public Text addressField;
    public Text priceField;
    public AnchorPane dynamicPane;
    public Button loadMoreButton;
    public ImageView star1;
    public ImageView star2;
    public ImageView star3;
    public ImageView star4;
    public ImageView star5;

    public VBox pageContainer;
    public Button showReview;
    public HBox bottomBox;
    public Pane parentContainer;
    public Button likeButton;
    public Button bookTableButton;
    private List<Document> reviewsDocuments;
    public String restId;
    private int currentIndex = 0;
    public double rate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        browseButton.setOnAction(this::openBrowserPage);
        logoutButton.setOnAction(this::logout);
        profileButton.setOnAction(this::openProfilePage);
        loadMoreButton.setOnAction(this::loadMoreReviews);
        pageContainer = new VBox();
        loadMoreButton.setVisible(false);
        showReview.setOnAction(this::showReviews);
        Session.setRestaurantPageController(this);
        writeReview.setOnAction(event -> {
            try {
                showReviewPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        addToList.setOnAction(event -> {
            try {
                showListPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        bookTableButton.setOnAction(event -> {
            try {
                showBookingPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        parentContainer = (Pane) bottomBox.getParent();
        likeButton.setOnAction(this::likeRestaurant);
    }

    private void likeRestaurant(ActionEvent event) {
        String url = ((ImageView) likeButton.getGraphic()).getImage().getUrl();

        Image newImage;
        if (url.substring(url.lastIndexOf('/') + 1).equals("empty_heart.png")) {
            Neo4jUserDAO.likeRestaurant(Session.loggedUser.getUsername(), restId);
            if(Neo4jUserDAO.isUserLikesRestaurant(Session.loggedUser.getUsername(), restId)) {
                newImage = new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/heart.png");
                ((ImageView) likeButton.getGraphic()).setImage(newImage);
            }else
                Utils.showAlert("Something went wrong! Please try again.");
        } else {
            Neo4jUserDAO.unlikeRestaurant(Session.loggedUser.getUsername(), restId);
            if(!Neo4jUserDAO.isUserLikesRestaurant(Session.loggedUser.getUsername(), restId)) {
                newImage = new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/empty_heart.png");
                ((ImageView) likeButton.getGraphic()).setImage(newImage);
            }else
                Utils.showAlert("Something went wrong! Please try again.");
        }
    }

    private void showListPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantsListPane.fxml"));
        Parent restListsRoot = loader.load();
        RestaurantsListController listRestController = loader.getController();
        listRestController.currentPage = "Restaurant";
        listRestController.restaurantId = this.restId;
        listRestController.showLists();
        this.loadMoreButton.setVisible(false);
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

    private void showReviewPane() throws IOException {
        Session.setRestaurantPageController(this);
        resetView();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/WriteReviewPane.fxml"));
        Parent writeReviewRoot = loader.load();

        Region writeReviewRegion = (Region) writeReviewRoot;

        writeReviewRegion.setPrefWidth(dynamicPane.getWidth());
        writeReviewRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(writeReviewRoot, 0.0);
        AnchorPane.setRightAnchor(writeReviewRoot, 0.0);
        AnchorPane.setBottomAnchor(writeReviewRoot, 0.0);
        AnchorPane.setLeftAnchor(writeReviewRoot, 0.0);

        dynamicPane.getChildren().setAll(writeReviewRoot);
    }

    public void showBookingPane() throws IOException {
        Session.setRestaurantPageController(this);
        resetView();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BookARestaurant.fxml"));
        Parent bookTableRoot = loader.load();

        Region bookTableRegion = (Region) bookTableRoot;

        bookTableRegion.setPrefWidth(dynamicPane.getWidth());
        bookTableRegion.setPrefHeight(dynamicPane.getHeight());

        AnchorPane.setTopAnchor(bookTableRoot, 0.0);
        AnchorPane.setRightAnchor(bookTableRoot, 0.0);
        AnchorPane.setBottomAnchor(bookTableRoot, 0.0);
        AnchorPane.setLeftAnchor(bookTableRoot, 0.0);

        dynamicPane.getChildren().setAll(bookTableRoot);
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

    public void updateStarImages() {
        if (rate != 0.0) {
            int fullStars = (int) rate;
            int halfStars = (rate - fullStars) >= 0.5 ? 1 : 0;

            star1.setImage(getStarImage(fullStars, halfStars, 0));
            star2.setImage(getStarImage(fullStars, halfStars, 1));
            star3.setImage(getStarImage(fullStars, halfStars, 2));
            star4.setImage(getStarImage(fullStars, halfStars, 3));
            star5.setImage(getStarImage(fullStars, halfStars, 4));
        } else {
            star1.setImage(getEmptyStarImage());
            star2.setImage(getEmptyStarImage());
            star3.setImage(getEmptyStarImage());
            star4.setImage(getEmptyStarImage());
            star5.setImage(getEmptyStarImage());
        }
    }


    private Image getEmptyStarImage() {
        return new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/empty_star.png");
    }

    private Image getStarImage(int fullStars, int halfStars, int index) {
        if (index < fullStars) {
            return new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/full_star.png");
        } else if (index == fullStars && halfStars > 0) {
            return new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/half_rating.png");
        } else {
            return new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/empty_star.png");
        }
    }


    public void setRestaurantInfo(String name,String id, String rate, String total, String address, String tag, List<Document> reviews, String price) {
        restName.setText(name);
        totalReviews.setText(total);
        addressField.setText(address);
        tagField.setText(tag);

        Image newImage;
        if (Neo4jUserDAO.isUserLikesRestaurant(Session.loggedUser.getUsername(), id)) {
            newImage = new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/heart.png");
        } else {
            newImage = new Image("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/empty_heart.png");
        }
        ((ImageView) likeButton.getGraphic()).setImage(newImage);

        if(price.equals("null"))
            priceField.setText("N/A");
        else
            priceField.setText(price);
        this.rate = (rate.equals("null")) ? 0.0 : Double.parseDouble(rate);

        Comparator<Document> comparator = Comparator.comparing((Document review) -> !review.getString("reviewer_pseudo").equals(Session.getLoggedUser().getUsername()))
                .thenComparing((Document review) -> review.getString("review_date"), Comparator.reverseOrder());

        reviews.sort(comparator);

        reviewsDocuments = reviews;
        this.restId = id;

        showReviews();
    }

    public RestaurantPageController getController() {
        return this;
    }

    public void showReviews() {
        currentIndex = 0;
        pageContainer.getChildren().clear();

        loadNextBatch();
    }

    public void showReviews(ActionEvent event) {
        refreshReviews();
    }

    public void loadMoreReviews(ActionEvent event) {
        loadNextBatch();
    }

    private void loadNextBatch() {
        int batchSize = 5;
        if(reviewsDocuments.size() == 0) {
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
        int endIndex = Math.min(currentIndex + batchSize, reviewsDocuments.size());
        List<Document> nextBatch = reviewsDocuments.subList(currentIndex, endIndex);

        for (Document review : nextBatch) {
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

        loadMoreButton.setVisible(currentIndex < reviewsDocuments.size());

        setupReviewView();
    }

    private void resetView() {
        pageContainer.getChildren().clear();
        currentIndex = 0;
        reviewsDocuments = null;
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

        Document restaurant = RestaurantDAO.getRestaurantDocumentById(restId);
        if(restaurant ==null)
            return;

        if(String.valueOf(restaurant.get("rest_rating")).equals("null")){
            this.rate = 0.0;
        }else{
            this.rate = Double.parseDouble(String.valueOf(restaurant.get("rest_rating")));
        }

        List<Document> updatedReviews = restaurant.getList("reviews", Document.class);

        Comparator<Document> comparator = Comparator.comparing((Document review) -> !review.getString("reviewer_pseudo").equals(Session.getLoggedUser().getUsername()))
                .thenComparing((Document review) -> review.getString("review_date"), Comparator.reverseOrder());

        updatedReviews.sort(comparator);

        reviewsDocuments = updatedReviews;
        totalReviews.setText(String.valueOf(reviewsDocuments.size()));
        updateStarImages();
        Session.setRestaurantPageController(this);

        showReviews();
    }

}
