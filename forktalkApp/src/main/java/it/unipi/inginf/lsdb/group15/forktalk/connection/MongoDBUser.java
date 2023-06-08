package it.unipi.inginf.lsdb.group15.forktalk.connection;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import it.unipi.inginf.lsdb.group15.forktalk.model.GeneralUser;
import it.unipi.inginf.lsdb.group15.forktalk.model.User;

import static com.mongodb.client.model.Filters.*;


import javax.swing.text.Document;

import static it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBDriver.userCollection;
import static javax.management.Query.eq;

public class MongoDBUser {
    MongoCursor<Document> cursor;
    //check if the username is already present
    public boolean userAlreadyPresent(String username) {

        cursor = userCollection.find(eq("username", username)).iterator();
        return cursor.hasNext();

    }
    /*public Boolean userExists(String usr, String eml)
    {
        MongoCollection<Document> collection = getDatabase().getCollection("Users");
        try(MongoCursor cursor = collection.find(or(eq("email", eml),eq("username",usr))).limit(1).iterator())
        {
            if (cursor.hasNext()) {
                return true;
            }
        }

        return false;
    }
    */
    public boolean registrationUser(GeneralUser u) {
        if (userAlreadyPresent(u.getUsername())) {
            System.out.println("This Username is already taken, choose a different one");
            return false;
        }


        Document User = new Document("username", GeneralUser.getUsername())
                .append("password", u.getPassword())
                .append("origin", User.getOrigin());
        try {
            userCollection.insertOne(User);
            return true;
        } catch (MongoException me) {
            Utility.infoBox("Registration not done", "Error", "MongoDB error");
            return false;
        }
    }


        //find user by username
        public Document findUserByUsername (String username){
            cursor = userCollection.find(Filters.eq("username", username)).iterator();

            return cursor.next();
        }
        //check if the username is already present
        public boolean userAlreadyPresent (String username){

            cursor = userCollection.find(eq("username", username)).iterator();
            return cursor.hasNext();

        }
        //check if the credentials are correct
        public boolean checkCredentials (String username, String encrypted){

            cursor = userCollection.find(and(eq("username", username), eq("password", encrypted))).iterator();

            return cursor.hasNext();
        }
    }

    private static class Utility {
        public static void infoBox(String s, String error, String s1) {
        }
    }
}
