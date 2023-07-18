package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.List;

import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jDriverDAO.driver;
import static org.neo4j.driver.Values.parameters;

public class Neo4jRestaurantDAO {
    /**
     * Adds a new restaurant to the Neo4j graph database.
     *
     * @param rest The RestaurantDTO object representing the restaurant to be added
     * @return true if the restaurant is successfully added, false otherwise
     */
    public static boolean addRestaurant(RestaurantDTO rest){
        boolean res;
        try (Session session = Neo4jDriverDAO.driver.session()){
            res = session.writeTransaction(tx -> {
                tx.run("CREATE (:Restaurant {rest_id: $rest_id})",
                        parameters("rest_id", rest.getId()));

                return true;
            });
        }catch (Neo4jException e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    /**
     * Deletes a restaurant from the Neo4j graph database.
     *
     * @param rest The RestaurantDTO object representing the restaurant to be deleted
     * @return true if the restaurant is successfully deleted, false otherwise
     */
    public static boolean deleteRestaurant(RestaurantDTO rest){
        boolean res;
        try (Session session = Neo4jDriverDAO.driver.session()){
            res = session.writeTransaction(tx -> {
                tx.run("MATCH (r:Restaurant) WHERE r.rest_id = $rest_id DETACH DELETE r",
                        parameters("rest_id", rest.getId()));

                return true;
            });
        }catch (Neo4jException e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    /**
     * Retrieves the number of likes for a restaurant from the Neo4j graph database.
     *
     * @param rest_id The ID of the restaurant
     * @return The number of likes for the restaurant
     */
    public static int getNumLikesRestaurant(String rest_id) {
        int numLikes;
        try (Session session = driver.session()) {
            numLikes = session.writeTransaction(tx -> {
                Result result = tx.run("MATCH (:Restaurant{rest_id: $rest_id})<-[r:Likes]-()" +
                        "RETURN COUNT(r) AS numLikes", parameters("rest_id", rest_id));
                return result.next().get("numLikes").asInt();
            });
        }
        return numLikes;
    }


    /* ********* ANALYTICS METHOD ********* */

    /**
     * Retrieves a list of the most liked restaurants from the Neo4j graph database.
     *
     * @param limit The maximum number of restaurants to retrieve
     * @return A list of Node objects representing the most liked restaurants
     */
    public static List<Node> getMostLikedRestaurants(int limit){
        List<Node> restNode = new ArrayList<>();;
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (r:Restaurant) <- [l:Likes]-()
                                RETURN r, COUNT(l) AS numLikes
                                ORDER BY numLikes DESC
                                LIMIT $limit""",
                        parameters("limit", limit));

                while (result.hasNext()) {
                    Record record = result.next();
                    Node restaurant = record.get("r").asNode();
                    restNode.add(restaurant);
                }

                return restNode;
            });
        }
        return restNode;
    }
}
