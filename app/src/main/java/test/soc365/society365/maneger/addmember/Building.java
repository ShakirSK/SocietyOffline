package test.soc365.society365.maneger.addmember;

public class Building {

    private String building_id;
    private String building_no;
    private String society_id;
    private String no_of_flats;
    private String created_on;
    private String status;

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getBuilding_no() {
        return building_no;
    }

    public void setBuilding_no(String building_no) {
        this.building_no = building_no;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public String getNo_of_flats() {
        return no_of_flats;
    }

    public void setNo_of_flats(String no_of_flats) {
        this.no_of_flats = no_of_flats;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        return building_no;
    }
}
