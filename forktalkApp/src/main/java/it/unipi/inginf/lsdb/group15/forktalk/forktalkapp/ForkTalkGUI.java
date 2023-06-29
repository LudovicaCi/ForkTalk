package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.DriverDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ForkTalkGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            DriverDAO.openConnection();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/FirstPage.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("ForkTalk");
            Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/img/FORKtALK.png")));
            stage.getIcons().add(logo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DriverDAO.closeConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}
