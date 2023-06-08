package it.unipi.inginf.lsdb.group15.forktalk.connection;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
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

    public boolean registrationUser(User u) {
        if (userAlreadyPresent(u.getUsername())) {
            Utility.infoBox("Please, choose another username and try again.",
                    "Error", "Username already used!");
            return false;
        }


        Document User = new Document("username", u.getUsername())
                .append("password", u.getPassword())
                .append("origin", u.getOrigin());
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
    }
}
