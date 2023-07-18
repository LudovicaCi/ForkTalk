package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.types.Node;

import java.util.ArrayList;
import java.util.List;

import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jDriverDAO.driver;
import static org.neo4j.driver.Values.parameters;

public class Neo4jUserDAO {

    /**
     * Adds a new user to the Neo4j graph database.
     *
     * @param user The UserDTO object representing the user to be added
     * @return true if the user is successfully added, false otherwise
     */
    public static boolean addUser(UserDTO user){
        boolean res;
        try (Session session = driver.session()){
            res = session.writeTransaction(tx -> {
                 tx.run("CREATE (:User {username: $username, name: $name, surname: $surname})",
                        parameters("username", user.getUsername(), "name", user.getName(), "surname", user.getSurname()));

                 return true;
            });
        }catch (Neo4jException e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    /**
     * Deletes a user from the Neo4j graph database.
     *
     * @param username The username of the user to be deleted
     * @return true if the user is successfully deleted, false otherwise
     */
    public static boolean deleteUser(String username){
        boolean res;
        try (Session session = driver.session()){
            res = session.writeTransaction(tx -> {
                tx.run("MATCH (u:User) WHERE u.username = $username DETACH DELETE u",
                        parameters("username", username));

                return true;
            });
        }catch (Neo4jException e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    /**
     * Adds a new restaurant list to the Neo4j graph database for a user.
     *
     * @param user  The UserDTO object representing the user who owns the restaurant list
     * @param title The title of the restaurant list
     * @return true if the restaurant list is successfully added, false otherwise
     */
    public static boolean addRestaurantList(UserDTO user, String title){
        boolean res;
        try (Session session = driver.session()){
            res = session.writeTransaction(tx -> {
                tx.run("CREATE (:RestaurantList {title: $title, owner: $username})",
                        parameters("title", title, "username", user.getUsername()));

                return true;
            });
        }catch (Neo4jException e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    /**
     * Deletes a restaurant list from the Neo4j graph database for a user.
     *
     * @param username The username of the user who owns the restaurant list
     * @param title    The title of the restaurant list
     * @return true if the restaurant list is successfully deleted, false otherwise
     */
    public static boolean deleteRestaurantList(String username, String title){
        boolean res;
        try (Session session = driver.session()){
            res = session.writeTransaction(tx -> {
                tx.run("MATCH (r:RestaurantList) WHERE r.title = $title AND r.owner = $username DETACH DELETE r",
                        parameters("title", title, "username", username));

                return true;
            });
        }catch (Neo4jException e){
            e.printStackTrace();
            return false;
        }
        return res;
    }

    /**
     * Retrieves the number of followers for a user from the Neo4j graph database.
     *
     * @param username The username of the user
     * @return The number of followers for the user
     */
    public static int getNumFollowersUser(String username) {
        int numFollowers;
        try (Session session = driver.session()) {
            numFollowers = session.writeTransaction(tx -> {
                Result result = tx.run("MATCH (:User {username: $username})<-[r:Follows]-() " +
                        "RETURN COUNT(r) AS numFollowers", parameters("username", username));
                return result.next().get("numFollowers").asInt();
            });
        }
        return numFollowers;
    }

    /**
     * Retrieves the number of followers for a restaurant list from the Neo4j graph database.
     *
     * @param owner The username of the owner of the restaurant list
     * @param title The title of the restaurant list
     * @return The number of followers for the restaurant list
     */
    public static int getNumFollowersRestaurantList(String owner, String title) {
        int numFollowers;
        try (Session session = driver.session()) {
            numFollowers = session.writeTransaction(tx -> {
                Result result = tx.run("MATCH (:RestaurantList {title: $title, owner: $owner})<-[r:Follows]-() " +
                        "RETURN COUNT(r) AS numFollowers", parameters("title", title, "owner", owner));
                return result.next().get("numFollowers").asInt();
            });
        }
        return numFollowers;
    }

    /**
     * Retrieves the number of users that a user is following from the Neo4j graph database.
     *
     * @param username The username of the user
     * @return The number of users that the user is following
     */
    public static int getNumFollowingUsers(String username) {
        int numFollowers;
        try (Session session = driver.session()) {
            numFollowers = session.writeTransaction(tx -> {
                Result result = tx.run("MATCH (:User {username: $username})-[r:Follows]->() RETURN COUNT(r) AS numFollowers", parameters("username", username));
                return result.next().get("numFollowers").asInt();
            });
        }
        return numFollowers;
    }

    /**
     * Checks if a user likes a specific restaurant in the Neo4j graph database.
     *
     * @param username The username of the user
     * @param rest_id  The ID of the restaurant
     * @return true if the user likes the restaurant, false otherwise
     */
    public static boolean isUserLikesRestaurant(String username, String rest_id){
        boolean res = false;
        try(Session session = driver.session()){
            res = session.readTransaction(tx -> {
                Result r = tx.run("MATCH (:User{username:$username})-[r:Likes]->(p:Restaurant) WHERE p.rest_id = $rest_id " +
                        "RETURN COUNT(*)", parameters("username", username, "rest_id", rest_id));
                Record record = r.next();
                return record.get(0).asInt() != 0;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Checks if a user is following a specific restaurant list in the Neo4j graph database.
     *
     * @param user   The username of the user
     * @param owner  The username of the owner of the restaurant list
     * @param title  The title of the restaurant list
     * @return true if the user is following the restaurant list, false otherwise
     */
    public static boolean isUserFollowingRestaurantList (String user, String owner, String title) {
        boolean res = false;
        try(Session session = driver.session()) {
            res = session.readTransaction(tx -> {
                Result r = tx.run("MATCH (a:User{username:$user})-[r:Follows]->(b:RestaurantList{title:$title, owner:$owner }) " +
                        "RETURN COUNT(*)", parameters("user", user, "title", title, "owner", owner));
                Record record = r.next();
                return record.get(0).asInt() != 0;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Checks if User A is following User B.
     *
     * @param usernameA the username of User A
     * @param usernameB the username of User B
     * @return true if User A is following User B, false otherwise
     */
    public static boolean isUserAFollowingUserB (String usernameA, String usernameB) {
        boolean res = false;
        try(Session session = driver.session()) {
            res = session.readTransaction(tx -> {
                Result r = tx.run("MATCH (a:User{username:$usernameA})-[r:Follows]->(b:User{username:$usernameB}) " +
                        "RETURN COUNT(*)", parameters("usernameA", usernameA, "usernameB", usernameB));
                Record record = r.next();
                return record.get(0).asInt() != 0;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Adds a like relationship between a user and a restaurant.
     *
     * @param username the username of the user
     * @param rest_id  the ID of the restaurant
     */
    public static void likeRestaurant(String username, String rest_id) {
        try(Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (a:User), (b:Restaurant) " +
                                "WHERE a.username = $username AND b.rest_id = $rest_id " +
                                "MERGE (a)-[r:Likes]->(b)",
                        parameters("username", username, "rest_id", rest_id));
                return null;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a "Likes" relationship between a user and a restaurant in the Neo4j graph database.
     *
     * @param username The username of the user
     * @param rest_id  The ID of the restaurant
     */
    public static void unlikeRestaurant(String username, String rest_id) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (a:User{username:$username})-[r:Likes]->(b:Restaurant) " +
                                "WHERE b.rest_id = $rest_id DELETE r",
                        parameters("username", username, "rest_id", rest_id));
                return null;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a "Follows" relationship between user A and user B in the Neo4j graph database.
     *
     * @param usernameA The username of user A
     * @param usernameB The username of user B
     */
    public static void userAFollowsUserB (String usernameA, String usernameB) {
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run("MATCH (u1:User {username: $usernameA}), (u2:User {username: $usernameB}) " +
                                "CREATE (u1)-[:Follows]->(u2)",
                        parameters("usernameA", usernameA, "usernameB", usernameB));
                return null;
            });
        }
    }

    /**
     * Removes the "Follows" relationship between user A and user B in the Neo4j graph database.
     *
     * @param usernameA The username of user A
     * @param usernameB The username of user B
     */
    public static void userAUnfollowUserB (String usernameA, String usernameB) {
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run("MATCH (:User {username: $usernameA})-[r:Follows]->(:User {username: $usernameB}) " +
                                "DELETE r",
                        parameters("usernameA", usernameA, "usernameB", usernameB));
                return null;
            });
        }
    }

    /**
     * Creates a "Follows" relationship between a user and a restaurant list in the Neo4j graph database.
     *
     * @param username The username of the user
     * @param title    The title of the restaurant list
     * @param owner    The username of the owner of the restaurant list
     */
    public static void userFollowRestaurantList (String username, String title, String owner) {
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run("MATCH (u1:User {username: $username}), (rl:RestaurantList {title: $title, owner: $owner}) " +
                                "CREATE (u1)-[:Follows]->(rl)",
                        parameters("username", username, "title", title, "owner", owner));
                return null;
            });
        }
    }

    /**
     * Removes the "Follows" relationship between a user and a restaurant list in the Neo4j graph database.
     *
     * @param username The username of the user
     * @param title    The title of the restaurant list
     * @param owner    The username of the owner of the restaurant list
     */
    public static void userUnfollowRestaurantList (String username, String title, String owner) {
        try (Session session = driver.session()){
            session.writeTransaction(tx -> {
                tx.run("MATCH (:User {username: $username})-[r:Follows]->(:RestaurantList {title: $title, owner: $owner}) " +
                                "DELETE r",
                        parameters("username", username, "title", title, "owner", owner));
                return null;
            });
        }
    }

    /**
     * Searches for restaurant lists in the Neo4j graph database based on a search title.
     * Returns a list of nodes representing the matching restaurant lists, ordered by the number of followers in descending order.
     *
     * @param searchTitle The search title to match against restaurant list titles
     * @return A list of nodes representing the matching restaurant lists
     */
    public static List<Node> searchRestaurantList(String searchTitle) {
        List<Node> nodeList = new ArrayList<>();

        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (rl:RestaurantList)
                                WHERE toLower(rl.title) CONTAINS toLower($searchTitle)
                                OPTIONAL MATCH (rl)<-[r:Follows]-()
                                RETURN rl, COUNT(r) AS numFollowers
                                ORDER BY numFollowers DESC""",
                        parameters("searchTitle", searchTitle));

                while (result.hasNext()) {
                    Record record = result.next();
                    Node restaurant = record.get("rl").asNode();
                    nodeList.add(restaurant);
                }

                return nodeList;
            });
        }
        return nodeList;
    }

    /* ********* ANALYTICS METHOD ********* */

    public static List<Node> getMostFollowedRestaurantList(int limit){
        List<Node> restNode = new ArrayList<>();
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (r:RestaurantList) <- [l:Follows]-()
                                RETURN r, COUNT(l) AS numFollowers
                                ORDER BY numFollowers DESC
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

    /**
     * Retrieves a list of nodes representing the users with the highest number of followers.
     *
     * @param limit the maximum number of users to return
     * @return a list of nodes representing the users with the highest number of followers
     */
    public static List<Node> getMostFollowedUsers(int limit){
        List<Node> restNode = new ArrayList<>();
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (u:User) <- [l:Follows]-()
                                RETURN u, COUNT(l) AS numFollowers
                                ORDER BY numFollowers DESC
                                LIMIT $limit""",
                        parameters("limit", limit));

                while (result.hasNext()) {
                    Record record = result.next();
                    Node restaurant = record.get("u").asNode();
                    restNode.add(restaurant);
                }

                return restNode;
            });
        }
        return restNode;
    }

    /**
     * Retrieves a list of suggested restaurants for a given user based on
     * their preferences and connections.
     *
     * @param username the username of the user for whom to suggest restaurants
     * @param limit the maximum number of restaurants to return
     * @return a list of suggested restaurants for the user
     */
    public static List<Node> getSuggestedRestaurant(String username, int limit){
        List<Node> restNode = new ArrayList<>();
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (me:User {username: $username})-[:Likes]->(r1:Restaurant)<-[:Likes]-(other:User)-[:Likes]->(similarRestaurant:Restaurant)
                                WHERE NOT EXISTS((me)-[:Likes]->(similarRestaurant))
                                AND other <> me
                                WITH DISTINCT similarRestaurant, COUNT(DISTINCT other) AS similarityScore
                                RETURN similarRestaurant AS r, similarityScore AS recommendationScore
                                ORDER BY recommendationScore DESC
                                LIMIT $limit
                                UNION
                                MATCH (me:User {username: $username})-[:Follows]->(other:User)-[:Likes]->(r2:Restaurant)
                                WHERE NOT EXISTS{
                                    MATCH (me:User {username: $username})-[:Likes]->(r1:Restaurant)<-[:Likes]-(other:User)-[:Likes]->(similarRestaurant:Restaurant)
                                    where r2 = similarRestaurant
                                }
                                RETURN DISTINCT r2 as r, 0 AS recommendationScore
                                LIMIT $limit
                                UNION
                                MATCH (r:Restaurant) <- [l:Likes]-()
                                RETURN r, COUNT(l) AS recommendationScore
                                ORDER BY recommendationScore DESC
                                LIMIT $limit""",
                        parameters("username", username, "limit", limit));

                int count = 0;
                while (result.hasNext() && count<limit) {
                    Record record = result.next();
                    Node restaurant = record.get("r").asNode();
                    restNode.add(restaurant);
                    count++;
                }

                return restNode;
            });
        }
        return restNode;
    }

    /**
     * Retrieves a list of suggested restaurant lists for a given user based on their connections.
     *
     * @param username the username of the user for whom to suggest restaurant lists
     * @param limit the maximum number of restaurant lists to return
     * @return a list of suggested restaurant lists for the user
     */
    public static List<Node> getSuggestedRestaurantList(String username, int limit){
        List<Node> nodeList = new ArrayList<>();
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (me:User {username: $username})-[:Follows]->(:User)-[:Follows]->(rl:RestaurantList)
                                WHERE NOT EXISTS {
                                  MATCH (me)-[:Follows]->(rl)
                                }
                                WITH rl, COUNT(DISTINCT me) AS followCount
                                RETURN rl, followCount AS recommendationScore
                                ORDER BY recommendationScore DESC
                                LIMIT $limit
                                UNION
                                MATCH (rl:RestaurantList) <- [l:Follows]-()
                                WITH rl, COUNT(l) as numFollowers
                                RETURN rl, numFollowers AS recommendationScore
                                ORDER BY numFollowers DESC
                                LIMIT $limit""",
                        parameters("username", username, "limit", limit));

                int count = 0;
                while (result.hasNext() && count < limit) {
                    Record record = result.next();
                    Node restaurant = record.get("rl").asNode();
                    nodeList.add(restaurant);
                    count++;
                }

                return nodeList;
            });
        }
        return nodeList;
    }

    /**
     * Retrieves a list of suggested users for a given user based on their connections.
     *
     * @param username the username of the user for whom to suggest users
     * @param limit the maximum number of users to return
     * @return a list of suggested users for the user
     */
    public static List<Node> getSuggestedUsers(String username, int limit){
        List<Node> nodeList = new ArrayList<>();
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (me:User {username: $username})-[:Follows]->(:User)-[:Follows]->(u:User)
                                WHERE NOT EXISTS ((me)-[:Follows]->(u))
                                AND u<>me
                                WITH u, COUNT(DISTINCT me) AS followCount
                                RETURN u, followCount AS recommendationScore
                                ORDER BY recommendationScore DESC
                                LIMIT $limit
                                UNION
                                MATCH (u:User) <- [l:Follows]-()
                                WITH u, COUNT(l) as numFollowers
                                RETURN u, numFollowers AS recommendationScore
                                ORDER BY numFollowers DESC
                                LIMIT $limit""",
                        parameters("username", username, "limit", limit));

                int count = 0;
                while (result.hasNext() && count<limit) {
                    Record record = result.next();
                    Node restaurant = record.get("u").asNode();
                    nodeList.add(restaurant);
                    count++;
                }
                return nodeList;
            });
        }
        return nodeList;
    }
}
