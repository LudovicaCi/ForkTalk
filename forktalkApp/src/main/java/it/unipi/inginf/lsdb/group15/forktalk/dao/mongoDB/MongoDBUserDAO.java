package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB;

import com.google.gson.GsonBuilder;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils.CustomDateDeserializer;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.UserDTO;

import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.unipi.inginf.lsdb.group15.forktalk.model.Restaurant;
import it.unipi.inginf.lsdb.group15.forktalk.model.RestaurantsList;
import it.unipi.inginf.lsdb.group15.forktalk.model.User;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.google.gson.Gson;

import static javax.management.Query.*;

public class MongoDBUserDAO extends MongoDBDriverDAO {
    MongoCursor<Document> cursor;
    //check if the username is already present

  /*  public boolean userAlreadyPresent(String username) {
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
    } */

    /**
     * Registers a new user in the database.
     * @param user The User object containing the user information to register.
     * @return true if the registration is successful, false if the username already exists in the database or in case of an error.
     */
    public static boolean registerUser(User user) {
        try {
            if (userCollection.countDocuments(new Document("username", user.getUsername())) == 0) {
                Document doc = new Document()
                        .append("name", user.getName())
                        .append("surname", user.getSurname())
                        .append("email", user.getEmail())
                        .append("username", user.getUsername())
                        .append("password", user.getPassword())
                        .append("origin", user.getOrigin())
                        .append("suspended", user.getSuspended())
                        .append("role", user.getRole());

                userCollection.insertOne(doc);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user from the database based on the username.
     * @param username The username of the user to delete.
     * @return true if the deletion is successful, false if the user is not found or in case of an error.
     */
    public static boolean deleteUser(String username) {
        try {
            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Use the filter to delete the matching user document from the collection
            DeleteResult result = userCollection.deleteOne(filter);

            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a user in the database based on the username.
     * @param username The username of the user to update.
     * @param updatedUser The UserDTO object containing the updated user information.
     * @return true if the update is successful, false if the user is not found or in case of an error.
     */
    public static boolean updateUser(String username, UserDTO updatedUser) {
        try {
            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Create a document with the updated fields
            Document updateDoc = new Document("$set", new Document()
                    .append("name", updatedUser.getName())
                    .append("surname", updatedUser.getSurname())
                    .append("email", updatedUser.getEmail())
                    .append("username", updatedUser.getUsername())
                    .append("password", updatedUser.getPassword())
                    .append("origin", updatedUser.getOrigin())
                    .append("suspended", updatedUser.getSuspended())
                    .append("role", updatedUser.getRole()));

            // Use the filter and update document to update the user in the collection
            UpdateResult updateResult = userCollection.updateOne(filter, updateDoc);

            // Check if the update was successful
            return updateResult.getModifiedCount() > 0;
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Logs in a user based on the username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The UserDTO object representing the logged-in user, or an empty UserDTO if the username or password is incorrect or in case of an error.
     */
    public static UserDTO loginUser(String username, String password) {
        UserDTO userDTO = new UserDTO();
        try {
            // Create a filter to match the username and password
            Bson filter = Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("password", password));

            // Use the filter to find a matching user document in the collection
            Document userDocument = userCollection.find(filter).first();

            // Create a GsonBuilder instance
            GsonBuilder gsonBuilder = new GsonBuilder();

            // Register a custom date deserializer with GsonBuilder
            gsonBuilder.registerTypeAdapter(Date.class, new CustomDateDeserializer());

            // Create a Gson instance
            Gson gson = gsonBuilder.create();

            // Convert the userDocument to JSON and deserialize it into a UserDTO object using Gson
            return gson.fromJson(gson.toJson(userDocument), UserDTO.class);
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
        }

        // No matching user
        System.out.println("Incorrect username or password!");
        return userDTO;
    }

    /**
     * Retrieves a user from the database based on the username.
     * @param username The username of the user to retrieve.
     * @return The UserDTO object representing the retrieved user, or null if the user is not found or in case of an error.
     */
    public static UserDTO getUserByUsername(String username) {
        try {
            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Use the filter to find a matching user document in the collection
            Document userDocument = userCollection.find(filter).first();

            // Create a GsonBuilder instance
            GsonBuilder gsonBuilder = new GsonBuilder();

            // Register a custom date deserializer with GsonBuilder
            gsonBuilder.registerTypeAdapter(Date.class, new CustomDateDeserializer());

            // Create a Gson instance
            Gson gson = gsonBuilder.create();

            // Convert the userDocument to JSON and deserialize it into a UserDTO object using Gson
            return gson.fromJson(gson.toJson(userDocument), UserDTO.class);
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a new restaurant list for a user.
     * @param user The UserDTO object representing the user.
     * @param title The title of the new restaurant list.
     * @return true if the creation is successful, false if a list with the same title already exists or in case of an error.
     */
    public static boolean createRestaurantListToUser(UserDTO user, String title) {
        try {
            // Check if there are other lists with the same name
            Document document = userCollection.find(Filters.and(Filters.eq("username", user.getUsername()),
                    Filters.eq("readingLists.title", title))).first();
            if (document != null) {
                System.err.println("ERROR: Name already in use.");
                return false;
            }

            // Create the new reading_list
            Document readingList = new Document("title", title)
                    .append("restaurants", List.of());

            // Insert the new reading_list
            userCollection.updateOne(
                    Filters.eq("username", user.getUsername()),
                    new Document().append(
                            "$push",
                            new Document("restaurantsList", readingList)
                    )
            );

            return true;
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a restaurant list from a user.
     * @param user The UserDTO object representing the user.
     * @param title The title of the restaurant list to delete.
     * @return true if the deletion is successful, false if the list is not found or in case of an error.
     */
    public static boolean deleteRestaurantListFromUser(UserDTO user, String title) {
        try {
            // Find the user document
            Document userDocument = userCollection.find(Filters.eq("username", user.getUsername())).first();

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            // Find the index of the reading_list with the specified title
            int listIndex = -1;
            List<Document> readingLists = userDocument.getList("restaurantsList", Document.class);
            if (readingLists != null) {
                for (int i = 0; i < readingLists.size(); i++) {
                    Document list = readingLists.get(i);
                    if (list.getString("title").equals(title)) {
                        listIndex = i;
                        break;
                    }
                }
            }

            // If the reading_list is found, remove it from the user document
            if (listIndex != -1) {
                userDocument.getList("restaurantsList", Document.class).remove(listIndex);

                // Update the user document in the collection
                userCollection.replaceOne(Filters.eq("username", user.getUsername()), userDocument);
                return true;
            } else {
                System.err.println("ERROR: Reading list not found.");
                return false;
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the restaurant lists of a user from the database.
     * @param user The UserDTO object representing the user.
     * @return An ArrayList of RestaurantsListDTO objects representing the restaurant lists of the user,
     *         or null if the user is not found or in case of an error.
     */
    public static ArrayList<RestaurantsListDTO> getRestaurantListsOfUser(UserDTO user) {
        try {
            // Find the user document
            Document userDocument = userCollection.find(Filters.eq("username", user.getUsername())).first();

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return null;
            }

            // Get the restaurantsList field from the user document
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);

            // Create an ArrayList to store RestaurantsListDTO objects
            ArrayList<RestaurantsListDTO> restaurantsListsDTO = new ArrayList<>();

            // Iterate over the restaurantLists and convert them to RestaurantsListDTO objects
            for (Document list : restaurantLists) {
                String title = list.getString("title");
                List<Document> restaurantsDocuments = list.getList("restaurants", Document.class);

                // Convert the restaurantsDocuments to Restaurant objects
                ArrayList<Restaurant> restaurants = new ArrayList<>();
                if (restaurantsDocuments != null) {
                    for (Document restaurantDoc : restaurantsDocuments) {
                        // Extract the necessary fields from the restaurantDoc and create a Restaurant object
                        String restaurantName = restaurantDoc.getString("name");
                        // Add more fields if necessary

                        Restaurant restaurant = new Restaurant(restaurantName);
                        // Add more fields if necessary

                        restaurants.add(restaurant);
                    }
                }

                // Create a RestaurantsListDTO object and add it to the ArrayList
                RestaurantsListDTO listDTO = new RestaurantsListDTO(title, restaurants);
                restaurantsListsDTO.add(listDTO);
            }

            return restaurantsListsDTO;
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a restaurant to a specific restaurant list of a user.
     * @param user The UserDTO object representing the user.
     * @param title The title of the restaurant list.
     * @param restaurant The RestaurantDTO object representing the restaurant to add.
     * @return true if the addition is successful, false if the user or restaurant list is not found or in case of an error.
     */
    public static boolean addRestaurantToList(UserDTO user, String title, RestaurantDTO restaurant) {
        try {
            // Find the user document
            Document userDocument = userCollection.find(Filters.eq("username", user.getUsername())).first();

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            // Find the restaurantsList with the specified title
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);
            if (restaurantLists == null) {
                System.err.println("ERROR: Restaurants list not found.");
                return false;
            }

            Document targetList = null;
            for (Document list : restaurantLists) {
                if (list.getString("title").equals(title)) {
                    targetList = list;
                    break;
                }
            }

            // Check if the targetList exists
            if (targetList == null) {
                System.err.println("ERROR: Restaurants list not found.");
                return false;
            }

            // Create a new document with the restaurant id and name
            Document newRestaurant = new Document("restaurant_id", restaurant.getId())
                    .append("restaurant_name", restaurant.getName());

            // Add the new restaurant to the list
            targetList.getList("restaurants", Document.class).add(newRestaurant);

            // Update the user document in the collection
            userCollection.replaceOne(Filters.eq("username", user.getUsername()), userDocument);

            return true;
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes a restaurant from a restaurantsList of a user.
     * @param user   The UserDTO object representing the user.
     * @param title  The title of the restaurantsList.
     * @param restaurantId  The ID of the restaurant to remove.
     * @return true if the restaurant is successfully removed, false otherwise (e.g., user not found, restaurantsList not found, restaurant not found).
     */
    public static boolean removeRestaurantFromList(UserDTO user, String title, String restaurantId) {
        try {
            // Find the user document
            Document userDocument = userCollection.find(Filters.eq("username", user.getUsername())).first();

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            // Find the restaurantsList with the specified title
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);
            if (restaurantLists == null) {
                System.err.println("ERROR: Restaurants list not found.");
                return false;
            }

            Document targetList = null;
            for (Document list : restaurantLists) {
                if (list.getString("title").equals(title)) {
                    targetList = list;
                    break;
                }
            }

            // Check if the targetList exists
            if (targetList == null) {
                System.err.println("ERROR: Restaurants list not found.");
                return false;
            }

            // Find the restaurant with the specified ID in the targetList
            List<Document> restaurants = targetList.getList("restaurants", Document.class);
            if (restaurants == null) {
                System.err.println("ERROR: Restaurants not found.");
                return false;
            }

            Document targetRestaurant = null;
            for (Document restaurant : restaurants) {
                if (restaurant.getString("restaurant_id").equals(restaurantId)) {
                    targetRestaurant = restaurant;
                    break;
                }
            }

            // Check if the targetRestaurant exists
            if (targetRestaurant == null) {
                System.err.println("ERROR: Restaurant not found.");
                return false;
            }

            // Remove the restaurant from the targetList
            restaurants.remove(targetRestaurant);

            // Update the user document in the collection
            userCollection.replaceOne(Filters.eq("username", user.getUsername()), userDocument);

            return true;
        } catch (Exception e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }



    //find user by username
  /*  public Document findUserByUsername (String username){
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
        try(MongoCursor<Document> cursor = collection.find(or(eq("email", eml),eq("username",usr))).limit(1).iterator())
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

        try(MongoCursor<Document> cursor = collection.find(or(eq("email", eml),eq("username",psw))).limit(1).iterator());
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
                GeneralUserDTO.setEmail(res.get("email") != null ? res.get("email").toString() : "EMail not available");
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
                            .append("username", GeneralUserDTO.getUsername())
                            .append("firstname", user.getNome())
                            .append("lastname", user.getCognome())
                            .append("password" , GeneralUserDTO.getPassword())
                            .append("email", GeneralUserDTO.getEmail())
                            .append("origin", user.getOrigin()));

//          A questo punto se sono riuscito ad inserire un utente, questo
//          ha utilizzato credenziali mai usate prima
//          se quindi fa ACK, sono sicuro che l'inserimento ha avuto successo
//          posso quindi restituire un nuovo utente

            if(result.wasAcknowledged()){
                UserDTO userDTO = new UserDTO();

                userDTO.setId(Integer.parseInt(id.toString()));
                userDTO.setUsername(GeneralUserDTO.getUsername());
                userDTO.setNome(user.getNome());
                userDTO.setCognome(user.getCognome());
                userDTO.setPassword(GeneralUserDTO.getPassword());
                GeneralUserDTO.setEmail(GeneralUserDTO.getEmail());
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
                Updates.set("password", GeneralUserDTO.getPassword()),
                Updates.set("email", GeneralUserDTO.getEmail()),
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
    } */

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



