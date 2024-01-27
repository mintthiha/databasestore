package superstore.DataControllers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;
import superstore.Data.Warehouse;

public class WarehouseController {
    List<Warehouse> warehouses;

    public WarehouseController(Connection connection) {
        this.warehouses = retrieveAllWarehouses(connection);
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
    // Gets warehouse by id
    public Warehouse getById(int id) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getWarehouseId() == id) {
                return warehouse;
            }
        }
        return null;
    }
    // Gets all warehouses names
    public List<String> getWarehousesNames () {
        List<String> warehousesNames = new ArrayList<>();
        for (Warehouse warehouse : warehouses) {
            warehousesNames.add(warehouse.getName());
        }
        return warehousesNames;
    }
    // Gets specific warehouse by name
    public Warehouse getWarehouseByName (String name) {
        Warehouse newWarehouse = null;
        for (Warehouse warehouse : warehouses) {
            if (name.equalsIgnoreCase(warehouse.getName())) {
                newWarehouse = warehouse;
            }
        }

        return newWarehouse;
    }
    // Gets all warehouses from the database
    public List<Warehouse> retrieveAllWarehouses (Connection connection) {
        List<Warehouse> warehouses = new ArrayList<Warehouse>();
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLWAREHOUSES}");){
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);

            while(resultSet.next()) {
                int productId = resultSet.getInt("warehouse_id");
                String name = resultSet.getString("name");
                int address_id = resultSet.getInt("address_id");
                warehouses.add(new Warehouse(productId, name, address_id));
        }
        } 
        catch (SQLException e) {
            e.getStackTrace();
            System.out.println("Error : Unable to get all products!");
        }
        return warehouses;
    }
}
