/*package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantDTO;
import org.bson.Document;
import org.bson.conversions.Bson;

import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.MongoDBDriverDAO.*;

public class MongoDBRestaurantDAO {
    // METODO LOGIN RISTORANTE
    public boolean loginRestaurant(String username, String password) {
        try {
            // Assuming you have a valid MongoDB client instance initialized
            MongoCollection<Document> restaurantCollection = mongoClient.getDatabase("ForkTalk").getCollection("Restaurants");

            // Create a filter to match the username and password
            Bson filter = Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("password", password));

            // Use the filter to find a matching user document in the collection
            Document restaurantDocument = restaurantCollection.find(filter).first();

            // Check if a matching user document was found
            return restaurantDocument != null;
        }
        catch (MongoException e) {
            // Handle any exceptions that occur during the database operation
            System.out.println("Username and Password are invalid");
            e.printStackTrace();

            return false;
        }

    }
    //METODO PER AGGIUNGERE UN RISTORANTE
    public boolean addRestaurant(RestaurantDTO rest) {
        if (restaurantCollection.countDocuments(new Document("username", GeneralUserDTO.getUsername())) == 0) {
            Document doc = new Document()
                    .append("_id", rest.getRestaurantId())
                    .append("username", GeneralUserDTO.getUsername())
                    .append("password", rest.getPassword())
                    .append("email", GeneralUserDTO.getEmail())
                    .append("name", rest.getName())
                    .append("coordinates", rest.getCoordinates())
                    .append("country", rest.getCountry())
                    .append("county", rest.getCounty())
                    .append("district", rest.getDistrict())
                    .append("city", rest.getCity())
                    .append("address", rest.getAddress())
                    .append("street_number", rest.getStreetNumber())
                    .append("postcode", rest.getPostcode())
                    .append("price", rest.getPrice())
                    .append("restaurant_tag", rest.getRestaurantTag())
                    .append("review", rest.getRestaurantReviews());                    ;
            restaurantCollection.insertOne(doc);
            return true;
        }
        else {
            System.out.println("Username already chosen, choose another one!");
            return false;
        }
    }
}
*/