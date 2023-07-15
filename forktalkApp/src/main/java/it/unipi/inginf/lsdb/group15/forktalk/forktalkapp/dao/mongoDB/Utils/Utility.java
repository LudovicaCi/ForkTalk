package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils;

import com.mongodb.MongoException;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.DriverDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.*;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.model.Restaurant;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;
import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.DriverDAO.restaurantCollection;

public class Utility {
    public static Document packUser(UserDTO user) {
        Document document = new Document();
        document.put("email", user.getEmail());
        document.put("username", user.getUsername());
        document.put("password", user.getPassword());
        document.put("name", user.getName());
        document.put("surname", user.getSurname());
        document.put("origin", user.getOrigin());
        document.put("suspended", user.getSuspended());
        document.put("role", user.getRole());

        document.put("reservations", packUserReservations(user.getReservations()));

        ArrayList<Document> restaurantListsDocuments = new ArrayList<>();
        for (RestaurantsListDTO restaurantList : user.getRestaurantLists()) {
            Document restaurantListDocument = new Document();
            restaurantListDocument.put("title", restaurantList.getTitle());

            ArrayList<Document> restaurantsDocuments = new ArrayList<>();
            for (RestaurantDTO restaurant : restaurantList.getRestaurants()) {
                Document restaurantDocument = new Document();
                restaurantDocument.put("restaurant_id", restaurant.getId());
                restaurantDocument.put("restaurant_name", restaurant.getName());

                restaurantsDocuments.add(restaurantDocument);
            }
            restaurantListDocument.put("restaurants", restaurantsDocuments);

            restaurantListsDocuments.add(restaurantListDocument);
        }
        document.put("restaurantsList", restaurantListsDocuments);

        return document;
    }

    public static Document packRestaurantDTO(RestaurantDTO restaurantDTO) {
        Document restaurantDoc = new Document();
        restaurantDoc.append("rest_id", restaurantDTO.getId())
                .append("restaurant_name", restaurantDTO.getName())
                .append("username", restaurantDTO.getUsername())
                .append("email", restaurantDTO.getEmail())
                .append("password", restaurantDTO.getPassword())
                .append("coordinates", restaurantDTO.getCoordinates())
                .append("location", restaurantDTO.getLocation())
                .append("country", restaurantDTO.getCountry())
                .append("county", restaurantDTO.getCounty())
                .append("district", restaurantDTO.getDistrict())
                .append("city", restaurantDTO.getCity())
                .append("address", restaurantDTO.getAddress())
                .append("street_number", restaurantDTO.getStreetNumber())
                .append("postcode", restaurantDTO.getPostCode())
                .append("price", restaurantDTO.getPrice())
                .append("tag", restaurantDTO.getFeatures())
                .append("reservations", new ArrayList<Document>())
                .append("rest_rating", restaurantDTO.getRating())
                .append("reviews", packReviews(restaurantDTO.getReviews()));

        return restaurantDoc;
    }

    public static RestaurantDTO unpackRestaurant(Document restaurantDoc) {
        RestaurantDTO restaurant = new RestaurantDTO();
        restaurant.setId(restaurantDoc.getString("rest_id"));
        restaurant.setName(restaurantDoc.getString("restaurant_name"));
        restaurant.setUsername(restaurantDoc.getString("username"));
        restaurant.setEmail(restaurantDoc.getString("email"));
        restaurant.setPassword(restaurantDoc.getString("password"));
        restaurant.setCoordinates((ArrayList<String>) unpackCoordinates(restaurantDoc.getList("coordinates", Document.class)));
        restaurant.setLocation((ArrayList<String>) restaurantDoc.getList("location", String.class));
        restaurant.setCountry(restaurantDoc.getString("country"));
        restaurant.setCounty(restaurantDoc.getString("county"));
        restaurant.setDistrict(restaurantDoc.getString("district"));
        restaurant.setCity(restaurantDoc.getString("city"));
        restaurant.setAddress(restaurantDoc.getString("address"));
        restaurant.setStreetNumber(String.valueOf(restaurantDoc.getInteger("street_number")));
        restaurant.setPostCode(restaurantDoc.getString("postcode"));
        Integer price = restaurantDoc.getInteger("price");
        restaurant.setPrice(price != null ? price : 0);
        restaurant.setFeatures((ArrayList<String>) restaurantDoc.getList("tag", String.class));
        Object ratingObj = restaurantDoc.get("rest_rating");
        double rating;
        if(ratingObj == null)
            rating = 0.0;
        else
            rating = Double.parseDouble(String.valueOf(ratingObj));
        restaurant.setRating(rating);
        restaurant.setReservations((ArrayList<ReservationDTO>) unpackRestaurantReservations(restaurantDoc.getList("reservations", Document.class)));
        restaurant.setReviews((ArrayList<ReviewDTO>) unpackReviews(restaurantDoc.getList("reviews", Document.class)));

        return restaurant;
    }

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

    public static List<String> unpackCoordinates(List<Document> coordinatesDocuments) {
        List<String> coordinates = new ArrayList<>();

        for (Document coordinateDocument : coordinatesDocuments) {
            String latitude = coordinateDocument.getString("latitude");
            String longitude = coordinateDocument.getString("longitude");

            coordinates.add(latitude);
            coordinates.add(longitude);
        }

        return coordinates;
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

        return new Document()
                .append("review_id", review.getId())
                .append("review_date", review.getTimestamp())
                .append("review_rating", review.getRating())
                .append("review_content", review.getContent())
                .append("reviewer_pseudo", review.getReviewer());
    }

    private static List<Document> packReviews(List<ReviewDTO> reviews) {
        List<Document> reviewDocs = new ArrayList<>();
        for (ReviewDTO review : reviews) {
            Document reviewDoc = packOneReview(review);
            reviewDocs.add(reviewDoc);
        }
        return reviewDocs;
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

                    RestaurantDTO restaurant = new RestaurantDTO(restaurantId, restaurantName);

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

    public static Document packOneRestaurantList(RestaurantsListDTO restaurantList) {
        try {
            Document document = new Document();
            document.append("title", restaurantList.getTitle());

            List<Document> restaurantsDocuments = new ArrayList<>();
            List<RestaurantDTO> restaurants = restaurantList.getRestaurants();

            for (RestaurantDTO restaurant : restaurants) {
                Document restaurantDoc = new Document();
                restaurantDoc.append("restaurant_id", restaurant.getId());
                restaurantDoc.append("restaurant_name", restaurant.getName());

                restaurantsDocuments.add(restaurantDoc);
            }

            document.append("restaurants", restaurantsDocuments);

            return document;
        } catch (MongoException e) {
            System.err.println("An error occurred while packing the restaurant list:");
            e.printStackTrace();
            return null;
        }
    }

    public static List<Document> packRestaurantLists(ArrayList<RestaurantsListDTO> restaurantLists) {
        try {
            List<Document> documents = new ArrayList<>();

            for (RestaurantsListDTO restaurantList : restaurantLists) {
                Document document = packOneRestaurantList(restaurantList);
                documents.add(document);
            }

            return documents;
        } catch (MongoException e) {
            System.err.println("An error occurred while packing the restaurant lists:");
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

    private static List<ReservationDTO> unpackRestaurantReservations(List<Document> reservationDocs) {
        List<ReservationDTO> reservations = new ArrayList<>();
        for (Document reservationDoc : reservationDocs) {
            ReservationDTO reservationDTO = unpackOneRestaurantReservation(reservationDoc);
            if (reservationDTO != null) {
                reservations.add(reservationDTO);
            }
        }
        return reservations;
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

    public static List<ReviewDTO> unpackReviews(List<Document> reviewDocs) {
        List<ReviewDTO> reviews = new ArrayList<>();
        for (Document reviewDoc : reviewDocs) {
            ReviewDTO reviewDTO = unpackOneReview(reviewDoc);
            if (reviewDTO != null) {
                reviews.add(reviewDTO);
            }
        }
        return reviews;
    }


    public static String generateUniqueReviewId(String restId, List<Document> reviewsDocuments) {
        String reviewId;
        boolean isUnique;
        do {
            reviewId = restId + "-r" + getRandomNumber();
            isUnique = true;
            for (Document reviewDoc : reviewsDocuments) {
                if (reviewDoc.getString("review_id").equals(reviewId)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return reviewId;
    }

    public static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000000);
    }

    public static boolean isRestaurantIdUnique(String restaurantId) {
        Bson filter = eq("restaurant_id", restaurantId);
        long count = restaurantCollection.countDocuments(filter);
        return count == 0;
    }

    public static String generateUniqueRestaurantId() {
        String prefix = "g";
        String suffix = "-d";
        Random random = new Random();

        String restaurantId;
        boolean isUnique = false;

        do {
            // Genera un numero casuale di 8 cifre per la parte numerica
            int randomNumber1 = random.nextInt(90000000) + 10000000;
            int randomNumber2 = random.nextInt(90000000) + 10000000;

            restaurantId = prefix + randomNumber1 + suffix + randomNumber2;
            isUnique = isRestaurantIdUnique(restaurantId);
        } while (!isUnique);

        return restaurantId;
    }


}
