package superstore.Data;

public class AuditLog {
    private int auditLog_id;
    private String action_performed;
    private String action_time;
    private String table_modified;
    private int id;

    public AuditLog(int auditLog_id, String action_perfomed, String action_time, String table_modified, int id){
        this.auditLog_id = auditLog_id;
        this.action_performed = action_perfomed;
        this.action_time = action_time;
        this.table_modified = table_modified;
        this.id = id;
    }

    public int getAuditLog_id() {
        return auditLog_id;
    }

    public String getAction_performed() {
        return action_performed;
    }

    public String getAction_time() {
        return action_time;
    }

    public String getTable_modified() {
        return table_modified;
    }

    public int getId() {
        return id;
    }

    public void setAuditLog_id(int auditLog_id) {
        this.auditLog_id = auditLog_id;
    }

    public void setAction_performed(String action_performed) {
        this.action_performed = action_performed;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }

    public void setTable_modified(String table_modified) {
        this.table_modified = table_modified;
    }

    public void setId(int id) {
        this.id = id;
    }
}
