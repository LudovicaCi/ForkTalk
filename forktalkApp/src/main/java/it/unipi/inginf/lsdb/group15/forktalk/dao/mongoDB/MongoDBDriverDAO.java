package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoDBDriverDAO {
    //    -------------------------------------
    static MongoClient mongoClient;
    private static MongoDatabase db;
    static MongoCollection<Document> userCollection;
    static MongoCollection<Document> restaurantCollection;
    //    -------------------------------------

    // Opens a connection to the MongoDB cluster
    public void connectToCluster() {
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
        } catch (MongoException e) {
            System.err.println("An error occurred while connecting to the database:");
            e.printStackTrace();
        }
    }

    // Opens a connection to the local MongoDB database
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

            System.out.println("Successfully connected to the local database.");
        } catch (MongoException e) {
            System.err.println("An error occurred while connecting to the local database:");
            e.printStackTrace();
        }
    }

    // Opens a connection to the database (local or cluster)
    public static void openConnection() {
        try {
            connectToLocal();

        /*for (String name : db.listCollectionNames()) {
            System.out.println(name);
        }

        System.out.println("**************** USERS ******************");
        System.out.println(userCollection.countDocuments());

        System.out.println("**************** RESTAURANTS ******************");
        System.out.println(restaurantCollection.countDocuments()); */
        } catch (Exception e) {
            System.err.println("ERROR: Failed to open the database connection.");
            e.printStackTrace();
        }
    }

    // Closes the database connection
    public static void closeConnection() {
        try {
            mongoClient.close();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to close the database connection.");
            e.printStackTrace();
        }
    }

}

