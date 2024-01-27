package superstore;
import java.sql.*;
import java.util.Scanner;
import superstore.DataControllers.*;

public class SuperStoreServices {
    static Scanner scan = new Scanner(System.in);  
	private Connection conn;
    private ProductController products;
    private WarehouseController warehouses;
    private InventoryController inventories;
    private ReviewController reviews;
    private CustomerController customers;
    private OrderController orders;
    private AuditLogController auditLogs;
    private StoreController stores;

    // When creating superstore object, it automatically retrieve all the data necessairy from the database.
    public SuperStoreServices(String username, String password) throws SQLException{
		try{
			this.conn = DriverManager.getConnection("jdbc:oracle:thin:@198.168.52.211: 1521/pdbora19c.dawsoncollege.qc.ca", username, password);
            this.products = new ProductController(conn);
            this.warehouses = new WarehouseController(conn);
            this.inventories = new InventoryController(conn);
            this.reviews = new ReviewController(conn);
            this.customers = new CustomerController(conn);
            this.orders = new OrderController(conn);
            this.auditLogs = new AuditLogController(conn);
            this.stores = new StoreController(conn);
		} catch (SQLException e){
            System.out.println("Failed to connect!");
			e.printStackTrace();
		}
  	}

    public void Close(){
        try {
            if(!this.conn.isClosed()){
			this.conn.close();
			System.out.println("Closed the connection.");
		}
        }
        catch (SQLException e) {
            e.getStackTrace();
            System.out.println("Error! Unable to clsoe connection!");
        }
    }

    public void exit () {
        Close();
        scan.close();
        System.out.println("Program is closed.");
        System.exit(0);
    }

    public Connection getConn() {
        return conn;
    }

    public ProductController getProducts() {
        return products;
    }

    public WarehouseController getWarehouses() {
        return warehouses;
    }

    public InventoryController getInventories() {
        return inventories;
    }

    public ReviewController getReviews() {
        return reviews;
    }

    public CustomerController getCustomers() {
        return customers;
    }

    public OrderController getOrders() {
        return orders;
    }

    public AuditLogController getAuditLogs() {
        return auditLogs;
    }

    public StoreController getStores() {
        return stores;
    }
}
