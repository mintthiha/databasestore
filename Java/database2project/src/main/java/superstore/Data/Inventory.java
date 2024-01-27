package superstore.Data;
import java.sql.*;

public class Inventory{
    private String productName;
    private String warehouseName;
    private int quantity;

    public Inventory(String productName, String warehouseName, int quantity) {
        this.productName = productName;
        this.warehouseName = warehouseName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateDatabase (Connection connection, Inventory newInventory) {
        String sqlQuery = "{call UPDATEDATA.UPDATEINVENTORY(?, ?, ?, ?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setString(1, this.productName);
            callableStatement.setString(2, this.warehouseName);
            callableStatement.setString(3, newInventory.getProductName());
            callableStatement.setString(4, newInventory.getWarehouseName());
            callableStatement.setInt(5, newInventory.getQuantity());
            callableStatement.execute();
            System.out.println("Updated inventory | Product: " + getProductName() + ", Warehouse: " + getWarehouseName() + ", Quantity: " + getQuantity());
            System.out.println("Succesfully updated an Inventory!!!");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't update Inventory!");
            e.getStackTrace();
        }
    }
}
