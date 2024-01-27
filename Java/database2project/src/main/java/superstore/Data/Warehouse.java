package superstore.Data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Warehouse {
    private int warehouseId;
    private String name;
    private int addressId;

    public Warehouse(int warehouseId, String name, int addressId) {
        this.warehouseId = warehouseId;
        this.name = name;
        this.addressId = addressId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAddressId() {
        return addressId;
    }
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void deleteFromDatabase (Connection connection) {
        String sqlQuery = "{call DELETEDATA.DELETEWAREHOUSE(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(sqlQuery)){
            callableStatement.setInt(1, warehouseId);
            callableStatement.execute();
            System.out.println("Deleted warehouse: " + getName());
            System.out.println("You have deleted a warehouse!!! \n");
        }
        catch (SQLException e) {
            System.out.println("Error! Can't delete a warehouse... \n");
            e.printStackTrace();
        }
    }
}
