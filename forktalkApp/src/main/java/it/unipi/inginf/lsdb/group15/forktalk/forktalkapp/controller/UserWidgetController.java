package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserWidgetController implements Initializable {
    public Text userName;
    public Text usernameField;
    public Text emailField;
    public Text originField;
    public Button showButton;

    FXMLLoader loader;
    Parent root;
    UserPageController userPageController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showButton.setOnAction(this::openRestaurantPage);
    }

    public void setUser(Document user) throws IOException {
        try {
            usernameField.setText(user.getString("username"));
            userName.setText(user.getString("name").trim() + " " + user.getString("surname"));
            emailField.setText(user.getString("email"));
            originField.setText(user.getString("origin"));

            loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/UserPage.fxml"));
            root = loader.load();
            userPageController = loader.getController();

            // Passa l'ID dell'utente al controller della nuova pagina
            userPageController.setUserInfo(user.getString("username"), user.getString("name").trim() + " " + user.getString("surname"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void openRestaurantPage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
