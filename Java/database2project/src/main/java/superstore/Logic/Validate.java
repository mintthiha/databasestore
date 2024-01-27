package superstore.Logic;

import java.util.Arrays;
import java.util.List;

import superstore.SuperStoreServices;
import superstore.Data.Customer;
import superstore.Data.Order;
import superstore.Data.Product;
import superstore.UI.Displayer;
import superstore.UI.Scan;

public class Validate {
    private SuperStoreServices superstore;

    public Validate(SuperStoreServices superstore) {
        this.superstore = superstore;
    }

    // This method is called when user chooses "Validate" option in menu
    public void getValidateLogic () {
        String[] validateOptions = new String[] {"Check If Order Available", "Check If Customer Is Flagged", "Check Average Review Score", "Check Total Products In All Warehouses", "Go Back"};
        Displayer.displayOptions(Arrays.asList(validateOptions));
        System.out.println("What you want to validate: ");
        String userChoice = Scan.getValidString(Arrays.asList(validateOptions));
        Scan.clear();

        switch (userChoice) {
            case "check if order available":
                validateOrder ();
            break;

            case "check if customer is flagged":
                validateCustomer ();
            break;

            case "check average review score":
                validateAverageScore();
            break;

            case "check total products in all warehouses":
                checkTotalProducts();
            break;

            case "go back" :
            break;
        }
    }

     // Logic method that prompts user for the order, then validates if the product from this order is in stock.
    public void validateOrder () {
        List<String> orderOptions = superstore.getOrders().getOrderString();
        Displayer.displayOptions(orderOptions);
        System.out.println("Choose an order you want to validate");
        System.out.print("Enter the product name: ");
        List<String> orderIds = superstore.getOrders().getAllIds();
        String currentOrder = Scan.getValidString(orderIds);
        Scan.clear();
        Order order = superstore.getOrders().getOrderById(Integer.parseInt(currentOrder));
        int canOrder = order.validateDatabase(superstore.getConn());
        if (canOrder == 0) {
            System.out.println("There are not enough " +  "(" + order.getProductName() + ")" +"  in inventory");
            Scan.enter();
        }
        else {
            System.out.println("There are enough " +  "(" + order.getProductName() + ")" +"  in inventory");
            Scan.enter();
        }
    }

    // Logic method that prompts user for the customer, then calculates the customer's flags.
    public void validateCustomer () {
        List<String> customerNames = superstore.getCustomers().getCustomersNames();
        Displayer.displayOptions(customerNames);
        System.out.print("Enter the customer you want to validate: ");
        String currentName = Scan.getValidString(customerNames);
        Scan.clear();
        Customer customer = superstore.getCustomers().getCustomerByName(currentName);
        System.out.println(customer.getCustomerId());
        int numOfFlaggs = customer.validateCustomer(superstore.getConn());
        System.out.println(currentName.toUpperCase() + " has : " + numOfFlaggs + " flags!");
        Scan.enter();
    }

    // Logic method that prompts user for the product, then calculates average score of this product.
    public void validateAverageScore () {
        List<String> productNames = superstore.getProducts().getProductsName();
        Displayer.displayOptions(productNames);
        System.out.print("Enter the product name: ");
        String currentProduct = Scan.getValidString(productNames);
        Scan.clear();
        Product product = superstore.getProducts().getProductByName(currentProduct);
        int score = product.getAverageScore(superstore.getConn());
        System.out.println("(" + currentProduct +")" + " has average score of : " + score);
        Scan.enter();
    }

    // Logic method that prompts user for the product, then checks if the product is in the inventory.
    public void checkTotalProducts () {
        List <String> productNames = superstore.getProducts().getProductsName();
        Displayer.displayOptions(productNames);
        System.out.println("Enter the product name: ");
        String currentProduct = Scan.getValidString(productNames);
        Scan.clear();
        Product product = superstore.getProducts().getProductByName(currentProduct);
        int totalAmount = product.getTotalAmount(superstore.getConn());
        System.out.println("There are " + totalAmount + " (" + currentProduct + ") in stock");
        Scan.enter();
    }
}
