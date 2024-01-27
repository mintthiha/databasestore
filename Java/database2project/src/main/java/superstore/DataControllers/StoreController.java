package superstore.DataControllers;

import java.util.List;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import superstore.Data.*;

public class StoreController {
    List<Store> stores;

    public StoreController(Connection conn){
        this.stores = retrieveAllStores(conn);
    }

    public List<Store> retrieveAllStores(Connection connection){
        List<Store> stores = new ArrayList<Store>();                
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLSTORES}")) { 
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);
            while(resultSet.next()) {
            int store_id = resultSet.getInt("store_id");
            String name = resultSet.getString("name");
            stores.add(new Store(store_id, name));
            }
        } catch (SQLException e) {
            System.out.println("Error ! Unable to retrieve all Stores from database! \n");
            e.getStackTrace();
        }
        return stores;
    }

    public List<Store> getStores(){
        return this.stores;
    }

    public List<String> getStoreNames(){
        List<String> storeNames = new ArrayList<>();
        for (Store store : stores){
            storeNames.add(store.getName());
        }
        return storeNames;
    }
}
