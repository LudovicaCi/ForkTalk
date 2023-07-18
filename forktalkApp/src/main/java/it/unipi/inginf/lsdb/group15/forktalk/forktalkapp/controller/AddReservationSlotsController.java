package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddReservationSlotsController implements Initializable {
    public TextField numberSlots;
    public TextField timeSlot;
    public DatePicker datePicker;
    public Button addSlotsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addSlotsButton.setOnAction(this::addSlots);
    }

    private void addSlots(ActionEvent event) {
        try {
            if (numberSlots.getText().isEmpty() || timeSlot.getText().isEmpty() || datePicker.getValue() == null) {
                Utils.showAlert("Please all fields are required.");
                return;
            }

            LocalDate date = datePicker.getValue();
            LocalDate currentDate = LocalDate.now();

            if (date != null && date.isBefore(currentDate)) {
                Utils.showAlert("Please select a valid date that is not before the current date.");
                return;
            }

            assert date != null;
            String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String slot = timeSlot.getText();
            if (!slot.matches("\\d{2}:\\d{2}")) {
                Utils.showAlert("Incorrect time slot format. Please enter in HH:mm format.");
            }

            LocalTime selectedTime = LocalTime.parse(slot);
            LocalTime currentTime = LocalTime.now();

            if (date.isEqual(currentDate) && !selectedTime.isAfter(currentTime)) {
                Utils.showAlert("Please select a valid time that is after the current time when the date is the current date.");
                return;
            }

            int numberOfSlots = Integer.parseInt(numberSlots.getText());

            if (RestaurantDAO.addFreeSlot(Session.loggedRestaurant, numberOfSlots, slot, dateString)){
                Session.loggedRestaurant.setReservations(RestaurantDAO.getReservations(Session.loggedRestaurant));
                showConfirmationDialog();
                Session.getRestaurantLoggedPageController().openAddSlotsPage();
            }else{
                Utils.showAlert("Reservation Failed! Unfortunately, we couldn't process your reservation.");
            }
        }catch(Exception e) {
        System.out.println("ERROR: An error occurred while making a reservation.");
        e.printStackTrace();
    }
    }

    private void showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Successful");
        alert.setHeaderText(null);
        alert.setContentText("Your slots have been added!");
        alert.showAndWait();
    }
}
