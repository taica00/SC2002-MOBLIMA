package main.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import main.models.Cinema;
import main.models.CinemaClass;
import main.models.Cineplex;
import main.models.Movie;
import main.models.Session;
import main.ui.TicketsPayment;
import main.ui.SeatsSelection;

/**
 * This class manages user actions pertaining to the {@link Session} class.
 * @author Tai Chen An
 * @version 1.1 
 * @since 2022-11-09 
 */

public class SessionController extends Controller {
    
    /**
     * Searches the given list of sessions for a session that matches the given fields.
     * Passes the session to {@link SeatsSelection}. 
     * @param movieSessions list of sessions of the movie
     * @param cineplexString location of the cineplex 
     * @param date date of session
     * @param cinemaClass class of cinema screening the session
     * @param time time of session
     * @return true if session that matches the given fields can be found
     */
    public static boolean viewSeating(List<List<Session>> movieSessions, String cineplexString, String date, String cinemaClass, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyHHmm");
        LocalDateTime dateTime = LocalDateTime.parse(date+time, formatter);
        for (List<Session> cineplexSessions : movieSessions) {
            Cinema cinema = cineplexSessions.get(0).getCinema();
            if (!cinema.getCineplex().getLocation().equalsIgnoreCase(cineplexString))
                continue;
            for (Session session : cineplexSessions) {
                if (dateTime.isEqual(session.getDateTime()) && session.getCinema().getCinemaClass().equals(CinemaClass.valueOf(cinemaClass))) {
                    SeatsSelection.view(session);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Searches the given cinema for a session that matches the given fields.
     * Passes the session to {@link SeatsSelection}. 
     * @param cinema cinema hosting the session
     * @param movie movie that session is screening
     * @param date date of session
     * @param cinemaClass class of cinema hosting the session
     * @param time time of session
     * @return true if session that matches the given fields can be found.
     */
    public static boolean viewSeating(Cineplex cineplex, String movie, String date, String cinemaClass, String time) {
        Session session = CineplexController.searchSession(cineplex, movie, date, cinemaClass, time);
        if (session == null) 
            return false;
        SeatsSelection.view(session);
        return true;
    }

    /**
     * Books the given array of seats for the given session.
     * If the string input is not a valid seat selection or that the seat selection is already booked, no seat will be booked.
     * Stores the booked seats in a list and passes it to {@link TicketsPayment}. 
     * @param session session to book tickets.
     * @param seats seats selected for booking.
     */
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
        TicketsPayment.view(session, bookedSeats);
    }
    
    /**
     * This method is called in the case where user does not complete payment.
     * The given list of seats will be marked back as available in the given session. 
     * @param session session to undo booking.
     * @param seats seats to mark back as available.
     */
    public static void undoBooking(Session session, List<String> seats) {
        for (String seat : seats) {
            char row = seat.charAt(0);
            int col = Integer.parseInt(seat.substring(1));
            session.getSeating().unBookSeat(row, col);
        }
    }
 
    /**
     * This method is called when admin requests to move the given session to another cinema. 
     * First search for a cinema in the same cineplex matching the given cinema class and cinema number.
     * If a cinema is found, update the given session with the new cinema.
     * @param session session to move to another cinema
     * @param cinemaClass class of cinema to search for
     * @param cinemaNumber number of cinema to search for
     * @return true if cinema can be found.
     */
    public static boolean updateCinema(Session session, String cinemaClass, int cinemaNumber) {
        Cinema cinema = session.getCinema().getCineplex().getCinema(CinemaClass.valueOf(cinemaClass), cinemaNumber);
        if (cinema == null)
            return false;
        session.setCinema(cinema);
        return true;
    }
    
    /**
     * This method is called when admin requests to change the movie screened by the given session. 
     * First search for a movie title matching the given string.
     * If a movie is found, update the given session with the new movie. 
     * @param session session to change movie screened.
     * @param movieString movie title to search for.
     * @return true if cinema can be found.
     */
    public static boolean updateMovie(Session session, String movieString) {
        Movie movie = MovieController.searchMovie(movieString);
        if (movie == null)
            return false;
        session.setMovie(movie);
        return true;
    }

    /**
     * This method is called when user requests to change the date for a session. 
     * The given string is parsed to a {@link LocalDate} and the given session is updated with the new date.
     * @param session session to change to another date
     * @param dateString string value of date
     */
    public static void updateDate(Session session, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        LocalTime time = session.getDateTime().toLocalTime();
        session.setDateTime(LocalDateTime.of(date, time));
    }

    /** 
     * This method is called when user requests to change the time for a session. 
     * The given string is parsed to a {@link LocalTime} and the given session is updated with the new time.
     * @param session session to change to another time
     * @param timeString string value of time
     */
    public static void updateTime(Session session, String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        LocalTime time = LocalTime.parse(timeString, formatter);
        LocalDate date = session.getDateTime().toLocalDate();
        session.setDateTime(LocalDateTime.of(date, time));
    }
}
