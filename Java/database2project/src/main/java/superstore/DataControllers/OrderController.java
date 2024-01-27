package superstore.DataControllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;
import superstore.Data.Order;

public class OrderController {
    private List<Order> orders;
    private Connection connection;

    public OrderController(Connection connection) {
        this.orders = retrieveAllOrders(connection);
        this.connection = connection;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    // Gets all orders from the database
    public List<Order> retrieveAllOrders (Connection connection) {
        List<Order> orders = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLORDERS}")) { 
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);
            while(resultSet.next()) {
            int orderId = resultSet.getInt("order_id");
            String customerName = resultSet.getString("customername");
            String productName = resultSet.getString("productname");
            int quantity = resultSet.getInt("quantity");
            String date = resultSet.getDate("order_date").toString();
            orders.add(new Order(orderId, customerName, productName, quantity, date));
        }
        }
        catch (SQLException e) {
            System.out.println("Error ! Unable to retrieve all orders from database! \n");
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> retrieveOrdersByStoreName (String storeName) {
        List<Order> filteredOrders = new ArrayList<>();
        try (CallableStatement statement = this.connection.prepareCall("{? = call RETRIEVEDATA.getAllOrdersByStoreName(?)}")) { 
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setString(2, storeName);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);
            while(resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                for (Order order : this.orders){
                    if(order.getOrderId() == orderId){
                        filteredOrders.add(order);
                    }
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error ! Unable to retrieve all orders using a store name from the database! \n");
            e.printStackTrace();
        }
        return filteredOrders;
    }


    // Gets all orders based on customer name
    public List<Order> getOrdersByCustomerName (String name) {
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomerName().equalsIgnoreCase(name)){
                list.add(order);
            }
        }
        return list;
    }

    // Gets all orders based on product name
    public List<Order> getOrdersByProductName (String name) {
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            if (order.getProductName().equalsIgnoreCase(name)){
                list.add(order);
            }
        }
        return list;
    }

    public List<Order> getOrdersByDate(String orderDate) {
        List<Order> list = new ArrayList<>();
        for (Order order : this.orders){
            if (order.getOrder_date().equals(orderDate)){
                list.add(order);
            }
        }
        return list;
    }
    // Gets all orders based on qunantity
    public List<Order> getOrdersByQuantity (double min, double max) {
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            if (order.getQuantity() >=  min && order.getQuantity() <= max){
                list.add(order);
            }
        }
        return list;
    }


    // Returns list of strings that are unique identifiers for orders
    public List<String> getOrderString () {
        List<String> list = new ArrayList<>();
        for (Order order : orders) {
            list.add(order.getCustomerName() + ", Product: " + order.getProductName() + ", Quantity: " + order.getQuantity() + ", Order Date: " + order.getOrder_date());
        }
        return list;
    }

    // Gets order based on its unique name
    public Order getOrderByName (String name) {
        Order newOrder = null;
        for (Order order : orders) {
            if (name.equalsIgnoreCase(order.getCustomerName() + ", Product: " + order.getProductName() + ", Quantity: " + order.getQuantity() + ", Order Date: " + order.getOrder_date())) {
                newOrder = order;
            }
        }
        return newOrder;
    }

    // Gets order based on its id
    public Order getOrderById (int id) {
        Order currentOrder = null;
        for (Order order : orders) {
            if (order.getOrderId() == id) {
                currentOrder = order;
            }
        }
        return currentOrder;
    }

    // Gets ids of all orders
    public List<String> getAllIds () {
        List<String> orderIds = new ArrayList<>();
        for (Order order : orders) {
            orderIds.add(order.getOrderId()+"");
        }
        return orderIds;
    }

    public List<String> getAllOrdersDates(){
        List<String> dates = new ArrayList<String>();
        for(Order order : this.orders){
            if(!dates.contains(order.getOrder_date()))
            dates.add(order.getOrder_date());
        }
        return dates;
    }
}
