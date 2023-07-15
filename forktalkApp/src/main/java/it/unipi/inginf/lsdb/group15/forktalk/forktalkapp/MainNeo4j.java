package it.unipi.inginf.lsdb.group15.forktalk.forktalkapp;


import it.unipi.inginf.lsdb.group15.forktalk.forktalkapp.dao.neo4j.Neo4jDriverDAO;

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
    }
}
