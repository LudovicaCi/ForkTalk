package it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.Utils.Utility;
import it.unipi.inginf.lsdb.group15.forktalk.dto.ReservationDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.UserDTO;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.RestaurantDAO.getFreeSlotsByDate;
import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.RestaurantDAO.getReservationByDate;

public class ReservationDAO extends DriverDAO{
    public static boolean makeReservation(UserDTO user, RestaurantDTO rest) {
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

            if(numberOfPerson > getReservationByDate(rest, date).size()){
                System.out.printf("There is no space available for %d people\n", numberOfPerson);
            }

            ReservationDTO newReservation = new ReservationDTO(user.getUsername(), user.getName(), user.getSurname(), rest.getId(), rest.getName(), rest.getCity(), rest.getAddress(), date + " " + slot + ":00", numberOfPerson);

            Document userDocument = Utility.packUserOneReservation(newReservation);
            Document restDocument = Utility.packRestaurantOneReservation(newReservation);

            // Create a filter to match the username
            Bson userFilter = Filters.eq("username", user.getUsername());
            Bson restFilter = Filters.eq("reservations.date", date + " " + slot + ":00");

            // Find the matching restaurant document in the collection
            Document restaurantUserDocument = userCollection.find(userFilter).first();

            // Retrieve the existing reservations from the restaurant document
            assert restaurantUserDocument != null;
            if(restaurantUserDocument.getList("reservations", Document.class) != null) {
                List<Document> reservationsUserDocs = restaurantUserDocument.getList("reservations", Document.class);
                reservationsUserDocs.add(userDocument);
                // Update the restaurant document with the updated reservation list
                restaurantUserDocument.append("reservations", reservationsUserDocs);
            }else{
                restaurantUserDocument.append("reservations", userDocument);
            }

            // Perform the update operation to update the restaurant document in the collection
            UpdateResult userResult = restaurantCollection.updateOne(userFilter, new Document("$set", restaurantUserDocument));

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

    //          ----------------------------------------
    //         INSERIRE RESERVATION NELLE DUE COLLECTIONS
    //          ----------------------------------------


    /*

        public void insertReservation(ReservationDTO res) {
        // Devo aggiungere sia in Ordini che in Ristoranti
        MongoCollection<Document> userCollection = getDatabase().getCollection("Users");
        MongoCollection<Document> restaurantCollection = getDatabase().getCollection("Restaurants");

//          ----------------------------------------
//              PREPARAZIONE RESERVATION PER RESERVATIONS
//          ----------------------------------------
        List<Document> ordersDishes = new ArrayList<>();
        List<Document> restaurantsDishes = new ArrayList<>();

//        Creo la lista piatti per l'ordine
        for(DishDTO item: order.getDishes())
        {
//            Lista piatti per l'ordine nella collection Orders
            ordersDishes.add(new Document()
                    .append("name", item.getName())
                    .append("price", item.getPrice())
                    .append("currency", item.getCurrency().replace(" ",""))
                    .append("quantity", item.getQuantity()));

//            Lista piatti per l'ordine embedded nel Restaurant
            restaurantsDishes.add(new Document()
                    .append("name", item.getName())
                    .append("quantity", item.getQuantity()));
        }

        ObjectId orderId = new ObjectId();
//            Ordine da inserire nella collection Orders
        Document toInsertOrders = new Document("_id", orderId).
                append("user", order.getUser()).
                append("restaurant", order.getRestaurant()).
                append("restaurantId", order.getRestaurantId()).
                append("paymentMethod", order.getPaymentMethod()).
                append("paymentNumber", order.getPaymentNumber()).
                append("creationDate", order.getCreationDate()).
                append("deliveryDate", order.getDeliveryDate()).
                append("address", order.getAddress()).
                append("zipcode", order.getZipcode()).
                append("total", order.getTotal()).
                append("currency", order.getCurrency()).
                append("status", order.getStatus()).
                append("dishes", ordersDishes);

//            Ordine da inserire nella collection Orders
        Document toInsertRestaurants = new Document("_id", orderId).
                append("user", order.getUser()).
                append("paymentMethod", order.getPaymentMethod()).
                append("paymentNumber", order.getPaymentNumber()).
                append("creationDate", order.getCreationDate()).
                append("address", order.getAddress()).
                append("zipcode", order.getZipcode()).
                append("total", order.getTotal()).
                append("currency", order.getCurrency()).
                append("dishes", ordersDishes);

//          --------------------------------
//              CREO LA TRANSAZIONE ACID
//          --------------------------------
        ClientSession session = getSession();
        try {
            session.startTransaction();

            collectionOrders.insertOne(session, toInsertOrders);

            collectionRestaurants.updateOne(session,
                    eq("_id", new ObjectId(order.getRestaurantId())),
                    Updates.addToSet("orders", toInsertRestaurants)
            );

            session.commitTransaction();
        } catch (Exception e) {
            session.abortTransaction();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

     */
}
