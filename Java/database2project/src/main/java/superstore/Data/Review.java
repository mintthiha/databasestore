package superstore.Data;

import java.sql.*;

public class Review{
    private int reviewId;
    private String customerName;
    private String email;
    private String productName;
    private int grade;
    private String description;
    private int flagged;

    
    public Review(int reviewId, String customerName, String email, String productName, int grade, String description, int flagged) {
        this.reviewId = reviewId;
        this.customerName = customerName;
        this.email = email;
        this.productName = productName;
        this.grade = grade;
        setDescription(description);
        this.flagged = flagged;
    }

    public Review(String customerName, String email, String productName, int grade, String description, int flagged) {
        this.customerName = customerName;
        this.email = email;
        this.productName = productName;
        this.grade = grade;
        setDescription(description);
        this.flagged = flagged;
    }

    public Review(int reviewId, String email, String productName, int grade, String description, int flagged) {
        this.reviewId = reviewId;
        this.email = email;
        this.productName = productName;
        this.grade = grade;
        setDescription(description);
        this.flagged = flagged;
    }


    public void addToDatabase (Connection connection)  {
        String sqlQuery = "{call INSERTFIELDS.INTOREVIEWS(?, ?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setString(1, this.email);
            callableStatement.setString(2, this.productName);
            callableStatement.setDouble(3, this.grade);
            callableStatement.setString(4, this.description);
            callableStatement.setDouble(5, this.flagged);
            callableStatement.execute();
            System.out.println("Review added | " + getCustomerName() + ", Product: " + getProductName() + ", Grade: " + getGrade() + ", Description: " + getDescription() + ", Flags: " + getFlagged());
            System.out.println("Succesfully added the review.");
        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error! Can't add a review to the database!");
        }
    } 

    public void updateDatabase (Connection connection) {
        String sqlQuery = "{call UPDATEDATA.UPDATEREVIEW(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setInt(1, reviewId);
            callableStatement.setString(2, email);
            callableStatement.setString(3, productName);
            callableStatement.setDouble(4, grade);
            callableStatement.setString(5, description);
            callableStatement.setInt(6, flagged);
            callableStatement.execute();
            System.out.println("Review added | " + getCustomerName() + ", Product: " + getProductName() + ", Grade: " + getGrade() + ", Description: " + getDescription() + ", Flags: " + getFlagged());
            System.out.println("Succesfully updated the review!!! \n");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't update review! \n");
            e.printStackTrace();
        }
    }


    public void deleteFromDatabase (Connection connection) {
        String sqlQuery = "{call DELETEDATA.DELETEREVIEW(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setInt(1, reviewId);
            callableStatement.execute();
            System.out.println("Review added | " + getCustomerName() + ", Product: " + getProductName() + ", Grade: " + getGrade() + ", Description: " + getDescription() + ", Flags: " + getFlagged());
            System.out.println("Succesfully deleted the review!!! \n");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't delete review! \n");
            e.printStackTrace();
        }
    }

    public int getReviewId() {
        return reviewId;
    }
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        if (description == null) {
            this.description = "N/a";
        }
        else {
            this.description = description;
        }
    }
    public int getFlagged() {
        return flagged;
    }
    public void setFlagged(int flagged) {
        this.flagged = flagged;
    }  
}
