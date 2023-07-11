package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.bson.Document;


import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
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
    private int currentIndex = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        logoutButton.setOnAction(this::logout);
        nameField.setText(Session.getLoggedRestaurant().getName());
        usernameField.setText(Session.getLoggedRestaurant().getUsername());
        loadMoreButton.setVisible(false);
        loadMoreButton.setOnAction(this::loadMoreReviews);
        reviewButton.setOnAction(this::showReviews);
        pageContainer = new VBox();
        reservationButton.setOnAction(this::createReservationGridPane);
        Session.setRestaurantLoggedPageController(this);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    public void logout(ActionEvent event) {
        Session.setLoggedUser(null);
        Session.setLoggedRestaurant(null);
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml", event);
    }

    public void showReviews(ActionEvent event) {
        currentIndex = 0; // Reimposta l'indice corrente a 0
        pageContainer.getChildren().clear(); // Rimuovi i ristoranti precedenti dalla vista
        loadNextBatch(); // Carica il primo batch di ristoranti
    }

    public void showReviews() {
        currentIndex = 0; // Reimposta l'indice corrente a 0
        pageContainer.getChildren().clear(); // Rimuovi i ristoranti precedenti dalla vista
        loadNextBatch(); // Carica il primo batch di ristoranti
    }

    public void loadMoreReviews(ActionEvent event) {
        loadNextBatch(); // Carica il prossimo batch di ristoranti
    }

    private void loadNextBatch() {
        // Numero di ristoranti da caricare in ogni batch
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

                // Imposta le informazioni del ristorante nel widget
                widgetController.setReview(review);
                widgetController.updateStarImages();

                pageContainer.getChildren().add(reviewWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        // Controlla se ci sono ulteriori ristoranti da caricare
        // Mostra il pulsante "Carica altro" se ci sono ancora ristoranti da caricare
        loadMoreButton.setVisible(currentIndex < Session.loggedRestaurant.getReviews().size()); // Nascondi il pulsante "Carica altro" se non ci sono più ristoranti da caricare

        setupReviewView();
    }

    private void resetView() {
        pageContainer.getChildren().clear();
        currentIndex = 0;
        loadMoreButton.setVisible(false);
    }

    private void setupReviewView() {
        ScrollPane scrollPane = new ScrollPane(pageContainer);
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

    public void refreshReviews() {
        resetView();
        Session.loggedRestaurant.setReviews(RestaurantDAO.getReviews(Session.loggedUser.getUsername()));
        showReviews();
    }

    public void createReservationGridPane(ActionEvent event) {
        pageContainer.getChildren().clear();

        // Recupera la lista di prenotazioni dell'utente dalla classe Sessione
        List<ReservationDTO> reservationList = Session.getLoggedRestaurant().getReservations();

        for (ReservationDTO reservation : reservationList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/ReservationWidget.fxml"));
                ReservationWidgetController widgetController = new ReservationWidgetController();
                fxmlLoader.setController(widgetController);
                VBox reservationWidget = fxmlLoader.load();

                // Imposta le informazioni della prenotazione nel widget
                widgetController.setReservation(reservation);

                pageContainer.getChildren().add(reservationWidget);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ScrollPane scrollPane = new ScrollPane(pageContainer);
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
