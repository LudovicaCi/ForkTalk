package it.unipi.inginf.lsdb.group15.forktalk;

import it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBAdministrator;
import it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBDriver;
import it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBRestaurant;
import it.unipi.inginf.lsdb.group15.forktalk.connection.MongoDBUser;
import it.unipi.inginf.lsdb.group15.forktalk.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static final MongoDBDriver mdb = new MongoDBDriver();
    private static final MongoDBUser mdbuser = new MongoDBUser();
    private static final MongoDBAdministrator mdbadmin = new MongoDBAdministrator();
    private static final MongoDBRestaurant mdbrest = new MongoDBRestaurant();
    private static InputStreamReader input = new InputStreamReader(System.in);

    private static BufferedReader tastiera = new BufferedReader(input);

    public static void main(String[] args) throws IOException {
        MongoDBDriver.openConnection();
        ;
        while (true) {

            System.out.println("\nSelect a command: \nI'm a:" +
                    "\n1. RESTAURANT" +
                    "\n2. USER" +
                    "\n3. ADMINISTRATOR ");


            String option = tastiera.readLine();
            switch (option) {
                case "1":

                    System.out.println("--RESTAURANT--\nWhat do you want to do? " +
                            "\n1. [Login]" +
                            "\n2. [Sign up]" +
                            "\n\n0. [Go back]");


                    String option_doctor = tastiera.readLine();
                case "2":
                    System.out.println("--USER--\nSelect a command: " +
                            "\n1. [LOGIN]" +
                            "\n2. [SIGN UP]" +
                            "\n\n0. [Shut down the application]");


                    String option_user = tastiera.readLine();

                    switch (option_user)
                    {
                        case "1": //LOGIN USER
                            while (true)
                            {
                                System.out.println("--LOGIN USER--\nInsert the username");
                                String username = tastiera.readLine();
                                System.out.println("Insert the password");
                                String password = tastiera.readLine();

                                // aggiungere costruttore

                                if (!mdbuser.loginUser(username, password)) {
                                    System.out.println("Please retry");
                                } else
                                {



                                }


            MongoDBDriver.closeConnection();


        }
    }
}