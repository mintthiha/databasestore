package superstore.Logic;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import superstore.SuperStoreServices;
import superstore.Data.Order;
import superstore.Data.Product;
import superstore.Data.Review;
import superstore.UI.Displayer;
import superstore.UI.Scan;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Add {
    private SuperStoreServices superstore;
    private Scanner scan;

    public Add(SuperStoreServices superstore) {
        this.superstore = superstore;
        this.scan = new Scanner(System.in);
    }

// This method is called when user chooses "Add" option in menu
    public void getAddLogic () {
        String[] addOptions = new String[]{"Products", "Reviews", "Orders", "Go Back"};
        Displayer.displayOptions(Arrays.asList(addOptions));
        System.out.print("Add: ");
        String addChoice = Scan.getValidString(Arrays.asList(addOptions));
        Scan.clear();

        switch (addChoice) {
            case "products": addProducts ();
                break;
            case "reviews": addReviews ();
                break;
            case "orders": addOrders ();
                break;
            case "go back": 
                break;
        }
    }

    // Logic method that prompts user for products data, then adds it to the database.
    public void addProducts () {
            System.out.println("Enter the product's name.");
            String name = scan.nextLine();
            System.out.println("Enter the product's category.");
            String category = scan.nextLine();
            System.out.println("Enter a product's price.");
            double price = Scan.getValidNumber(0, 1000000);
            Scan.clear();
            Product product = new Product(name, category, price);
            product.addToDatabase(superstore.getConn());
            Scan.enter();
    }

    // Logic method that prompts user for reviews data, then adds it to the database.
    public void addReviews () {
        List<String> customerNames = superstore.getCustomers().getCustomersNames();
        Displayer.displayOptions(customerNames);
        System.out.print("Who are you: ");
        String currentCustomer = Scan.getValidString(customerNames);
        List<String> productNames = superstore.getProducts().getProductsName();
        Displayer.displayOptions(productNames);
        System.out.println("What product: ");
        String currentProduct = Scan.getValidString(productNames);
        Scan.clear();
        System.out.print("What grade (0-5): ");
        double grade = Scan.getValidNumber(0, 5);
        Scan.clear();
        System.out.print("What description: ");
        String description = scan.nextLine();
        Scan.clear();

        Review review = new Review(currentCustomer, superstore.getCustomers().getEmailByName(currentCustomer), currentProduct, ((int)grade), description, 0);
        review.addToDatabase(superstore.getConn());
        Scan.enter();
        Scan.clear();
    }

    // Logic method that prompts user for orders data, then adds it to the database.
    public void addOrders () {
        List<String> customerNames = superstore.getCustomers().getCustomersNames();
        Displayer.displayOptions(customerNames);
        System.out.print("Who are you: ");
        String currentCustomer = Scan.getValidString(customerNames);
        Scan.clear();
        String currentEmail = superstore.getCustomers().getEmailByName(currentCustomer);

        List<String> productNames = superstore.getProducts().getProductsName();
        Displayer.displayOptions(productNames);
        System.out.print("What product you want to order: ");
        String currentProduct = Scan.getValidString(productNames);
        Scan.clear();

        System.out.print("How many products: ");
        double quantity = Scan.getValidNumber(0, 100);
        Scan.clear();

        LocalDate todayDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formatedDate = todayDate.format(format);
        Order order = new Order(currentEmail, currentProduct, (int) quantity, formatedDate);
        order.addToDatabase(superstore.getConn());
        Scan.enter();
        Scan.clear();
    }
}
