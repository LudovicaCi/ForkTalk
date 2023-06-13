package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantDTO;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;

import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils.Utility.*;
import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils.Utility.unpackOneCoordinates;


public class MongoDBRestaurantDAO extends MongoDBDriverDAO {

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
            // Assuming you have a valid MongoDB client instance initialized
            MongoCollection<Document> restaurantCollection = mongoClient.getDatabase("ForkTalk").getCollection("Restaurants");

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
     * @param rest The RestaurantDTO object containing the restaurant details.
     * @return True if the restaurant is added successfully, false if the username already exists.
     */
    public boolean addRestaurant(RestaurantDTO rest) {
        if (restaurantCollection.countDocuments(new Document("username", rest.getUsername())) == 0) {
            Document doc = new Document()
                    .append("_id", rest.getId())
                    .append("username", rest.getUsername())
                    .append("password", rest.getPassword())
                    .append("email", rest.getEmail())
                    .append("name", rest.getName())
                    .append("coordinates", rest.getCoordinates())
                    .append("country", rest.getCountry())
                    .append("county", rest.getCounty())
                    .append("district", rest.getDistrict())
                    .append("city", rest.getCity())
                    .append("address", rest.getAddress())
                    .append("street_number", rest.getStreetNumber())
                    .append("postcode", rest.getPostCode())
                    .append("price", rest.getPrice())
                    .append("restaurant_tag", rest.getFeatures())
                    .append("review", rest.getReviews());                    ;
            restaurantCollection.insertOne(doc);
            return true;
        }
        else {
            System.out.println("Username already chosen, choose another one!");
            return false;
        }
    }
}
