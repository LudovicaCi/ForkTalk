package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import org.bson.Document;

public class DriverDAO {
    //    -------------------------------------
    static MongoClient mongoClient;
    private static MongoDatabase db;
    public static MongoCollection<Document> userCollection;
    public static MongoCollection<Document> restaurantCollection;
    //    -------------------------------------

    /**
     * Connects to the MongoDB cluster and initializes the necessary collections.
     */
    public static boolean connectToCluster() {
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

            System.out.println("Successfully connected to the database.");
            return true;
        } catch (MongoException e) {
            System.err.println("An error occurred while connecting to the database:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Connects to the local MongoDB instance and initializes the necessary collections.
     */
    public static boolean connectToLocal() {
        try {
            // Create connection string
            ConnectionString uri = new ConnectionString("mongodb://localhost:27017/?retryWrites=false");

            // Create a MongoDB client
            mongoClient = MongoClients.create(uri);

            // Connect to the "ForkTalk" database
            db = mongoClient.getDatabase("ForkTalk");

            // Select the "Users" collection
            userCollection = db.getCollection("Users");
            restaurantCollection = db.getCollection("Restaurants");

            System.out.println("Successfully connected to the local database.");
            return true;
        } catch (MongoException e) {
            System.err.println("An error occurred while connecting to the local database:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens the database connection by connecting to the MongoDB instance.
     */
    public static boolean openConnection() {
        try {
            return connectToCluster();

        } catch (MongoException e) {
            System.err.println("ERROR: Failed to open the database connection.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        try {
            mongoClient.close();
        } catch (MongoException e) {
            System.err.println("ERROR: Failed to close the database connection.");
            e.printStackTrace();
        }
    }

}

