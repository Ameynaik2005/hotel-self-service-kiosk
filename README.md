# 🏨 Hotel Self-Service Kiosk

A console-based hotel self-service kiosk written in Java. Guests log in with a PIN, top up a wallet, book rooms, view their bookings and cancel for an instant refund. Data is saved to a text file between runs.

## Features

- **PIN login** – 3 attempts before access is denied
- **Wallet** – check balance and add money
- **Room booking** – choose a room type and number of nights; the cost is paid from the wallet
- **View bookings** – list all current bookings with ID, guest, room, nights and cost
- **Cancel booking** – full refund of the booking cost back to the wallet
- **Persistence** – wallet balance and bookings are saved to `data.txt` and reloaded on the next start
- **Input validation** – rejects non-numeric input, negative amounts, empty names and invalid room choices

## Room Types

| Room     | Price per night |
|----------|-----------------|
| Standard | Rs. 1500        |
| Deluxe   | Rs. 2500        |
| Suite    | Rs. 4000        |

## Project Structure

```
HotelKiosk/
└── src/com/hotel/kiosk/
    ├── Main.java      # Menu, login, booking/cancel logic, file save/load
    ├── Wallet.java    # Balance, PIN check, deposit, pay, refund
    └── Booking.java   # Booking details (id, guest, room, nights, cost)
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later

### Compile

```bash
cd HotelKiosk
mkdir out
javac -d out src/com/hotel/kiosk/*.java
```

### Run

```bash
java -cp out com.hotel.kiosk.Main
```

## Usage

The demo PIN is **`1234`** and the starting wallet balance is **Rs. 5000**.

```
===== HOTEL SELF-SERVICE KIOSK =====
Enter guest PIN: 1234

===== GUEST MENU =====
1. Check Wallet Balance
2. Add Money to Wallet
3. Book a Room
4. View Bookings
5. Cancel Booking
6. Exit
```

Example flow: book a Deluxe room for 2 nights (Rs. 5000 is deducted), view the booking, then cancel it with its ID to get Rs. 5000 refunded.

## Data Storage

Data is stored in `data.txt` in the folder you run the program from:

```
<wallet balance>
<id>,<guest name>,<room type>,<nights>,<cost>
```

Delete `data.txt` to reset the kiosk to its starting state.

## Limitations

- Single hard-coded demo PIN and a single wallet (no multiple guests or accounts)
- PIN is stored in plain code and data in a plain text file, so this is not secure for real use
- No room availability or date handling; bookings are by number of nights only
- Console interface only

## Future Improvements

- Multiple guest accounts with hashed PINs
- Room availability and check-in/check-out dates
- Database storage instead of a text file
- GUI (Swing/JavaFX)
- Unit tests for `Wallet` and booking logic

## Author

Amey Naik – [@Ameynaik2005](https://github.com/Ameynaik2005)
