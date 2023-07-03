package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.bson.Document;

public class ReviewWidgetController {
    public Label rateField;
    public Text dateField;
    public Text contentField;
    public Label usernameField;
    public Button deleteButton;

    public void setReview(Document review){
        rateField.setText(String.valueOf(review.get("review_rating")));
        dateField.setText(review.getString("review_date"));
        contentField.setText(review.getString("review_content"));
        usernameField.setText(review.getString("reviewer_pseudo"));
        if(!review.getString("reviewer_pseudo").equals(Session.getLoggedUser().getUsername()))
            deleteButton.setVisible(false);
    }
}
