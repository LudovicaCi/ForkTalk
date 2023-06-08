package it.unipi.inginf.lsdb.group15.forktalk.connection;

import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import it.unipi.inginf.lsdb.group15.forktalk.model.User;
import org.bson.Document;

public class MongoDBDriver {
    static MongoClient mongoClient;
    private static MongoDatabase db;


    static MongoCollection<Document> userCollection;
    static MongoCollection<Document> restaurantCollection;

    public void connectToCluster(){
        try {
            //Create a mongodbDB client
            String clusterAddress = "mongodb://10.1.1.18:27017,10.1.1.19:27017,10.1.1.20:27017/" +
                    "?retryWrites=true&w=1&readPreferences=nearest&wtimeout=10000";
            mongoClient = MongoClients.create(clusterAddress);

            //Connect to db database
            db = mongoClient.getDatabase("ForkTalk");

            //Select the collection Users e Restaurants
            userCollection = db.getCollection("Users");
            restaurantCollection = db.getCollection("Restaurants");
            // Create a cursor
            MongoCursor<Document> cursor;
            System.out.println("Connessione al database locale avvenuta con successo.");
        }catch (Exception e) {
            System.err.println("Si è verificato un errore durante la connessione al database locale:");
            e.printStackTrace();
        }
    }

    public static void connectToLocal() {
        try {
            // Create connection string
            ConnectionString uri = new ConnectionString("mongodb://localhost:27017");

            // Create a MongoDB client
            mongoClient = MongoClients.create(uri);

            // Connect to the "ForkTalk" database
            db = mongoClient.getDatabase("ForkTalk");

            // Select the "Users" collection
            userCollection = db.getCollection("Users");
            restaurantCollection = db.getCollection("Restaurants");

            System.out.println("Connessione al database locale avvenuta con successo.");
        } catch (Exception e) {
            System.err.println("Si è verificato un errore durante la connessione al database locale:");
            e.printStackTrace();
        }
    }


    public static void openConnection() {
        connectToLocal();

        for (String name : db.listCollectionNames()) {
            System.out.println(name);
        }

        System.out.println("**************** USERS ******************");
        System.out.println(userCollection.countDocuments());

        System.out.println("**************** RESTAURANTS ******************");
        System.out.println(restaurantCollection.countDocuments());

    }

    public static void closeConnection() {

        mongoClient.close();
    }
    private static User<Document> printDocuments() {
        return doc -> System.out.println(doc.toJson());
    }
    //******************************************************************************************************************
    //                              CRUD OPERATIONS
    //******************************************************************************************************************

    //registrazione utente
    /*public void registrationUser(){

    }
    public boolean registrationUser(User u) {

        if (userAlreadyPresent(u.getUsername())) {
            Utility.infoBox("Please, choose another username and try again.",
                    "Error", "Username already used!");
            return false;
        }

        //trovare un utente tramite username
    public void findUserByUsername(String username){

    }
    */
    //******************************************************************************************************************
    //                              ANALYTICS
    //******************************************************************************************************************
}
