package superstore.DataControllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;
import superstore.Data.*;

public class CustomerController {
    List<Customer> customers;

    public CustomerController(Connection connection) {
        this.customers = retrieveAllCustomers(connection);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    // Gets all customers from the database
     public List<Customer> retrieveAllCustomers(Connection connection) {
        List<Customer> customers = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLCUSTOMERS}")) { 
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);

            while(resultSet.next()) {
            int customerId = resultSet.getInt("customer_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String address = resultSet.getString("fulladdress");
            customers.add(new Customer(customerId, firstName, lastName, email, address));
        }
        }
        catch (SQLException e) {
            System.out.println("Error ! Unable to retrieve all customers from database! \n");
            e.getStackTrace();
        }
        return customers;
    }
    // Gets customer email by name
    public String getEmailByName (String fullname) {
        String email = "";
        for (Customer customer : customers) {
            if (fullname.equalsIgnoreCase(customer.getFullName())) {
                email = customer.getEmail();
            }
        }
        return email;
    }

    // Gets specific customer by name
    public Customer getCustomerByName (String name) {
        Customer newCustomer = null;
        for (Customer customer : customers) {
            if (name.equalsIgnoreCase(customer.getFullName())) {
                newCustomer = customer;
            }
        }
        return newCustomer;
    }
    // Gets all customer names
    public List<String> getCustomersNames () {
        List<String> customerNames = new ArrayList<>();
        for (Customer customer : customers) {
            customerNames.add(customer.getFullName() );
        }

        return customerNames;
    }    

    // Gets all customer ids
    public List<String> getAllIds () {
        List<String> customerIds = new ArrayList<>();
        for (Customer customer : customers) {
            customerIds.add(customer.getCustomerId()+"");
        }

        return customerIds;
    }    
}
