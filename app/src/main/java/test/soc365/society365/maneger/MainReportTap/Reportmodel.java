package test.soc365.society365.maneger.MainReportTap;

/**
 * Created by Anas on 2/5/2019.
 */

public class Reportmodel {

    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public int getReportImageId() {
        return reportImageId;
    }

    public void setReportImageId(int reportImageId) {
        this.reportImageId = reportImageId;
    }

    public Reportmodel(String reportname, int reportImageId) {
        this.reportname = reportname;
        this.reportImageId = reportImageId;
    }
    // Save car name.
    private String reportname;


    public Reportmodel() {
    }

    // Save car image resource id.
    private int reportImageId;
}
