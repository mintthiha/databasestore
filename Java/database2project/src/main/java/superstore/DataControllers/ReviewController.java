package superstore.DataControllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;
import superstore.Data.Review;

public class ReviewController {
    List<Review> reviews;

    public ReviewController(Connection connection) {
        this.reviews = retrieveAllReviews(connection);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // Gets al customer names
    public List<String> getCustomerNames () {
        List<String> customerNames = new ArrayList<>();
        for (Review review  : reviews) {
            customerNames.add(review.getCustomerName());
        }
        return customerNames;
    }

    // Gets reviews based on customer name
    public List<Review> getByCustomerNames (String name) {
        List<Review> customerNames = new ArrayList<>();
        for (Review review  : reviews) {
            if (name.equalsIgnoreCase(review.getCustomerName())){
                customerNames.add(review);
            }
        }
        return customerNames;
    }

    // Gets reviews based on the grade
    public List<Review> getByGrade (double grade) {
        List<Review> fitleredReview = new ArrayList<>();
        for (Review review : reviews) {
            if (grade == review.getGrade()) {
                fitleredReview.add(review);
            }
        }
        return fitleredReview;
    }

    // Gets reviews based on product name
    public List<Review> getByProductNames (String name) {
        List<Review> productNames = new ArrayList<>();
        for (Review review  : reviews) {
            if (name.equalsIgnoreCase(review.getProductName())){
                productNames.add(review);
            }
        }
        return productNames;
    }

    // Gets list of string with reviews unique identifiers
    public List<String> getByReviewNames () {
        List<String> reviewNames = new ArrayList<>();
        for (Review review  : reviews) {
            reviewNames.add(review.getCustomerName() + ", Product: " + review.getProductName() + ", Grade: " +  review.getGrade() + ", Description: " +review.getDescription() + ", Flagged: " + review.getFlagged());
        }
        return reviewNames ;
    }

    // Gets list of ids of all reviews
    public List<String> getAllIds () {
        List<String> reviewNames = new ArrayList<>();
        for (Review review  : reviews) {
            reviewNames.add(review.getReviewId() + "");
        }
        return reviewNames ;
    }

    // Gets a specific review based on id
    public Review getById (int id) {
        Review currentReview = null;
        for (Review review : reviews) {
            if (review.getReviewId() == id) {
                currentReview = review;
            }
        }
        return currentReview;
    }

    // Gets all reviews from the database.
    public List<Review> retrieveAllReviews (Connection connection) {
        List<Review> reviews = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLREVIEWS}");){
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);

            while(resultSet.next()) {
                int reviewId = resultSet.getInt("review_id");
                String customerName = resultSet.getString("fullname");
                String email = resultSet.getString("email");
                String productName  = resultSet.getString("name");
                int grade = resultSet.getInt("grade");
                String description = resultSet.getString("description");
                int flagged = resultSet.getInt("flagged");
                reviews.add(new Review(reviewId, customerName, email, productName, grade, description, flagged));
        }
        } 
        catch (SQLException e) {
            e.getStackTrace();
            System.out.println("Error : Unable to get all reviews!");
        }
        return reviews;
    }

    
}
