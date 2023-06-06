package it.unipi.inginf.lsdb.group15.forktalk;

import it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBDriver;

public class Main {
    public static void main(String[] args) {
        MongoDBDriver.openConnection();
    }
}