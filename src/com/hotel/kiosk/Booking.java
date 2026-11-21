
package com.hotel.kiosk;

public class Booking {

    private int id;
    private String guestName;
    private String roomType;
    private int nights;
    private double cost;

    public Booking(int id, String guestName, String roomType,
                   int nights, double cost) {

        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getCost() {
        return cost;
    }

    public void display() {
        System.out.println("ID: " + id + " | " + guestName
                + " | " + roomType + " | " + nights
                + " nights | Rs. " + cost);
    }
}
