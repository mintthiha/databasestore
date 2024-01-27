package superstore.DataControllers;

import java.util.List;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import superstore.Data.*;

public class AuditLogController {
    List<AuditLog> auditLogs;
    private Connection connection;

    public AuditLogController(Connection connection){
        this.auditLogs = retrieveAllAuditLogs(connection);
        this.connection = connection;
    }

    // Gets audit logs from the database.
    public List<AuditLog> retrieveAllAuditLogs(Connection connection){
        List<AuditLog> auditLogs = new ArrayList<AuditLog>();                
        try (CallableStatement statement = connection.prepareCall("{? = call RETRIEVEDATA.GETALLAUDITLOGS}")) { 
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            ResultSet resultSet = (ResultSet) statement.getObject(1);
            while(resultSet.next()) {
            int auditLog_id = resultSet.getInt("auditLog_id");
            String action_performed = resultSet.getString("action_performed");
            String action_time = resultSet.getString("action_time");
            String table_modified = resultSet.getString("table_modified");
            int id = resultSet.getInt("id");
            auditLogs.add(new AuditLog(auditLog_id, action_performed, action_time, table_modified, id));
            }
        } catch (SQLException e) {
            System.out.println("Error ! Unable to retrieve all auditLogs from database! \n");
            e.getStackTrace();
        }
        return auditLogs;
    }

    public List<AuditLog> getAuditLogs(){
        this.auditLogs = retrieveAllAuditLogs(this.connection);
        return this.auditLogs;
    }
}
