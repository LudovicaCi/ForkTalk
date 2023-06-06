package it.unipi.inginf.lsdb.group15.forktalk.connection;

import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import org.bson.Document;

public class MongoDBDriver {
    private final String clusterAddress = "mongodb://10.1.1.18:27017,10.1.1.19:27017,10.1.1.20:27017/" +
            "?retryWrites=true&w=1&readPreferences=nearest&wtimeout=10000";
    private static MongoClient mongoClient;
    private static MongoDatabase db;

    static MongoCollection<Document> userCollection;
    static MongoCollection<Document> restaurantCollection;

    public void connectToCluster(){
        try {
            //Create a mongoDB client
            mongoClient = MongoClients.create(clusterAddress);

            //Connect to db database
            db = mongoClient.getDatabase("ForkTalk");

            //Select the collection Users e Restaurants
            userCollection = db.getCollection("Users");
            //userCollection = db.getCollection("Restaurants");

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
            //userCollection = db.getCollection("Restaurants");

            System.out.println("Connessione al database locale avvenuta con successo.");
        } catch (Exception e) {
            System.err.println("Si è verificato un errore durante la connessione al database locale:");
            e.printStackTrace();
        }
    }


    public static void openConnection() {
        connectToLocal();

        System.out.println("**************** USER ******************");
        System.out.println(userCollection.countDocuments());

    }

    public static void closeConnection() {
        mongoClient.close();
    }

    //******************************************************************************************************************
    //                              CRUD OPERATIONS
    //******************************************************************************************************************

    //registrazione utente
    public void registrationUser(){

    }

    //trovare un utente tramite username
    public void findUserByUsername(String username){

    }

    //******************************************************************************************************************
    //                              ANALYTICS
    //******************************************************************************************************************
}
