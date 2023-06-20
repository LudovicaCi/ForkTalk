package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.UserDTO;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils.Utility.*;
import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils.Utility.unpackOneCoordinates;


public class RestaurantDAO extends DriverDAO {

    /**
     * Method for restaurant login
     *
     * @param username Username of the restaurant.
     * @param password Password of the restaurant.
     * @return RestaurantDTO object if the login is successful, otherwise null.
     */
    public static RestaurantDTO loginRestaurant(String username, String password) {
        RestaurantDTO rest = new RestaurantDTO();
        try {
            // Create a filter to match the username and password
            Bson filter = Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("password", password));

            // Use the filter to find a matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Check if a matching restaurant document was found
            if (restaurantDocument != null) {
                // Extract the necessary fields from the document to create a RestaurantDTO object
                rest.setId(restaurantDocument.getString("rest_id"));
                rest.setName(restaurantDocument.getString("restaurant_name"));
                rest.setEmail(restaurantDocument.getString("email"));
                rest.setUsername(restaurantDocument.getString("username"));
                rest.setPassword(restaurantDocument.getString("password"));
                rest.setCountry(restaurantDocument.getString("country"));
                rest.setCounty(restaurantDocument.getString("county"));
                rest.setDistrict(restaurantDocument.getString("district"));
                rest.setCity(restaurantDocument.getString("city"));
                rest.setAddress(restaurantDocument.getString("address"));
                rest.setStreetNumber(String.valueOf(restaurantDocument.getInteger("street_number")));
                rest.setPostCode(restaurantDocument.getString("postcode"));
                Integer price = restaurantDocument.getInteger("price");
                rest.setPrice(price != null ? price : 0); // o assegna un valore di default appropriato
                rest.setFeatures((ArrayList<String>) restaurantDocument.getList("tag", String.class));
                rest.setLocation((ArrayList<String>) restaurantDocument.getList("location", String.class));
                Integer rating = restaurantDocument.getInteger("rest_rating");
                rest.setRating(rating != null ? rating : 0);

                //retrieve coordinates
                List<Document> coordinatesDocuments = restaurantDocument.getList("coordinates", Document.class);

                if (coordinatesDocuments != null) {
                    for (Document doc : coordinatesDocuments) {
                        rest.getCoordinates().addAll(unpackOneCoordinates(doc));
                    }
                }

                //retrieve reservations
                List<Document> reservationsDocuments = restaurantDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        rest.getReservations().add(unpackOneRestaurantReservation(doc));
                    }
                }

                //retrieve reviews
                List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

                if (reviewsDocuments != null) {
                    for (Document doc : reviewsDocuments) {
                        rest.getReviews().add(unpackOneReview(doc));
                    }
                }

                return rest;
            } else {
                return null;
            }
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
            System.out.println("Username and Password are invalid");
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Method for adding a new restaurant to the collection.
     *
     * @param restaurant The RestaurantDTO object containing the restaurant details.
     * @return True if the restaurant is added successfully, false if the username already exists.
     */
    public static boolean addRestaurant(RestaurantDTO restaurant) {
        try {
            // Create a new document with the restaurant data
            Document restaurantDocument = new Document();
            restaurantDocument.append("rest_id", restaurant.getId())
                    .append("restaurant_name", restaurant.getName())
                    .append("email", restaurant.getEmail())
                    .append("username", restaurant.getUsername())
                    .append("password", restaurant.getPassword())
                    .append("country", restaurant.getCountry())
                    .append("county", restaurant.getCounty())
                    .append("district", restaurant.getDistrict())
                    .append("city", restaurant.getCity())
                    .append("address", restaurant.getAddress())
                    .append("street_number", Integer.parseInt(restaurant.getStreetNumber()))
                    .append("postcode", restaurant.getPostCode())
                    .append("price", restaurant.getPrice())
                    .append("tag", restaurant.getFeatures())
                    .append("location", restaurant.getLocation())
                    .append("rest_rating", restaurant.getRating())
                    .append("coordinates", packCoordinates(restaurant.getCoordinates()));

            // Insert the restaurant document into the collection
            restaurantCollection.insertOne(restaurantDocument);

            System.out.println("Ristorante aggiunto con successo.");
            return true;
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
            System.out.println("Si è verificato un errore durante l'aggiunta del ristorante.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a restaurant with the specified changes.
     *
     * @param restaurant The RestaurantDTO object containing the updated restaurant data.
     * @return true if the restaurant was successfully updated, false otherwise.
     */
    public static boolean updateRestaurant(RestaurantDTO restaurant) {
        try {
            // Create a filter to match the restaurant ID
            Bson filter = Filters.eq("rest_id", restaurant.getId());

            // Create an update document with the changed values
            Document updateDocument = new Document();

            if (restaurant.getName() != null) {
                updateDocument.append("restaurant_name", restaurant.getName());
            }
            if (restaurant.getEmail() != null) {
                updateDocument.append("email", restaurant.getEmail());
            }
            if (restaurant.getUsername() != null) {
                updateDocument.append("username", restaurant.getUsername());
            }
            if (restaurant.getPassword() != null) {
                updateDocument.append("password", restaurant.getPassword());
            }
            if (restaurant.getCountry() != null) {
                updateDocument.append("country", restaurant.getCountry());
            }
            if (restaurant.getCounty() != null) {
                updateDocument.append("county", restaurant.getCounty());
            }
            if (restaurant.getDistrict() != null) {
                updateDocument.append("district", restaurant.getDistrict());
            }
            if (restaurant.getCity() != null) {
                updateDocument.append("city", restaurant.getCity());
            }
            if (restaurant.getAddress() != null) {
                updateDocument.append("address", restaurant.getAddress());
            }
            if (restaurant.getStreetNumber() != null) {
                updateDocument.append("street_number", Integer.parseInt(restaurant.getStreetNumber()));
            }
            if (restaurant.getPostCode() != null) {
                updateDocument.append("postcode", restaurant.getPostCode());
            }
            if (restaurant.getPrice() > 0) {
                updateDocument.append("price", restaurant.getPrice());
            }
            if (restaurant.getFeatures() != null) {
                updateDocument.append("tag", restaurant.getFeatures());
            }
            if (restaurant.getLocation() != null) {
                updateDocument.append("location", restaurant.getLocation());
            }
            if (restaurant.getCoordinates() != null) {
                updateDocument.append("coordinates", packCoordinates(restaurant.getCoordinates()));
            }

            // Perform the update operation
            UpdateResult updateResult = restaurantCollection.updateOne(filter, new Document("$set", updateDocument));

            // Check the result of the update
            if (updateResult.getModifiedCount() > 0) {
                System.out.println("Restaurant updated successfully.");
                return true;
            } else {
                System.out.println("No restaurant found with the specified ID.");
                return false;
            }
        } catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
            System.out.println("An error occurred while updating the restaurant.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a restaurant based on the specified username.
     *
     * @param username The username of the restaurant to be deleted.
     * @return true if the restaurant was successfully deleted, false otherwise.
     */
    public static boolean deleteRestaurantByUsername(String username) {
        try {
            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Delete the restaurant document that matches the filter
            DeleteResult deleteResult = restaurantCollection.deleteOne(filter);

            // Check the result of the deletion
            if (deleteResult.getDeletedCount() > 0) {
                System.out.println("Restaurant deleted successfully.");
                return true;
            } else {
                System.out.println("No restaurant found with the specified username.");
                return false;
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while deleting the restaurant.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of restaurants based on the specified name.
     *
     * @param name The name of the restaurants to be retrieved.
     * @return A list of RestaurantDTO objects matching the specified name, or null if an error occurs.
     */
    public static List<RestaurantDTO> getRestaurantsByName(String name) {
        List<RestaurantDTO> restaurants = new ArrayList<>();
        try {
            // Create a filter to match the restaurant name
            Bson filter = Filters.eq("restaurant_name", name);

            // Use the filter to find matching restaurant documents in the collection
            FindIterable<Document> restaurantDocuments = restaurantCollection.find(filter);

            // Iterate over the restaurant documents
            for (Document restaurantDocument : restaurantDocuments) {
                RestaurantDTO rest = new RestaurantDTO();

                // Extract the necessary fields from the document to create a RestaurantDTO object
                rest.setId(restaurantDocument.getString("rest_id"));
                rest.setName(restaurantDocument.getString("restaurant_name"));
                rest.setEmail(restaurantDocument.getString("email"));
                rest.setUsername(restaurantDocument.getString("username"));
                rest.setPassword(restaurantDocument.getString("password"));
                rest.setCountry(restaurantDocument.getString("country"));
                rest.setCounty(restaurantDocument.getString("county"));
                rest.setDistrict(restaurantDocument.getString("district"));
                rest.setCity(restaurantDocument.getString("city"));
                rest.setAddress(restaurantDocument.getString("address"));
                rest.setStreetNumber(String.valueOf(restaurantDocument.getInteger("street_number")));
                rest.setPostCode(restaurantDocument.getString("postcode"));
                Integer price = restaurantDocument.getInteger("price");
                rest.setPrice(price != null ? price : 0); // o assegna un valore di default appropriato
                rest.setFeatures((ArrayList<String>) restaurantDocument.getList("tag", String.class));
                rest.setLocation((ArrayList<String>) restaurantDocument.getList("location", String.class));
                Integer rating = restaurantDocument.getInteger("rest_rating");
                rest.setRating(rating != null ? rating : 0);

                // Retrieve coordinates
                List<Document> coordinatesDocuments = restaurantDocument.getList("coordinates", Document.class);

                if (coordinatesDocuments != null) {
                    for (Document doc : coordinatesDocuments) {
                        rest.getCoordinates().addAll(unpackOneCoordinates(doc));
                    }
                }

                // Retrieve reservations
                List<Document> reservationsDocuments = restaurantDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        rest.getReservations().add(unpackOneRestaurantReservation(doc));
                    }
                }

                // Retrieve reviews
                List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

                if (reviewsDocuments != null) {
                    for (Document doc : reviewsDocuments) {
                        rest.getReviews().add(unpackOneReview(doc));
                    }
                }

                restaurants.add(rest);
            }

            return restaurants;
        } catch (MongoException e) {
            System.out.println("An error occurred while retrieving the restaurants");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a restaurant based on the specified username.
     *
     * @param username The username of the restaurant to be retrieved.
     * @return A RestaurantDTO object matching the specified username, or null if no matching restaurant is found or an error occurs.
     */
    public static RestaurantDTO getRestaurantByUsername(String username) {
        RestaurantDTO rest = new RestaurantDTO();
        try {
            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Use the filter to find a matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Check if a matching restaurant document was found
            if (restaurantDocument != null) {
                // Extract the necessary fields from the document to create a RestaurantDTO object
                rest.setId(restaurantDocument.getString("rest_id"));
                rest.setName(restaurantDocument.getString("restaurant_name"));
                rest.setEmail(restaurantDocument.getString("email"));
                rest.setUsername(restaurantDocument.getString("username"));
                rest.setPassword(restaurantDocument.getString("password"));
                rest.setCountry(restaurantDocument.getString("country"));
                rest.setCounty(restaurantDocument.getString("county"));
                rest.setDistrict(restaurantDocument.getString("district"));
                rest.setCity(restaurantDocument.getString("city"));
                rest.setAddress(restaurantDocument.getString("address"));
                rest.setStreetNumber(String.valueOf(restaurantDocument.getInteger("street_number")));
                rest.setPostCode(restaurantDocument.getString("postcode"));
                Integer price = restaurantDocument.getInteger("price");
                rest.setPrice(price != null ? price : 0); // o assegna un valore di default appropriato
                rest.setFeatures((ArrayList<String>) restaurantDocument.getList("tag", String.class));
                rest.setLocation((ArrayList<String>) restaurantDocument.getList("location", String.class));
                Integer rating = restaurantDocument.getInteger("rest_rating");
                rest.setRating(rating != null ? rating : 0);

                // Retrieve coordinates
                List<Document> coordinatesDocuments = restaurantDocument.getList("coordinates", Document.class);

                if (coordinatesDocuments != null) {
                    for (Document doc : coordinatesDocuments) {
                        rest.getCoordinates().addAll(unpackOneCoordinates(doc));
                    }
                }

                // Retrieve reservations
                List<Document> reservationsDocuments = restaurantDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        rest.getReservations().add(unpackOneRestaurantReservation(doc));
                    }
                }

                // Retrieve reviews
                List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

                if (reviewsDocuments != null) {
                    for (Document doc : reviewsDocuments) {
                        rest.getReviews().add(unpackOneReview(doc));
                    }
                }

                return rest;
            } else {
                return null; // Return null if no matching restaurant document was found
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while retrieving the restaurant");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the reviews of a restaurant based on the specified username.
     *
     * @param username The username of the restaurant to be retrieved.
     * @return An ArrayList of reviews matching the specified username, or null if no matching restaurant is found or an error occurs.
     */
    public static ArrayList<ReviewDTO> getReviews(String username){
        try{
            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Use the filter to find a matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Retrieve reviews
            assert restaurantDocument != null;
            List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

            if (reviewsDocuments != null) {
                ArrayList<ReviewDTO> reviews = new ArrayList<>();
                for (Document doc : reviewsDocuments) {
                    reviews.add(unpackOneReview(doc));
                }
                return reviews;
            }
        }catch (MongoException e){
            System.out.println("An error occurred while retrieving the reviews of the given restaurant");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Writes a new review to the "reviews" key of a document in the collection, preserving existing reviews.
     *
     * @param review   The review to be written.
     * @param username The username of the user associated with the restaurant document.
     * @return True if the review was written successfully, false otherwise.
     */
    public static boolean writeReview(ReviewDTO review, String username) {
        try {
            // Create a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Find the matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Retrieve the existing reviews from the restaurant document
            assert restaurantDocument != null;
            List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

            // Create a new document for the review
            Document reviewDoc = packOneReview(review);

            // Add the new review to the existing reviews
            reviewsDocuments.add(reviewDoc);

            if(restaurantDocument.get("rest_rating") == null){
                restaurantDocument.append("rest_rating", review.getRating());
            }else{
                System.out.println((int)restaurantDocument.get("rest_rating"));
                System.out.println(review.getRating());
                System.out.println(reviewsDocuments.size());
                int newRate = (int)(((int)restaurantDocument.get("rest_rating") + review.getRating()) / reviewsDocuments.size());
                restaurantDocument.append("rest_rating", newRate);
            }

            // Update the restaurant document with the updated reviews list
            restaurantDocument.append("reviews", reviewsDocuments);

            // Perform the update operation to update the restaurant document in the collection
            UpdateResult result = restaurantCollection.updateOne(filter, new Document("$set", restaurantDocument));

            // Check if the update was successful
            if (result.getModifiedCount() > 0) {
                System.out.println("Review added successfully.");
                return true;
            } else {
                System.out.println("No matching restaurant document found.");
                return false;
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while writing the new review");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the reservations for a given restaurant.
     *
     * @param rest The RestaurantDTO object representing the restaurant.
     * @return An ArrayList of ReservationDTO objects representing the reservations of the restaurant,
     *         or null if the restaurant is not found or an error occurs while retrieving the reservations.
     */
    public static ArrayList<ReservationDTO> getReservations(RestaurantDTO rest) {
        try {
            // Find the user document
            Document restDocument = restaurantCollection.find(Filters.eq("username", rest.getUsername())).first();

            // Check if the user document exists
            if (restDocument == null) {
                System.err.println("ERROR: Restaurant not found.");
                return null;
            }

            // retrieve reservations list
            List<Document> reservationsDocuments = restDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> result = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    result.add(unpackOneRestaurantReservation(doc));
                }
            }
            return result;
        } catch (MongoException e) {
            System.err.println("ERROR: An error occurred while retrieving reservations.");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> getFreeSlotsByDate(RestaurantDTO rest, String date) {
        try {
            if(rest == null) {
                System.out.println("Restaurant object is null");
                return null;
            }

            // Find the user document
            Document restDocument = restaurantCollection.find(Filters.eq("username", rest.getUsername())).first();

            // Check if the user document exists
            if (restDocument == null) {
                System.err.println("ERROR: Restaurant not found.");
                return null;
            }

            // retrieve reservations list
            List<Document> reservationsDocuments = restDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> reservationsList = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    reservationsList.add(unpackOneRestaurantReservation(doc));
                }
            }

            ArrayList<String> result = new ArrayList<>();

            for(ReservationDTO reservation: reservationsList){
                // Parsing della stringa in un oggetto LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(reservation.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Formattazione della data nel formato desiderato "yyyy-MM-dd"
                String dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // Formattazione del timeslot nel formato desiderato "HH:mm"
                String timeSlotString = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                if(dateString.equals(date)){
                    if(reservation.getClientUsername() == null){
                        result.add(timeSlotString);
                    }
                }
            }
            return result;
        } catch (MongoException e) {
            System.err.println("ERROR: An error occurred while retrieving reservations.");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ReservationDTO> getReservationByDate(RestaurantDTO rest, String date) {
        try {
            if(rest == null) {
                System.out.println("Restaurant object is null");
                return null;
            }

            // Find the user document
            Document restDocument = restaurantCollection.find(Filters.eq("username", rest.getUsername())).first();

            // Check if the user document exists
            if (restDocument == null) {
                System.err.println("ERROR: Restaurant not found.");
                return null;
            }

            // retrieve reservations list
            List<Document> reservationsDocuments = restDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> reservationsList = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    reservationsList.add(unpackOneRestaurantReservation(doc));
                }
            }

            ArrayList<ReservationDTO> result = new ArrayList<>();

            for(ReservationDTO reservation: reservationsList){
                // Parsing della stringa in un oggetto LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(reservation.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Formattazione della data nel formato desiderato "yyyy-MM-dd"
                String dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if(dateString.equals(date)){
                    if(reservation.getClientUsername() != null){
                        result.add(reservation);
                    }
                }
            }
            return result;
        } catch (MongoException e) {
            System.err.println("ERROR: An error occurred while retrieving reservations.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the maximum number of clients for a given restaurant.
     *
     * @param numberOfClient The maximum number of clients to be set.
     * @param rest           The RestaurantDTO object representing the restaurant.
     * @return true if the maximum number of clients is successfully set, false otherwise.
     */
    public static boolean setMaxClient(int numberOfClient, RestaurantDTO rest){
        try{
            // Insert the new reading_list
            restaurantCollection.updateOne(
                    Filters.eq("username", rest.getUsername()),
                    new Document().append(
                            "$set",
                            new Document("max_number_of_client", numberOfClient)
                    )
            );

            return true;
        }catch (MongoException e){
            System.out.println("ERROR: An error occurred while setting the maximum number of client");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds free slots to the reservations of a given restaurant.
     *
     * @param rest The RestaurantDTO object representing the restaurant.
     * @return true if the free slots are successfully added, false otherwise.
     */
    public static boolean addFreeSlot(RestaurantDTO rest) {
        try {
            ArrayList<String> timeSlotToAdd = new ArrayList<>();
            ArrayList<String> availableTimeSlot = new ArrayList<>();

            // Generating available time slots
            for (int hour = 8; hour <= 23; hour++) {
                availableTimeSlot.add(String.format("%02d:00", hour));
                availableTimeSlot.add(String.format("%02d:30", hour));
            }

            System.out.println("Slots available:");
            for (String time : availableTimeSlot) {
                System.out.println(time);
            }

        Scanner scanner = new Scanner(System.in);

            // Getting the number of slots to add
            System.out.println("Enter how many slots you want to add");
            int numberOfSlots = scanner.nextInt();

            // Adding selected slots
            for (int i = 0; i < numberOfSlots; i++) {
                System.out.println("Slots available:");
                for (String time : availableTimeSlot) {
                    System.out.println(time);
                }
                System.out.println("Enter the time slot you want to add");
                String timeSlot = scanner.next();

                if (availableTimeSlot.contains(timeSlot)) {
                    timeSlotToAdd.add(timeSlot);
                    availableTimeSlot.remove(timeSlot);
                } else {
                    System.out.println("ERROR: Time Slot not valid or available");
                }
            }

            // Getting the date for adding new slots
            System.out.println("Enter the date in which you want to add the new slots in yyyy-MM-dd");
            String inputDate = scanner.next();

            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate currentDate = LocalDate.now();
            LocalDate enteredDate = LocalDate.parse(inputDate, dateFormatter);

            // Checking if the entered date is after the current date
            if (enteredDate.isAfter(currentDate)) {
                ArrayList<ReservationDTO> newReservationsList = new ArrayList<>();

                for(String slot: timeSlotToAdd){
                    String newDate = enteredDate.toString() + " " + slot + ":00";
                    newReservationsList.add(new ReservationDTO(newDate));
                }

                ArrayList<Document> newReservationDocs = packRestaurantReservations(newReservationsList);

                // Create a filter to match the username
                Bson filter = Filters.eq("username", rest.getUsername());

                // Find the matching restaurant document in the collection
                Document restaurantDocument = restaurantCollection.find(filter).first();

                // Retrieve the existing reservations from the restaurant document
                assert restaurantDocument != null;
                List<Document> reservationsDoc = restaurantDocument.getList("reservations", Document.class);

                reservationsDoc.addAll(newReservationDocs);

                // Update the restaurant document with the updated reservation list
                restaurantDocument.append("reservations", reservationsDoc);

                // Perform the update operation to update the restaurant document in the collection
                UpdateResult result = restaurantCollection.updateOne(filter, new Document("$set", restaurantDocument));

                // Check if the update was successful
                if (result.getModifiedCount() > 0) {
                    System.out.println("Time Slots added successfully.");
                    return true;
                } else {
                    System.out.println("No matching restaurant document found.");
                    return false;
                }
            } else {
                System.out.println("ERROR: The date must be after the current date.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("ERROR: An error occurred while adding FreeSlots");
            e.printStackTrace();
            return false;
        }
    }

    //TODO: write delete review method
}






