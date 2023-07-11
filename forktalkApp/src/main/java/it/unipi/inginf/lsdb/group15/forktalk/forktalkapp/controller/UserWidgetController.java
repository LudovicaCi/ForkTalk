package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
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
    public Button adminButton;
    public Button deleteButton;
    Document user;

    FXMLLoader loader;
    Parent root;
    UserPageController userPageController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showButton.setOnAction(this::openRestaurantPage);
        adminButton.setOnAction(this::makeAdmin);
        deleteButton.setOnAction(this::deleteUser);
        if(Session.loggedUser.getRole() != 2) {
            adminButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }

    private void deleteUser(ActionEvent event) {
        if(UserDAO.deleteUser(usernameField.getText())){
            Utils.showAlert("OK!");
            Session.getFindUserBarController().allUsers.remove(user);
            Session.getFindUserBarController().resetView();
            Session.getFindUserBarController().loadNextBatch();
        }else{
            Utils.showAlert("Something went wrong! Please try again.");
        }
    }

    private void makeAdmin(ActionEvent event) {
        if(UserDAO.makeAdmin(usernameField.getText()))
            Utils.showAlert("User upgraded!");
        else
            Utils.showAlert("Something went wrong! Please try again.");
    }

    public void setUser(Document user) throws IOException {
        try {
            this.user = user;
            usernameField.setText(user.getString("username"));
            userName.setText(user.getString("name").trim() + " " + user.getString("surname"));
            emailField.setText(user.getString("email"));
            originField.setText(user.getString("origin"));

            loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/UserPage.fxml"));
            root = loader.load();
            userPageController = loader.getController();

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
