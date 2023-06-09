package it.unipi.inginf.lsdb.group15.forktalk.dto;

import java.util.Date;

public class ReservationDTO {
    public class Reservation {
        private String restaurantName;
        private String clientUsername;
        private String restaurantUsername;
        private Date date;
        private int people;

        // METODI SET

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public void setClientUsername(String clientUsername) {
            this.clientUsername = clientUsername;
        }

        public void setRestaurantUsername(String restaurantUsername) {
            this.restaurantUsername = restaurantUsername;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setPeople(int people) {
            this.people = people;
        }
        // METODI GET

        public String getRestaurantName() {
            return restaurantName;
        }

        public String getClientUsername() {
            return clientUsername;
        }

        public String getRestaurantUsername() {
            return restaurantUsername;
        }

        public Date getDate() {
            return date;
        }

        public int getPeople() {
            return people;
        }
    }
}
