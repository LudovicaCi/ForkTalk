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
    public static List<Document> packRestaurantReservations(List<ReservationDTO> reservations) {
        List<Document> reservationsDocuments = new ArrayList<>();

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

    /**
     * Packs the reviews into a list of MongoDB Documents.
     *
     * @param reviews The list of ReviewDTO objects representing the reviews.
     * @return The list of MongoDB Documents representing the reviews.
     */
    public static List<Document> packReviews(List<ReviewDTO> reviews) {
        List<Document> reviewsDocuments = new ArrayList<>();

        for (ReviewDTO review : reviews) {
            Document reviewDocument = new Document()
                    .append("review_id", review.getId())
                    .append("review_date", review.getTimestamp())
                    .append("review_rating", review.getRating())
                    .append("review_content", review.getContent())
                    .append("reviewer_pseudo", review.getReviewer());

            reviewsDocuments.add(reviewDocument);
        }

        return reviewsDocuments;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
