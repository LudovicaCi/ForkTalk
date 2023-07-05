package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WriteReviewController implements Initializable {
    @FXML
    public Button star1;

    @FXML
    public Button star2;

    @FXML
    public Button star3;

    @FXML
    public Button star4;

    @FXML
    public Button star5;

    public int rating = 0;
    public TextArea contentField;
    public Button writeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        star1.setOnAction(this::handleStarButtonEnter);
        star2.setOnAction(this::handleStarButtonEnter);
        star3.setOnAction(this::handleStarButtonEnter);
        star4.setOnAction(this::handleStarButtonEnter);
        star5.setOnAction(this::handleStarButtonEnter); 
        writeButton.setOnAction(this::writeReview);
    }

    private void writeReview(ActionEvent event) {
        ReviewDTO newReview = new ReviewDTO();
        if(rating == 0){
            Utils.showAlert("click a rate!");
        }else if(contentField.getText().isEmpty()){
            Utils.showAlert("The review is empty!");
        }else{
            newReview.setRating(rating);
            newReview.setContent(contentField.getText());
            newReview.setReviewer(Session.getLoggedUser().getUsername());
            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
            newReview.setTimestamp(formattedDate);

            //write review
            if(RestaurantDAO.writeReview(newReview, Session.getRestaurantPageController().rest_id)){
                Session.getRestaurantPageController().refreshReviews();
            }else{
                Utils.showAlert("Error writing the review.");
            }
        }
    }

    @FXML
    private void handleStarButtonEnter(ActionEvent event) {
        Button currentButton = (Button) event.getSource();
        
        resetStars();
        
        if (currentButton == star1) {
            star1.setGraphic(getFullStarImage());
            rating = 1;
        } else if (currentButton == star2) {
            star1.setGraphic(getFullStarImage());
            star2.setGraphic(getFullStarImage());
            rating = 2;
        } else if (currentButton == star3) {
            star1.setGraphic(getFullStarImage());
            star2.setGraphic(getFullStarImage());
            star3.setGraphic(getFullStarImage());
            rating = 3;
        } else if (currentButton == star4) {
            star1.setGraphic(getFullStarImage());
            star2.setGraphic(getFullStarImage());
            star3.setGraphic(getFullStarImage());
            star4.setGraphic(getFullStarImage());
            rating = 4;
        } else if (currentButton == star5) {
            star1.setGraphic(getFullStarImage());
            star2.setGraphic(getFullStarImage());
            star3.setGraphic(getFullStarImage());
            star4.setGraphic(getFullStarImage());
            star5.setGraphic(getFullStarImage());
            rating = 5;
        }
    }

    @FXML
    private void resetStars() {
        star1.setGraphic(getEmptyStarImage());
        star2.setGraphic(getEmptyStarImage());
        star3.setGraphic(getEmptyStarImage());
        star4.setGraphic(getEmptyStarImage());
        star5.setGraphic(getEmptyStarImage());
    }

    private ImageView getEmptyStarImage() {
        return new ImageView("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/empty_star.png");
    }

    private ImageView getFullStarImage() {
        return new ImageView("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/full_star.png");
    }
}
