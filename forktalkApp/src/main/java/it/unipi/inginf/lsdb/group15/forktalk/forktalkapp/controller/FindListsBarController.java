package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.neo4j.driver.types.Node;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FindListsBarController implements Initializable {
    public TextField titleField;
    public Button searchButton;
    public AnchorPane dynamicPane;

    public VBox restaurantListContainer;
    public Button loadMoreButton;
    List<Node> allRestLists;
    public int currentIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchButton.setOnAction(this::showLists);
        restaurantListContainer = new VBox();
        loadMoreButton.setVisible(false);
        loadMoreButton.setOnAction(this::loadMoreRestaurantsLists);
        Session.setFindListsBarController(this);
    }

    public void showLists(ActionEvent event) {
        currentIndex = 0;
        restaurantListContainer.getChildren().clear();
        if(titleField.getText().isEmpty())
            Utils.showAlert("Please insert the title in the field!");

        allRestLists = Neo4jUserDAO.searchRestaurantList(titleField.getText());
        loadNextBatch();
    }

    public void refreshLists() {
        currentIndex = 0;
        restaurantListContainer.getChildren().clear();
        if(titleField.getText().isEmpty())
            Utils.showAlert("Please insert the title in the field!");

        allRestLists = Neo4jUserDAO.searchRestaurantList(titleField.getText());
        loadNextBatch();
    }

    public void loadMoreRestaurantsLists(ActionEvent event) {
        loadNextBatch();
    }

    public void loadNextBatch() {
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

                restaurantListContainer.getChildren().add(restaurantListWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        loadMoreButton.setVisible(currentIndex < allRestLists.size());

        setupRestaurantListsView();
    }

    public void resetView() {
        restaurantListContainer.getChildren().clear();
        currentIndex = 0;
        loadMoreButton.setVisible(false);
    }

    private void setupRestaurantListsView() {
        ScrollPane scrollPane = new ScrollPane(restaurantListContainer);
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
