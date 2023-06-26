module forktalkApp {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires com.google.gson;
    requires org.neo4j.driver;
    requires java.xml;

    opens it.unipi.inginf.lsdb.group15.forktalk.forktalkapp to javafx.fxml;

    exports it.unipi.inginf.lsdb.group15.forktalk.forktalkapp;
    exports it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller;
    opens it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.controller to javafx.fxml;
}