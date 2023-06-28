package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;
import com.mongodb.internal.client.model.Util;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstPageController implements Initializable {

    @FXML
    private Button restButton;

    @FXML
    private Button userButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restButton.setOnAction(this::openRestLoginPage);
        userButton.setOnAction(this::openUserLoginPage);
    }

    private void openRestLoginPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/LoginRestaurantPage.fxml", event);
    }

    private void openUserLoginPage(ActionEvent event) {
        Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/LoginUserPage.fxml", event);
    }
}



