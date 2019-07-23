package test.soc365.society365.maneger.partyledger;

/**
 * Created by Anas on 1/30/2019.
 */



public class partymodel {

    public String title;
    public String rating;
    public String ledgerid;

    public String getFlatnoremoved_SC() {
        return flatnoremoved_SC;
    }

    public void setFlatnoremoved_SC(String flatnoremoved_SC) {
        this.flatnoremoved_SC = flatnoremoved_SC;
    }

    public String flatnoremoved_SC;

    public String getLedgerid() {
        return ledgerid;
    }

    public void setLedgerid(String ledgerid) {
        this.ledgerid = ledgerid;
    }

    public String getIsparty() {
        return isparty;
    }

    public void setIsparty(String isparty) {
        this.isparty = isparty;
    }

    public String isparty;

    public partymodel() {

    }

    public partymodel(String title, String rating, String ledgerid) {
        this.title = title;
        this.rating = rating;
        this.ledgerid = ledgerid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getledgerid() {
        return ledgerid;
    }

    public void setledgerid(String ledgerid) {
        this.ledgerid = ledgerid;
    }
}
