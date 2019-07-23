package test.soc365.society365.maneger.Daybook;

/**
 * Created by Anas on 2/6/2019.
 */

public class daybookmodel {
    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public String getVouchernumber() {
        return vouchernumber;
    }

    public void setVouchernumber(String vouchernumber) {
        this.vouchernumber = vouchernumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVouchertype() {
        return vouchertype;
    }

    public void setVouchertype(String vouchertype) {
        this.vouchertype = vouchertype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public daybookmodel() {
    }

    public daybookmodel(String partyname, String vouchernumber, String date, String vouchertype, String amount) {
        this.partyname = partyname;
        this.vouchernumber = vouchernumber;
        this.date = date;
        this.vouchertype = vouchertype;
        this.amount = amount;
    }

    String partyname;
    String vouchernumber;
    String date;
    String vouchertype;
    String amount;

    public String getLedgername() {
        return ledgername;
    }

    public void setLedgername(String ledgername) {
        this.ledgername = ledgername;
    }

    String ledgername;

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String date1) {
        Date1 = date1;
    }

    public String getDate2() {
        return Date2;
    }

    public void setDate2(String date2) {
        Date2 = date2;
    }

    String Date1;
    String Date2;
}
