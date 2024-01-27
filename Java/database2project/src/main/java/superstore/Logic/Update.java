package superstore.Logic;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import superstore.SuperStoreServices;
import superstore.Data.Inventory;
import superstore.Data.Product;
import superstore.Data.Review;
import superstore.UI.Displayer;
import superstore.UI.Scan;

public class Update {
    private SuperStoreServices superstore;
    private Scanner scan;

    public Update(SuperStoreServices superstore) {
        this.superstore = superstore;
        scan = new Scanner(System.in);
    }

    // This method is called when user chooses "Update" option in menu
    public void getUpdateLogic () {
        String[] updateOptions = new String[] {"Product", "Inventory", "Review", "Go Back"};
        Displayer.displayOptions(Arrays.asList(updateOptions));
        System.out.print("Update: ");
        String currentOption = Scan.getValidString(Arrays.asList(updateOptions));
        Scan.clear();

        switch (currentOption) {
            case "product":
                List<String> productNames = superstore.getProducts().getProductsName();
                Displayer.displayOptions(productNames);
                System.out.print("What product do you want to update: ");
                String currentProduct = Scan.getValidString(productNames);
                Scan.clear();
                Product product = superstore.getProducts().getProductByName(currentProduct);
                updateProduct(product);
            break;

            case "inventory" :
                List<String> inventoryNames = superstore.getInventories().getInventoryWarehouseNames();
                Displayer.displayOptions(inventoryNames);
                System.out.print("What inventory do you want to update in inventory: ");
                String currentInventory = Scan.getValidString(inventoryNames);
                Scan.clear();
                Inventory inventory = superstore.getInventories().getInventoryByName(currentInventory);
                updateInventory (inventory);
            break;
            case "review" :
                List<String> reviewNames = superstore.getReviews().getByReviewNames();
                Displayer.displayOptions(reviewNames);
                System.out.print("What review do you want to update: ");
                List<String> reviewIds = superstore.getReviews().getAllIds();
                String currentId = Scan.getValidString(reviewIds);
                Review currentReview = superstore.getReviews().getById(Integer.parseInt(currentId));
                updateReview (currentReview);


            case "go back" :
                break;
        }
    }

    // Logic method that prompts user for Product data, then updates it in the database.
    public void updateProduct (Product product)  {
        Displayer.displayProducts(Arrays.asList(product));
        String [] updateOptions = new String[] {"Name", "Category", "Price", "Go Back"};
        Displayer.displayOptions(Arrays.asList(updateOptions));
        System.out.print("What to update: ");
        String userChoice = Scan.getValidString(Arrays.asList(updateOptions));

        switch (userChoice) {
            case "name" :
            System.out.print("Enter your new name: ");
            String newName = scan.nextLine();
            Scan.clear();
            product.setName(newName);
            product.updateDatabase(superstore.getConn());
            break;

            case "category" :
                System.out.print("Enter your new category: ");
                String newCategory = scan.nextLine();
                Scan.clear();
                product.setCategory(newCategory);
                product.updateDatabase(superstore.getConn());
            break;

            case "price" :
                System.out.print("Enter your new price: ");
                double newPrice = Scan.getValidNumber(0, 1000000);
                Scan.clear();
                product.setPrice(newPrice);
                product.updateDatabase(superstore.getConn());
            break;

            case "go back" :
            break;
        }
        Scan.enter();
    }

    // Logic method that prompts user for Inventory data, then updates it in the database.
    public void updateInventory (Inventory inventory) {
        Displayer.displayInventories(Arrays.asList(inventory));
        String[] updateOptions = new String[] {"Product", "Warehouse", "Quantity", "Go Back"};
        Displayer.displayOptions(Arrays.asList(updateOptions));
        System.out.print("What aspect do you want to update: ");
        String userChoice = Scan.getValidString(Arrays.asList(updateOptions));
        Scan.clear();

        switch (userChoice) {
            case "product" :
                List<String> productOptions = superstore.getProducts().getProductsName();
                Displayer.displayOptions(productOptions);
                System.out.print("Choose a new product name: ");
                String newProductName = Scan.getValidString(productOptions);
                Scan.clear();
                Inventory newProductInventory = new Inventory(newProductName, inventory.getWarehouseName(), inventory.getQuantity());  
                inventory.updateDatabase(superstore.getConn(), newProductInventory);
                Scan.enter();
            break;
            case "warehouse" :
                List<String> warehouseOptions = superstore.getWarehouses().getWarehousesNames();
                Displayer.displayOptions(warehouseOptions);
                System.out.print("Choose a new warehouse name: ");
                String newWarehouseName = Scan.getValidString(warehouseOptions);
                Scan.clear();
                Inventory newWarehouseInventory = new Inventory(inventory.getProductName(), newWarehouseName, inventory.getQuantity());  
                inventory.updateDatabase(superstore.getConn(), newWarehouseInventory);
                Scan.enter();
            break;
            case "quantity" :
                System.out.print("Choose a new quantity: ");
                double newQuantity = Scan.getValidNumber(0);
                Scan.clear();
                Inventory newQuantityInventory = new Inventory(inventory.getProductName(), inventory.getWarehouseName(), (int) newQuantity);
                inventory.updateDatabase(superstore.getConn(), newQuantityInventory);
                Scan.enter();
            break;
            case "go back" :
            break;
        }
    }

    // Logic method that prompts user for Review data, then updates it in the database
    public void updateReview (Review review) {
        Displayer.displayReviews(Arrays.asList(review));
        String[] updateOptions = new String[] {"Customer", "Product", "Grade", "Description", "Flag", "Go Back"};
        Displayer.displayOptions(Arrays.asList(updateOptions));
        System.out.print("What aspect of review you want to update: ");
        String userChoice = Scan.getValidString(Arrays.asList(updateOptions));

        switch (userChoice) {
            case "customer":
                List<String> customerOptions = superstore.getCustomers().getCustomersNames();
                Displayer.displayOptions(customerOptions);
                System.out.print("Choose a new customer: ");
                String newCustomerName = Scan.getValidString(customerOptions);
                String newCustomerEmail = superstore.getCustomers().getEmailByName(newCustomerName); 
                Scan.clear();
                review.setEmail(newCustomerEmail);
                review.updateDatabase(superstore.getConn());
                Scan.enter();
                break;
            case "product" :
                List<String> productOptions = superstore.getProducts().getProductsName();
                Displayer.displayOptions(productOptions);
                System.out.print("Choose a new product: ");
                String newProductName = Scan.getValidString(productOptions);
                Scan.clear();
                review.setProductName(newProductName);
                review.updateDatabase(superstore.getConn());
                Scan.enter();
                break;
            case "grade" :
                System.out.print("Enter new grade: ");
                int grade = (int) Math.round(Scan.getValidNumber(0, 5));
                Scan.clear();
                review.setGrade(grade);
                review.updateDatabase(superstore.getConn());
                Scan.enter();
                break;
            case "description" :
                System.out.print("Enter new description: ");
                String newDescription = Scan.getString();
                Scan.clear();
                review.setDescription(newDescription);
                review.updateDatabase(superstore.getConn());
                Scan.enter();
                break;
            case "flag" :
                System.out.print("Enter new flag value: ");
                int flag = (int) Math.round(Scan.getValidNumber(0));
                Scan.clear();
                review.setFlagged(flag);
                review.updateDatabase(superstore.getConn());
                Scan.enter();
                break;
            case "go back" :
                break;
        }
    }

    // Finally, we can't update warehouse quantity because all the quantity is place inside of inventory. 
}
