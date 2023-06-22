module forktalkApp {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens it.unipi.inginf.lsdb.group15.forktalk.forktalkapp;
}