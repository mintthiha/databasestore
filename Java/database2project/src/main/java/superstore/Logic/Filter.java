package superstore.Logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import superstore.SuperStoreServices;
import superstore.Data.Order;
import superstore.UI.Displayer;
import superstore.UI.Scan;

public class Filter {
    private SuperStoreServices superstore;

    public Filter(SuperStoreServices superstore) {
        this.superstore = superstore;
    }

    // This method is called when user chooses "Filter" option in menu
    public void getFilterLogic () {
        String[] filterOptions = new String[]{"Products", "Inventory", "Reviews", "Orders", "Go Back"};
        Displayer.displayOptions(Arrays.asList(filterOptions));
        System.out.print("Filter: ");
        String filterChoise = Scan.getValidString(Arrays.asList(filterOptions));
        Scan.clear();

        switch (filterChoise) {
            case "products" : filterProducts ();
            break;
            case "inventory" : filterInventory ();
            break;
            case "reviews" : filterReviews ();
            break;
            case "orders" : filterOrders();
            break;
            case "go back" : 
            break;
        }
    }

    // Logic method that prompts user for product data, then filters it based on category.
    public void filterProducts () {
        String[] sortOptions = new String[] {"Name", "Category", "Price", "None", "Go Back"};
            Displayer.displayOptions(Arrays.asList(sortOptions));
            System.out.print("Filter products based on : ");
            String productChoice = Scan.getValidString(Arrays.asList(sortOptions));
            Scan.clear();
            switch (productChoice) {
                case "name" : 
                    List<String> names = superstore.getProducts().getProductsName();
                    Displayer.displayOptions(names);
                    System.out.print("Choose product name: ");
                    String chosenName = Scan.getValidString(names);
                    Scan.clear();
                    Displayer.displayProducts(Arrays.asList(superstore.getProducts().getProductByName(chosenName)));
                    Scan.enter();
                break;

                case "category" :
                    List<String> categories = superstore.getProducts().getProductsCategory();
                    Displayer.displayOptions(categories);
                    System.out.print("Choose product category: ");
                    String category = Scan.getValidString(categories);
                    Scan.clear();
                    Displayer.displayProducts(superstore.getProducts().getProductsByCategory(category));
                    Scan.enter();
                break;

                case "price" :
                    System.out.println("Choose product price range: ");
                    System.out.print("Min price: ");
                    double min = Scan.getValidNumber(0);
                    System.out.println("Max price: ");
                    double max = Scan.getValidNumber(min);
                    Scan.clear();
                    Displayer.displayProducts(superstore.getProducts().getByPriceRange(min, max));
                    Scan.enter();
                break;

                case "none" :
                    Displayer.displayProducts(superstore.getProducts().getProducts());
                    Scan.enter();
                break;

                case "go back" :
                break;
            }
    }

// Logic method that prompts user for Inventory data, then filters it based on category.
    public void filterInventory () {
        String[] sortOptions = new String[] {"Product", "Warehouse", "Quantity", "None", "Go Back"};
        Displayer.displayOptions(Arrays.asList(sortOptions));
        System.out.print("Filter inventory based on: ");
        String productChoice = Scan.getValidString(Arrays.asList(sortOptions));
        Scan.clear();
            switch (productChoice) {
                case "product" : 
                    List<String> names = superstore.getProducts().getProductsName();
                    Displayer.displayOptions(names);
                    System.out.print("Choose product name: ");
                    String chosenName = Scan.getValidString(names);
                    Scan.clear();
                    Displayer.displayInventories(superstore.getInventories().getInventoriesByProductName(chosenName));
                    Scan.enter();
                break;

                case "warehouse" :
                    List<String> warehouseNames = superstore.getWarehouses().getWarehousesNames();
                    Displayer.displayOptions(warehouseNames);
                    System.out.print("Choose warehouse name: ");
                    String chosenWarehouse = Scan.getValidString(warehouseNames);
                    Scan.clear();
                    Displayer.displayInventories(superstore.getInventories().getByWarehouseName(chosenWarehouse));
                    Scan.enter();
                break;

                case "quantity" :
                    System.out.println("Choose quantity range: ");
                    System.out.print("Min quantity: ");
                    double min = Scan.getValidNumber(0);
                    System.out.print("Max quantity: ");
                    double max = Scan.getValidNumber(min);
                    Scan.clear();
                    Displayer.displayInventories((superstore.getInventories().getByQuantityRange(min, max)));
                    Scan.enter();
                break;

                case "none" :
                    Displayer.displayInventories(superstore.getInventories().getInventories());
                    Scan.enter();
                break;

                case "go back" :
                break;
            }
            }

// Logic method that prompts user for Reviews data, then filters it based on category.
    public void filterReviews () {
        String[] sortOptions = new String[] {"Customer Name", "Product Name", "Grade", "None", "Go Back"};
        Displayer.displayOptions(Arrays.asList(sortOptions));
        System.out.print("Filter reviews based on: ");
        String reviewChoice = Scan.getValidString(Arrays.asList(sortOptions));
        Scan.clear();

            switch (reviewChoice) {
                case "customer name" : 
                    List<String> customerNames = superstore.getReviews().getCustomerNames();
                    Displayer.displayOptions(customerNames);
                    System.out.print("Choose customer: ");
                    String chosenCustomer = Scan.getValidString(customerNames);
                    Scan.clear();
                    Displayer.displayReviews(superstore.getReviews().getByCustomerNames(chosenCustomer));
                    Scan.enter();
                break;

                case "product name" :
                    List<String> productNames = superstore.getProducts().getProductsName();
                    Displayer.displayOptions(productNames);
                    System.out.print("Choose product: ");
                    String chosenProduct = Scan.getValidString(productNames);
                    Scan.clear();
                    Displayer.displayReviews(superstore.getReviews().getByProductNames(chosenProduct));
                    Scan.enter();
                break;

                case "grade" :
                    System.out.print("Choose grade (0-5): ");
                    double grade = Scan.getValidNumber(0, 5);
                    Scan.clear();
                    Displayer.displayReviews(superstore.getReviews().getByGrade(grade));
                    Scan.enter();
                break;
                case "none" :
                    Displayer.displayReviews(superstore.getReviews().getReviews());
                    Scan.enter();
                break;
                case "go back":
                break;
            }
    }

// Logic method that prompts user for Orders data, then filters it based on category.
    public void filterOrders () {
        String[] sortOptions = new String[] {"Customer Name", "Product Name", "Quantity", "Date", "Store name", "None", "Go Back"};
        Displayer.displayOptions(Arrays.asList(sortOptions));
        System.out.print("Filter orders based on: ");
        String reviewChoice = Scan.getValidString(Arrays.asList(sortOptions));
        Scan.clear();

            switch (reviewChoice) {
                case "customer name" :
                    List<String> customerNames = superstore.getCustomers().getCustomersNames();
                    Displayer.displayOptions(customerNames);
                    System.out.print("What customer: ");
                    String customerName = Scan.getValidString(customerNames);
                    Scan.clear();
                    Displayer.displayOrders(superstore.getOrders().getOrdersByCustomerName(customerName));
                    Scan.enter();
                break;
                case "product name" :
                    List<String> productNames = superstore.getProducts().getProductsName();
                    Displayer.displayOptions(productNames);
                    System.out.print("What product: ");
                    String productName = Scan.getValidString(productNames);
                    Scan.clear();
                    Displayer.displayOrders(superstore.getOrders().getOrdersByProductName(productName));
                    Scan.enter();
                case "quantity" :
                    System.out.println("What quantity: ");
                    System.out.print("Min: ");
                    double min = Scan.getValidNumber(0);
                    System.out.print("Max: ");
                    double max = Scan.getValidNumber(min);
                    Scan.clear();
                    Displayer.displayOrders(superstore.getOrders().getOrdersByQuantity(min, max));
                    Scan.enter();
                break;
                case "date" :
                List<String> dates = superstore.getOrders().getAllOrdersDates();
                Displayer.displayOptions(dates);
                System.out.println("Which date would you like to filter with?");
                String orderDateValue = Scan.getValidString(dates);
                Scan.clear();
                Displayer.displayOrders(superstore.getOrders().getOrdersByDate(orderDateValue));
                Scan.enter();
                break;
                case "store name" : 
                List<String> storeNames = superstore.getStores().getStoreNames();
                Displayer.displayOptions(storeNames);
                System.out.println("Which store name would you like to filter with?");
                String storeName = Scan.getValidString(storeNames);
                Scan.clear();
                Displayer.displayOrders(superstore.getOrders().retrieveOrdersByStoreName(storeName));
                Scan.enter();
                break;
                case "none" : 
                    Displayer.displayOrders(superstore.getOrders().getOrders());
                    Scan.enter();
                break;
                case "go back" :
                break;
            }
    }
}
