
package com.hotel.kiosk;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String FILE_NAME = "data.txt";
    private static final int DEMO_PIN = 1234;
    private static final double START_BALANCE = 5000;

    private static int readInt(Scanner sc, String message) {

        while (true) {
            System.out.print(message);

            if (sc.hasNextInt()) {
                return sc.nextInt();
            }

            System.out.println("Please enter a valid number.");
            sc.next();
        }
    }

    private static double readDouble(Scanner sc, String message) {

        while (true) {
            System.out.print(message);

            if (sc.hasNextDouble()) {
                return sc.nextDouble();
            }

            System.out.println("Please enter a valid amount.");
            sc.next();
        }
    }

    private static void saveData(Wallet wallet,
                                 ArrayList<Booking> bookings) {

        try (PrintWriter pw = new PrintWriter(
                new FileWriter(FILE_NAME))) {

            pw.println(wallet.getBalance());

            for (Booking b : bookings) {

                pw.println(b.getId() + ","
                        + b.getGuestName() + ","
                        + b.getRoomType() + ","
                        + b.getNights() + ","
                        + b.getCost());
            }

        } catch (Exception e) {
            System.out.println("Could not save data: "
                    + e.getMessage());
        }
    }

    private static double loadData(ArrayList<Booking> bookings) {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return START_BALANCE;
        }

        double balance = START_BALANCE;

        try (Scanner fs = new Scanner(file)) {

            if (!fs.hasNextLine()) {
                return START_BALANCE;
            }

            balance = Double.parseDouble(fs.nextLine());

            while (fs.hasNextLine()) {

                String[] p = fs.nextLine().split(",", -1);

                if (p.length != 5) {
                    continue;
                }

                int id = Integer.parseInt(p[0]);
                String name = p[1];
                String room = p[2];
                int nights = Integer.parseInt(p[3]);
                double cost = Double.parseDouble(p[4]);

                if (id > 0 && !name.trim().isEmpty()
                        && nights > 0 && cost > 0
                        && (room.equals("Standard")
                        || room.equals("Deluxe")
                        || room.equals("Suite"))) {

                    bookings.add(new Booking(
                            id, name, room, nights, cost));
                }
            }

            return Math.max(0, balance);

        } catch (Exception e) {

            System.out.println("Saved data could not be read.");
            bookings.clear();

            return START_BALANCE;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Booking> bookings = new ArrayList<>();

        double balance = loadData(bookings);

        Wallet wallet = new Wallet(DEMO_PIN, balance);

        int nextId = 1;

        for (Booking b : bookings) {

            if (b.getId() >= nextId) {
                nextId = b.getId() + 1;
            }
        }

        System.out.println("===== HOTEL SELF-SERVICE KIOSK =====");

        // PIN Authentication

        int attempts = 3;
        boolean authenticated = false;

        while (attempts > 0) {

            int enteredPin = readInt(sc, "Enter guest PIN: ");

            if (wallet.checkPin(enteredPin)) {
                authenticated = true;
                break;
            }

            attempts--;

            System.out.println("Incorrect PIN. Attempts remaining: "
                    + attempts);
        }

        if (!authenticated) {

            System.out.println("Access denied.");
            sc.close();
            return;
        }

        int choice;

        do {

            System.out.println("\n===== GUEST MENU =====");
            System.out.println("1. Check Wallet Balance");
            System.out.println("2. Add Money to Wallet");
            System.out.println("3. Book a Room");
            System.out.println("4. View Bookings");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");

            choice = readInt(sc, "Enter choice: ");

            switch (choice) {

                case 1:

                    System.out.printf("Wallet balance: Rs. %.2f%n",
                            wallet.getBalance());
                    break;

                case 2:

                    double amount = readDouble(
                            sc, "Enter amount to add: Rs. ");

                    if (wallet.deposit(amount)) {

                        saveData(wallet, bookings);

                        System.out.printf(
                                "Money added. New balance: Rs. %.2f%n",
                                wallet.getBalance());

                    } else {
                        System.out.println(
                                "Amount must be greater than zero.");
                    }

                    break;

                case 3:

                    sc.nextLine();

                    System.out.print("Enter guest name: ");

                    String name = sc.nextLine().trim()
                            .replace(",", "");

                    System.out.println("\nRoom Types:");
                    System.out.println("1. Standard - Rs. 1500 per night");
                    System.out.println("2. Deluxe - Rs. 2500 per night");
                    System.out.println("3. Suite - Rs. 4000 per night");

                    int roomChoice = readInt(sc,
                            "Choose room type: ");

                    int nights = readInt(sc,
                            "Enter number of nights: ");

                    String roomName = "";
                    double rate = 0;

                    switch (roomChoice) {

                        case 1:
                            roomName = "Standard";
                            rate = 1500;
                            break;

                        case 2:
                            roomName = "Deluxe";
                            rate = 2500;
                            break;

                        case 3:
                            roomName = "Suite";
                            rate = 4000;
                            break;

                        default:
                            System.out.println("Invalid room type.");
                    }

                    if (name.isEmpty() || rate == 0 || nights <= 0) {

                        System.out.println(
                                "Please enter valid booking details.");

                    } else {

                        double total = rate * nights;

                        if (wallet.pay(total)) {

                            Booking booking = new Booking(
                                    nextId, name, roomName,
                                    nights, total);

                            bookings.add(booking);

                            saveData(wallet, bookings);

                            System.out.println(
                                    "Room booked successfully!");

                            System.out.println(
                                    "Your booking ID is: " + nextId);

                            System.out.printf(
                                    "Amount paid: Rs. %.2f%n", total);

                            nextId++;

                        } else {

                            System.out.println(
                                    "Insufficient wallet balance.");
                        }
                    }

                    break;

                case 4:

                    if (bookings.isEmpty()) {

                        System.out.println("No bookings found.");

                    } else {

                        for (Booking b : bookings) {
                            b.display();
                        }
                    }

                    break;

                case 5:

                    if (bookings.isEmpty()) {

                        System.out.println(
                                "There are no bookings to cancel.");

                        break;
                    }

                    int id = readInt(sc,
                            "Enter booking ID to cancel: ");

                    Booking found = null;

                    for (Booking b : bookings) {

                        if (b.getId() == id) {
                            found = b;
                            break;
                        }
                    }

                    if (found == null) {

                        System.out.println("Booking ID not found.");

                    } else {

                        double refundAmount = found.getCost();

                        wallet.refund(refundAmount);

                        bookings.remove(found);

                        saveData(wallet, bookings);

                        System.out.printf(
                                "Booking cancelled. Rs. %.2f refunded.%n",
                                refundAmount);
                    }

                    break;

                case 6:

                    saveData(wallet, bookings);

                    System.out.println(
                            "Thank you for using the Hotel Kiosk!");

                    break;

                default:

                    System.out.println("Invalid menu choice.");
            }

        } while (choice != 6);

        sc.close();
    }
}


