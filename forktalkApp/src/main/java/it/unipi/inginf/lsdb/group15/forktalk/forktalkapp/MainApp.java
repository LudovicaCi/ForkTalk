package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("ForkTalk");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch();}
}
