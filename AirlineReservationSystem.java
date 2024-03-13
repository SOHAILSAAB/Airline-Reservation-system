import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class AirlineReservationSystem{
    static boolean[] economySeats = new boolean[100]; // Represents the economy class seats, true means seat is booked
    static boolean[] businessSeats = new boolean[25]; // Represents the business class seats, true means seat is booked
    static String[][] passengerInfo = new String[125][7]; // Store passenger information for booked seats (100 economy + 25 business)
    static LocalDateTime[] bookingDates = new LocalDateTime[125]; // Store booking dates for booked seats (100 economy + 25 business)
    static final String AIRLINE_NAME = "Indian Airlines";
    static final String FLIGHT_NUMBER = "IA101"; // Flight number
    static final double TICKET_PRICE_ECONOMY = 6500.0; // Price of the economy class ticket
    static final double TICKET_PRICE_BUSINESS = 17000.0; // Price of the business class ticket
    static final String START_POINT = "Delhi"; // Starting point of the flight
    static final String END_POINT = "Kolkata"; // End point of the flight
    static final ZoneId IST_ZONE = ZoneId.of("Asia/Kolkata");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("------------------------------------------------------------------");
            System.out.println("------------------------" + AIRLINE_NAME + "---------------------------");
            System.out.println("------------------------------------------------------------------");
            System.out.println("\n\nFrom: " + START_POINT + "                               " + "        To: " + END_POINT);
            System.out.println("\n\n----------------------*Select Your Choice*----------------------\n\n");
            System.out.println("           ||           1. Book a seat                ||\n\n");
            System.out.println("           ||           2. View available seats       ||\n\n");
            System.out.println("           ||           3. Cancel booking             ||\n\n");
            System.out.println("           ||           4. Exit                       ||\n\n");
            System.out.println("------------------------------------------------------------------\n\n");
            System.out.print("            Please Enter your choice : ");
            int choice = scanner.nextInt();
            System.out.println();
            switch (choice) {
                case 1:
                    bookSeat(scanner);
                    break;
                case 2:
                    viewAvailableSeats();
                    break;
                case 3:
                    cancelBooking(scanner);
                    break;
                case 4:
                    System.out.println("\n            Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n            Invalid choice. Please try again.");
            }
        }
    }

    static void bookSeat(Scanner scanner) {
        System.out.print("            Choose class (1. Economy, 2. Business): ");
        int classChoice = scanner.nextInt();
        String classType;
        double ticketPrice;
        int maxSeatNumber;
        if (classChoice == 1) {
            classType = "Economy";
            ticketPrice = TICKET_PRICE_ECONOMY;
            maxSeatNumber = 100;
        } else if (classChoice == 2) {
            classType = "Business";
            ticketPrice = TICKET_PRICE_BUSINESS;
            maxSeatNumber = 25;
        } else {
            System.out.println("\n            Invalid choice for class. Booking aborted.");
            return;
        }
        System.out.print("\n            Enter the seat number to book (1-" + maxSeatNumber + "): ");
        int numSeats = scanner.nextInt();
        if (numSeats < 1 || numSeats > maxSeatNumber) {
            System.out.println("\n            OOPS!! Invalid seat number.");
            return;
        }
        int[] availableSeats = findAvailableSeats(classChoice, numSeats);
        if (availableSeats.length < numSeats) {
            System.out.println("\n            Insufficient available seats.");
            return;
        }
        for (int i = 0; i < numSeats; i++) {
            bookPassenger(scanner, availableSeats[i], classType, ticketPrice);
        }
    }

    static int[] findAvailableSeats(int classChoice, int numSeats) {
        int maxSeatNumber;
        boolean[] seats;
        if (classChoice == 1) {
            maxSeatNumber = 100;
            seats = economySeats;
        } else {
            maxSeatNumber = 25;
            seats = businessSeats;
        }
        int[] availableSeats = new int[numSeats];
        int count = 0;
        for (int i = 0; i < maxSeatNumber && count < numSeats; i++) {
            if (!seats[i]) {
                availableSeats[count++] = i + 1;
            }
        }
        return availableSeats;
    }

    static void bookPassenger(Scanner scanner, int seatNumber, String classType, double ticketPrice) {
        scanner.nextLine(); // Consume newline character
        System.out.println();
        System.out.print("            Enter passenger name: ");
        String passengerName = scanner.nextLine();
        System.out.print("\n            Enter passenger gender: ");
        String gender = scanner.nextLine();
        System.out.print("\n            Enter passenger age: ");
        String age = scanner.nextLine();
        System.out.print("\n            Enter passenger mobile number: ");
        String mobileNumber = scanner.nextLine();
        System.out.print("\n            Enter passenger email ID: ");
        String email = scanner.nextLine();

        // Display message for 5 seconds
        System.out.println("\n            Booking your ticket...");
        try {
            Thread.sleep(5000); // 5 seconds pause
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (classType.equals("Economy")) {
            economySeats[seatNumber - 1] = true;
        } else {
            businessSeats[seatNumber - 1] = true;
        }
        passengerInfo[seatNumber - 1] = new String[]{passengerName, gender, age, mobileNumber, email, Integer.toString(seatNumber), classType};
        // Calculate departure date and time (1 day after booking)
        LocalDateTime departureDateTime = LocalDateTime.now(IST_ZONE).plusDays(1).withHour(17).withMinute(0).withSecond(0);
        bookingDates[seatNumber - 1] = departureDateTime;
        System.out.println("\n            Seat " + seatNumber + " booked successfully for " + passengerName + ".");
        displayBill(seatNumber - 1, ticketPrice, classType);
    }

    static void viewAvailableSeats() {
        System.out.println("\n            Available seats:");

        // Display available economy class seats
        System.out.println("\n            Economy Class: " + countAvailableSeats(economySeats) + " seats available");

        // Display available business class seats
        System.out.println("\n            Business Class: " + countAvailableSeats(businessSeats) + " seats available");
    }

    static int countAvailableSeats(boolean[] seats) {
        int count = 0;
        for (boolean seat : seats) {
            if (!seat) {
                count++;
            }
        }
        return count;
    }

    static void cancelBooking(Scanner scanner) {
        System.out.print("            Enter seat number to cancel booking: ");
        int seatNumber = scanner.nextInt();
        if (seatNumber < 1 || seatNumber > 125) {
            System.out.println("\n            Invalid seat number.");
            return;
        }
        if (seatNumber <= 100 && !economySeats[seatNumber - 1]) {
            System.out.println("\n            Seat is not booked.");
            return;
        }
        if (seatNumber > 100 && !businessSeats[seatNumber - 101]) {
            System.out.println("\n            Seat is not booked.");
            return;
        }
        String passengerName = passengerInfo[seatNumber - 1][0];
        if (seatNumber <= 100) {
            economySeats[seatNumber - 1] = false;
        } else {
            businessSeats[seatNumber - 101] = false;
        }
        passengerInfo[seatNumber - 1] = null;
        bookingDates[seatNumber - 1] = null;
        System.out.println("\n            Booking canceled successfully for " + passengerName + ".");
    }
    static void displayBill(int seatIndex, double ticketPrice, String classType) {
        System.out.println();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a");
        LocalDateTime departureDateTime = bookingDates[seatIndex];
        LocalDateTime arrivalDateTime = departureDateTime.withHour(23).withMinute(0).withSecond(0);
        LocalDateTime bookingDateTime = LocalDateTime.now(IST_ZONE);
        System.out.println("\n**BILL");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("\n__Booking  Details");
        System.out.println("\nFlight Number: " + FLIGHT_NUMBER);
        System.out.println("\nFrom: " + START_POINT +"   ---------------------->      To: " + END_POINT);
        System.out.println(departureDateTime.format(DateTimeFormatter.ofPattern("dd MMMM,yyyy")) + "                              " + arrivalDateTime.format(DateTimeFormatter.ofPattern("dd MMMM,yyyy")));
        System.out.println(departureDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")) + "      ---------6h0m--------->      "+ arrivalDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        System.out.println("\nPassenger Name: " + passengerInfo[seatIndex][0]);
        System.out.println("\nGender "+ passengerInfo[seatIndex][1] + "                       " + "age " + passengerInfo[seatIndex][2]);
        System.out.println("\nMobile Number: " + passengerInfo[seatIndex][3] + "               "+"Email ID: " + passengerInfo[seatIndex][4]);
        System.out.println("\nFlight Number: " + FLIGHT_NUMBER + "           " +"Seat Number: " + passengerInfo[seatIndex][5] );
        System.out.println("\nClass: " + classType +"                 "+ "Ticket Price: Rs." + ticketPrice);
        System.out.println("\nDate of Booking: " + bookingDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM,yyyy "))+ bookingDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        System.out.println("\n               Thank you for booking with " + AIRLINE_NAME + "!!!!!!\n\n");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("");
    }
}