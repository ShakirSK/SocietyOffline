package test.soc365.society365.maneger.viewmember;

public class member {

    private String image;
    private String firstname;
    private String lastname;
    private String phonno;
    private String buildingno;
    private String status;
    private String flatno;
    private String memberid;
    private String emailid;
    private String password;
    private String createdon;
    private String updatedon;
    private String joindate;
    private String societyid;
    private String usertype;
    private String flatstatus;
    private String no_of_members;
   private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNo_of_members() {
        return no_of_members;
    }

    public void setNo_of_members(String no_of_members) {
        this.no_of_members = no_of_members;
    }

    public String getFlatstatus() {
        return flatstatus;
    }

    public void setFlatstatus(String flatstatus) {
        this.flatstatus = flatstatus;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getSocietyid() {
        return societyid;
    }

    public void setSocietyid(String societyid) {
        this.societyid = societyid;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getUpdatedon() {
        return updatedon;
    }

    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getFirstname(){return firstname;}

    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getPhonno() {
        return phonno;
    }

    public void setPhonno(String phonno) {
        this.phonno = phonno;
    }

    public String getBuildingno() {
        return buildingno;
    }

    public void setBuildingno(String buildingno) {
        this.buildingno = buildingno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlatno() {
        return flatno;
    }

    public void setFlatno(String flatno) {
        this.flatno = flatno;
    }

    @Override
    public String toString() {
        return   firstname + lastname;
    }
}
