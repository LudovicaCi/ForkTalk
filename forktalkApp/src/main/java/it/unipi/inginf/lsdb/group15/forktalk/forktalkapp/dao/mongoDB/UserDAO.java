package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;

import java.lang.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Session;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility.packRestaurantLists;

public class UserDAO extends DriverDAO {

    /**
     * Registers a new user in the database.
     *
     * @param user The User object containing the user information to register.
     * @return true if the registration is successful, false if the username already exists in the database or in case of an error.
     */
    public static boolean registerUser(UserDTO user) {
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
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user from the database based on the username.
     *
     * @param username The username of the user to delete.
     * @return true if the deletion is successful, false if the user is not found or in case of an error.
     */
    public static boolean deleteUser(String username) {
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();

            Bson filter = eq("username", username);

            // Updates the user's reservations in the restaurant collection
            Bson reservationsFilter = eq("reservations.client_username", username);
            Bson reservationsUpdate = Updates.combine(
                    set("reservations.$[reservation].client_username", null),
                    set("reservations.$[reservation].client_name", null),
                    set("reservations.$[reservation].client_surname", null),
                    set("reservations.$[reservation].number of person", 0)
            );
            UpdateOptions updateOptions = new UpdateOptions().arrayFilters(
                    List.of(eq("reservation.client_username", username))
            );

            // Deletes the user document from the collection
            DeleteResult userDeleteResult = userCollection.deleteOne(filter);

            UpdateResult reservationUpdateResult = restaurantCollection.updateMany(reservationsFilter, reservationsUpdate, updateOptions);

            if(Session.loggedUser.getReservations().size() == 0){
                if(userDeleteResult.getDeletedCount() > 0 && reservationUpdateResult.getModifiedCount() == 0){
                    System.out.println("Delete Successful.");
                    clientSession.commitTransaction();
                    return true;
                }else{
                    System.out.println("Delete Unsuccessful.");
                    clientSession.abortTransaction();
                    return false;
                }
            }else {
                if(userDeleteResult.getDeletedCount() > 0 && reservationUpdateResult.getModifiedCount() > 0) {
                    System.out.println("Delete Successful.");
                    clientSession.commitTransaction();
                    return true;
                }else{
                    clientSession.abortTransaction();
                    System.out.println("Delete Unsuccessful.");
                    return false;
                }
            }
        } catch (MongoException e) {
            e.printStackTrace();
            clientSession.abortTransaction();
            return false;
        }
    }

    public static void recoverUser(UserDTO user){
        ClientSession clientSession = mongoClient.startSession();
        boolean res = true;
        try {
            clientSession.startTransaction();
            Document userDoc = Utility.packUser(user);

            if(user.getReservations() !=null){
                for(ReservationDTO reservation: user.getReservations()){
                    Bson restFilter = Filters.eq("username", reservation.getRestaurantID());

                    Document restDocument = restaurantCollection.find(restFilter).first();

                    assert restDocument != null;
                    ReservationDTO newReservation = new ReservationDTO(user.getUsername(), user.getName(), user.getSurname(), restDocument.getString("rest_id"), restDocument.getString("restaurant_name"), restDocument.getString("city"), restDocument.getString("address"), reservation.getDate(), reservation.getPeople());

                    Document newRestDocument = Utility.packRestaurantOneReservation(newReservation);

                    // Retrieve the existing reservations from the restaurant document
                    List<Document> reservationsRestDocs = restDocument.getList("reservations", Document.class);
                    if (reservationsRestDocs != null) {
                        Iterator<Document> iterator = reservationsRestDocs.iterator();
                        boolean slotAvailable = false;
                        while (iterator.hasNext()) {
                            Document doc = iterator.next();
                            if (doc.getString("date").equals(reservation.getDate()) && doc.getString("client_username") == null) {
                                iterator.remove();
                                slotAvailable = true;
                                break;
                            }
                        }

                        if (!slotAvailable) {
                            System.out.println("Invalid or unavailable time slot. Please choose another slot.");
                            return;
                        }
                    } else {
                        return;
                    }

                    reservationsRestDocs.add(newRestDocument);

                    // Updates the restaurant document with the updated reservation list
                    restDocument.append("reservations", reservationsRestDocs);

                    UpdateResult restResult = restaurantCollection.updateOne(restFilter, new Document("$set", restDocument));
                    if(restResult.getModifiedCount() > 0){
                        res = true;
                    }else{
                        res = false;
                        break;
                    }
                }
            }

            InsertOneResult userResult = userCollection.insertOne(userDoc);

            if(userResult.wasAcknowledged() && res){
                clientSession.commitTransaction();
                System.out.println("User recover correctly.");
            }else{
                clientSession.abortTransaction();
                System.out.println("Something went wrong.");
            }
        }catch (MongoException e){
            e.printStackTrace();
            clientSession.abortTransaction();
        }
    }

    /**
     * Suspends a user by setting their suspension status to 1.
     *
     * @param username The username of the user to be suspended
     * @return true if the user is successfully suspended, false otherwise
     */
    public static boolean suspendUser(String username){
        try{
            Bson userFilter = eq("username", username);
            Bson setAdmin = set("suspended", 1);

            UpdateResult result = userCollection.updateOne(userFilter, setAdmin);

            return result.getModifiedCount() > 0;
        }catch (MongoException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Unsuspends a user by setting their suspension status to 0.
     *
     * @param username The username of the user to be unsuspended
     * @return true if the user is successfully unsuspended, false otherwise
     */
    public static boolean unsuspendUser(String username){
        try{
            Bson userFilter = eq("username", username);
            Bson setAdmin = set("suspended", 0);

            UpdateResult result = userCollection.updateOne(userFilter, setAdmin);

            return result.getModifiedCount() > 0;
        }catch (MongoException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Makes a user an admin by setting their role to 2.
     *
     * @param username The username of the user to be made an admin
     * @return true if the user is successfully made an admin, false otherwise
     */
    public static boolean makeAdmin(String username){
        try{
            Bson userFilter = eq("username", username);
            Bson setAdmin = set("role", 2);

            UpdateResult result = userCollection.updateOne(userFilter, setAdmin);

            return result.getModifiedCount() > 0;
        }catch (MongoException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if there is already a user with the same username.
     *
     * @param username the username to check
     * @return true if the username is already taken by another user, false otherwise
     */
    public static boolean isUsernameTaken(String username) {
        try {
            // Creates a filter to match the username
            Bson filter = eq("username", username);

            // Uses the filter to find any user with the given username
            Document user = userCollection.find(filter).first();

            // Checks if a user with the given username already exists
            return user != null;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if there is already a user with the same email.
     *
     * @param email the email to check
     * @return true if the email is already taken by another user, false otherwise
     */
    public static boolean isEmailTaken(String email) {
        try {
            // Creates a filter to match the email
            Bson filter = eq("email", email);

            // Uses the filter to find any user with the given email
            Document user = userCollection.find(filter).first();

            // Checks if a user with the given email already exists
            return user != null;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Updates a user in the database based on the username.
     *
     * @param username    The username of the user to update.
     * @param updatedUser The UserDTO object containing the updated user information.
     * @return true if the update is successful, false if the user is not found or in case of an error.
     */
    public static boolean updateUser(String username, UserDTO updatedUser) {
        try {
            // Creates a filter to match the username
            Bson filter = eq("username", username);

            // Creates a document with the updated fields
            Document updateDoc = new Document("$set", new Document()
                    .append("name", updatedUser.getName())
                    .append("surname", updatedUser.getSurname())
                    .append("email", updatedUser.getEmail())
                    .append("username", updatedUser.getUsername())
                    .append("password", updatedUser.getPassword())
                    .append("origin", updatedUser.getOrigin())
                    .append("suspended", updatedUser.getSuspended())
                    .append("role", updatedUser.getRole()));

            // Updates the user in the user collection
            UpdateResult updateResult = userCollection.updateOne(filter, updateDoc);

            // Updates the user's reservations in the restaurant collection
            Bson reservationsFilter = eq("reservations.client_username", username);
            Bson reservationsUpdate = Updates.combine(
                    set("reservations.$[reservation].client_username", updatedUser.getUsername()),
                    set("reservations.$[reservation].client_name", updatedUser.getName()),
                    set("reservations.$[reservation].client_surname", updatedUser.getSurname())
            );
            UpdateOptions updateOptions = new UpdateOptions().arrayFilters(
                    List.of(eq("reservation.client_username", username))
            );
            restaurantCollection.updateMany(reservationsFilter, reservationsUpdate, updateOptions);

            // Updates the user's reviews in the restaurant collection
            Bson reviewsFilter = eq("reviews.reviewer_pseudo", username);
            Bson reviewsUpdate = set("reviews.$[review].reviewer_pseudo", updatedUser.getUsername());
            UpdateOptions reviewsUpdateOptions = new UpdateOptions().arrayFilters(
                    List.of(eq("review.reviewer_pseudo", username))
            );
            restaurantCollection.updateMany(reviewsFilter, reviewsUpdate, reviewsUpdateOptions);

            // Checks if the update was successful
            return updateResult.getModifiedCount() > 0;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Logs in a user based on the username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The UserDTO object representing the logged-in user, or an empty UserDTO if the username or password is incorrect or in case of an error.
     */
    public static UserDTO loginUser(String username, String password) {
        UserDTO userDTO = new UserDTO();
        try {

            Bson filter = Filters.and(
                    eq("username", username),
                    eq("password", password));


            Document userDocument = userCollection.find(filter).first();


            if (userDocument != null) {
                userDTO.setName(userDocument.getString("name"));
                userDTO.setSurname(userDocument.getString("surname"));
                userDTO.setEmail(userDocument.getString("email"));
                userDTO.setUsername(userDocument.getString("username"));
                userDTO.setPassword(userDocument.getString("password"));
                userDTO.setOrigin(userDocument.getString("origin"));
                userDTO.setSuspended(userDocument.getInteger("suspended"));
                userDTO.setRole(userDocument.getInteger("role"));

                List<Document> restaurantListDocuments = userDocument.getList("restaurantsList", Document.class);

                if (restaurantListDocuments != null) {
                    for (Document doc : restaurantListDocuments) {
                        userDTO.getRestaurantLists().add(Utility.unpackOneRestaurantList(doc));
                    }
                }

                List<Document> reservationsDocuments = userDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        userDTO.getReservations().add(Utility.unpackOneUserReservation(doc));
                    }
                }

                return userDTO;
            } else {
                System.out.println("Incorrect username or password!");
                return null;
            }
        } catch (MongoException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a user from the database based on the username.
     *
     * @param username The username of the user to retrieve.
     * @return The UserDTO object representing the retrieved user, or null if the user is not found or in case of an error.
     */
    public static UserDTO getUserByUsername(String username){
        UserDTO user = new UserDTO();
        try{
            // Creates a filter to match the username
            Bson filter = eq("username", username);

            // Uses the filter to find a matching user document in the collection
            Document userDocument = userCollection.find(filter).first();

            // Checks if a matching user document was found
            if(userDocument != null){
                user.setEmail(userDocument.getString("email"));
                user.setUsername(userDocument.getString("username"));
                user.setPassword(userDocument.getString("password"));
                user.setName(userDocument.getString("name"));
                user.setSurname(userDocument.getString("surname"));
                user.setOrigin(userDocument.getString("origin"));
                user.setSuspended(userDocument.getInteger("suspended"));
                user.setRole(userDocument.getInteger("role"));

                List<Document> reservationsDocuments = userDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        user.getReservations().add(Utility.unpackOneUserReservation(doc));
                    }
                }

                List<Document> restaurantListDocuments = userDocument.getList("restaurantList", Document.class);

                if (restaurantListDocuments != null) {
                    for (Document doc : restaurantListDocuments) {
                        user.getRestaurantLists().add(Utility.unpackOneRestaurantList(doc));
                    }
                }

            }

            return user;
        }catch (MongoException e){
            System.out.println("An error occurred while retrieving the user");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the user document from the collection based on the username.
     *
     * @param username The username of the user
     * @return The user document if found, or null if not found or an exception occurs
     */
    public static Document getUserDocumentByUsername(String username){
        try {
            Bson filter = Filters.eq("username", username);

            return userCollection.find(filter).first();
        }catch (MongoException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches for users matching the specified criteria.
     *
     * @param username The username to search for. If empty or null, no matching is performed.
     * @param name     The name of the user to search for. If empty or null, no matching is performed.
     * @param surname  The surname of the user to search for. If empty or null, no matching is performed.
     * @param email    The email of the user to search for. If empty or null, no matching is performed.
     * @return A list of documents representing the users matching the search criteria.
     */
    public static List<Document> searchUsers(String username, String name, String surname, String email, int page, int pageSize) {
        try {
            List<Bson> aggregationPipeline = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                Bson matchUsername = match(eq("username", username));
                aggregationPipeline.add(matchUsername);
            }

            if (name != null && !name.isEmpty()) {
                Bson matchName = Aggregates.match(Filters.regex("name", Pattern.quote(name), "i"));
                aggregationPipeline.add(matchName);
            }

            if (surname != null && !surname.isEmpty()) {
                Bson matchSurname = Aggregates.match(Filters.regex("surname", Pattern.quote(surname), "i"));
                aggregationPipeline.add(matchSurname);
            }

            if (email != null && !email.isEmpty()) {
                Bson matchEmail = Aggregates.match(Filters.regex("email", Pattern.quote(email), "i"));
                aggregationPipeline.add(matchEmail);
            }

            if (aggregationPipeline.isEmpty()) {
                Bson matchDummy = match(new Document());
                aggregationPipeline.add(matchDummy);
            }

            Bson sortByParameters = Sorts.orderBy(
                    Sorts.ascending("username"),
                    Sorts.ascending("name"),
                    Sorts.ascending("surname"),
                    Sorts.ascending("email")
            );

            aggregationPipeline.add(Aggregates.sort(sortByParameters));

            aggregationPipeline.add(Aggregates.skip((page - 1) * pageSize));
            aggregationPipeline.add(Aggregates.limit(pageSize));

            return userCollection.aggregate(aggregationPipeline).into(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("An error occurred while searching for users");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Creates a new restaurant list for a user.
     *
     * @param user  The UserDTO object representing the user.
     * @param title The title of the new restaurant list.
     * @return true if the creation is successful, false if a list with the same title already exists or in case of an error.
     */
    public static boolean createRestaurantListToUser(UserDTO user, String title) {
        try {
            Document document = userCollection.find(Filters.and(eq("username", user.getUsername()),
                    eq("restaurantsList.title", title))).first();
            if (document != null) {
                System.err.println("ERROR: Name already in use.");
                return false;
            }

            Document restaurantList = new Document("title", title)
                    .append("restaurants", List.of());

            userCollection.updateOne(
                    eq("username", user.getUsername()),
                    new Document().append(
                            "$push",
                            new Document("restaurantsList", restaurantList)
                    )
            );

            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a restaurant list from a user.
     *
     * @param username  The username representing the user.
     * @param title The title of the restaurant list to delete.
     * @return true if the deletion is successful, false if the list is not found or in case of an error.
     */
    public static boolean deleteRestaurantListFromUser(String username, String title) {
        try {
            Document userDocument = userCollection.find(eq("username", username)).first();

            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            int listIndex = -1;
            List<Document> restaurantsList = userDocument.getList("restaurantsList", Document.class);
            if (restaurantsList != null) {
                for (int i = 0; i < restaurantsList.size(); i++) {
                    Document list = restaurantsList.get(i);
                    if (list.getString("title").equals(title)) {
                        listIndex = i;
                        break;
                    }
                }
            }

            if (listIndex != -1) {
                userDocument.getList("restaurantsList", Document.class).remove(listIndex);

                userCollection.replaceOne(eq("username", username), userDocument);
                return true;
            } else {
                System.err.println("ERROR: Restaurant list not found.");
                return false;
            }
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the restaurant lists of a user from the database.
     *
     * @param user The UserDTO object representing the user.
     * @return An ArrayList of Strings representing the titles of the restaurant lists of the user,
     * or null if the user is not found or in case of an error.
     */
    public static ArrayList<String> getTitlesRestaurantListsOfUser(UserDTO user) {
        try {
            // Finds the user document
            Document userDocument = userCollection.find(eq("username", user.getUsername())).first();

            // Checks if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return null;
            }

            // Gets the restaurantsList field from the user document
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);

            // Creates an ArrayList to store titles
            ArrayList<String> titles = new ArrayList<>();

            // Iterates over the restaurantLists and convert them to RestaurantsListDTO objects
            for (Document list : restaurantLists) {
                String title = list.getString("title");
                titles.add(title);
            }

            return titles;
        } catch (MongoException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the restaurant lists of a user.
     * @param user The UserDTO object representing the user whose restaurant lists are to be obtained
     * @return An ArrayList of RestaurantsListDTO containing the user's restaurant lists, or null if an exception occurs
     */
    public static ArrayList<RestaurantsListDTO> getRestaurantListsByUser(UserDTO user) {
        try {
            Bson filter = eq("username", user.getUsername());

            Document userDocument = userCollection.find(filter).first();

            if (userDocument != null) {
                List<Document> restaurantListDocuments = userDocument.getList("restaurantsList", Document.class);

                ArrayList<RestaurantsListDTO> restaurantLists = new ArrayList<>();

                if(restaurantListDocuments == null)
                    return restaurantLists;

                for (Document restaurantListDocument : restaurantListDocuments) {
                    String title = restaurantListDocument.getString("title");

                    List<Document> restaurantDocuments = restaurantListDocument.getList("restaurants", Document.class);

                    ArrayList<RestaurantDTO> restaurants = new ArrayList<>();

                    for (Document restaurantDocument : restaurantDocuments) {
                        String restaurantId = restaurantDocument.getString("restaurant_id");
                        String restaurantName = restaurantDocument.getString("restaurant_name");
                        RestaurantDTO restaurant = new RestaurantDTO();
                        restaurant.setId(restaurantId);
                        restaurant.setName(restaurantName);

                        restaurants.add(restaurant);
                    }

                    RestaurantsListDTO restaurantsList = new RestaurantsListDTO(title, restaurants);

                    restaurantLists.add(restaurantsList);
                }

                return restaurantLists;
            } else {
                System.err.println("ERROR: User not found.");
                return null;
            }
        } catch (MongoException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the restaurants from a specific list of a user from the database.
     *
     * @param user  The UserDTO object representing the user.
     * @param title The title of the restaurant list to retrieve.
     * @return A RestaurantsListDTO object representing the restaurant list,
     * or an empty RestaurantsListDTO if the list is not found or in case of an error.
     */
    public static RestaurantsListDTO getRestaurantsFromLists(UserDTO user, String title) {
        try {
            Bson filter = eq("username", user.getUsername());

            Document userDocument = userCollection.find(filter).first();

            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return null;
            }

            List<Document> restaurantLists;

            if (userDocument.containsKey("restaurantList")) {
                restaurantLists = userDocument.getList("restaurantList", Document.class);

            }else if (userDocument.containsKey("restaurantsList")) {
                restaurantLists = userDocument.getList("restaurantsList", Document.class);
            }else{
                restaurantLists = null;
            }

            // Iterates over the restaurantLists and find the list with the given title
            assert restaurantLists != null;
            for (Document list : restaurantLists) {
                String listTitle = list.getString("title");
                if (listTitle.equals(title)) {
                    List<Document> restaurantsDocuments = list.getList("restaurants", Document.class);

                    // Converts the restaurantsDocuments to Restaurant objects
                    ArrayList<RestaurantDTO> restaurants = new ArrayList<>();
                    if (restaurantsDocuments != null) {
                        for (Document restaurantDoc : restaurantsDocuments) {
                            // Extracts the necessary fields
                            String restaurantId = restaurantDoc.getString("restaurant_id");
                            String restaurantName = restaurantDoc.getString("restaurant_name");

                            RestaurantDTO restaurant = new RestaurantDTO();
                            restaurant.setId(restaurantId);
                            restaurant.setName(restaurantName);

                            restaurants.add(restaurant);
                        }
                    }

                    return new RestaurantsListDTO(listTitle, restaurants);
                }
            }

            return new RestaurantsListDTO();
        } catch (MongoException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a restaurant to a specific restaurant list of a user.
     *
     * @param user       The UserDTO object representing the user.
     * @param title      The title of the restaurant list.
     * @param restaurant The RestaurantDTO object representing the restaurant to add.
     * @return true if the addition is successful, false if the user or restaurant list is not found or in case of an error.
     */
    public static boolean addRestaurantToList(UserDTO user, String title, RestaurantDTO restaurant) {
        try {
            Bson filter = eq("username", user.getUsername());

            Document userDocument = userCollection.find(filter).first();

            // Checks if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            // Gets the restaurantsList field from the user document
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);

            // Iterates over the restaurantLists and find the list with the given title
            for (Document list : restaurantLists) {
                String listTitle = list.getString("title");
                if (listTitle.equals(title)) {
                    List<Document> restaurantsDocuments = list.getList("restaurants", Document.class);

                    // Checks if the restaurant already exists in the list
                    boolean isRestaurantAlreadyPresent = restaurantsDocuments.stream()
                            .anyMatch(doc -> doc.getString("restaurant_id").equals(restaurant.getId()));

                    // If the restaurant is already in the list, return false
                    if (isRestaurantAlreadyPresent) {
                        System.out.println("Restaurant already exists in the list.");
                        return false;
                    }

                    // If the restaurant is not already in the list, add it
                    Document newRestaurantDoc = new Document("restaurant_id", restaurant.getId())
                            .append("restaurant_name", restaurant.getName());
                    restaurantsDocuments.add(newRestaurantDoc);

                    // Updates the user document in the database
                    userCollection.updateOne(eq("username", user.getUsername()),
                            set("restaurantsList", restaurantLists));

                    return true;
                }
            }

            // If the list with the given title is not found, return false
            System.err.println("ERROR: List with title " + title + " not found.");
            return false;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recovers the restaurant list of a user.
     *
     * @param username           The username of the user
     * @param newRestaurantLists The new restaurant lists to be set for the user
     */
    public static void recoverRestaurantList(String username, ArrayList<RestaurantsListDTO> newRestaurantLists) {
        try {
            Bson filter = eq("username", username);

            // Creates a new document with the updated restaurantList field
            Document updatedDocument = new Document("$set", new Document("restaurantsList", packRestaurantLists(newRestaurantLists)));

            // Performs the update operation to replace the existing document with the updated one
            UpdateResult result = userCollection.updateOne(filter, updatedDocument);

            if (result.getModifiedCount() > 0) {
                System.out.println("Restaurant list updated successfully.");
            } else {
                System.out.println("No document found for the given username: " + username);
            }
        } catch (MongoException e) {
            System.err.println("An error occurred while updating the restaurant list:");
            e.printStackTrace();
        }
    }

    /**
     * Removes a restaurant from a restaurantsList of a user.
     *
     * @param user         The UserDTO object representing the user.
     * @param title        The title of the restaurantsList.
     * @param restaurantId The ID of the restaurant to remove.
     * @return true if the restaurant is successfully removed, false otherwise (e.g., user not found, restaurantsList not found, restaurant not found).
     */
    public static boolean removeRestaurantFromList(UserDTO user, String title, String restaurantId) {
        try {
            // Finds the user document
            Document userDocument = userCollection.find(eq("username", user.getUsername())).first();

            // Checks if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            // Finds the restaurantsList with the specified title
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

            // Checks if the targetList exists
            if (targetList == null) {
                System.err.println("ERROR: Restaurants list not found.");
                return false;
            }

            // Finds the restaurant with the specified ID in the targetList
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

            // Checks if the target Restaurant exists
            if (targetRestaurant == null) {
                System.err.println("ERROR: Restaurant not found.");
                return false;
            }

            // Removes the restaurant from the target List
            restaurants.remove(targetRestaurant);

            // Updates the user document in the collection
            userCollection.replaceOne(eq("username", user.getUsername()), userDocument);

            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the reservations for a given user.
     *
     * @param user The UserDTO object representing the user.
     * @return An ArrayList of ReservationDTO objects representing the reservations, or null if an error occurs.
     */
    public static ArrayList<ReservationDTO> getReservations(UserDTO user) {
        try {
            // Finds the user document
            Document userDocument = userCollection.find(eq("username", user.getUsername())).first();

            // Checks if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return null;
            }

            // Retrieves reservations list
            List<Document> reservationsDocuments = userDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> result = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    result.add(Utility.unpackOneUserReservation(doc));
                }
            }
            return result;
        } catch (MongoException e) {
            System.err.println("ERROR: An error occurred while retrieving reservations.");
            e.printStackTrace();
            return null;
        }
    }
}



