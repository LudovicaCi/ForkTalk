package it.unipi.inginf.lsdb.group15.forktalk;

import it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.MongoDBDriverDAO;
import it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.MongoDBUserDAO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.RestaurantsListDTO;
import it.unipi.inginf.lsdb.group15.forktalk.dto.UserDTO;
import it.unipi.inginf.lsdb.group15.forktalk.model.User;

import java.io.IOException;
import java.util.ArrayList;

import static it.unipi.inginf.lsdb.group15.forktalk.dao.mongoDB.MongoDBUserDAO.getRestaurantListsOfUser;

public class Main {
    public static void main(String[] args) throws IOException {
        MongoDBDriverDAO.openConnection();
        //login: FUNZIONA
       /* UserDTO userDTO = MongoDBUserDAO.loginUser("ianmW7519KN", "cbzuu0Bq");
        String userDTOString = userDTO.toString();  // Assegna la rappresentazione in forma di stringa a una variabile
        System.out.println(userDTOString); */

        //Register a new user: FUNZIONA
        /*User newUser = new User("f.rossi87@gmail.com", "filo87", "ssjj283ss","Filippo", "Rossi", "Milan");
        System.out.println(MongoDBUserDAO.registerUser(newUser)); */

        //remove user: FUNZIONA
        //System.out.println(MongoDBUserDAO.deleteUser("filo87"));

        //update info user: FUNZIONA
        /*UserDTO userDTO = new UserDTO("f_rossi87@gmail.com", "filo87", "ssjj283ss","Filippo", "Rossi", "Rome");
        System.out.println(MongoDBUserDAO.updateUser("filo87", userDTO)); */

        //get user by username: FUNZIONA
        /*UserDTO userDTO = MongoDBUserDAO.getUserByUsername("ianmW7519KN");
        assert userDTO != null;
        String userDTOString = userDTO.toString();  // Assegna la rappresentazione in forma di stringa a una variabile
        System.out.println(userDTOString);*/

        //create RestaurantsList: FUNZIONA
        //System.out.println(MongoDBUserDAO.createRestaurantListToUser(MongoDBUserDAO.getUserByUsername("filo87"), "Best London Fast Food"));

        //delete RestaurantList: FUNZIONA
        //System.out.println(MongoDBUserDAO.deleteRestaurantListFromUser(MongoDBUserDAO.getUserByUsername("filo87"), "Favorite Sushi Restaurant"));

        //get RestaurantsList: FUNZIONA
        /*ArrayList<RestaurantsListDTO> restaurantLists = getRestaurantListsOfUser(MongoDBUserDAO.getUserByUsername("filo87"));

        if (restaurantLists != null) {
            for (RestaurantsListDTO list : restaurantLists) {
                System.out.println(list.toString());
            }
        } else {
            System.err.println("ERROR: Failed to retrieve restaurant lists.");
        } */

        //add a restaurant to a restaurants list
        RestaurantDTO new_rest = new RestaurantDTO("g10259438-d19087750", "Band of Burgers");
        //System.out.println(MongoDBUserDAO.addRestaurantToList(MongoDBUserDAO.getUserByUsername("filo87"), "Best London Fast Food", new_rest));


        //delete a restaurant from a restaurants list
        //System.out.println(MongoDBUserDAO.removeRestaurantFromList(MongoDBUserDAO.getUserByUsername("filo87"), "Best London Fast Food", new_rest.getId()));
        MongoDBDriverDAO.closeConnection();
    }

   /* private static final MongoDBDriverDAO mdb = new MongoDBDriverDAO();
    private static final MongoDBUserDAO mdbuser = new MongoDBUserDAO();
    private static final MongoDBAdministratorDAO mdbadmin = new MongoDBAdministratorDAO();
    private static final MongoDBRestaurantDAO mdbrest = new MongoDBRestaurantDAO();
    private static InputStreamReader input = new InputStreamReader(System.in);

    private static BufferedReader tastiera = new BufferedReader(input);

    public static void main(String[] args) throws IOException {
        MongoDBDriverDAO.openConnection();
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


            MongoDBDriverDAO.closeConnection();


        }
    } */
}