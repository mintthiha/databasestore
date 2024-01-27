package superstore.Logic;

import java.util.Arrays;
import java.util.List;

import superstore.SuperStoreServices;
import superstore.Data.Order;
import superstore.Data.Product;
import superstore.Data.Review;
import superstore.Data.Warehouse;
import superstore.UI.Displayer;
import superstore.UI.Scan;

public class Delete {
    private SuperStoreServices superstore;

    
    public Delete(SuperStoreServices superstore) {
        this.superstore = superstore;
    }

    // This method is called when user chooses "Delete" option in menu
    public void getDeleteLogic () {
        
        String[] deleteOptions = new String[] {"Product", "Warehouse", "Review", "Order", "Go Back"};
        Displayer.displayOptions(Arrays.asList(deleteOptions));
        System.out.print("What to delete: ");
        String deleteChoice = Scan.getValidString(Arrays.asList(deleteOptions));
        Scan.clear();

        switch (deleteChoice) {
            case "product": deleteProduct();
                break;
            case "warehouse": deleteWarehouse();
                break;
            case "review" : deleteReview();
            break;
            case "order" : deleteOrder();
            break;
            case "go back": 
                break;
        }
    }

    // Logic method that prompts user for product name, then deletes it from the database.
    public void deleteProduct () {
        List<String> productNames = superstore.getProducts().getProductsName();
        Displayer.displayOptions(productNames);
        System.out.print("What product to delete: ");
        String currentProduct = Scan.getValidString(productNames);
        Scan.clear();
        Product product = superstore.getProducts().getProductByName(currentProduct);
        product.deleteFromDatabase(superstore.getConn());
        Scan.enter();
        Scan.clear();
    }

    // Logic method that prompts user for warehouse name, then deletes it from the database.
    public void deleteWarehouse () {
        List <String> warehouseNames = superstore.getWarehouses().getWarehousesNames();
        Displayer.displayOptions(warehouseNames);
        System.out.print("What warehouse to delete: ");
        String currentWarehouse = Scan.getValidString(warehouseNames);
        Scan.clear();
        Warehouse warehouse = superstore.getWarehouses().getWarehouseByName(currentWarehouse);
        warehouse.deleteFromDatabase(superstore.getConn());
        Scan.enter();
        Scan.clear();
    }

    // Logic method that prompts user for Review data, then deltes it in the database
    public void deleteReview () {
        List <String> reviewNames = superstore.getReviews().getByReviewNames();
        Displayer.displayOptions(reviewNames);
        System.out.print("Choose a review to delete: ");
        List <String> reviewIds = superstore.getReviews().getAllIds();
        String currentId = Scan.getValidString(reviewIds);
        Review currentReview = superstore.getReviews().getById(Integer.parseInt(currentId));
        currentReview.deleteFromDatabase(superstore.getConn());
        Scan.enter();
        Scan.clear();
    }

    // Logic method that prompts user for Order data, then deletes it in the database
    public void deleteOrder () {
        List <String> orderNames = superstore.getOrders().getOrderString();
        Displayer.displayOptions(orderNames);
        System.out.print("Choose an order to delete: ");
        List<String> orderIds = superstore.getOrders().getAllIds();
        String currentId = Scan.getValidString(orderIds);
        Order currentOrder = superstore.getOrders().getOrderById(Integer.parseInt(currentId));
        currentOrder.deleteFromDatabase(superstore.getConn());
        Scan.enter();
        Scan.clear();
    }
}
