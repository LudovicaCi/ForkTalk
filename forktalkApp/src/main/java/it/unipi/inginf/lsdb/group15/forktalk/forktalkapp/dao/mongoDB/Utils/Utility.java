package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils;

import com.mongodb.MongoException;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReviewDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Restaurant;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    /**
     * Packs the coordinates into a list of MongoDB Documents.
     *
     * @param coordinates The list of coordinates [latitude, longitude].
     * @return The list of MongoDB Documents representing the coordinates.
     */
    public static List<Document> packCoordinates(List<String> coordinates) {
        List<Document> coordinatesDocuments = new ArrayList<>();

            Document coordinateDocument = new Document()
                    .append("latitude", coordinates.get(0))
                    .append("longitude", coordinates.get(1));

            coordinatesDocuments.add(coordinateDocument);


        return coordinatesDocuments;
    }

    /**
     * Packs the restaurant reservations into a list of MongoDB Documents.
     *
     * @param reservations The list of ReservationDTO objects representing the reservations.
     * @return The list of MongoDB Documents representing the reservations.
     */
    public static ArrayList<Document> packRestaurantReservations(ArrayList<ReservationDTO> reservations) {
        ArrayList<Document> reservationsDocuments = new ArrayList<>();

        for (ReservationDTO reservation : reservations) {
            Document reservationDocument = new Document()
                    .append("date", reservation.getDate())
                    .append("client_username", reservation.getClientUsername())
                    .append("client_name", reservation.getClientName())
                    .append("client_surname", reservation.getClientSurname())
                    .append("number of person", reservation.getPeople());

            reservationsDocuments.add(reservationDocument);
        }

        return reservationsDocuments;
    }

    public static Document packRestaurantOneReservation(ReservationDTO reservation){

        return new Document()
                .append("date", reservation.getDate())
                .append("client_username", reservation.getClientUsername())
                .append("client_name", reservation.getClientName())
                .append("client_surname", reservation.getClientSurname())
                .append("number of person", reservation.getPeople());
    }

    public static ArrayList<Document> packUserReservations(ArrayList<ReservationDTO> reservations) {
        ArrayList<Document> reservationsDocuments = new ArrayList<>();

        for (ReservationDTO reservation : reservations) {
            Document reservationDocument = new Document()
                    .append("date", reservation.getDate())
                    .append("restaurant_id", reservation.getRestaurantID())
                    .append("restaurant_name", reservation.getRestaurantName())
                    .append("restaurant_city", reservation.getRestaurantCity())
                    .append("restaurant_address", reservation.getRestaurantAddress())
                    .append("number of person", reservation.getPeople());

            reservationsDocuments.add(reservationDocument);
        }

        return reservationsDocuments;
    }

    public static Document packUserOneReservation(ReservationDTO reservation){

        return new Document()
                .append("date", reservation.getDate())
                .append("restaurant_id", reservation.getRestaurantID())
                .append("restaurant_name", reservation.getRestaurantName())
                .append("restaurant_city", reservation.getRestaurantCity())
                .append("restaurant_address", reservation.getRestaurantAddress())
                .append("number of person", reservation.getPeople());
    }

    /**
     * Packs the review into a MongoDB Document.
     *
     * @param review The ReviewDTO object representing the review.
     * @return TheMongoDB Documents representing the review.
     */
    public static Document packOneReview(ReviewDTO review) {

        Document reviewDocument = new Document()
                .append("review_id", review.getId())
                .append("review_date", review.getTimestamp())
                .append("review_rating", review.getRating())
                .append("review_content", review.getContent())
                .append("reviewer_pseudo", review.getReviewer());

        return reviewDocument;
    }


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
        } catch (MongoException e) {
            System.err.println("An error occurred while unpacking the restaurant list:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Unpacks a single restaurant list from a MongoDB Document.
     *
     * @param document The MongoDB Document representing the restaurant list.
     * @return The RestaurantsListDTO object representing the unpacked restaurant list.
     */
    public static ReservationDTO unpackOneUserReservation(Document document) {
        try {
            // Retrieve values from the document
            String dateStr = document.getString("date");

            String restaurantId = document.getString("restaurant_id");
            String restaurantName = document.getString("restaurant_name");
            String restaurantCity = document.getString("restaurant_city");
            String restaurantAddress = document.getString("restaurant_address");
            int numberOfPerson = document.getInteger("number of person");

            return new ReservationDTO(dateStr, restaurantId, restaurantName, restaurantCity, restaurantAddress, numberOfPerson);
        } catch (MongoException e) {
            System.err.println("An error occurred while unpacking the user reservation:");
            e.printStackTrace();
            return null;
        }
    }


    public static ReservationDTO unpackOneRestaurantReservation(Document document) {
        try {
            // Retrieve values from the document
            String dateStr = document.getString("date");

            String clientUsername = document.getString("client_username");
            String clientName = document.getString("client_name");
            String clientSurname = document.getString("client_surname");
            int numberOfPerson = document.getInteger("number of person");

            return new ReservationDTO(dateStr, clientName, clientUsername, clientSurname, numberOfPerson);
        } catch (MongoException e) {
            System.err.println("An error occurred while unpacking the restaurant reservation:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Unpacks a single restaurant reservation from a MongoDB Document.
     *
     * @param document The MongoDB Document representing the restaurant reservation.
     * @return The ReservationDTO object representing the unpacked restaurant reservation.
     */
    public static ArrayList<String> unpackOneCoordinates(Document document) {
        ArrayList<String> coordinate = new ArrayList<>();

        try {
            // Extract the longitude and latitude from the document
            coordinate.add(document.getString("longitude"));
            coordinate.add(document.getString("latitude"));
        } catch (MongoException e) {
            // Print the error message and stack trace
            System.err.println("An error occurred while unpacking of coordinates: " + e.getMessage());
            e.printStackTrace();
        }

        return coordinate;
    }

    /**
     * Unpacks a single review from a MongoDB Document.
     *
     * @param document The MongoDB Document representing the review.
     * @return The ReviewDTO object representing the unpacked review.
     */
    public static ReviewDTO unpackOneReview(Document document) {
        ReviewDTO review = new ReviewDTO();
        try {
            // Recupera i campi dalla recensione
            review.setId(document.getString("review_id"));
            String reviewDateStr = document.getString("review_date");
            review.setTimestamp(reviewDateStr);
            review.setRating(document.getInteger("review_rating"));
            review.setContent(document.getString("review_content"));
            review.setReviewer(document.getString("reviewer_pseudo"));

            return review;
        } catch (MongoException e) {
            System.err.println("An error occurred while unpacking the restaurant review:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
