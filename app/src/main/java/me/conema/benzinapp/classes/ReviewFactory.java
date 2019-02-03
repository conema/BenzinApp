package me.conema.benzinapp.classes;

import java.util.ArrayList;

public class ReviewFactory {
    private static ReviewFactory singleton;
    private ArrayList<Review> reviewList = new ArrayList<>();

    public static ReviewFactory getInstance() {
        if (singleton == null) {
            singleton = new ReviewFactory();
        }
        return singleton;
    }

    private ReviewFactory() {
        Review r1 = new Review(0, 0, "Mario Fronzoli", 3.5, "Che bella cosa na jurnata 'e sole, N'aria serena doppo na tempesta!");
        Review r2 = new Review(1, 0, "Lucio Pirelli", 3.0, "Pe' ll'aria fresca pare gia' na festa... Che bella cosa na jurnata 'e sole.");

        Review r3 = new Review(2, 1, "Beppe Eppeb", 5.0, "Ma n'atu sole, Cchiu' bello, oi ne'.");

        reviewList.add(r1);
        reviewList.add(r2);
        reviewList.add(r3);
    }

    public ArrayList<Review> getReviews() {
        ArrayList<Review> reviews = new ArrayList<>();

        for (Review review : reviewList) {
            reviews.add(review);
        }

        return reviews;
    }

    public int addReview(Review review) {
        reviewList.add(review);
        return review.getId();
    }

    public boolean removeReview(Review review) {
        return reviewList.remove(review);
    }

    public Review getReviewById(int id) {
        for (Review review : reviewList) {
            if (review.getId() == id) {
                return review;
            }
        }

        return null;
    }

    public ArrayList<Review> getReviewByStation(int id) {
        ArrayList<Review> newReviewList = new ArrayList<>();
        for (Review review : reviewList) {
            if (review.getStationId() == id)
                newReviewList.add(review);
        }
        return newReviewList;
    }
}

