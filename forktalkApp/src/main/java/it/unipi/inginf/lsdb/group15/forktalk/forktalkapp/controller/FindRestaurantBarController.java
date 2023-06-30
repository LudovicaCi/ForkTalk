package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FindRestaurantBarController implements Initializable {
    public TextField locationField;
    public TextField nameField;
    public TextField cuisine;
    public TextField keywordsField;
    public Button searchButton;
    public AnchorPane dynamicPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchButton.setOnAction(this::searchRestaurants);
    }

    public void searchRestaurants(ActionEvent event) {
        GridPane reservationGridPane = new GridPane();
        reservationGridPane.setPadding(new Insets(10));
        reservationGridPane.setVgap(10);
        reservationGridPane.setStyle("-fx-background-color: transparent;"); // Imposta lo sfondo trasparente


        List<Document> restFinded = RestaurantDAO.searchRestaurants(locationField.getText(), nameField.getText(), cuisine.getText(), keywordsField.getText());

        int row = 0;
        for (Document rest : restFinded) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/RestaurantWidget.fxml"));
                RestaurantWidgetController widgetController = new RestaurantWidgetController();
                fxmlLoader.setController(widgetController);
                HBox reservationWidget = fxmlLoader.load();

                // Imposta le informazioni della prenotazione nel widget
                widgetController.setRestaurant(rest);

                reservationGridPane.add(reservationWidget, 0, row);

                row++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ScrollPane scrollPane = new ScrollPane(reservationGridPane);
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
