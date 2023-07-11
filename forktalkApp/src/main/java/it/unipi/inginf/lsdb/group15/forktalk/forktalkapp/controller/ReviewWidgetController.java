package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviewWidgetController implements Initializable {
    public Text dateField;
    public Text contentField;
    public Label usernameField;
    public Button deleteButton;
    public ImageView star1;
    public ImageView star2;
    public ImageView star3;
    public ImageView star4;
    public ImageView star5;

    private String reviewID;

    private double rate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteButton.setOnAction(this::deleteReview);
    }

    private void deleteReview(ActionEvent event) {
        if(RestaurantDAO.deleteReviewById(reviewID)){
            Session.getRestaurantPageController().refreshReviews();
        }else{
            Utils.showAlert("Error deleting the review.");
            Session.getRestaurantPageController().refreshReviews();
        }
    }

    public void setReview(Document review){
        dateField.setText(review.getString("review_date"));
        contentField.setText(review.getString("review_content"));
        usernameField.setText(review.getString("reviewer_pseudo"));
        if(!review.getString("reviewer_pseudo").equals(Session.getLoggedUser().getUsername()))
            deleteButton.setVisible(false);
        reviewID = review.getString("review_id");

        this.rate = (String.valueOf(review.get("review_rating")).equals("null")) ? 0.0 : Double.parseDouble(String.valueOf(review.get("review_rating")));
    }

    public void setReview(ReviewDTO review){
        dateField.setText(review.getTimestamp());
        contentField.setText(review.getContent());
        usernameField.setText(review.getReviewer());
        deleteButton.setVisible(false);
        reviewID = review.getId();

        this.rate = review.getRating();
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
}
