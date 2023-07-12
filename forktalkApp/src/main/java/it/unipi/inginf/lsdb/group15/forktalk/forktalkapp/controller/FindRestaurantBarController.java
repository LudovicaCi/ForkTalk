package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FindRestaurantBarController implements Initializable {
    public TextField locationField;
    public TextField nameField;
    public TextField cuisineField;
    public TextField keywordsField;
    public Button searchButton;
    public Button loadMoreButton;
    public AnchorPane dynamicPane;
    public TextField ratingField;

    private VBox restaurantContainer;
    public List<Document> allRestaurants;
    private int currentIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchButton.setOnAction(this::searchRestaurants);
        loadMoreButton.setOnAction(this::loadMoreRestaurants);
        restaurantContainer = new VBox();
        loadMoreButton.setVisible(false);
        Session.setFindRestaurantBarController(this);
    }

    public void searchRestaurants(ActionEvent event) {
        currentIndex = 0; // Reimposta l'indice corrente a 0
        restaurantContainer.getChildren().clear(); // Rimuovi i ristoranti precedenti dalla vista
        String location = locationField.getText().isEmpty() ? null : locationField.getText();
        String name = nameField.getText().isEmpty() ? null : nameField.getText();
        String cuisine = cuisineField.getText().isEmpty() ? null : cuisineField.getText();
        String keywords = keywordsField.getText().isEmpty() ? null : keywordsField.getText();
        String rating = ratingField.getText().isEmpty() ? null : ratingField.getText();

        allRestaurants = RestaurantDAO.searchRestaurants(location, name, cuisine, keywords, rating);

        loadNextBatch(); // Carica il primo batch di ristoranti
    }

    public void loadMoreRestaurants(ActionEvent event) {
        loadNextBatch(); // Carica il prossimo batch di ristoranti
    }

    public void loadNextBatch() {
        // Numero di ristoranti da caricare in ogni batch
        int batchSize = 5;
        if(allRestaurants.size() == 0) {
            Text noListText = new Text("No Restaurant Found");
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

        int endIndex = Math.min(currentIndex + batchSize, allRestaurants.size());
        List<Document> nextBatch = allRestaurants.subList(currentIndex, endIndex);

        for (Document rest : nextBatch) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantWidget.fxml"));
                RestaurantWidgetController widgetController = new RestaurantWidgetController();
                fxmlLoader.setController(widgetController);
                HBox restaurantWidget = fxmlLoader.load();

                // Imposta le informazioni del ristorante nel widget
                widgetController.setRestaurant(rest);

                restaurantContainer.getChildren().add(restaurantWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        // Controlla se ci sono ulteriori ristoranti da caricare
        if (currentIndex >= allRestaurants.size()) {
            loadMoreButton.setVisible(false); // Nascondi il pulsante "Carica altro" se non ci sono più ristoranti da caricare
        } else {
            loadMoreButton.setVisible(true); // Mostra il pulsante "Carica altro" se ci sono ancora ristoranti da caricare
        }

        setupRestaurantView();
    }

    public void resetView() {
        restaurantContainer.getChildren().clear();
        currentIndex = 0;
        loadMoreButton.setVisible(false);
    }

    private void setupRestaurantView() {
        ScrollPane scrollPane = new ScrollPane(restaurantContainer);
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
}