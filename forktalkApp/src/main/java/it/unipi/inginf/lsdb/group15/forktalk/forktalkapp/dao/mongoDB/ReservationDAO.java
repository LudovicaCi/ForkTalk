package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDate;
import java.util.*;

import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO.*;

public class ReservationDAO extends DriverDAO{

    /**
     * Makes a reservation for a user at a restaurant.
     *
     * @param user           The user making the reservation.
     * @param rest           The restaurant where the reservation is being made.
     * @param date           The date for the reservation in yyyy-MM-dd format.
     * @param slot           The time slot for the reservation in HH:mm format.
     * @param numberOfPerson The number of people for the reservation.
     * @return True if the reservation was successfully confirmed, false otherwise.
     */
    public static boolean makeReservation(UserDTO user, RestaurantDTO rest, String date, String slot, int numberOfPerson) {
        ClientSession clientSession = mongoClient.startSession();

        try {
            clientSession.startTransaction();

            List<String> availableSlots = getFreeSlotsByDate(rest, date);
            assert availableSlots != null;
            if (availableSlots.contains(slot)) { // Controlla se lo slot desiderato è presente nella lista degli slot disponibili
                System.out.println("Time slot available.");
            } else {
                System.out.println("Invalid or unavailable time slot. Please choose another slot.");
                return false;
            }

            if (numberOfPerson > getEmptySeatsByDate(rest, date)) {
                System.out.printf("There is no space available for %d people\n", numberOfPerson);
                return false;
            }

            ReservationDTO newReservation = new ReservationDTO(user.getUsername(), user.getName(), user.getSurname(), rest.getId(), rest.getName(), rest.getCity(), rest.getAddress(), date + " " + slot + ":00", numberOfPerson);

            Document newUserDocument = Utility.packUserOneReservation(newReservation);
            Document newRestDocument = Utility.packRestaurantOneReservation(newReservation);

            // Creates a filter to match the username
            Bson userFilter = Filters.eq("username", user.getUsername());
            Bson restFilter = Filters.eq("username", rest.getUsername());

            // Finds the matching restaurant and userDocument document in the collection
            Document restDocument = restaurantCollection.find(restFilter).first();
            Document userDocument = userCollection.find(userFilter).first();

            // Retrieves the existing reservations from the user document
            List<Document> reservationsUserDocs = new ArrayList<>();
            assert userDocument != null;
            if (userDocument.getList("reservations", Document.class) != null) {
                reservationsUserDocs = userDocument.getList("reservations", Document.class);

                if (reservationsUserDocs == null) {
                    return false;
                }

                reservationsUserDocs.add(newUserDocument);
            } else {
                reservationsUserDocs.add(newUserDocument);
            }

            // Updates the user document with the updated reservation list
            userDocument.append("reservations", reservationsUserDocs);

            // Retrieves the existing reservations from the restaurant document
            assert restDocument != null;
            List<Document> reservationsRestDocs = restDocument.getList("reservations", Document.class);
            if (reservationsRestDocs != null) {
                Iterator<Document> iterator = reservationsRestDocs.iterator();
                boolean slotAvailable = false;
                while (iterator.hasNext()) {
                    Document doc = iterator.next();
                    if (doc.getString("date").equals(date + " " + slot + ":00") && doc.getString("client_username") == null) {
                        slotAvailable = true;
                        iterator.remove();
                        break;
                    }
                }

                if (!slotAvailable) {
                    System.out.println("Invalid or unavailable time slot. Please choose another slot.");
                    return false;
                }
            } else {
                return false;
            }

            reservationsRestDocs.add(newRestDocument);

            // Updates the restaurant document with the updated reservation list
            restDocument.append("reservations", reservationsRestDocs);

            // Performs the update operation to update the user and restaurant documents in the collection
            UpdateResult userResult = userCollection.updateOne(userFilter, new Document("$set", userDocument));
            UpdateResult restResult = restaurantCollection.updateOne(restFilter, new Document("$set", restDocument));

            // Checks if the update was successful
            if (userResult.getModifiedCount() > 0 && restResult.getModifiedCount() > 0) {
                clientSession.commitTransaction();
                System.out.println("The reservation has been successfully confirmed!");
                return true;
            } else {
                System.out.println("The reservation unfortunately could not be processed!");
                clientSession.abortTransaction();
                return false;
            }
        } catch (Exception e) {
            System.out.println("ERROR: An error occurred while making a reservation.");
            clientSession.abortTransaction();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a reservation from a restaurant made by a user
     *
     * @param user               The user who made the reservation.
     * @param rest               The restaurant where the reservation was made.
     * @param reservationToDelete The reservation to be deleted.
     * @return True if the reservation was successfully deleted, false otherwise.
     */
    public static boolean deleteReservation(UserDTO user, RestaurantDTO rest, ReservationDTO reservationToDelete) {
        ClientSession clientSession = mongoClient.startSession();

        try {
            clientSession.startTransaction();

            Bson userFilter = Filters.eq("username", user.getUsername());
            Bson restFilter = Filters.eq("username", rest.getUsername());

            // Finds the matching restaurant and user document in the collection
            Document restDocument = restaurantCollection.find(restFilter).first();
            Document userDocument = userCollection.find(userFilter).first();

            assert userDocument != null;
            List<Document> userReservationList = userDocument.getList("reservations", Document.class);
            assert restDocument != null;
            List<Document> restReservationList = restDocument.getList("reservations", Document.class);

            if (userReservationList == null || restReservationList == null)
                return false;

            Iterator<Document> userIterator = userReservationList.iterator();
            while (userIterator.hasNext()) {
                Document doc = userIterator.next();
                if (doc.getString("date").equals(reservationToDelete.getDate()) && doc.getString("restaurant_id").equals(rest.getId())) {
                    userIterator.remove();
                    break;
                }
            }

            userDocument.append("reservations", userReservationList);

            for(Document doc: restReservationList) {
                String client = doc.getString("client_username");
                if(client == null)
                    continue;

                if (doc.getString("date").equals(reservationToDelete.getDate()) && client.equals(user.getUsername())) {
                    doc.put("client_username", null);
                    doc.put("client_name", null);
                    doc.put("client_surname", null);
                    doc.put("number of person", 0);
                    break;
                }
            }

            restDocument.append("reservations", restReservationList);

            // Performs the update operation
            UpdateResult userResult = userCollection.updateOne(userFilter, new Document("$set", userDocument));
            UpdateResult restResult = restaurantCollection.updateOne(restFilter, new Document("$set", restDocument));


            // Checks if the update was successful
            if (userResult.getModifiedCount() > 0 && restResult.getModifiedCount() > 0) {
                System.out.println("The reservation has been successfully removed!");
                clientSession.commitTransaction();
                return true;
            } else {
                System.out.println("The reservation unfortunately could not be removed!");
                clientSession.abortTransaction();
                return false;
            }

        }catch (MongoException e){
            System.out.println("An error occurred while deleting the reservation");
            clientSession.abortTransaction();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete reservations with a date prior to the current date for all users.
     */
    public static void deleteOldReservations() {
        ClientSession clientSession = mongoClient.startSession();

        try {
            clientSession.startTransaction();
            LocalDate currentDate = LocalDate.now();

            Bson filter = Filters.lt("reservations.date", currentDate.toString());
            Bson update = Updates.pull("reservations", filter);

            UpdateResult userResult = userCollection.updateMany(new Document(), update);
            UpdateResult restResult = restaurantCollection.updateMany(new Document(), update);

            // Checks if the update was successful
            if (userResult.getModifiedCount() > 0 && restResult.getModifiedCount() > 0) {
                System.out.println("The reservations have been successfully removed!");
                clientSession.commitTransaction();
            } else {
                System.out.println("The reservations unfortunately could not be removed or are not present!");
                clientSession.abortTransaction();
            }
        }catch (MongoException e){
            System.out.println("An error occurred while deleting the reservation");
            clientSession.abortTransaction();
            e.printStackTrace();
        }
    }
}
