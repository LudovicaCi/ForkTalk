package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {
    /**
     * Changes the scene of the application to the specified file.
     *
     * @param fileName the name of the FXML file representing the new scene
     * @param event    the event triggering the scene change
     */
    public static void changeScene (String fileName, Event event) {
        Scene scene;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(Utils.class.getResource(fileName));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
            loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an error alert with the specified message.
     *
     * @param message the error message to display in the alert
     */
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation with the specified message.
     *
     * @param message the error message to display in the alert
     */
    public static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
