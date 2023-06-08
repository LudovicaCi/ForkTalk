package it.unipi.inginf.lsdb.group15.forktalk.connection;

import it.unipi.inginf.lsdb.group15.forktalk.model.User;
import org.bson.Document;

import static it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBDriver.userCollection;

public class MongoDBAdministrator {
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


}
