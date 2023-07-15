package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j;

import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dto.RestaurantDTO;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.Neo4jException;

import static org.neo4j.driver.Values.parameters;

public class Neo4jRestaurantDAO {
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
}
