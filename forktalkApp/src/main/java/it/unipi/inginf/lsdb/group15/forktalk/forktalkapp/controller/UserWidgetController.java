package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
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
    public Button suspendedButton;
    Document user;

    FXMLLoader loader;
    Parent root;
    UserPageController userPageController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showButton.setOnAction(this::openRestaurantPage);
        adminButton.setOnAction(this::makeAdmin);
        if(Session.loggedUser.getRole() != 2) {
            adminButton.setVisible(false);
            suspendedButton.setVisible(false);
        }
    }

    private void suspendUser(ActionEvent event) {
        if(UserDAO.suspendUser(usernameField.getText())){
            suspendedButton.setText("Unsuspend Account");
            suspendedButton.setOnAction(this::unsuspendUser);
        }else{
            Utils.showAlert("Something went wrong! Please try again.");
        }
    }

    private void unsuspendUser(ActionEvent event) {
        if(UserDAO.unsuspendUser(usernameField.getText())){
            suspendedButton.setText("Suspend Account");
            suspendedButton.setOnAction(this::suspendUser);
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
            if(user.getInteger("suspended") == 0) {
                suspendedButton.setText("Suspend Account");
                suspendedButton.setOnAction(this::suspendUser);
            }else{
                suspendedButton.setText("Unsuspend Account");
                suspendedButton.setOnAction(this::unsuspendUser);
            }

            loader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/UserPage.fxml"));
            root = loader.load();
            userPageController = loader.getController();

            String sizeList;

            if (user.containsKey("restaurantList")) {
                sizeList = String.valueOf(user.getList("restaurantList", Document.class).size());

            }else if (user.containsKey("restaurantsList")) {
                sizeList = String.valueOf(user.getList("restaurantsList", Document.class).size());
            }else{
                sizeList = "0";
            }

            userPageController.setUserInfo(user.getString("username"), user.getString("name").trim() + " " + user.getString("surname"), sizeList);
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
