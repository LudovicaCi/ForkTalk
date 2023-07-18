package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;


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

            // Find a matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Check if a matching restaurant document was found
            if (restaurantDocument != null) {
                // Extract the fields from the matching document
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
                Object ratingObj = restaurantDocument.get("rest_rating");
                double rating;
                if(ratingObj == null)
                    rating = 0.0;
                else
                    rating = Double.parseDouble(String.valueOf(ratingObj));
                rest.setRating(rating);

                List<Document> coordinatesDocuments = restaurantDocument.getList("coordinates", Document.class);

                if (coordinatesDocuments != null) {
                    for (Document doc : coordinatesDocuments) {
                        rest.getCoordinates().addAll(Utility.unpackOneCoordinates(doc));
                    }
                }

                List<Document> reservationsDocuments = restaurantDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        rest.getReservations().add(Utility.unpackOneRestaurantReservation(doc));
                    }
                }

                List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

                if (reviewsDocuments != null) {
                    for (Document doc : reviewsDocuments) {
                        rest.getReviews().add(Utility.unpackOneReview(doc));
                    }
                }

                return rest;
            } else {
                return null;
            }
        } catch (MongoException e) {
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
                    .append("reviews", new ArrayList<>())
                    .append("coordinates", Utility.packCoordinates(restaurant.getCoordinates()));

            restaurantCollection.insertOne(restaurantDocument);

            System.out.println("Restaurant added successfully.");
            return true;
        } catch (MongoException e) {
            System.out.println("\"An error occurred while adding the restaurant.\".");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a restaurant in the collection
     *
     * @param restaurant The RestaurantDTO object containing the updated restaurant data.
     * @return true if the restaurant was successfully updated, false otherwise.
     */
    public static boolean updateRestaurant(RestaurantDTO restaurant) {
        try {
            // Creates a filter to match the id of the restaurant
            Bson filter = Filters.eq("rest_id", restaurant.getId());

            // Creates an update document with the changed values
            Document updateDocument = new Document();

            // Checks if the field contains a non-null value that corresponds to the updated value
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
            if (restaurant.getFeatures().size() != 0) {
                updateDocument.append("tag", restaurant.getFeatures());
            }
            if (restaurant.getLocation() != null) {
                updateDocument.append("location", restaurant.getLocation());
            }

            // Performs the update operation
            UpdateResult updateResult = restaurantCollection.updateOne(filter, new Document("$set", updateDocument));

            // Checks the result of the update
            if (updateResult.getModifiedCount() > 0) {
                System.out.println("Restaurant updated successfully.");
                return true;
            } else {
                System.out.println("No restaurant found with the specified ID.");
                return false;
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while updating the restaurant.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a restaurant from the collection.
     *
     * @param rest The restaurant to be deleted.
     * @return true if the restaurant was successfully deleted, false otherwise.
     */
    public static boolean deleteRestaurant(RestaurantDTO rest) {
        try {
            for(ReservationDTO reservation : rest.getReservations()){
                UserDTO user = UserDAO.getUserByUsername(reservation.getClientUsername());
                assert user != null;
                if(!ReservationDAO.deleteReservation(user, rest, reservation))
                    return false;
            }

            // Creates a filter to match the username
            Bson filter = Filters.eq("rest_id", rest.getId());

            // Deletes the restaurant document that matches the filter
            DeleteResult deleteResult = restaurantCollection.deleteOne(filter);

            // Checks the result of the deletion
            if (deleteResult.getDeletedCount() > 0) {
                System.out.println("Restaurant deleted successfully.");
                return true;
            } else {
                System.out.println("No restaurant.");
                return false;
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while deleting the restaurant.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recovers a restaurant from a RestaurantDTO object and its associated reservations.
     *
     * @param rest The RestaurantDTO object representing the restaurant to recover.
     */
    public static void recoverRestaurant(RestaurantDTO rest){
        try{
            // Converts a RestaurantDTO obeject to a MongoDB Document
            Document restDoc = Utility.packRestaurantDTO(rest);

            // Inserts the document into the MongoDB collection
            InsertOneResult insertOneResult = restaurantCollection.insertOne(restDoc);
            if(insertOneResult.wasAcknowledged()) {
                // Processes each reservation related to the restaurant
                for (ReservationDTO reservation : rest.getReservations()) {
                    // Retrieves the user associated with the reservation
                    UserDTO user = UserDAO.getUserByUsername(reservation.getClientUsername());

                    String[] parts = reservation.getDate().split(" ");
                    String date = parts[0];
                    String time = parts[1].substring(0, 5);

                    // Makes the reservation
                    assert user != null;
                    if (!ReservationDAO.makeReservation(user, rest, date, time, reservation.getPeople()))
                        return;
                }
            }
        }catch (MongoException e){
            e.printStackTrace();
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
            // Creates a filter to match the restaurant name
            Bson filter = Filters.eq("restaurant_name", name);

            // Uses the filter to find matching restaurant documents in the collection
            FindIterable<Document> restaurantDocuments = restaurantCollection.find(filter);

            // Iterate over the restaurant documents
            for (Document restaurantDocument : restaurantDocuments) {
                RestaurantDTO rest = new RestaurantDTO();

                // Extract the necessary fields from the document
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
                Double rating = Double.parseDouble(String.valueOf(restaurantDocument.get("rest_rating")));
                rest.setRating(rating);

                List<Document> coordinatesDocuments = restaurantDocument.getList("coordinates", Document.class);

                if (coordinatesDocuments != null) {
                    for (Document doc : coordinatesDocuments) {
                        rest.getCoordinates().addAll(Utility.unpackOneCoordinates(doc));
                    }
                }

                List<Document> reservationsDocuments = restaurantDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        rest.getReservations().add(Utility.unpackOneRestaurantReservation(doc));
                    }
                }

                List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

                if (reviewsDocuments != null) {
                    for (Document doc : reviewsDocuments) {
                        rest.getReviews().add(Utility.unpackOneReview(doc));
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
            // Creates a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Uses the filter to find a matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Checks if a matching restaurant document was found
            if (restaurantDocument != null) {
                // Extracts the necessary fields
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
                Double rating = Double.parseDouble(String.valueOf(restaurantDocument.get("rest_rating")));
                rest.setRating(rating);

                List<Document> coordinatesDocuments = restaurantDocument.getList("coordinates", Document.class);

                if (coordinatesDocuments != null) {
                    for (Document doc : coordinatesDocuments) {
                        rest.getCoordinates().addAll(Utility.unpackOneCoordinates(doc));
                    }
                }

                List<Document> reservationsDocuments = restaurantDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        rest.getReservations().add(Utility.unpackOneRestaurantReservation(doc));
                    }
                }

                List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

                if (reviewsDocuments != null) {
                    for (Document doc : reviewsDocuments) {
                        rest.getReviews().add(Utility.unpackOneReview(doc));
                    }
                }

                return rest;
            } else {
                return null;
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while retrieving the restaurant");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a restaurant based on the specified ID.
     *
     * @param restId The ID of the restaurant to be retrieved.
     * @return A RestaurantDTO object matching the specified username, or null if no matching restaurant is found or an error occurs.
     */
    public static RestaurantDTO getRestaurantById(String restId) {
        RestaurantDTO rest = new RestaurantDTO();
        try {
            // Creates a filter to match the username
            Bson filter = Filters.eq("rest_id", restId);

            // Uses the filter to find a matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Checks if a matching restaurant document was found
            if (restaurantDocument != null) {
                // Extracts the necessary fields from the document
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
                rest.setPrice(price != null ? price : 0);
                rest.setFeatures((ArrayList<String>) restaurantDocument.getList("tag", String.class));
                rest.setLocation((ArrayList<String>) restaurantDocument.getList("location", String.class));
                //Double rating = Double.parseDouble(String.valueOf(restaurantDocument.get("rest_rating")));
                Object ratingObj = restaurantDocument.get("rest_rating");
                double rating;
                if(ratingObj == null)
                    rating = 0.0;
                else
                    rating = Double.parseDouble(String.valueOf(ratingObj));
                rest.setRating(rating);

                List<Document> coordinatesDocuments = restaurantDocument.getList("coordinates", Document.class);

                if (coordinatesDocuments != null) {
                    for (Document doc : coordinatesDocuments) {
                        rest.getCoordinates().addAll(Utility.unpackOneCoordinates(doc));
                    }
                }

                List<Document> reservationsDocuments = restaurantDocument.getList("reservations", Document.class);

                if (reservationsDocuments != null) {
                    for (Document doc : reservationsDocuments) {
                        rest.getReservations().add(Utility.unpackOneRestaurantReservation(doc));
                    }
                }

                List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

                if (reviewsDocuments != null) {
                    for (Document doc : reviewsDocuments) {
                        rest.getReviews().add(Utility.unpackOneReview(doc));
                    }
                }

                return rest;
            } else {
                return null;
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while retrieving the restaurant");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a restaurant document from the database using the restaurant ID.
     *
     * @param restId the ID of the restaurant to retrieve the document for
     * @return the restaurant document corresponding to the specified ID, or null if no document is found
     */
    public static Document getRestaurantDocumentById(String restId){
        try {
            // Creates a filter to match the username
            Bson filter = Filters.eq("rest_id", restId);

            return restaurantCollection.find(filter).first();
        }catch (MongoException e){
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
            // Creates a filter to match the username
            Bson filter = Filters.eq("username", username);

            // Uses the filter to find a matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Retrieves reviews
            assert restaurantDocument != null;
            List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

            if (reviewsDocuments != null) {
                ArrayList<ReviewDTO> reviews = new ArrayList<>();
                for (Document doc : reviewsDocuments) {
                    reviews.add(Utility.unpackOneReview(doc));
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
     * Add a new review to the "reviews" key of a document in the collection.
     *
     * @param review   The review to be written.
     * @param restId The id of the restaurant associated with the restaurant document.
     * @return True if the review was written successfully, false otherwise.
     */
    public static boolean writeReview(ReviewDTO review, String restId) {
        try {
            // Creates a filter to match the username
            Bson filter = Filters.eq("rest_id", restId);

            // Finds the matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Retrieves the existing reviews from the restaurant document
            assert restaurantDocument != null;
            List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

            // Assigns unique id to the review
            review.setId(Utility.generateUniqueReviewId(restaurantDocument.getString("rest_id"), reviewsDocuments));

            // Creates a new document for the review
            Document reviewDoc = Utility.packOneReview(review);

            // Adds the new review to the existing reviews
            reviewsDocuments.add(reviewDoc);

            if(restaurantDocument.get("rest_rating") == null){
                restaurantDocument.append("rest_rating", review.getRating());
            }else{
                System.out.println(restaurantDocument.get("rest_rating"));
                System.out.println(review.getRating());
                System.out.println(reviewsDocuments.size());
                double newReviewRate = review.getRating();
                double restRate = Double.parseDouble(String.valueOf(restaurantDocument.get("rest_rating"))) * (reviewsDocuments.size() - 1);
                double newRate = (restRate + newReviewRate) / reviewsDocuments.size();
                double roundedFinalRating = Math.round(newRate * 2) /2.0;
                restaurantDocument.append("rest_rating", roundedFinalRating);
            }

            // Updates the restaurant document with the updated reviews list
            restaurantDocument.append("reviews", reviewsDocuments);

            // Performs the update operation to update the restaurant document in the collection
            UpdateResult result = restaurantCollection.updateOne(filter, new Document("$set", restaurantDocument));

            // Checks if the update was successful
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
     * Deletes a review given an ID and updates the rest_rating of the restaurant.
     *
     * @param id the ID of the review to delete
     * @return true if the review is deleted successfully, false otherwise
     */
    public static boolean deleteReviewById(String id) {
        try {
            // Creates a filter to match the review ID
            Bson filter = Filters.eq("reviews.review_id", id);

            // Finds the matching restaurant document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Retrieves the existing reviews from the restaurant document
            assert restaurantDocument != null;
            List<Document> reviewsDocuments = restaurantDocument.getList("reviews", Document.class);

            // Finds the review to delete and retrieve its rating
            int reviewRating = 0;
            for (Document reviewDoc : reviewsDocuments) {
                if (reviewDoc.getString("review_id").equals(id)) {
                    reviewRating = reviewDoc.getInteger("review_rating", 0);
                    reviewsDocuments.remove(reviewDoc);
                    break;
                }
            }

            // Calculates the new rest_rating
            String restRatingString = String.valueOf(restaurantDocument.get("rest_rating"));
            double restRating = Double.parseDouble(restRatingString);
            double totalRating = restRating * reviewsDocuments.size();

            Double newRestRating = reviewsDocuments.isEmpty() ? null : (totalRating - reviewRating) / reviewsDocuments.size();
            Double roundedFinalRating = newRestRating == null ? null : Math.round(newRestRating * 2) /2.0;

            // Updates the restaurant document with the updated reviews list and rest_rating
            restaurantDocument.append("reviews", reviewsDocuments)
                    .append("rest_rating", roundedFinalRating);

            // Performs the update operation on the collection
            UpdateResult result = restaurantCollection.updateOne(filter, new Document("$set", restaurantDocument));

            // Checks if the update was successful
            if (result.getModifiedCount() > 0) {
                System.out.println("Review deleted successfully. Rest_rating updated.");
                return true;
            } else {
                System.out.println("No matching review found or error occurred while deleting the review.");
                return false;
            }
        } catch (MongoException e) {
            System.out.println("An error occurred while deleting the review");
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
            // Finds the user document
            Document restDocument = restaurantCollection.find(Filters.eq("username", rest.getUsername())).first();

            // Checks if the user document exists
            if (restDocument == null) {
                System.err.println("ERROR: Restaurant not found.");
                return null;
            }

            // Retrieves reservations list
            List<Document> reservationsDocuments = restDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> result = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    result.add(Utility.unpackOneRestaurantReservation(doc));
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
     * Retrieves a list of free reservation time slots for a specific date in a restaurant.
     *
     * @param rest the RestaurantDTO object representing the restaurant
     * @param date the date for which to retrieve the free time slots in the format "yyyy-MM-dd"
     * @return an ArrayList of Strings representing the free time slots for the specified date, or null if an error occurs
     */
    public static ArrayList<String> getFreeSlotsByDate(RestaurantDTO rest, String date) {
        try {
            if(rest == null) {
                System.out.println("Restaurant object is null");
                return null;
            }

            // Finds the user document
            Document restDocument = restaurantCollection.find(Filters.eq("username", rest.getUsername())).first();

            // Checks if the user document exists
            if (restDocument == null) {
                System.err.println("ERROR: Restaurant not found.");
                return null;
            }

            // Retrieves reservations
            List<Document> reservationsDocuments = restDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> reservationsList = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    reservationsList.add(Utility.unpackOneRestaurantReservation(doc));
                }
            }

            ArrayList<String> result = new ArrayList<>();

            for(ReservationDTO reservation: reservationsList){
                LocalDateTime dateTime = LocalDateTime.parse(reservation.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String timeSlotString = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                if(dateString.equals(date)){
                    if(reservation.getClientUsername() == null){
                        if(!result.contains(timeSlotString))
                            result.add(timeSlotString);
                    }
                }
            }

            Collections.sort(result);

            return result;
        } catch (MongoException e) {
            System.err.println("ERROR: An error occurred while retrieving reservations.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the reservations at a restaurant for a given date.
     *
     * @param rest The restaurant to retrieve reservations from.
     * @return An ArrayList of ReservationDTO objects representing the reservations.
     */
    public static ArrayList<ReservationDTO> getReservationByDateAndTime(RestaurantDTO rest) {
        try {
            if(rest == null) {
                System.out.println("Restaurant object is null");
                return null;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the date of the reservation that you want to retrieve yyyy-MM-dd:");
            String date = scanner.next();
            System.out.println("Enter the time of reservation HH:mm:");
            String time = scanner.next();

            date = date + " " + time + ":00";

            // Finds the user document
            Document restDocument = restaurantCollection.find(Filters.eq("username", rest.getUsername())).first();

            // Checks if the user document exists
            if (restDocument == null) {
                System.err.println("ERROR: Restaurant not found.");
                return null;
            }

            // Retrieves reservations list
            List<Document> reservationsDocuments = restDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> reservationsList = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    reservationsList.add(Utility.unpackOneRestaurantReservation(doc));
                }
            }

            ArrayList<ReservationDTO> result = new ArrayList<>();

            for(ReservationDTO reservation: reservationsList){
                if(reservation.getDate().equals(date)){
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
     * Gets the number of empty seats available at a restaurant for a given date.
     *
     * @param rest The restaurant to check for available seats.
     * @param date The date for which to check the availability.
     * @return The number of empty seats available.
     */
    public static int getEmptySeatsByDate(RestaurantDTO rest, String date){
        try {
            if(rest == null) {
                System.out.println("Restaurant object is null");
                return 0;
            }

            // Finds the user document
            Document restDocument = restaurantCollection.find(Filters.eq("username", rest.getUsername())).first();

            // Check if the user document exists
            if (restDocument == null) {
                System.err.println("ERROR: Restaurant not found.");
                return 0;
            }

            // Retrieves reservations
            List<Document> reservationsDocuments = restDocument.getList("reservations", Document.class);

            ArrayList<ReservationDTO> reservationsList = new ArrayList<>();
            if (reservationsDocuments != null) {
                for (Document doc : reservationsDocuments) {
                    reservationsList.add(Utility.unpackOneRestaurantReservation(doc));
                }
            }

            Integer totalSeatsAvailable = restDocument.getInteger("max_number_of_client");

            if (totalSeatsAvailable == null) {
                return 0;
            }


            for (ReservationDTO reservation : reservationsList) {
                LocalDateTime dateTime = LocalDateTime.parse(reservation.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (dateString.equals(date)) {
                    if (reservation.getClientUsername() != null) {
                        totalSeatsAvailable = totalSeatsAvailable - reservation.getPeople();
                    }
                }
            }

            return totalSeatsAvailable;

        } catch (MongoException e) {
            System.err.println("ERROR: An error occurred while retrieving reservations.");
            e.printStackTrace();
            return 0;
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
    public static boolean addFreeSlot(RestaurantDTO rest, int numberOfSlots, String timeSlot, String inputDate) {
        try {
            ArrayList<String> timeSlotToAdd = new ArrayList<>();
            ArrayList<String> availableTimeSlot = new ArrayList<>();

            // Generating possible available time slots
            for (int hour = 8; hour <= 23; hour++) {
                availableTimeSlot.add(String.format("%02d:00", hour));
                availableTimeSlot.add(String.format("%02d:30", hour));
            }

            // Adds selected slots
            for (int i = 0; i < numberOfSlots; i++) {
                if (availableTimeSlot.contains(timeSlot)) {
                    timeSlotToAdd.add(timeSlot);
                } else {
                    System.out.println("ERROR: Time Slot not valid or available");
                    return false;
                }
            }

            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate currentDate = LocalDate.now();
            LocalDate enteredDate = LocalDate.parse(inputDate, dateFormatter);

            // Checks if the entered date is after the current date
            if (enteredDate.isAfter(currentDate)) {
                ArrayList<ReservationDTO> newReservationsList = new ArrayList<>();

                for(String slot: timeSlotToAdd){
                    String newDate = enteredDate + " " + slot + ":00";
                    newReservationsList.add(new ReservationDTO(newDate));
                }

                ArrayList<Document> newReservationDocs = Utility.packRestaurantReservations(newReservationsList);

                // Creates a filter to match the username
                Bson filter = Filters.eq("username", rest.getUsername());

                // Finds the matching restaurant document in the collection
                Document restaurantDocument = restaurantCollection.find(filter).first();

                if(restaurantDocument == null){
                    System.out.println("Restaurant hasn't been found!");
                    return false;
                }

                // Checks if the capacity of the restaurant is set
                if(restaurantDocument.getInteger("max_number_of_client")==null) {
                    setMaxClient(20, rest);
                }

                // Retrieves the existing reservations from the restaurant document
                List<Document> reservationsDoc = restaurantDocument.getList("reservations", Document.class);
                if(reservationsDoc == null){
                    reservationsDoc = new ArrayList<>();
                }

                reservationsDoc.addAll(newReservationDocs);

                // Updates the restaurant document with the updated reservation list
                restaurantDocument.append("reservations", reservationsDoc);

                // Performs the update operation to update the restaurant document in the collection
                UpdateResult result = restaurantCollection.updateOne(filter, new Document("$set", restaurantDocument));

                // Checks if the update was successful
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

    /**
     * Deletes a free slot (without a reservation) from a specified restaurant.
     *
     * @param rest          The RestaurantDTO object representing the restaurant
     * @param dateToDelete  The date of the slot to be deleted
     * @return true if the slot is successfully deleted, false otherwise
     */
    public static boolean deleteFreeSlot(RestaurantDTO rest, String dateToDelete){
        try{
            Bson restFilter = Filters.eq("username", rest.getUsername());

            Document restDocument = restaurantCollection.find(restFilter).first();

            assert restDocument != null;
            List<Document> restReservationList = restDocument.getList("reservations", Document.class);

            if (restReservationList == null)
                return false;

            Iterator<Document> restIterator = restReservationList.iterator();
            while (restIterator.hasNext()) {
                Document doc = restIterator.next();
                if (doc.getString("date").equals(dateToDelete) && doc.getString("client_username") == null && doc.getString("client_name") == null && doc.getString("client_surname") == null && doc.getInteger("number of person") == 0) {
                    restIterator.remove();
                    break;
                }
            }

            restDocument.append("reservations", restReservationList);

            UpdateResult restResult = restaurantCollection.updateOne(restFilter, new Document("$set", restDocument));

            // Checks if the update was successful
            if (restResult.getModifiedCount() > 0) {
                System.out.println("The slot has been successfully removed!");
                return true;
            } else {
                System.out.println("The slot unfortunately could not be removed!");
                return false;
            }
        }catch (MongoException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Searches for restaurants based on the specified parameters.
     *
     * @param location  The location(s) to match. Can be a comma-separated list of locations.
     * @param name      The name of the restaurant(s) to match.
     * @param cuisine   The cuisine(s) to match. Can be a comma-separated list of cuisines.
     * @param keywords  The keyword(s) to match. Can be a comma-separated list of keywords.
     * @return A list of Document objects representing the matching restaurants, sorted by rating in descending order.
     *         Returns null if no criteria are provided or an error occurs.
     */
    /*public static List<Document> searchRestaurants(String location, String name, String cuisine, String keywords, String rating) {
        try {

            List<Bson> aggregationPipeline = new ArrayList<>();

            if (location != null && !location.isEmpty()) {
                String[] locationArray = location.split(",");
                Bson matchLocation = Aggregates.match(Filters.in("location", Arrays.asList(locationArray)));
                aggregationPipeline.add(matchLocation);
            }

            if (name != null && !name.isEmpty()) {
                Bson matchName = Aggregates.match(Filters.regex("restaurant_name", Pattern.quote(name), "i"));
                aggregationPipeline.add(matchName);
            }

            if (cuisine != null && !cuisine.isEmpty()) {
                String[] cusineArray = cuisine.split(",");
                Bson matchCuisine = Aggregates.match(Filters.in("tag", Arrays.asList(cusineArray)));
                aggregationPipeline.add(matchCuisine);
            }

            if (keywords != null && !keywords.isEmpty()) {
                String[] keywordArray = keywords.split(",");
                Bson matchKeywords = Aggregates.match(Filters.in("tag", Arrays.asList(keywordArray)));
                aggregationPipeline.add(matchKeywords);
            }

            if (rating != null && !rating.isEmpty()) {
                double minRating = Double.parseDouble(rating);
                Bson matchRating = Aggregates.match(Filters.gte("rest_rating", minRating));
                aggregationPipeline.add(matchRating);
            }

            if (location != null && !location.isEmpty() || name != null && !name.isEmpty() || cuisine != null && !cuisine.isEmpty() || keywords != null && !keywords.isEmpty()) {
                Bson addTotalReviews = Aggregates.addFields(
                        new Field<>("totalReviews", new Document("$size", "$reviews"))
                );
                aggregationPipeline.add(addTotalReviews);
            }

            if (aggregationPipeline.isEmpty()) {
                Bson matchDummy = Aggregates.match(new Document());
                aggregationPipeline.add(matchDummy);
            }

            Bson sortByRating = Sorts.orderBy(
                    Sorts.descending("rest_rating"),
                    Sorts.descending("totalReviews"),
                    Sorts.ascending("restaurant_name")
            );

            aggregationPipeline.add(Aggregates.sort(sortByRating));

            return restaurantCollection.aggregate(aggregationPipeline).into(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("An error occurred while searching for restaurants");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }*/

    public static List<Document> searchRestaurants(String location, String name, String cuisine, String keywords, String rating, int page, int pageSize) {
        try {
            List<Bson> aggregationPipeline = new ArrayList<>();

            if (location != null && !location.isEmpty()) {
                String[] locationArray = location.split(",");
                Bson matchLocation = Aggregates.match(Filters.in("location", Arrays.asList(locationArray)));
                aggregationPipeline.add(matchLocation);
            }

            if (name != null && !name.isEmpty()) {
                Bson matchName = Aggregates.match(Filters.regex("restaurant_name", Pattern.quote(name), "i"));
                aggregationPipeline.add(matchName);
            }

            if (cuisine != null && !cuisine.isEmpty()) {
                String[] cuisineArray = cuisine.split(",");
                Bson matchCuisine = Aggregates.match(Filters.in("tag", Arrays.asList(cuisineArray)));
                aggregationPipeline.add(matchCuisine);
            }

            if (keywords != null && !keywords.isEmpty()) {
                String[] keywordArray = keywords.split(",");
                Bson matchKeywords = Aggregates.match(Filters.in("tag", Arrays.asList(keywordArray)));
                aggregationPipeline.add(matchKeywords);
            }

            if (rating != null && !rating.isEmpty()) {
                double minRating = Double.parseDouble(rating);
                Bson matchRating = Aggregates.match(Filters.gte("rest_rating", minRating));
                aggregationPipeline.add(matchRating);
            }

            if (location != null && !location.isEmpty() || name != null && !name.isEmpty() || cuisine != null && !cuisine.isEmpty() || keywords != null && !keywords.isEmpty()) {
                Bson addTotalReviews = Aggregates.addFields(
                        new Field<>("totalReviews", new Document("$size", "$reviews"))
                );
                aggregationPipeline.add(addTotalReviews);
            }

            if (aggregationPipeline.isEmpty()) {
                Bson matchDummy = Aggregates.match(new Document());
                aggregationPipeline.add(matchDummy);
            }

            Bson sortByRating = Sorts.orderBy(
                    Sorts.descending("rest_rating"),
                    Sorts.descending("totalReviews"),
                    Sorts.ascending("restaurant_name")
            );

            aggregationPipeline.add(Aggregates.sort(sortByRating));

            aggregationPipeline.add(Aggregates.skip((page - 1) * pageSize));
            aggregationPipeline.add(Aggregates.limit(pageSize));

            return restaurantCollection.aggregate(aggregationPipeline).into(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("An error occurred while searching for restaurants");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /* ********* ANALYTICS METHOD ********* */
    /**
     * Retrieves the top K rated restaurants by cuisine.
     *
     * @param k       The number of restaurants to retrieve.
     * @param cuisine The cuisine to match.
     * @return A list of Document objects representing the top-rated restaurants, sorted by totalReviews in descending order.
     *         Returns an empty list if no criteria are provided or an error occurs.
     */
    public static List<Document> getTopKRatedRestaurantsByCuisine(int k, String cuisine) {
        try {
            List<Bson> aggregationPipeline = new ArrayList<>();

            if (cuisine != null && !cuisine.isEmpty()) {
                Bson matchCuisine = Aggregates.match(Filters.in("tag", cuisine));
                aggregationPipeline.add(matchCuisine);
            }

            Bson matchRating = Aggregates.match(Filters.elemMatch("reviews", Filters.eq("review_rating", 5)));
            aggregationPipeline.add(matchRating);

            Bson addTotalReviews = Aggregates.addFields(
                    new Field<>("totalReviews", new Document("$size", "$reviews"))
            );
            aggregationPipeline.add(addTotalReviews);

            Bson sortTotalReviews = Aggregates.sort(Sorts.descending("totalReviews"));
            aggregationPipeline.add(sortTotalReviews);

            Bson limitResults = Aggregates.limit(k);
            aggregationPipeline.add(limitResults);

            return restaurantCollection.aggregate(aggregationPipeline).into(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("An error occurred while retrieving top-rated Italian restaurants");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Returns a list of documents of the most active users based on the number of reviews they have written.
     *
     * @param k the number of users to return as the most active users
     * @return a list of documents of the most active users based on the number of reviews
     */
    public static List<Document> getUsersWithMostReviews(int k) {
        List<Document> userList = new ArrayList<>();

        AggregateIterable<Document> result = restaurantCollection.aggregate(Arrays.asList(
                new Document("$unwind", "$reviews"),
                new Document("$group", new Document("_id", "$reviews.reviewer_pseudo")
                        .append("totalReviews", new Document("$sum", 1L))),
                new Document("$sort", new Document("totalReviews", -1L)),
                new Document("$limit", k)
        ));

        for (Document document : result) {
            userList.add(document);
        }

        return userList;
    }

    /**
     * Returns the top k restaurants that received reviews for the longest timespan between
     * the latest review and the first review on a restaurant.
     *
     * @param k the number of restaurants to return with the highest lifespan
     * @return a list of documents representing the top k restaurants with the highest lifespan
     */
    public static List<Document> getHighestLifespanRestaurants(int k){
        List<Document> restaurantList = new ArrayList<>();

        AggregateIterable<Document> result = restaurantCollection.aggregate(Arrays.asList(new Document("$unwind", "$reviews"),
                new Document("$group",
                        new Document("_id", "$_id")
                                .append("rest_id",
                                        new Document("$first", "$rest_id"))
                                .append("minDate",
                                        new Document("$min",
                                                new Document("$toDate", "$reviews.review_date")))
                                .append("maxDate",
                                        new Document("$max",
                                                new Document("$toDate", "$reviews.review_date")))),
                new Document("$project",
                        new Document("_id", 1L)
                                .append("rest_id", 1L)
                                .append("minDate",
                                        new Document("$dateToString",
                                                new Document("format", "%Y-%m-%d")
                                                        .append("date", "$minDate")))
                                .append("maxDate",
                                        new Document("$dateToString",
                                                new Document("format", "%Y-%m-%d")
                                                        .append("date", "$maxDate")))
                                .append("lifespan",
                                        new Document("$divide", Arrays.asList(new Document("$subtract", Arrays.asList("$maxDate", "$minDate")), 24L * 60L * 60L * 1000L)))),
                new Document("$sort",
                        new Document("lifespan", -1L)),
                new Document("$limit", k)
        ));

        for (Document document : result) {
            restaurantList.add(document);
        }

        return restaurantList;
    }

    /**
     * Returns the total number of reviews and the average rating for a given restaurant within a date range.
     *
     * @param restId     the ID of the restaurant to retrieve the total number of reviews and the average rating
     * @param startDate  the start date of the date range
     * @param endDate    the end date of the date range
     * @return a document containing the results within the specified date range
     */
    public static Document getReviewsStatsByDateRange(String restId, String startDate, String endDate){

        AggregateIterable<Document> result = restaurantCollection.aggregate(Arrays.asList(new Document("$match",
                        new Document("rest_id", restId)),
                new Document("$unwind", "$reviews"),
                new Document("$match",
                        new Document("$expr",
                                new Document("$and", Arrays.asList(new Document("$gte", Arrays.asList(new Document("$dateFromString",
                                                        new Document("dateString", endDate)
                                                                .append("format", "%Y-%m-%d")),
                                                new Document("$dateFromString",
                                                        new Document("dateString", "$reviews.review_date")
                                                                .append("format", "%Y-%m-%d")))),
                                        new Document("$lt", Arrays.asList(new Document("$dateFromString",
                                                        new Document("dateString", startDate)
                                                                .append("format", "%Y-%m-%d")),
                                                new Document("$dateFromString",
                                                        new Document("dateString", "$reviews.review_date")
                                                                .append("format", "%Y-%m-%d")))))))),
                new Document("$group",
                        new Document("_id",
                                new BsonNull())
                                .append("total_reviews",
                                        new Document("$sum", 1L))
                                .append("average_rating",
                                        new Document("$avg", "$reviews.review_rating"))),
                new Document("$project",
                        new Document("total_reviews", 1L)
                                .append("average_rating",
                                        new Document("$round", Arrays.asList("$average_rating", 1L))))
        ));

        return result.first();
    }
}






