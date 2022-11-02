package main.ui;

import main.controllers.InputController;
import main.controllers.TransactionsController;
import main.models.MovieGoer;
import main.models.Transaction;

public class BookingHistoryUI extends UI {
    public static void view() {
        String email = InputController.getString("Enter email address: ");
        MovieGoer movieGoer = TransactionsController.getMovieGoer(email);
        if (movieGoer == null) {
            System.out.println("No transactions found. Returning to homepage.");
            return;
        }
        System.out.println("Past transactions found. Please confirm your identity.");
        if (!InputController.confirmIdentity(movieGoer)) {
            System.out.println("Returning to homepage.");
            return;
        }
        System.out.println("\nBOOKING HISTORY:\n");
        for (Transaction transaction : movieGoer.getTransactions())
            System.out.println(transaction);
        InputController.getString("Enter any key to return to homepage.\n");
    }
}
