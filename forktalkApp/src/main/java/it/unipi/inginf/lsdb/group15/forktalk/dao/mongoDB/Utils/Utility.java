package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils;

import it.unipi.inginf.lsdb.group15.forktalk.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.model.Restaurant;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils.GsonUtils.parseTimestamp;

public class Utility {
    public static RestaurantsListDTO unpackOneRestaurantList(Document document) {
        try {
            // Retrieve the title of the restaurant list
            String title = document.getString("title");

            RestaurantsListDTO r_list = new RestaurantsListDTO(title);

            // Retrieve the list of restaurants as Documents
            List<Document> restaurantsDocuments = document.getList("restaurants", Document.class);

            // Convert the restaurantsDocuments to Restaurant objects
            if (restaurantsDocuments != null) {
                for (Document restaurantDoc : restaurantsDocuments) {
                    // Extract the necessary fields from the restaurantDoc and create a Restaurant object
                    String restaurantId = restaurantDoc.getString("restaurant_id");
                    String restaurantName = restaurantDoc.getString("restaurant_name");

                    Restaurant restaurant = new Restaurant(restaurantId, restaurantName);

                    r_list.getRestaurants().add(restaurant);
                }
            }
            return r_list;
        } catch (Exception e) {
            System.err.println("An error occurred while unpacking the restaurant list:");
            e.printStackTrace();
            return null;
        }
    }


    public static ReservationDTO unpackOneUserReservation(Document document) {
        try {
            // Retrieve values from the document
            String dateStr = document.getString("date");
            Date date = parseTimestamp(dateStr);

            String restaurantId = document.getString("restaurant_id");
            String restaurantName = document.getString("restaurant_name");
            String restaurantCity = document.getString("restaurant_city");
            String restaurantAddress = document.getString("restaurant_address");
            int numberOfPerson = document.getInteger("number of person");

            return new ReservationDTO(date, restaurantId, restaurantName, restaurantCity, restaurantAddress, numberOfPerson);
        } catch (Exception e) {
            System.err.println("An error occurred while unpacking the user reservation:");
            e.printStackTrace();
            return null;
        }
    }


    public static ReservationDTO unpackOneRestaurantReservation(Document document) {
        try {
            // Retrieve values from the document
            String dateStr = document.getString("date");
            Date date = parseTimestamp(dateStr);

            String clientUsername = document.getString("client_username");
            String clientName = document.getString("client_name");
            String clientSurname = document.getString("client_surname");
            int numberOfPerson = document.getInteger("number of person");

            return new ReservationDTO(date, clientName, clientUsername, clientSurname, numberOfPerson);
        } catch (Exception e) {
            System.err.println("An error occurred while unpacking the restaurant reservation:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<String> unpackOneCoordinates(Document document) {
        ArrayList<String> coordinate = new ArrayList<>();

        try {
            // Extract the longitude and latitude from the document
            coordinate.add(document.getString("longitude"));
            coordinate.add(document.getString("latitude"));
        } catch (Exception e) {
            // Print the error message and stack trace
            System.err.println("An error occurred while unpacking of coordinates: " + e.getMessage());
            e.printStackTrace();
        }

        return coordinate;
    }


    public static ReviewDTO unpackOneReview(Document document) {
        ReviewDTO review = new ReviewDTO();
        try {
            // Recupera i campi dalla recensione
            review.setId(document.getString("review_id"));
            String reviewDateStr = document.getString("review_date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            review.setTimestamp(dateFormat.parse(reviewDateStr));
            review.setRating(document.getInteger("review_rating"));
            review.setContent(document.getString("review_content"));
            review.setReviewer(document.getString("reviewer_pseudo"));

            return review;
        } catch (Exception e) {
            System.err.println("An error occurred while unpacking the restaurant review:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
