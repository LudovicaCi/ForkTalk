package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public VBox reviewContainer;
    public Button showReview;
    private List<Document> reviewsDocuments;
    private int currentIndex = 0;
    private double rate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(this::openPreviousPage);
        browseButton.setOnAction(this::openBrowserPage);
        logoutButton.setOnAction(this::logout);
        profileButton.setOnAction(this::openProfilePage);
        loadMoreButton.setOnAction(this::loadMoreReviews);
        reviewContainer = new VBox();
        loadMoreButton.setVisible(false);
        showReview.setOnAction(this::showReviews);
        updateStarImages();
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
        Utils.changeScene(" it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
    }

    private void openPreviousPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/BrowserPage.fxml", event);
    }

    private void updateStarImages() {
        if (rate != 0.0) {
            int fullStars = (int) rate;
            int halfStars = (rate - fullStars) >= 0.5 ? 1 : 0;

            star1.setImage(getStarImage(fullStars, halfStars, 1));
            star2.setImage(getStarImage(fullStars, halfStars, 2));
            star3.setImage(getStarImage(fullStars, halfStars, 3));
            star4.setImage(getStarImage(fullStars, halfStars, 4));
            star5.setImage(getStarImage(fullStars, halfStars, 5));
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


    public void setRestaurantInfo(String name, String rate, String total, String address, String tag, List<Document> reviews, String price) {
        restName.setText(name);
        totalReviews.setText(total);
        addressField.setText(address);
        tagField.setText(tag);
        priceField.setText(price);
        this.rate = (rate.equals("null")) ? 0.0 : Double.parseDouble(rate);

        // Creazione del Comparator
        Comparator<Document> comparator = Comparator.comparing((Document review) -> !review.getString("reviewer_pseudo").equals(Session.getLoggedUser().getUsername()))
                .thenComparing((Document review) -> review.getString("review_date"), Comparator.reverseOrder());

        // Ordinamento della lista
        reviews.sort(comparator);

        reviewsDocuments = reviews;

        showReviews();
    }


    public void showReviews() {
        currentIndex = 0; // Reimposta l'indice corrente a 0
        reviewContainer.getChildren().clear(); // Rimuovi i ristoranti precedenti dalla vista

        loadNextBatch(); // Carica il primo batch di ristoranti
    }

    public void showReviews(ActionEvent event) {
        currentIndex = 0; // Reimposta l'indice corrente a 0
        reviewContainer.getChildren().clear(); // Rimuovi i ristoranti precedenti dalla vista

        loadNextBatch(); // Carica il primo batch di ristoranti
    }

    public void loadMoreReviews(ActionEvent event) {
        loadNextBatch(); // Carica il prossimo batch di ristoranti
    }

    private void loadNextBatch() {
        // Numero di ristoranti da caricare in ogni batch
        int batchSize = 5;
        int endIndex = Math.min(currentIndex + batchSize, reviewsDocuments.size());
        List<Document> nextBatch = reviewsDocuments.subList(currentIndex, endIndex);

        for (Document review : nextBatch) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/ReviewWidget.fxml"));
                ReviewWidgetController widgetController = new ReviewWidgetController();
                fxmlLoader.setController(widgetController);
                VBox reviewWidget = fxmlLoader.load();

                // Imposta le informazioni del ristorante nel widget
                widgetController.setReview(review);

                reviewContainer.getChildren().add(reviewWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        // Controlla se ci sono ulteriori ristoranti da caricare
        // Mostra il pulsante "Carica altro" se ci sono ancora ristoranti da caricare
        loadMoreButton.setVisible(currentIndex < reviewsDocuments.size()); // Nascondi il pulsante "Carica altro" se non ci sono più ristoranti da caricare

        setupReviewView();
    }

    private void resetView() {
        reviewContainer.getChildren().clear();
        currentIndex = 0;
        reviewsDocuments = null;
        loadMoreButton.setVisible(false);
    }

    private void setupReviewView() {
        ScrollPane scrollPane = new ScrollPane(reviewContainer);
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
