package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB;

import com.mongodb.MongoException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.RestaurantDAO.*;

public class ReservationDAO extends DriverDAO{
    /**
     * Makes a reservation for a user at a restaurant.
     *
     * @param user The user making the reservation.
     * @param rest The restaurant where the reservation is being made.
     * @return True if the reservation was successfully confirmed, false otherwise.
     */
    public static boolean makeLocalReservation(UserDTO user, RestaurantDTO rest) {
        try {
            System.out.println("Enter the date for the reservation in yyyy-MM-dd format:");
            Scanner scanner = new Scanner(System.in);
            String date = scanner.next();

            System.out.println("Choose the time slot you want to book from the list:");
            for (String timeSlot : Objects.requireNonNull(getFreeSlotsByDate(rest, date))) {
                System.out.println(timeSlot);
            }

            String slot;
            boolean isFormatCorrect = false;

            do {
                System.out.println("Enter the time slot in HH:mm format:");
                slot = scanner.next();

                try {
                    // Define the date-time formatter pattern
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                    // Parse the input time slot string
                    LocalTime parsedTime = LocalTime.parse(slot, formatter);

                    // Format the parsed time slot back to the desired format
                    String formattedSlot = parsedTime.format(formatter);

                    // Check if the entered string matches the desired format
                    if (slot.equals(formattedSlot)) {
                        isFormatCorrect = true;
                    } else {
                        System.out.println("Incorrect timeslot format.");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Incorrect timeslot format. Please enter in HH:mm format.");
                }
            } while (!isFormatCorrect);

            System.out.println("Enter the number of person for the reservation:");
            int numberOfPerson = scanner.nextInt();

            if(numberOfPerson > getEmptySeatsByDate(rest, date)){
                System.out.printf("There is no space available for %d people\n", numberOfPerson);
                return false;
            }

            ReservationDTO newReservation = new ReservationDTO(user.getUsername(), user.getName(), user.getSurname(), rest.getId(), rest.getName(), rest.getCity(), rest.getAddress(), date + " " + slot + ":00", numberOfPerson);

            Document newUserDocument = Utility.packUserOneReservation(newReservation);
            Document newRestDocument = Utility.packRestaurantOneReservation(newReservation);

            // Create a filter to match the username
            Bson userFilter = Filters.eq("username", user.getUsername());
            Bson restFilter = Filters.eq("username", rest.getUsername());

            // Find the matching restaurant and userDocument document in the collection
            Document restDocument = restaurantCollection.find(restFilter).first();
            Document userDocument = userCollection.find(userFilter).first();

            // Retrieve the existing reservations from the user document
            List<Document> reservationsUserDocs = new ArrayList<>();
            assert userDocument != null;
            if(userDocument.getList("reservations", Document.class) != null) {
                reservationsUserDocs = userDocument.getList("reservations", Document.class);

                if(reservationsUserDocs == null){
                    return false;
                }

                reservationsUserDocs.add(newUserDocument);
            }else{
                reservationsUserDocs.add(newUserDocument);
            }

            // Update the restaurant document with the updated reservation list
            userDocument.append("reservations", reservationsUserDocs);

            // Retrieve the existing reservations from the restaurant document
            assert restDocument != null;
            List<Document> reservationsRestDocs = restDocument.getList("reservations", Document.class);
            if(reservationsRestDocs != null) {
                Iterator<Document> iterator = reservationsRestDocs.iterator();
                while (iterator.hasNext()) {
                    Document doc = iterator.next();
                    if (doc.getString("date").equals(date + " " + slot + ":00") && doc.getString("client_username") == null) {
                        iterator.remove();
                        break;
                    }
                }
            }else{
                reservationsRestDocs = new ArrayList<>();
            }

            reservationsRestDocs.add(newRestDocument);
            // Update the restaurant document with the updated reservation list
            restDocument.append("reservations", reservationsRestDocs);

            // Perform the update operation to update the restaurant document in the collection
            UpdateResult userResult = userCollection.updateOne(userFilter, new Document("$set", userDocument));

            // Perform the update operation
            UpdateResult restResult = restaurantCollection.updateOne(restFilter, new Document("$set", restDocument));

            // Check if the update was successful
            if (userResult.getModifiedCount() > 0 && restResult.getModifiedCount() > 0) {
                System.out.println("The reservation has been successfully confirmed!");
                return true;
            } else {
                System.out.println("The reservation unfortunately could not be processed!");
                return false;
            }
        } catch (Exception e) {
            System.out.println("ERROR: An error occurred while making a reservation.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteReservation(UserDTO user, RestaurantDTO rest, ReservationDTO reservationToDelete) {

        try {
            Bson userFilter = Filters.eq("username", user.getUsername());
            Bson restFilter = Filters.eq("username", rest.getUsername());

            // Find the matching restaurant and userDocument document in the collection
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
                if (doc.getString("date").equals(reservationToDelete.getDate()) && doc.getString("client_username").equals(user.getUsername())) {
                    doc.put("client_username", null);
                    doc.put("client_name", null);
                    doc.put("client_surname", null);
                    doc.put("number of person", 0);
                    break;
                }
            }

            restDocument.append("reservations", restReservationList);

            // Perform the update operation to update the restaurant document in the collection
            UpdateResult userResult = userCollection.updateOne(userFilter, new Document("$set", userDocument));

            // Perform the update operation
            UpdateResult restResult = restaurantCollection.updateOne(restFilter, new Document("$set", restDocument));

            // Check if the update was successful
            if (userResult.getModifiedCount() > 0 && restResult.getModifiedCount() > 0) {
                System.out.println("The reservation has been successfully removed!");
                return true;
            } else {
                System.out.println("The reservation unfortunately could not be removed!");
                return false;
            }

        }catch (MongoException e){
            System.out.println("An error occurred while deleting the reservation");
            e.printStackTrace();
            return false;
        }
    }
}
