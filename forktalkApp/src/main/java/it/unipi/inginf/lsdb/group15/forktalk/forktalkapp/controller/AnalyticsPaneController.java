package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jRestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.bson.Document;
import org.neo4j.driver.types.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AnalyticsPaneController implements Initializable{
    public Button searchButton;
    public AnchorPane dynamicPane;
    public Button loadMoreButton;
    public TextField numberField;
    public TextField cuisineField;

    public int entity; //0 = user and 1 = restaurant

    private List<Document> resultDocsList;
    private int currentIndex = 0;
    VBox resultContainer;
    List<Node> allRestLists;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resultContainer = new VBox();
        loadMoreButton.setVisible(false);
        loadMoreButton.setOnAction(this::loadMoreResults);
        entity = 1;
    }

    public void searchTopKRatedRestaurantsByCuisine(){
        if(numberField.getText().isEmpty() || cuisineField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        resultDocsList = RestaurantDAO.getTopKRatedRestaurantsByCuisine(Integer.parseInt(numberField.getText()), cuisineField.getText().toLowerCase());

        loadNextBatch();
    }

    public void searchKMostActiveUsers(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        List<Document> userDocs = RestaurantDAO.getUsersWithMostReviews(Integer.parseInt(numberField.getText()));
        resultDocsList = new ArrayList<>();
        for(Document user: userDocs){
            resultDocsList.add(UserDAO.getUserDocumentByUsername(user.getString("_id")));
        }

        loadNextBatch();
    }

    public void searchMostLikedRestaurants(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        List<Node> userNodes = Neo4jRestaurantDAO.getMostLikedRestaurants(Integer.parseInt(numberField.getText()));
        resultDocsList = new ArrayList<>();
        for(Node user: userNodes){
            resultDocsList.add(RestaurantDAO.getRestaurantDocumentById(user.get("rest_id").asString()));
        }

        loadNextBatch();
    }

    public void suggestedRestaurants(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        List<Node> userNodes = Neo4jUserDAO.getSuggestedRestaurant(Session.loggedUser.getUsername(), Integer.parseInt(numberField.getText()));
        resultDocsList = new ArrayList<>();
        for(Node user: userNodes){
            resultDocsList.add(RestaurantDAO.getRestaurantDocumentById(user.get("rest_id").asString()));
        }

        loadNextBatch();
    }

    public void searchMostFollowedUsers(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        List<Node> userNodes = Neo4jUserDAO.getMostFollowedUsers(Integer.parseInt(numberField.getText()));
        resultDocsList = new ArrayList<>();
        for(Node user: userNodes){
            resultDocsList.add(UserDAO.getUserDocumentByUsername(user.get("username").asString()));
        }

        loadNextBatch();
    }

    public void searchSuggestedUsers(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        List<Node> userNodes = Neo4jUserDAO.getSuggestedUsers(Session.loggedUser.getUsername(), Integer.parseInt(numberField.getText()));
        resultDocsList = new ArrayList<>();
        for(Node user: userNodes){
            resultDocsList.add(UserDAO.getUserDocumentByUsername(user.get("username").asString()));
        }

        loadNextBatch();
    }

    public void searchMostFollowedRestaurantLists(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        allRestLists = Neo4jUserDAO.getMostFollowedRestaurantList(Integer.parseInt(numberField.getText()));

        loadNextListBatch();
    }

    public void searchSuggestedRestaurantLists(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        allRestLists = Neo4jUserDAO.getSuggestedRestaurantList(Session.loggedUser.getUsername(), Integer.parseInt(numberField.getText()));

        loadNextListBatch();
    }

    public void searchKHighestLifespanRestaurants(){
        if(numberField.getText().isEmpty())
            Utils.showAlert("Please fill all the field!");

        List<Document> lifespanDocs = RestaurantDAO.getHighestLifespanRestaurants(Integer.parseInt(numberField.getText()));
        resultDocsList = new ArrayList<>();
        for(Document doc: lifespanDocs){
            resultDocsList.add(RestaurantDAO.getRestaurantDocumentById(doc.getString("rest_id")));
        }

        loadNextBatch();
    }

    public void loadMoreResults(ActionEvent event) {
        loadNextBatch();
    }

    public void loadNextBatch() {

        int batchSize = 5;
        if(resultDocsList.size() == 0) {
            Text noListText;
            if(entity == 1)
                noListText = new Text("No Restaurant Found");
            else
                noListText = new Text("No User Found");
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

        int endIndex = Math.min(currentIndex + batchSize, resultDocsList.size());
        List<Document> nextBatch = resultDocsList.subList(currentIndex, endIndex);

        for (Document doc : nextBatch) {
            try {
                if(entity == 1) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantWidget.fxml"));
                    RestaurantWidgetController widgetController = new RestaurantWidgetController();
                    fxmlLoader.setController(widgetController);
                    HBox restaurantWidget = fxmlLoader.load();

                    widgetController.setRestaurant(doc);

                    resultContainer.getChildren().add(restaurantWidget);
                }else if(entity == 0){
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/UserWidget.fxml"));
                    UserWidgetController widgetController = new UserWidgetController();
                    fxmlLoader.setController(widgetController);
                    HBox userWidget = fxmlLoader.load();

                    widgetController.setUser(doc);

                    resultContainer.getChildren().add(userWidget);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        loadMoreButton.setVisible(currentIndex < resultDocsList.size());

        setupResultView();
    }

    public void loadMoreRestaurantsLists() {
        loadNextListBatch();
    }

    public void loadNextListBatch() {
        int batchSize = 5;
        if(allRestLists.size() == 0) {
            Text noListText = new Text("No Restaurant List Found");
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

        int endIndex = Math.min(currentIndex + batchSize, allRestLists.size());
        List<Node> nextBatch = allRestLists.subList(currentIndex, endIndex);

        for (Node list : nextBatch) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantListBar.fxml"));
                RestaurantListBarController widgetController = new RestaurantListBarController();
                fxmlLoader.setController(widgetController);
                VBox restaurantListWidget = fxmlLoader.load();

                widgetController.currentPage = "/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FindListsBar.fxml";

                if(Session.loggedUser.getUsername().equals(list.get("owner").asString()))
                    widgetController.username = "";
                else {
                    widgetController.username = list.get("owner").asString();
                    if(Session.loggedUser.getRole() == 1)
                        widgetController.deleteButton.setVisible(false);
                }

                widgetController.setList(list.get("title").asString().trim(), list.get("owner").asString().trim());

                resultContainer.getChildren().add(restaurantListWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        loadMoreButton.setVisible(currentIndex < allRestLists.size());

        setupResultView();
    }


    public void resetView() {
        resultContainer.getChildren().clear();
        currentIndex = 0;
        loadMoreButton.setVisible(false);
    }

    private void setupResultView() {
        ScrollPane scrollPane = new ScrollPane(resultContainer);
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
