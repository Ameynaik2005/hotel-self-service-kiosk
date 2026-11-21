
package com.hotel.kiosk;

public class Wallet {

    private double balance;
    private int pin;

    public Wallet(int pin, double balance) {
        this.pin = pin;
        this.balance = balance;
    }

    public boolean checkPin(int enteredPin) {
        return enteredPin == pin;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {

        if (amount <= 0) {
            return false;
        }

        balance += amount;
        return true;
    }

    public boolean pay(double amount) {

        if (amount <= 0 || amount > balance) {
            return false;
        }

        balance -= amount;
        return true;
    }

    public void refund(double amount) {

        if (amount > 0) {
            balance += amount;
        }
    }
}
