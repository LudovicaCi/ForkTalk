package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Restaurant;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

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
            // Handle any exceptions that occur during the database operation
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
        try {
            // Create a filter to match the username
            Bson filter = eq("username", username);

            // Delete the user document from the collection
            DeleteResult userDeleteResult = userCollection.deleteOne(filter);

            // Update the user's reservations in the restaurant collection
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

            UpdateResult reservationUpdateResult = restaurantCollection.updateMany(reservationsFilter, reservationsUpdate, updateOptions);

            // Check if the deletion was successful in both collections
            return userDeleteResult.getDeletedCount() > 0 && reservationUpdateResult.getModifiedCount() > 0;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
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
            // Create a filter to match the username
            Bson filter = eq("username", username);

            // Use the filter to find any user with the given username
            Document user = userCollection.find(filter).first();

            // Check if a user with the given username already exists
            return user != null;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
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
            // Create a filter to match the email
            Bson filter = eq("email", email);

            // Use the filter to find any user with the given email
            Document user = userCollection.find(filter).first();

            // Check if a user with the given email already exists
            return user != null;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
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
            // Create a filter to match the username
            Bson filter = eq("username", username);

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

            // Update the user in the user collection
            UpdateResult updateResult = userCollection.updateOne(filter, updateDoc);

            // Update the user's reservations in the restaurant collection
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

            // Update the user's reviews in the restaurant collection
            Bson reviewsFilter = eq("reviews.reviewer_pseudo", username);
            Bson reviewsUpdate = set("reviews.$[review].reviewer_pseudo", updatedUser.getUsername());
            UpdateOptions reviewsUpdateOptions = new UpdateOptions().arrayFilters(
                    List.of(eq("review.reviewer_pseudo", username))
            );
            restaurantCollection.updateMany(reviewsFilter, reviewsUpdate, reviewsUpdateOptions);

            // Check if the update was successful
            return updateResult.getModifiedCount() > 0;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
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
            // Creazione del filtro per corrispondere a username e password
            Bson filter = Filters.and(
                    eq("username", username),
                    eq("password", password));

            // Utilizzo del filtro per trovare un documento utente corrispondente nella collezione
            Document userDocument = userCollection.find(filter).first();

            // Controllo se il documento utente esiste
            if (userDocument != null) {
                // Creazione di un nuovo oggetto UserDTO e impostazione delle informazioni dall'oggetto Document
                userDTO.setName(userDocument.getString("name"));
                userDTO.setSurname(userDocument.getString("surname"));
                userDTO.setEmail(userDocument.getString("email"));
                userDTO.setUsername(userDocument.getString("username"));
                userDTO.setPassword(userDocument.getString("password"));
                userDTO.setOrigin(userDocument.getString("origin"));
                userDTO.setSuspended(userDocument.getInteger("suspended"));
                userDTO.setRole(userDocument.getInteger("role"));

                // Recupero la lista dei ristoranti come Document
                List<Document> restaurantListDocuments = userDocument.getList("restaurantsList", Document.class);

                if (restaurantListDocuments != null) {
                    for (Document doc : restaurantListDocuments) {
                        userDTO.getRestaurantLists().add(Utility.unpackOneRestaurantList(doc));
                    }
                }

                //recupero la lista di reservation
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
            // Create a filter to match the username
            Bson filter = eq("username", username);

            // Use the filter to find a matching user document in the collection
            Document userDocument = userCollection.find(filter).first();

            // Check if a matching user document was found
            if(userDocument != null){
                user.setEmail(userDocument.getString("email"));
                user.setUsername(userDocument.getString("username"));
                user.setPassword(userDocument.getString("password"));
                user.setName(userDocument.getString("name"));
                user.setSurname(userDocument.getString("surname"));
                user.setOrigin(userDocument.getString("origin"));
                user.setSuspended(userDocument.getInteger("suspended"));
                user.setRole(userDocument.getInteger("role"));

                // Retrieve reservations
                List<Document> reservationsDocuments = userDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        user.getReservations().add(Utility.unpackOneUserReservation(doc));
                    }
                }

                //retrieve restaurantList
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
     * Searches for users matching the specified criteria.
     *
     * @param username The username to search for. If empty or null, no matching is performed.
     * @param name     The name of the user to search for. If empty or null, no matching is performed.
     * @param surname  The surname of the user to search for. If empty or null, no matching is performed.
     * @param email    The email of the user to search for. If empty or null, no matching is performed.
     * @return A list of documents representing the users matching the search criteria.
     */
    public static List<Document> searchUsers(String username, String name, String surname, String email) {
        try {
            List<Bson> aggregationPipeline = new ArrayList<>();

            // Match by username
            if (username != null && !username.isEmpty()) {
                Bson matchUsername = match(eq("username", username));
                aggregationPipeline.add(matchUsername);
            }

            // Match by name (case-insensitive, partial match)
            if (name != null && !name.isEmpty()) {
                Bson matchName = Aggregates.match(Filters.regex("name", Pattern.quote(name), "i"));
                aggregationPipeline.add(matchName);
            }

            // Match by surname (case-insensitive, partial match)
            if (surname != null && !surname.isEmpty()) {
                Bson matchSurname = Aggregates.match(Filters.regex("surname", Pattern.quote(surname), "i"));
                aggregationPipeline.add(matchSurname);
            }

            // Match by email (case-insensitive, partial match)
            if (email != null && !email.isEmpty()) {
                Bson matchEmail = Aggregates.match(Filters.regex("email", Pattern.quote(email), "i"));
                aggregationPipeline.add(matchEmail);
            }

            // Check if any pipeline stages are added
            if (aggregationPipeline.isEmpty()) {
                // Add a dummy match stage to avoid the error
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

            // Perform aggregation
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
            // Check if there are other lists with the same name
            Document document = userCollection.find(Filters.and(eq("username", user.getUsername()),
                    eq("readingLists.title", title))).first();
            if (document != null) {
                System.err.println("ERROR: Name already in use.");
                return false;
            }

            // Create the new reading_list
            Document readingList = new Document("title", title)
                    .append("restaurants", List.of());

            // Insert the new reading_list
            userCollection.updateOne(
                    eq("username", user.getUsername()),
                    new Document().append(
                            "$push",
                            new Document("restaurantsList", readingList)
                    )
            );

            return true;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a restaurant list from a user.
     *
     * @param user  The UserDTO object representing the user.
     * @param title The title of the restaurant list to delete.
     * @return true if the deletion is successful, false if the list is not found or in case of an error.
     */
    public static boolean deleteRestaurantListFromUser(UserDTO user, String title) {
        try {
            // Find the user document
            Document userDocument = userCollection.find(eq("username", user.getUsername())).first();

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
                userCollection.replaceOne(eq("username", user.getUsername()), userDocument);
                return true;
            } else {
                System.err.println("ERROR: Reading list not found.");
                return false;
            }
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
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
            // Find the user document
            Document userDocument = userCollection.find(eq("username", user.getUsername())).first();

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return null;
            }

            // Get the restaurantsList field from the user document
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);

            // Create an ArrayList to store titles
            ArrayList<String> titles = new ArrayList<>();

            // Iterate over the restaurantLists and convert them to RestaurantsListDTO objects
            for (Document list : restaurantLists) {
                String title = list.getString("title");
                titles.add(title);
            }

            return titles;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
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

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return null;
            }

            // Get the restaurantsList field from the user document
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);

            // Iterate over the restaurantLists and find the list with the given title
            for (Document list : restaurantLists) {
                String listTitle = list.getString("title");
                if (listTitle.equals(title)) {
                    List<Document> restaurantsDocuments = list.getList("restaurants", Document.class);

                    // Convert the restaurantsDocuments to Restaurant objects
                    ArrayList<RestaurantDTO> restaurants = new ArrayList<>();
                    if (restaurantsDocuments != null) {
                        for (Document restaurantDoc : restaurantsDocuments) {
                            // Extract the necessary fields from the restaurantDoc and create a Restaurant object
                            String restaurantId = restaurantDoc.getString("restaurant_id");
                            String restaurantName = restaurantDoc.getString("restaurant_name");

                            RestaurantDTO restaurant = new RestaurantDTO();
                            restaurant.setId(restaurantId);
                            restaurant.setName(restaurantName);

                            restaurants.add(restaurant);
                        }
                    }

                    // Create a RestaurantsListDTO object and add it to the ArrayList
                    return new RestaurantsListDTO(listTitle, restaurants);
                }
            }

            // If the list with the given title is not found, return an empty restaurant list
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

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return false;
            }

            // Get the restaurantsList field from the user document
            List<Document> restaurantLists = userDocument.getList("restaurantsList", Document.class);

            // Iterate over the restaurantLists and find the list with the given title
            for (Document list : restaurantLists) {
                String listTitle = list.getString("title");
                if (listTitle.equals(title)) {
                    List<Document> restaurantsDocuments = list.getList("restaurants", Document.class);

                    // Check if the restaurant already exists in the list
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

                    // Update the user document in the database
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
     * Removes a restaurant from a restaurantsList of a user.
     *
     * @param user         The UserDTO object representing the user.
     * @param title        The title of the restaurantsList.
     * @param restaurantId The ID of the restaurant to remove.
     * @return true if the restaurant is successfully removed, false otherwise (e.g., user not found, restaurantsList not found, restaurant not found).
     */
    public static boolean removeRestaurantFromList(UserDTO user, String title, String restaurantId) {
        try {
            // Find the user document
            Document userDocument = userCollection.find(eq("username", user.getUsername())).first();

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
            userCollection.replaceOne(eq("username", user.getUsername()), userDocument);

            return true;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
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
            // Find the user document
            Document userDocument = userCollection.find(eq("username", user.getUsername())).first();

            // Check if the user document exists
            if (userDocument == null) {
                System.err.println("ERROR: User not found.");
                return null;
            }

            // retrieve reservations list
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



