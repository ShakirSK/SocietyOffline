package test.soc365.society365.maneger.Sales_Register;

/**
 * Created by Anas on 2/7/2019.
 */

public class sales_register_model {

    public sales_register_model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;
    String name;
    String amount;
    String date1;
    String date2;

    public String getIsparty() {
        return Isparty;
    }

    public void setIsparty(String isparty) {
        Isparty = isparty;
    }

    String Isparty;

    public String getLed_name() {
        return led_name;
    }

    public void setLed_name(String led_name) {
        this.led_name = led_name;
    }

    String led_name;

    public int getAmt_for_receipt() {
        return amt_for_receipt;
    }

    public void setAmt_for_receipt(int amt_for_receipt) {
        this.amt_for_receipt = amt_for_receipt;
    }

    int amt_for_receipt;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    String date;
    String vn;
    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    String test;

    public String getFlatno() {
        return flatno;
    }

    public void setFlatno(String flatno) {
        this.flatno = flatno;
    }

    String flatno;

    public String getCqno() {
        return cqno;
    }

    public void setCqno(String cqno) {
        this.cqno = cqno;
    }

    public String getCqdate() {
        return cqdate;
    }

    public void setCqdate(String cqdate) {
        this.cqdate = cqdate;
    }

    public String getBankdate() {
        return bankdate;
    }

    public void setBankdate(String bankdate) {
        this.bankdate = bankdate;
    }

    public String cqno,cqdate,bankdate;

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String narration;

    public sales_register_model(String name, String amount, String date1, String date2) {
        this.name = name;
        this.amount = amount;
        this.date1 = date1;
        this.date2 = date2;
    }
}
