package superstore.Logic;

import java.util.List;

import superstore.SuperStoreServices;
import superstore.Data.AuditLog;
import superstore.UI.Displayer;
import superstore.UI.Scan;

public class Audit {
    private SuperStoreServices superStore;

    public Audit(SuperStoreServices superStore){
        this.superStore = superStore;
    }

    // This method is called when user chooses "Audit" option in menu
    public void getAuditLogic(){
        Scan.clear();
        List<AuditLog> audits = superStore.getAuditLogs().getAuditLogs();
        Displayer.displayAllAudits(audits);
        Scan.enter();
    }
}
