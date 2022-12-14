package main.utils;

import java.util.List;
import java.util.Random;

import main.controllers.TransactionsController;
import main.models.Cinema;
import main.models.Cineplex;
import main.models.Seating;
import main.models.Session;

public class PopulateSessionSeats extends Populator {  
    public static void main(String[] args) {
        TransactionsController.loadTicketSales();
        List<Cineplex> cineplexes = deserialise("cineplexes.ser");
        for (Cineplex cineplex : cineplexes) {
            for (Cinema cinema : cineplex.getCinemas()) {
                for (Session session : cinema.getSessions())
                    helper(session);
            }
        }
        serialize(cineplexes, "cineplexes.ser");
        TransactionsController.saveTicketSales();
    }

    private static void helper(Session session) {
        Random rd = new Random();
        Seating seating = session.getSeating();
        int rows = seating.getNumRows();
        int cols = seating.getNumCols();
        unBookAllSeats(session, rows, cols);
        int numSeatsToBook = rd.nextInt(cols * rows * 1/2);
        int actualSeatsBooked = 0;
        for (int i = 0; i < numSeatsToBook; i++) {
            char row = (char)('A' + rd.nextInt(rows));
            int col = rd.nextInt(cols) + 1;
            if (seating.bookSeat(row, col))
                actualSeatsBooked++;     
        }
        TransactionsController.addTicketSales(session.getMovie(), actualSeatsBooked);
    }

    private static void unBookAllSeats(Session session, int rows, int cols) {
        Seating seating = session.getSeating();
        for (int i = 0; i < rows; i++) {
            for (int j = 1; j <= cols; j++) {
                seating.unBookSeat((char)('A' + i), j);
            }
        }
    }
}
