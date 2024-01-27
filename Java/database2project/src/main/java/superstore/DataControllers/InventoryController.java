package superstore.DataControllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;
import superstore.Data.Inventory;

public class InventoryController {
    List<Inventory> inventories;

    public InventoryController(Connection connection) {
        this.inventories = retrieveAllInventories(connection);
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public Inventory getMyInventory(int index) {
        return inventories.get(index);
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

// Gets all inventories from the database
    public List<Inventory> retrieveAllInventories (Connection connection) {
        List<Inventory> inventories = new ArrayList<>();
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLINVENTORY}")) { 
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);

            while(resultSet.next()) {
            String productName = resultSet.getString("productname");
            String warehouseName = resultSet.getString("warehousename");
            int quantity = resultSet.getInt("quantity");
            inventories.add(new Inventory(productName, warehouseName, quantity));
        }
        }
        catch (SQLException e) {
            System.out.println("Error ! Unable to retrieve all inventories from database! \n");
            e.getStackTrace();
        }
        return inventories;
    }

    public List<String> getProductNames () {
        List<String> namesList = new ArrayList<>();
        for (Inventory inventory : inventories) {
            namesList.add(inventory.getProductName());
        }
        return namesList;
    }

    // Gets a list of inventories based on product name
    public List<Inventory> getInventoriesByProductName (String productName) {
        List<Inventory> filteInventories = new ArrayList<>();
        for (Inventory inventory : inventories) {
            if (productName.equalsIgnoreCase(inventory.getProductName())) {
                filteInventories.add(inventory);
            }
        }
        return filteInventories;
    }

    // Gets a list of inventrories based on quantity
    public List <Inventory> getByQuantityRange (double  min, double max) {
        List <Inventory> filteredInventory = new ArrayList<>();
        for (Inventory inventory : inventories) {
            if (inventory.getQuantity() >= min && inventory.getQuantity() <= max) {
                filteredInventory.add(inventory);
            }
        }
        return filteredInventory;
    }   

    // Gets a list of inventories based on warehouse name
    public List<Inventory> getByWarehouseName (String name) {
        List<Inventory> filteredInventory = new ArrayList<>();
        for (Inventory inventory : inventories) {
            if (name.equalsIgnoreCase(inventory.getWarehouseName())) {
                filteredInventory.add(inventory);
            }
        }
        return filteredInventory;
    }

    // Gets inventory vased on product and warehouse name
    public Inventory getBasedOnProductWarehouse (String productName, String warehouseName) {
        Inventory newInventory = null;
        for (Inventory inventory : inventories) {
            if (inventory.getProductName().equalsIgnoreCase(productName) && inventory.getWarehouseName().equalsIgnoreCase(warehouseName)) {
                newInventory = inventory;
            }
        }
        return newInventory;
    }

    // Gets all warehouses names 
    public List<String> getInventoryWarehouseNames () {
        List<String> names = new ArrayList<>();
        for (Inventory inventory : inventories) {
            names.add(inventory.getProductName() + " " + inventory.getWarehouseName());
        }
        return names;
    }

    // Gets specific inventory based on name
    public Inventory getInventoryByName (String name) {
        Inventory newInventory = null;
        for (Inventory inventory : inventories) {
            if (name.equalsIgnoreCase(inventory.getProductName() + " " + inventory.getWarehouseName())) {
                newInventory = inventory;
            }
        }
        return newInventory;
    }
}
