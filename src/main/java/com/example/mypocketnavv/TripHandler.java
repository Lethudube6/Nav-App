package com.example.mypocketnavv;

public class TripHandler {
    String email,
            Departure_address1,
            Departure_address2,
            Destination_address1,
            Destination_address2,
            Date_of_trip;

    public TripHandler(){}

    public TripHandler(String email, String departure_address1, String departure_address2, String destination_address1, String destination_address2, String date_of_trip) {
        this.email = email;
        this.Departure_address1 = departure_address1;
        this.Departure_address2 = departure_address2;
        this.Destination_address1 = destination_address1;
        this.Destination_address2 = destination_address2;
        this.Date_of_trip = date_of_trip;
    }


    public String getEmail() {
        return email;
    }

    public String getDeparture_address1() {
        return Departure_address1;
    }

    public String getDeparture_address2() {
        return Departure_address2;
    }

    public String getDestination_address1() {
        return Destination_address1;
    }

    public String getDestination_address2() {
        return Destination_address2;
    }

    public String getDate_of_trip() {
        return Date_of_trip;
    }


    /////////my setters


    public void setEmail(String email) {
        this.email = email;
    }

    public void setDeparture_address1(String departure_address1) {
        Departure_address1 = departure_address1;
    }

    public void setDeparture_address2(String departure_address2) {
        Departure_address2 = departure_address2;
    }

    public void setDestination_address1(String destination_address1) {
        Destination_address1 = destination_address1;
    }

    public void setDestination_address2(String destination_address2) {
        Destination_address2 = destination_address2;
    }

    public void setDate_of_trip(String date_of_trip) {
        Date_of_trip = date_of_trip;
    }
}
