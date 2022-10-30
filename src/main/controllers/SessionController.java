package main.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import main.models.Cinema;
import main.models.CinemaClass;
import main.models.Session;
import main.ui.BookingUI;
import main.ui.SeatingUI;

public class SessionController extends Controller {
    public static void viewSeating(List<List<Session>> movieSessions, String cinemaCode, String date, String cinemaClass, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyHHmm");
        LocalDateTime dateTime = LocalDateTime.parse(date+time, formatter);
        for (List<Session> cinemaSessions : movieSessions) {
            for (Session session : cinemaSessions) {
                if (!session.getCinema().getCinemaCode().equals(cinemaCode))
                    break;
                if (dateTime.isEqual(session.getDateTime()) && CinemaClass.valueOf(cinemaClass).equals(session.getCinemaClass())) {
                    SeatingUI.view(session);
                    return;
                }
            }
        }
        System.out.println("Session not found. Returning to homepage.");
    }

    public static void viewSeating(Cinema cinema, String movie, String date, String cinemaClass, String time) {
        Session session = CineplexController.searchSession(cinema, movie, date, cinemaClass, time);
        if (session == null) {
            System.out.println("Session not found. Returning to homepage.");
            return;
        }
        SeatingUI.view(session);
    }

    public static void bookSeats(Session session, String[] seats) {
        List<String> bookedSeats = new ArrayList<>();
        for (String seat : seats) {
            char row = seat.charAt(0);
            String col = seat.substring(1);
            if (!Character.isUpperCase(row) || !StringUtils.isNumeric(col) || Integer.parseInt(col) == 0 || Integer.parseInt(col) > session.getSeating().getNumCols()) 
                System.out.println(seat + " is not a valid selection.");
            else if (!session.getSeating().bookSeat(row, Integer.parseInt(col))) 
                System.out.println("Seat " + seat + " is occupied");
            else
                bookedSeats.add(seat);
        }
        if (bookedSeats.isEmpty()) {
            System.out.println("No seats booked. Returning to homepage.");
            return;
        }
        BookingUI.view(session, bookedSeats);
    }

    public static void undoBooking(Session session, List<String> seats) {
        for (String seat : seats) {
            char row = seat.charAt(0);
            int col = Integer.parseInt(seat.substring(1));
            session.getSeating().unBookSeat(row, col);
        }
    }
}
