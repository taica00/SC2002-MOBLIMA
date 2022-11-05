package main.ui;

import java.util.List;

import main.controllers.InputController;
import main.models.Movie;
import main.models.Review;

public class ReviewsList extends UI {
    public static void view(Movie movie) {
        List<Review> reviews = movie.getReviews();
        if (reviews.isEmpty())
            System.out.println("There are no reviews for this movie.\n");
        for (Review review : reviews)
            System.out.println(review + "\n");
        System.out.println("1. Add review | 2. Return to homepage");
        int choice = InputController.getInt(1, 2, "Enter your option: ");
        if (choice == 1) {
            String name = InputController.getString("Enter your name: ");
            int rating = InputController.getInt(1, 5, "Enter your rating for the movie (1-5[best]): ");
            String reviewContent = InputController.getString("Enter your review: \n");
            movie.addReview(new Review(name, reviewContent, rating));
            System.out.print("Review successfully added. ");
        }
        System.out.println("Returning to homepage.");
    }
}