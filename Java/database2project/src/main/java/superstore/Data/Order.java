package superstore.Data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

public class Order {
    private int orderId;
    private String customerName;
    private String productName;
    private int quantity;
    private String order_date;

    public Order(int orderId, String customerName, String productName, int quantity, String order_date) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.order_date = order_date;
    }

    public Order(String customerName, String productName, int quantity, String order_date) {
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.order_date = order_date;
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productNamer) {
        this.productName = productNamer;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getOrder_date() {
        return order_date;
    }
    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void addToDatabase (Connection connection)  {
        String sqlQuery = "{call INSERTFIELDS.INTOORDERS(?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setString(1, customerName);
            callableStatement.setString(2, productName);
            callableStatement.setInt(3, quantity);
            callableStatement.setString(4, order_date);
            callableStatement.execute();
            System.out.println("Added order | " + getCustomerName() + ", Product: " + getProductName() + ", Quantity: " + getQuantity() + ", Order Date: " + getOrder_date());
            System.out.println("Succesfully added order!");
        }
        catch (SQLException e){
            System.out.println("Error! Can't add a product to the database!");
            e.printStackTrace();
        }
    } 

    public void deleteFromDatabase (Connection connection) {
        String sqlQuery = "{call DELETEDATA.DELETEORDER(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setInt(1, orderId);
            callableStatement.execute();
            System.out.println("Deleted order | " + getCustomerName() + ", Product: " + getProductName() + ", Quantity: " + getQuantity() + ", Order Date: " + getOrder_date());
            System.out.println("You have deleted a order!!! \n");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't delete order... \n");
            e.printStackTrace();
        }
    }


    // Validates the orders if their product is in stock
    public int validateDatabase (Connection connection)  {
        int canOrder = 0;
        String sqlQuery = "{ ? = call VALIDATEDATA.VALIDATEORDER(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){

            callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
            callableStatement.setInt(2, orderId);
            callableStatement.execute();
            canOrder = callableStatement.getInt(1);
            System.out.println("Current order | " + getCustomerName() + ", Product: " + getProductName() + ", Quantity: " + getQuantity() + ", Order Date: " + getOrder_date());
            System.out.println("Succesfully validated order!");
        }
        catch (SQLException e){
            System.out.println("Error! Can't validate a product to the database!");
            e.printStackTrace();
        }
        return canOrder;
    } 
}
