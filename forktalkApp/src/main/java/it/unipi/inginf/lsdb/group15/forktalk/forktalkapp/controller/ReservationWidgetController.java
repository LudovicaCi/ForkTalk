package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.ReservationDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReservationWidgetController implements Initializable {
    public Text restName;
    public Text locationRest;
    public Text dateReservation;
    public Text nPerson;
    public Button deleteButton;

    private ReservationDTO reservation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteButton.setOnAction(this::deleteReservation);
    }

    public void setReservation(ReservationDTO reservation) {
        this.reservation = reservation;
        restName.setText(reservation.getRestaurantName());
        locationRest.setText(reservation.getRestaurantAddress().trim() + ", " + reservation.getRestaurantCity());
        dateReservation.setText(LocalDateTime.parse(reservation.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm")));
        nPerson.setText(String.valueOf(reservation.getPeople()));
    }

    public void deleteReservation(ActionEvent event){
        RestaurantDTO restaurant = RestaurantDAO.getRestaurantById(reservation.getRestaurantID());
        assert restaurant != null;
        if(ReservationDAO.deleteReservation(Session.getLoggedUser(),restaurant, reservation)){
            Session.getLoggedUser().setReservations(UserDAO.getReservations(Session.getLoggedUser()));
            Session.getPersonalPageController().createReservationGridPane(event);
        }else
            Utils.showAlert("Failed to delete the reservation. Please try again.");
    }
}
