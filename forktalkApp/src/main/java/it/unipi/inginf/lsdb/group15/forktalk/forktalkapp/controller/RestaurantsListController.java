package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RestaurantsListController implements Initializable {
    public TextField titleField;
    public Button createButton;
    public AnchorPane dynamicPane;
    public Button loadMoreButton;
    public HBox topBox;

    public VBox ListsContainer;
    private int currentIndex = 0;
    public String currentPage = "";
    public String restaurantId = "";
    public String username = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setOnAction(this::createList);
        loadMoreButton.setVisible(false);
        ListsContainer = new VBox();
        loadMoreButton.setOnAction(this::loadMoreLists);
    }

    private void createList(ActionEvent event) {
        if(titleField.getText().isEmpty()) {
            Utils.showAlert("The title field is empty. Please write a title.");
        }else{
            if(UserDAO.createRestaurantListToUser(Session.getLoggedUser(), titleField.getText())) {
                if(Neo4jUserDAO.addRestaurantList(Session.getLoggedUser(), titleField.getText())) {
                    Session.getLoggedUser().setRestaurantLists(UserDAO.getRestaurantListsByUser(Session.loggedUser));
                    refreshLists();
                }else{
                    UserDAO.recoverRestaurantList(Session.loggedUser.getUsername(), Session.loggedUser.getRestaurantLists());
                    Utils.showAlert("Something went wrong! Please try again.");
                }
            }else
                Utils.showAlert("Something went wrong! Please try again.");
        }
    }

    public void showLists() {
        currentIndex = 0;
        ListsContainer.getChildren().clear();
        loadNextBatch();
    }

    public void loadMoreLists(ActionEvent event) {
        loadNextBatch(); // Carica il prossimo batch di ristoranti
    }

    private void loadNextBatch() {
        // Numero di ristoranti da caricare in ogni batch
        int batchSize = 5;
        ArrayList<RestaurantsListDTO> restListsDocuments;
        if(!this.username.equals("")){
            UserDTO userProfile = UserDAO.getUserByUsername(this.username);
            assert userProfile != null;
            restListsDocuments = UserDAO.getRestaurantListsByUser(userProfile);
        }else {
            restListsDocuments = Session.getLoggedUser().getRestaurantLists();
        }

        assert restListsDocuments != null;
        if(restListsDocuments.size() == 0) {
            Text noListText = new Text("No List Yet");
            noListText.setFill(Paint.valueOf("#00000080"));
            noListText.setStrokeType(StrokeType.OUTSIDE);
            noListText.setStrokeWidth(0.0);
            noListText.setTextAlignment(TextAlignment.CENTER);
            noListText.setWrappingWidth(119.40230560302734);
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

        int endIndex = Math.min(currentIndex + batchSize, restListsDocuments.size());
        List<RestaurantsListDTO> nextBatch = restListsDocuments.subList(currentIndex, endIndex);

        for (RestaurantsListDTO list : nextBatch) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantListBar.fxml"));
                RestaurantListBarController widgetController = new RestaurantListBarController();
                fxmlLoader.setController(widgetController);
                VBox listWidget = fxmlLoader.load();

                widgetController.restaurantId = this.restaurantId;
                widgetController.username = this.username;

                if(!this.username.equals(""))
                    widgetController.deleteButton.setVisible(false);

                if(currentPage.equals("Restaurant")) {
                    widgetController.currentPage = this.currentPage;
                    widgetController.restListButton.setText("Add");
                    widgetController.deleteButton.setVisible(false);
                    widgetController.restListButton.setOnAction(event -> widgetController.addToThisList());
                }

                widgetController.setList(list);
                Session.setRestaurantsListController(this);

                ListsContainer.getChildren().add(listWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        loadMoreButton.setVisible(currentIndex < restListsDocuments.size());

        setupListsView();
    }

    private void resetView() {
        ListsContainer.getChildren().clear();
        currentIndex = 0;
        loadMoreButton.setVisible(false);
    }

    private void setupListsView() {
        ScrollPane scrollPane = new ScrollPane(ListsContainer);
        scrollPane.setFitToWidth(true); // Abilita la ridimensione automatica in larghezza
        scrollPane.setFitToHeight(true); // Abilita la ridimensione automatica in altezza
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Mostra sempre la barra di scorrimento verticale
        scrollPane.setStyle("-fx-background-color: transparent;"); // Imposta lo sfondo trasparente

        // Rimuovi eventuali elementi precedenti dal dynamicPane
        dynamicPane.getChildren().clear();

        dynamicPane.setStyle("-fx-background-color: #F0F0F0;");

        // Aggiungi lo ScrollPane contenente il GridPane all'AnchorPane e adatta alla grandezza dell'AnchorPane
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        dynamicPane.getChildren().add(scrollPane);
    }

    public void refreshLists() {
        // Resetta la vista corrente
        resetView();
        showLists();
    }
}
