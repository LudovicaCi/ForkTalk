package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RestaurantStatisticsController implements Initializable {
    public DatePicker startDate;
    public DatePicker endDate;
    public Button showResultsButton;
    public Text totReview;
    public Text avgRate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showResultsButton.setOnAction(this::showStatistics);
    }

    private void showStatistics(ActionEvent event) {
        if(startDate.getValue() == null || endDate.getValue() == null){
            Utils.showAlert("All fields are required.");
            return;
        }

        LocalDate startDateInsert = startDate.getValue();
        LocalDate endDateInsert = endDate.getValue();

        assert startDateInsert != null;
        assert endDateInsert != null;
        String startDateString = startDateInsert.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDateString = endDateInsert.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Document result = RestaurantDAO.getReviewsStatsByDateRange(Session.loggedRestaurant.getId(), startDateString, endDateString);
        if(result == null){
            totReview.setText("0");
            avgRate.setText("0.0");
        }else {
            totReview.setText(String.valueOf(result.getLong("total_reviews")));
            avgRate.setText(String.valueOf(result.getDouble("average_rating")));
        }
    }
}
