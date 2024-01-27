package superstore.Data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;

public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String  email;
    private String address;
    
    public Customer(int customerId, String firstName, String lastName, String email, String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    public String getFullName () {
        return this.firstName + " "  +  this.lastName;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void updateDatabase (Connection connection) {
        String sqlQuery = "{call UPDATEDATA.UPDATECUSTOMER(?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setInt(1, customerId);
            callableStatement.setString(2, firstName);
            callableStatement.setString(3, lastName);
            callableStatement.setString(4, email);
            callableStatement.execute();
            System.out.println("You have updated a customer!!! \n");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't update customer... \n");
            e.printStackTrace();
        }
    }

    public int validateCustomer (Connection connection) {
        String sqlQuery = "{? = call VALIDATEDATA.VALIDATECUSTOMER(?)}";
        int numOfFlaggs = 0;
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.registerOutParameter(1, OracleTypes.NUMBER);
            callableStatement.setInt(2, customerId);
            callableStatement.execute();
            numOfFlaggs = callableStatement.getInt(1);
            System.out.println("Succesfully validated customer!");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't update customer... \n");
            e.printStackTrace();
        }
        return numOfFlaggs;
    }

    public String getUniqueIdentifier (Customer customer) {
        return getFullName() + ", " + getEmail();
    }
}
