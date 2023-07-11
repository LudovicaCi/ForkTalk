package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FindUserBarController implements Initializable {

    public TextField usernameField;
    public TextField nameField;
    public TextField surnameField;
    public TextField emailField;
    public Button searchButton;
    public AnchorPane dynamicPane;
    public Button loadMoreButton;

    private VBox userContainer;
    private List<Document> allUsers;
    private int currentIndex = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchButton.setOnAction(this::searchUsers);
        loadMoreButton.setOnAction(this::loadMoreRestaurants);
        userContainer = new VBox();
        loadMoreButton.setVisible(false); // Nascondi il pulsante "Carica altro" inizialmente
    }

    public void searchUsers(ActionEvent event) {
        currentIndex = 0; // Reimposta l'indice corrente a 0
        userContainer.getChildren().clear(); // Rimuovi i ristoranti precedenti dalla vista
        String username = usernameField.getText().isEmpty() ? null : usernameField.getText();
        String name = nameField.getText().isEmpty() ? null : nameField.getText();
        String surname = surnameField.getText().isEmpty() ? null : surnameField.getText();
        String email = emailField.getText().isEmpty() ? null : emailField.getText();

        allUsers = UserDAO.searchUsers(username, name, surname, email);

        loadNextBatch(); // Carica il primo batch di ristoranti
    }

    public void loadMoreRestaurants(ActionEvent event) {
        loadNextBatch(); // Carica il prossimo batch di ristoranti
    }

    private void loadNextBatch() {
        // Numero di ristoranti da caricare in ogni batch
        int batchSize = 5;
        if(allUsers.size() == 0) {
            Text noListText = new Text("No User Found");
            noListText.setFill(Paint.valueOf("#00000080"));
            noListText.setStrokeType(StrokeType.OUTSIDE);
            noListText.setStrokeWidth(0.0);
            noListText.setTextAlignment(TextAlignment.CENTER);
            noListText.setWrappingWidth(300);
            noListText.setFont(new Font(24.0));
            VBox newBox = new VBox();
            newBox.getChildren().setAll(noListText);
            newBox.setAlignment(Pos.CENTER);

            AnchorPane.setTopAnchor(newBox, 0.0);
            AnchorPane.setBottomAnchor(newBox, 0.0);
            AnchorPane.setLeftAnchor(newBox, 0.0);
            AnchorPane.setRightAnchor(newBox, 0.0);

            dynamicPane.getChildren().add(newBox);
            return;
        }
        int endIndex = Math.min(currentIndex + batchSize, allUsers.size());
        List<Document> nextBatch = allUsers.subList(currentIndex, endIndex);

        for (Document user : nextBatch) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ it.unipi.inginf.lsdb.group15.forktalk.forktalkapp/layout/UserWidget.fxml"));
                UserWidgetController widgetController = new UserWidgetController();
                fxmlLoader.setController(widgetController);
                HBox userWidget = fxmlLoader.load();

                // Imposta le informazioni del ristorante nel widget
                widgetController.setUser(user);

                userContainer.getChildren().add(userWidget);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currentIndex += batchSize;

        // Controlla se ci sono ulteriori ristoranti da caricare
        if (currentIndex >= allUsers.size()) {
            loadMoreButton.setVisible(false); // Nascondi il pulsante "Carica altro" se non ci sono più ristoranti da caricare
        } else {
            loadMoreButton.setVisible(true); // Mostra il pulsante "Carica altro" se ci sono ancora ristoranti da caricare
        }

        setupRestaurantView();
    }

    private void resetView() {
        userContainer.getChildren().clear();
        currentIndex = 0;
        allUsers = null;
        loadMoreButton.setVisible(false);
    }

    private void setupRestaurantView() {
        ScrollPane scrollPane = new ScrollPane(userContainer);
        scrollPane.setFitToWidth(true); // Abilita la ridimensione automatica in larghezza
        scrollPane.setFitToHeight(true); // Abilita la ridimensione automatica in altezza
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Mostra sempre la barra di scorrimento verticale
        scrollPane.setStyle("-fx-background-color: transparent;"); // Imposta lo sfondo trasparente

        // Rimuovi eventuali elementi precedenti dal dynamicPane
        dynamicPane.getChildren().clear();

        dynamicPane.setStyle("-fx-background-color: #F0F0F0;");

        // Aggiungi lo ScrollPane contenente il GridPane all'AnchorPane e adatta alla grandezza dell'AnchorPane
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        dynamicPane.getChildren().add(scrollPane);
    }
}
