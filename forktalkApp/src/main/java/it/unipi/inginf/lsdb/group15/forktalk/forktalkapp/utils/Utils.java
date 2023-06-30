package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {
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

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
