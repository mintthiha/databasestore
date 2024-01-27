package superstore.UI;

import java.util.List;

import superstore.Data.AuditLog;
import superstore.Data.Customer;
import superstore.Data.Inventory;
import superstore.Data.Order;
import superstore.Data.Product;
import superstore.Data.Review;

public class Displayer {

    public static void displayOptions (List<String> options) {
        String message = "";
        for (int i = 0; i < options.size(); i++) {
            if (i % 1 == 0) {
                message += "\n";
            }
            message += "(" + (i+1)  + ")" + ": " + options.get(i) + " "; 
        }
        System.out.println(message);
        System.out.println();
    }

    public static void displayInvalidMessage () {
        System.out.println("You entered invalid data !");
    }

    public static void displayProducts(List<Product> products) {
        for (Product product : products) {
            System.out.print("Name: " + product.getName() + ", ");
            System.out.print("Category: " + product.getCategory() + ", ");
            System.out.print("Price: " + product.getPrice() + ", ");
            System.out.println("\n");
        }
    }

    public static void displayInventories(List<Inventory> inventories) {
        for (Inventory inventory : inventories) {
            System.out.print("Product: " + inventory.getProductName()+ ", ");
            System.out.print("Warehouse: " + inventory.getWarehouseName()+ ", ");
            System.out.print("Quantity: " + inventory.getQuantity()+ ", ");
            System.out.println("\n");
        }
    }

    public static void displayReviews(List<Review> reviews) {
        for (Review review : reviews) {
            System.out.print("Customer name: " + review.getCustomerName()+ ", ");
            System.out.print("Product name: " + review.getProductName()+ ", ");
            System.out.print("Grade: " + review.getGrade()+ ", ");
            System.out.print("Description: " + review.getDescription()+ ", ");
            System.out.print("Flags: " + review.getFlagged()+ ", ");
            System.out.println("\n");
        }
    }

    public static void displayCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            System.out.print("First name: " + customer.getFirstName()+ ", ");
            System.out.print("Last name: " + customer.getLastName()+ ", ");
            System.out.print("Email: " + customer.getEmail()+ ", ");
            System.out.print("Address: " + customer.getAddress()+ ", ");
            System.out.println("\n");
        }
    }
    public static void displayOrders (List<Order> orders) {
        for (Order order : orders) {
            System.out.print("Customer name: " + order.getCustomerName() + ", ");
            System.out.print("Product name: " + order.getProductName()+ ", ");
            System.out.print("Quantity: " + order.getQuantity()+ ", ");
            System.out.print("Order date: " + order.getOrder_date()+ ", ");
            System.out.println("\n");
        }
    }

    public static void displayAllAudits (List<AuditLog> audits) {
        for (AuditLog auditLog : audits){
            System.out.print("Action performed: " + auditLog.getAction_performed()+ ", ");
            System.out.print("Action Time: " + auditLog.getAction_time()+ ", ");
            System.out.print("Table Modified: " + auditLog.getTable_modified()+ ", ");
            System.out.print("ID of the row modified: " + auditLog.getId()+ ", ");
            System.out.println("\n");
        }
    }
}
