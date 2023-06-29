package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProfilePageController implements Initializable {
    public TextField name;
    public TextField surname;
    public TextField email;
    public TextField username;
    public PasswordField password;
    public TextField origin;
    public Button updateButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.setOnAction(this::updateProfile);
    }

    private void updateProfile(ActionEvent event) {
        UserDTO updatedUser = UserDAO.getUserByUsername(Session.loggedUser.getUsername());

        if(!name.getText().isEmpty()) {
            assert updatedUser != null;
            updatedUser.setName(name.getText());
        }

        if(!surname.getText().isEmpty()) {
            assert updatedUser != null;
            updatedUser.setUsername(surname.getText());
        }

        if(!email.getText().isEmpty()){
            if(!UserDAO.isEmailTaken(email.getText())) {
                assert updatedUser != null;
                updatedUser.setEmail(email.getText());
            }
            else{
                Utils.showAlert("Email still exists!");
            }
        }

        if(!username.getText().isEmpty()){
            if(!UserDAO.isUsernameTaken(username.getText())) {
                assert updatedUser != null;
                updatedUser.setUsername(username.getText());
            }
            else{
                Utils.showAlert("Username not available!");
            }
        }

        if(!password.getText().isEmpty()) {
            assert updatedUser != null;
            updatedUser.setPassword(password.getText());
        }

        if(!origin.getText().isEmpty()) {
            assert updatedUser != null;
            updatedUser.setOrigin(origin.getText());
        }

        if(UserDAO.updateUser(Session.loggedUser.getUsername(), updatedUser)) {
            Session.setLoggedUser(updatedUser);
            Utils.changeScene("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/PersonalPage.fxml", event);
        }else
            Utils.showAlert("Update unsuccessful");
    }
}
