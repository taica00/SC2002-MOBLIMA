package main.ui;

import main.controllers.InputController;
import main.controllers.SessionController;
import main.models.CinemaClass;
import main.models.Session;

public class UpdateSessionUI extends UI {
    public static void view(Session session) {
        String choice = "";
        while (true) {
            System.out.println("\n" + session + "\n");
            choice = InputController.getString("Enter field(in lowercase) to update or '0' to return to admin menu: ");
            if (choice.equals("0"))
                return;
            switch(choice) {
                case "cinema": 
                    String cinema = InputController.getString("Enter cinema location, e.g Funan: ");
                    if (SessionController.updateCinema(session, cinema))
                        System.out.println("Cinema updated.");
                    else
                        System.out.println("Cinema not found.");
                    break;
                case "movie": 
                    String movie = InputController.getString("Enter title of movie, e.g Black Adam: ");
                    if (SessionController.updateMovie(session, movie))
                        System.out.println("Movie updated.");
                    else
                        System.out.println("Movie not found.");
                    break;
                case "date": 
                    String date = InputController.getNumericString("Enter date in ddMMyy format: ", 6, 6);
                    SessionController.updateDate(session, date);
                    break;
                case "time": 
                    String time = InputController.getNumericString("Enter time in HHmm format: ", 4, 4);
                    SessionController.updateDate(session, time);
                    break;
                case "cinema class": 
                    String cinemaClass = InputController.getString("Enter cinema class, replace spaces with underscores: ");
                    session.setCinemaClass(CinemaClass.valueOf(cinemaClass.toUpperCase()));
                    break;
                case "3D Screening": 
                    boolean is3D = InputController.getBoolean("Set this session as 3D?", 'Y', 'N');
                    session.set3D(is3D);
                    break;
                default: System.out.println("Invalid input. Please try again."); 
            }
        } 
    }
}