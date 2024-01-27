package superstore.Data;
import java.sql.*;

import oracle.jdbc.OracleTypes;

public class Product{
    private int productId;
    private String name;
    private String category;
    private double price;

    public Product(int productId, String name, String category, double price) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        if (this.category == null) {
            return "NONE";
        }
        return this.category;
    }

    public double getPrice() {
        return price;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addToDatabase (Connection connection)  {
        String sqlQuery = "{call INSERTFIELDS.INTOPRODUCTS(?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setString(1, name);
            callableStatement.setString(2, category);
            callableStatement.setDouble(3, price);
            callableStatement.execute();
            System.out.println("Product added | " + getName() + ", Category: "  +getCategory() + ", Price: " + getPrice());
            System.out.println("Succesfully added product to database! \n");
        }
        catch (SQLException e){
            System.out.println("Error! Can't add a product to the database! \n");
            e.getStackTrace();
        }
    } 

    public void updateDatabase (Connection connection) {
        String sqlQuery = "{call UPDATEDATA.UPDATEPRODUCT(?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setInt(1, productId);
            callableStatement.setString(2, name);
            callableStatement.setString(3, category);
            callableStatement.setDouble(4, price);
            callableStatement.execute();
            System.out.println("Product updated | " + getName() + ", Category: " + getCategory() + ", Price: " + getPrice());
            System.out.println("Succesfully updated! \n");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't update product... \n");
            e.printStackTrace();
        }
    }

    public void deleteFromDatabase (Connection connection) {
        String sqlQuery = "{call DELETEDATA.DELETEPRODUCT(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setInt(1, productId);
            callableStatement.execute();
            System.out.println("Product deleted | " + getName() + ", Category: " + getCategory() + ", Price: " + getPrice());
            System.out.println("Succesfully deleted! \n");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't delete product... \n");
            e.printStackTrace();
        }
    }

    public int getAverageScore (Connection connection) {
        String sqlQuery = "{? = call VALIDATEDATA.VALIDATEAVERAGERATING(?)}";
        int score = 0;
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
            callableStatement.setInt(2, productId);
            callableStatement.execute();
            score = callableStatement.getInt(1);
            System.out.println("Product validated | " + getName() + ", Category: " + getCategory() + ", Price: " + getPrice());
            System.out.println("Succesfully retrieved the score!");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't retrieve a score!\n");
            e.printStackTrace();
        }
        return score;
    }

    public int getTotalAmount (Connection connection) {
        String sqlQuery = "{? = call VALIDATEDATA.GETTOTALAMOUNT(?)}";
        int totalAmount = 0;
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
            callableStatement.setInt(2, productId);
            callableStatement.execute();
            totalAmount = callableStatement.getInt(1);
            System.out.println("Product validated | " + getName() + ", Category: " + getCategory() + ", Price: " + getPrice());
            System.out.println("Succesfully retrieved the total amount!");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't retrieve the total amount!\n");
            e.printStackTrace();
        }
        return totalAmount;
    }
}
