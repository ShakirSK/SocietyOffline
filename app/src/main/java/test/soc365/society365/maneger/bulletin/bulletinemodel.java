package test.soc365.society365.maneger.bulletin;

public class bulletinemodel {

    private String bulletin_id;
    private String activity;
    private String publish_date;
    private String is_delete;
    private String created_at;

    public String getBulletin_id() {
        return bulletin_id;
    }

    public void setBulletin_id(String bulletin_id) {
        this.bulletin_id = bulletin_id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
