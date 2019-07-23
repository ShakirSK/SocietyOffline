package test.soc365.society365.user.audit_report;

/**
 * Created by PC1 on 28-07-2018.
 */

public class auditModel
{
    private String auditid;
    private String audittitle;
    private String attach;
    private String auditdate;


    public String getAuditid() {
        return auditid;
    }

    public void setAuditid(String auditid) {
        this.auditid = auditid;
    }

    public String getAudittitle() {
        return audittitle;
    }

    public void setAudittitle(String audittitle) {
        this.audittitle = audittitle;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getAuditdate() {
        return auditdate;
    }

    public void setAuditdate(String auditdate) {
        this.auditdate = auditdate;
    }
}
