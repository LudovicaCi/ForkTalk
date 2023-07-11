package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.ReservationDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO.getEmptySeatsByDate;

public class BookARestaurantController implements Initializable{

    @FXML
    private TextField timeSlotTextField;

    @FXML
    private TextField numberOfPersonsTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button makeReservationButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeReservationButton.setOnAction(this::makeReservation);
    }

    public void makeReservation(ActionEvent event) {
        try {
            if(datePicker.getValue() == null || timeSlotTextField.getText().isEmpty() || numberOfPersonsTextField.getText().isEmpty()) {
                Utils.showAlert("All fields are required.");
                return;
            }

            // Get the selected restaurant from your application's logic
            RestaurantDTO rest = RestaurantDAO.getRestaurantById(Session.getRestaurantPageController().restId);

            // Get the selected date from the date picker
            LocalDate date = datePicker.getValue();
            LocalDate currentDate = LocalDate.now();

            if (date != null && date.isBefore(currentDate)) {
                Utils.showAlert("Please select a valid date that is not before the current date.");
                return;
            }

            assert date != null;
            String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Get the entered time slot from the text field
            String slot = timeSlotTextField.getText();
            if (!slot.matches("\\d{2}:\\d{2}")) {
                Utils.showAlert("Incorrect time slot format. Please enter in HH:mm format.");
            }

            LocalTime selectedTime = LocalTime.parse(slot);
            LocalTime currentTime = LocalTime.now();

            if (date.isEqual(currentDate) && !selectedTime.isAfter(currentTime)) {
                Utils.showAlert("Please select a valid time that is after the current time when the date is the current date.");
                return;
            }

            // Get the entered number of persons from the text field
            int numberOfPersons = Integer.parseInt(numberOfPersonsTextField.getText());

            if (numberOfPersons > getEmptySeatsByDate(rest, dateString)) {
                Utils.showAlert("There is no space available for the people selected");
                return;
            }

            if(ReservationDAO.makeLocalReservation(Session.getLoggedUser(), rest, dateString,slot, numberOfPersons)) {
                Session.loggedUser.setReservations(UserDAO.getReservations(Session.getLoggedUser()));
                showConfirmationDialog();
                Session.getRestaurantPageController().showBookingPane();
            }else {
                Utils.showAlert("Reservation Failed! Unfortunately, we couldn't process your reservation.");
            }

        } catch (Exception e) {
            System.out.println("ERROR: An error occurred while making a reservation.");
            e.printStackTrace();
        }
    }

    private void showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Successful");
        alert.setHeaderText(null);
        alert.setContentText("Your reservation has been confirmed!");
        alert.showAndWait();
    }
}