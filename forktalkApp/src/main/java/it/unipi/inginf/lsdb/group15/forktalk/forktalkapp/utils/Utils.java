package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {
    public static void changeScene (String fileName, Event event) {
        Scene scene = null;
        FXMLLoader loader = null;
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
}
