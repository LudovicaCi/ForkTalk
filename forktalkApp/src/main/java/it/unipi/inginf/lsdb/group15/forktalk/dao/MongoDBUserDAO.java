package it.unipi.inginf.lsdb.group15.forktalk.dao;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.dto.UserDTO;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import java.lang.*;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static it.unipi.inginf.lsdb.group15.forktalk.dao.MongoDBDriverDAO.mongoClient;
import static it.unipi.inginf.lsdb.group15.forktalk.dao.MongoDBDriverDAO.userCollection;
import static javax.management.Query.*;

public class MongoDBUserDAO {
    MongoCursor<Document> cursor;
    //check if the username is already present

    public boolean userAlreadyPresent(String username) {
        try {
            // Assuming you have a valid MongoDB client instance initialized
            MongoCollection<Document> userCollection = MongoDBDriverDAO.mongoClient.getDatabase("Forktalk").getCollection("Users");

            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Use the filter to find documents in the collection
            FindIterable<Document> documents = userCollection.find(filter);

            // Check if any documents were found
            return documents.iterator().hasNext();
        }
        catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
            // or throw a custom exception if desired
        }
    }

    public boolean addUser(UserDTO user)
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
    //DA QUI IN POI ULTIMA MODIFICA
    //>>>>>>>>>>>>>OK<<<<<<<<<<<<<//>>>>>>>>>>>>>OK<<<<<<<<<<<<<//>>>>>>>>>>>>>OK<<<<<<<<<<<<<//>>>>>>>>>>>>>OK<<<<<<<<<<<<<
    //LOGIN - Email, Password
    //CREDENTIALS - Username, Email
    public Boolean userExists(String usr, String eml)
    {
        MongoCollection<Document> collection =  mongoClient.getDatabase("ForkTalk").getCollection("Users");
        try(MongoCursor cursor = collection.find(or(eq("email", eml),eq("username",usr))).limit(1).iterator())
        {
            if (cursor.hasNext()) {
                return true;
            }
        }

        return false;
    }
    //>>>>>>>>>>>>>OK<<<<<<<<<<<<<
    public UserDTO getUserLogin(String eml, String psw)
    {
        MongoCollection<Document> collection = mongoClient.getDatabase("ForkTalk").getCollection("Users");
        UserDTO userDTO = new UserDTO();

        try(MongoCursor cursor = collection.find(and(eq("email", eml),eq("password",psw))).limit(1).iterator())
        {
            if (cursor.hasNext())
            {
                Document res = (Document) cursor.next();
//              One user found

                userDTO.setId(Integer.parseInt(res.get("_id") != null ? res.get("_id").toString() : "ID not available"));
                userDTO.setUsername(res.get("username") != null ? res.get("username").toString() : "Username not available");
                userDTO.setNome(res.get("firstname") != null ? res.get("firstname").toString() : "Firstname not available");
                userDTO.setCognome(res.get("lastname") != null ? res.get("lastname").toString() : "Lastname not available");
                userDTO.setPassword(res.get("password") != null ? res.get("password").toString() : "Password not available");
                userDTO.setEmail(res.get("email") != null ? res.get("email").toString() : "EMail not available");
                userDTO.setOrigin(res.get("origin") != null ? res.get("origin").toString() : "Origin not available");

                return userDTO;
            }
//          No match
            userDTO.setId(0);
            return userDTO;
        }
        catch (MongoException mongoException)
        {
            System.err.println("Mongo Exception: " + mongoException.getMessage());
        }

//      No match
        userDTO.setId(0);
        return userDTO;
    }

    //>>>>>>>>>>>>>OK<<<<<<<<<<<<<
    public UserDTO registerUser(UserDTO user)
    {
        MongoCollection<Document> collection = mongoClient.getDatabase("ForkTalk").getCollection("Users");

        try
        {
            ObjectId id = new ObjectId();
            InsertOneResult result = collection.insertOne(
                    new Document()
                            .append("_id", id)
                            .append("username", user.getUsername())
                            .append("firstname", user.getNome())
                            .append("lastname", user.getCognome())
                            .append("password" ,user.getPassword())
                            .append("email", user.getEmail())
                            .append("origin", user.getOrigin()));

//          A questo punto se sono riuscito ad inserire un utente, questo
//          ha utilizzato credenziali mai usate prima
//          se quindi fa ACK, sono sicuro che l'inserimento ha avuto successo
//          posso quindi restituire un nuovo utente

            if(result.wasAcknowledged()){
                UserDTO userDTO = new UserDTO();

                userDTO.setId(Integer.parseInt(id.toString()));
                userDTO.setUsername(user.getUsername());
                userDTO.setNome(user.getNome());
                userDTO.setCognome(user.getCognome());
                userDTO.setPassword(user.getPassword());
                userDTO.setEmail(user.getEmail());
                userDTO.setOrigin(user.getOrigin());


                return userDTO;
            }

        }
        catch (MongoException mongoException)
        {
            System.err.println("Mongo Exception: " + mongoException.getMessage());
        }

        return null;
    }

    //MODIFICARE UN USER
    public boolean updateUser(UserDTO user){

        Document query = new Document("_id", new ObjectId(String.valueOf(user.getId())));
        Bson updates = Updates.combine(
                Updates.set("nome", user.getNome()),
                Updates.set("cognome", user.getCognome()),
                Updates.set("password", user.getPassword()),
                Updates.set("email", user.getEmail()),
                Updates.set("origin", user.getOrigin()));

        try{
            UpdateResult result = mongoClient.getDatabase("ForkTalk").getCollection("Users").
                    updateOne(query,updates);

            if(result.getModifiedCount() == 1){
                return true;
            }

        }catch (MongoException e){
            System.err.println(e);
            return false;
        }

        return false;
    }

    /*public ListDTO<UserDTO> getUsers(String usr){
        ListDTO<UserDTO> toReturn = new ListDTO<>();

        MongoCollection<Document> collection = getDatabase().getCollection("Users");
        List<Document> qList = new ArrayList<>();

        qList.add(new Document("username", new Document("$regex", usr)));
        qList.add(new Document("firstname", usr));
        qList.add(new Document("lastname", usr));

        Document query = new Document("$or", qList);

        try(MongoCursor cursor = collection.find(query).limit(20).iterator())
        {
            List<UserDTO> toSet = new ArrayList<>();

            while (cursor.hasNext()) {
                Document res = (Document) cursor.next();
                UserDTO toAppend = new UserDTO();

                toAppend.setId(res.get("_id") != null ? res.get("_id").toString() : "ID not available");
                toAppend.setUsername(res.get("username") != null ? res.get("username").toString() : "Username not available");
                toAppend.setFirstName(res.get("firstname") != null ? res.get("firstname").toString() : "Firstname not available");
                toAppend.setLastName(res.get("lastname") != null ? res.get("lastname").toString() : "Lastname not available");

                toSet.add(toAppend);
            }

            toReturn.setList(toSet);
            toReturn.setItemCount(toSet.size());
        }

        return toReturn;
    }

     */

    /*public void makeReservation(String restaurantName, String clientUsername, String restaurantUsername, Date date, int people) {
        // Create a new reservation object
        Reservation reservation = new Reservation(this, restaurantName, clientUsername, restaurantUsername, date, people);
        Restaurant.addReservation(reservation);
    }

     */
    }



