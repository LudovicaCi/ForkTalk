package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp;


import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jDriverDAO;
import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO;
import org.neo4j.driver.types.Node;

import java.util.List;

import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jDriverDAO.driver;
import static it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jUserDAO.searchRestaurantList;

public class MainNeo4j {
    public static void main(String[] args) {
        Neo4jDriverDAO.openConnection();


        // Controlla se la connessione è aperta
        /*if (DriverDAO.driver != null) {
            System.out.println("Connessione a Neo4j aperta correttamente");

            // Esegui le operazioni desiderate sul database Neo4j

            DriverDAO.closeConnection();
            System.out.println("Connessione a Neo4j chiusa correttamente");
        } else {
            System.out.println("Impossibile aprire la connessione a Neo4j");
        } */

       /* String searchTitle = "food"; // Termine di ricerca parziale
        List<Node> restaurants = searchRestaurantList(searchTitle);

        // Stampa i risultati
        assert restaurants != null;
        for (Node restaurant : restaurants) {
            String title = restaurant.get("title").asString();
            String owner = restaurant.get("owner").asString();
            //long numFollowers = restaurant.get("numFollowers").asLong();
            System.out.println("Title: " + title);
            System.out.println("Author: " + owner);
            //System.out.println("Number of Followers: " + numFollowers);
            System.out.println("------------------------");
        } */

        Neo4jDriverDAO.closeConnection();
    }
}
