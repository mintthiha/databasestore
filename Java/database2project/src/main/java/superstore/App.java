package superstore;

import java.util.Arrays;

import superstore.UI.Scan;
import superstore.UI.Displayer;
import superstore.Logic.*;

import java.sql.*;

public class App 
{
    public static void main( String[] args ) throws SQLException, ClassNotFoundException
    {
        String username = new String(System.console().readPassword("Enter your username: "));
        String password = new String(System.console().readPassword("Enter your password: "));
        String userChoice  = "";

        do {
            SuperStoreServices storeServices = new SuperStoreServices(username, password);
            Filter filterTool = new Filter(storeServices);
            Add addTool = new Add(storeServices);
            Update updateTool = new Update(storeServices);
            Delete deleteTool = new Delete(storeServices);
            Validate validateTool = new Validate(storeServices);
            Audit auditTool = new Audit(storeServices);

            String[] options = new String[] {"Filter", "Add", "Update", "Delete", "Validate", "Audit", "Exit"};
            Displayer.displayOptions(Arrays.asList(options));
            System.out.print("Which actions would you like to do today: ");
            userChoice = Scan.getValidString(Arrays.asList(options));
            Scan.clear();
            switch (userChoice) {
            case "filter": filterTool.getFilterLogic();
                break;
            case "add" : addTool.getAddLogic();
                break;
            case "update": updateTool.getUpdateLogic();
                break;
            case "delete": deleteTool.getDeleteLogic();
                break;
            case "validate": validateTool.getValidateLogic();
                break;
            case "audit": auditTool.getAuditLogic();
                break;
        }
        } while (! userChoice.equalsIgnoreCase("exit"));
     }
}
