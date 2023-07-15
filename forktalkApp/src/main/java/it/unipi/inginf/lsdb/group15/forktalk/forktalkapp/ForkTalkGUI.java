package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.DriverDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jDriverDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class ForkTalkGUI extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Connect to MongoDB database
            boolean mongoDBConnected = DriverDAO.openConnection();
            if (!mongoDBConnected) {
                System.out.println("Failed to open connection to MongoDB");
                return;
            }

            // Connect to Neo4j database
            boolean neo4jConnected = Neo4jDriverDAO.openConnection();
            if (!neo4jConnected) {
                System.out.println("Failed to open connection to Neo4j");
                DriverDAO.closeConnection();
                return;
            }

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
        Neo4jDriverDAO.openConnection();
    }

    public static void main(String[] args) {
        launch();
    }
}
