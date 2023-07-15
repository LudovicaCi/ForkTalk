package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.mongoDB.UserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.UserDTO;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;

import static org.neo4j.driver.Values.parameters;

public class Neo4jUserDAO {
    public static boolean addUser(UserDTO user){
        boolean res;
        try (Session session = Neo4jDriverDAO.driver.session()){
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
        try (Session session = Neo4jDriverDAO.driver.session()){
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
        try (Session session = Neo4jDriverDAO.driver.session()){
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

    public static boolean deleteRestaurantList(UserDTO user, String title){
        boolean res;
        try (Session session = Neo4jDriverDAO.driver.session()){
            res = session.writeTransaction(tx -> {
                tx.run("MATCH (r:RestaurantList) WHERE r.title = $title AND r.owner = $username DETACH DELETE r",
                        parameters("title", title, "username", user.getUsername()));

                return true;
            });
        }catch (Neo4jException e){
            e.printStackTrace();
            return false;
        }
        return res;
    }
}
