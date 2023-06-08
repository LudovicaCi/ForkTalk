package it.unipi.inginf.lsdb.group15.forktalk.connection;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import it.unipi.inginf.lsdb.group15.forktalk.model.GeneralUser;
import it.unipi.inginf.lsdb.group15.forktalk.model.User;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import java.lang.*;
import java.util.regex.*;
    import it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBDriver;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import javax.swing.text.Document;

import static it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBDriver.mongoClient;
import static it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBDriver.userCollection;
import static javax.management.Query.eq;

public class MongoDBUser {
    MongoCursor<Document> cursor;
    //check if the username is already present

    public boolean userAlreadyPresent(String username) {
        try {
            // Assuming you have a valid MongoDB client instance initialized
            MongoCollection<Document> userCollection = MongoDBDriver.mongoClient.getDatabase("Forktalk").getCollection("Users");

            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Use the filter to find documents in the collection
            FindIterable<Document> documents = userCollection.find(filter);

            // Check if any documents were found
            return documents.iterator().hasNext();
        }
        catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
            // or throw a custom exception if desired
        }
    }

    public boolean addUser(User user)
    {
        if (userCollection.countDocuments(new Document("username", user.getUsername())) == 0) {
            Document doc = new Document()
                    .append("_id", user.getId())
                    .append("nome", user.getNome())
                    .append("cognome", user.getCognome())
                    .append("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("email", user.getEmail())
                    .append("role", user.getRole());                    ;
            userCollection.insertOne(doc);
            return true;
        }
        else return false;
    }


    public boolean loginUser(String username, String password) {
        try {
            // Assuming you have a valid MongoDB client instance initialized
            MongoCollection<Document> userCollection = mongoClient.getDatabase("ForkTalk").getCollection("Users");

            // Create a filter to match the username and password
            Bson filter = Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("password", password));

            // Use the filter to find a matching user document in the collection
            Document userDocument = userCollection.find(filter).first();

            // Check if a matching user document was found
            return userDocument != null;
        }
        catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }

    }

        //find user by username
    public Document findUserByUsername (String username){
            cursor = userCollection.find(Filters.eq("username", username)).iterator();

            return cursor.next();
    }

        //check if the credentials are correct
    public boolean checkCredentials(String username, String encrypted) {
        try {
            // Assuming you have a valid MongoDB client instance initialized
            MongoCollection<Document> userCollection = mongoClient.getDatabase("ForkTalk").getCollection("Users");

            // Create a filter to match the username and encrypted password
            Bson filter = Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("password", encrypted)
            );

            // Use the filter to find a matching user document in the collection
            FindIterable<Document> documents = userCollection.find(filter);

            // Check if any matching user documents were found
            return documents.iterator().hasNext();
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false; // or throw a custom exception if desired
        }
    }

}

