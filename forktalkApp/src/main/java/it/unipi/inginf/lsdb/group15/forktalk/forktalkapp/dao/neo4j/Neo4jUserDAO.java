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

    public static boolean isUserAFollowingUserB (String usernameA, String usernameB) {
        boolean res = false;
        try(Session session = driver.session()) {
            res = session.readTransaction(tx -> {
                Result r = tx.run("MATCH (a:User{username:$usernameA})-[r:Follows]->(b:User{username:$usernameB}) " +
                        "RETURN COUNT(*)", parameters("usernameA", usernameA, "usernameB", usernameB));
                Record record = r.next();
                if (record.get(0).asInt() == 0)
                    return false;
                else
                    return true;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

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

    public static List<Node> getMostFollowedRestaurantList(int limit){
        List<Node> restNode = new ArrayList<>();;
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

    public static List<Node> getMostFollowedUsers(int limit){
        List<Node> restNode = new ArrayList<>();;
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

    public static List<Node> getSuggestedRestaurant(String username, int limit){
        List<Node> restNode = new ArrayList<>();;
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
                                LIMIT $limit""",
                        parameters("username", username, "limit", limit));

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

    public static List<Node> getSuggestedRestaurantList(String username, int limit){
        List<Node> nodeList = new ArrayList<>();;
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
                                LIMIT $limit""",
                        parameters("username", username, "limit", limit));

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

    public static List<Node> getSuggestedUsers(String username, int limit){
        List<Node> nodeList = new ArrayList<>();;
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                var result = tx.run("""
                                MATCH (me:User {username: $username})-[:Follows]->(:User)-[:Follows]->(u:User)
                                WHERE NOT EXISTS ((me)-[:Follows]->(u))
                                AND u<>me
                                WITH u, COUNT(DISTINCT me) AS followCount
                                RETURN u, followCount AS recommendationScore
                                ORDER BY recommendationScore DESC
                                LIMIT $limit""",
                        parameters("username", username, "limit", limit));

                while (result.hasNext()) {
                    Record record = result.next();
                    Node restaurant = record.get("u").asNode();
                    nodeList.add(restaurant);
                }
                return nodeList;
            });
        }
        return nodeList;
    }
}
