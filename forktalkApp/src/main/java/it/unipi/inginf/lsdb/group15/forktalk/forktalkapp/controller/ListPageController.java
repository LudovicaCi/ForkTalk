package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.bson.Document;

import java.io.IOException;
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
    public Button loadMoreButton;

    private ArrayList<RestaurantDTO> restaurantOfList;
    private int currentIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        profileButton.setOnAction(this::openProfilePage);
        browseButton.setOnAction(this::openBrowserPage);
        logoutButton.setOnAction(this::logout);
        pageContainer = new VBox();
        loadMoreButton.setOnAction(this::loadMoreRestaurants);
        Session.setListPageController(this);
        loadMoreButton.setVisible(false);
        follower.setText("0");
        followButton.setOnAction(this::followList);
    }

    private void followList(ActionEvent event) {
        if(followButton.getText().equals("Follow")){
            Neo4jUserDAO.userFollowRestaurantList(Session.loggedUser.getUsername(), titleField.getText(), authorField.getText());
            if(Neo4jUserDAO.isUserFollowingRestaurantList(Session.loggedUser.getUsername(), authorField.getText(), titleField.getText())){
                followButton.setText("Unfollow");
                follower.setText(String.valueOf(Neo4jUserDAO.getNumFollowersRestaurantList(authorField.getText(), titleField.getText())));
            }else{
                Utils.showAlert("Something went wrong! Please try again.");
            }
        }else if(followButton.getText().equals("Unfollow")){
            Neo4jUserDAO.userUnfollowRestaurantList(Session.loggedUser.getUsername(), titleField.getText(), authorField.getText());
            if(!Neo4jUserDAO.isUserFollowingRestaurantList(Session.loggedUser.getUsername(), authorField.getText(), titleField.getText())){
                followButton.setText("Follow");
                follower.setText(String.valueOf(Neo4jUserDAO.getNumFollowersRestaurantList(authorField.getText(), titleField.getText())));
            }else{
                Utils.showAlert("Something went wrong! Please try again.");
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
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
    }

    public void setListPageInfo(String title, String author){
        titleField.setText(title);
        authorField.setText(author);
        follower.setText(String.valueOf(Neo4jUserDAO.getNumFollowersRestaurantList(author, title)));
        if(authorField.getText().equals(Session.getLoggedUser().getUsername())) {
            restaurantOfList = Objects.requireNonNull(UserDAO.getRestaurantsFromLists(Session.loggedUser, titleField.getText())).getRestaurants();
            followButton.setVisible(false);
        }else{
            UserDTO currentUser = UserDAO.getUserByUsername(authorField.getText());
            assert currentUser != null;
            restaurantOfList = Objects.requireNonNull(UserDAO.getRestaurantsFromLists(currentUser, titleField.getText())).getRestaurants();
        }
        numberRest.setText(String.valueOf(restaurantOfList.size()));
        if(Neo4jUserDAO.isUserFollowingRestaurantList(Session.loggedUser.getUsername(), author, title))
            followButton.setText("Unfollow");
        Session.setListPageController(this);
    }

    public void showRestaurants() {
        currentIndex = 0;
        pageContainer.getChildren().clear();
        loadNextBatch();
    }

    public void loadMoreRestaurants(ActionEvent event) {
        loadNextBatch(); // Carica il prossimo batch di ristoranti
    }

    private void loadNextBatch() {
        int batchSize = 5;

        if(restaurantOfList.size() == 0) {
            Text noListText = new Text("No Restaurants Yet");
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

        int endIndex = Math.min(currentIndex + batchSize, restaurantOfList.size());
        List<RestaurantDTO> nextBatch = restaurantOfList.subList(currentIndex, endIndex);

        for (RestaurantDTO rest : nextBatch) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantBar.fxml"));
                RestaurantBarController widgetController = new RestaurantBarController();
                fxmlLoader.setController(widgetController);
                VBox listWidget = fxmlLoader.load();

                widgetController.nameField.setText(rest.getName());
                widgetController.username = authorField.getText();
                widgetController.title = titleField.getText();
                widgetController.restaurantId = rest.getId();

                if(!authorField.getText().equals(Session.getLoggedUser().getUsername()))
                    widgetController.deleteButton.setVisible(false);


                pageContainer.getChildren().add(listWidget);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        loadMoreButton.setVisible(currentIndex < restaurantOfList.size());

        setupListsView();
    }

    private void resetView() {
        pageContainer.getChildren().clear();
        currentIndex = 0;
        loadMoreButton.setVisible(false);
    }

    private void setupListsView() {
        ScrollPane scrollPane = new ScrollPane(pageContainer);
        scrollPane.setFitToWidth(true);
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

    public void refreshLists() {
        resetView();
        showRestaurants();
    }

}
