package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDriverDAO {
    public static Driver driver;

    /**
     * Opens a connection to Neo4j graph database.
     *
     * @return true if the connection is successfully opened, false otherwise
     */
    public static boolean openConnection() {
        try {
            driver = GraphDatabase.driver( "neo4j://10.1.1.18:7687", AuthTokens.basic( "neo4j", "12345678" ) );
            return driver != null;
        }catch (Exception ex){
            System.out.println("Impossible open connection with Neo4j");
            return false;
        }
    }

    /**
     * Closes the connection to the Neo4j graph database.
     */
    public static void closeConnection() {
        try{
            driver.close();
        }catch (Exception ex){
            System.out.println("Impossible close connection with Neo4j");
        }
    }
}
