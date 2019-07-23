package test.soc365.society365.staticurl;

public class StaticUrl {

    public static String baseurl = "http://society365.in/sms";

    public static String reportlink=baseurl+"/report_config.php";

    public static String login=baseurl + "/api/login.php";
    public static String forgotpassword = baseurl + "/api/forgotpassword.php";
    public static String changepassword=baseurl+"/api/changepassword.php";

    public static String building = baseurl + "/api/buildinglist.php";
    public static String flat = baseurl + "/api/flatlist.php";
    public static String memberlist = baseurl + "/api/searchmember.php";
    public static String addmember = baseurl + "/api/addmember.php";
    public static String editmember=baseurl+"/api/editmember.php";

    public static String bulletinboard = baseurl + "/api/bulletin.php";
    public static String bulletinlist = baseurl + "/api/bulletinlist.php";
    public static String bulletindelete = baseurl + "/api/bulletindelete.php";

    public static String activityboard = baseurl + "/api/activity.php";
    public static String activitydelete = baseurl + "/api/activitydelete.php";
    public static String activitylist = baseurl + "/api/activitylist.php";

    public static String auditreport=baseurl+"/api/auditreport.php";
    public static String auditlist=baseurl+"/api/auditreportlist.php";

    public static String momreport=baseurl+"/api/mom.php";
    public static String momlist=baseurl+"/api/momlist.php";

    public static String processrequestlist=baseurl+"/api/requestlist.php";
    public static String replymember=baseurl+"/api/replymember.php";

    public static String createbilluserlist=baseurl+"/api/createbillmemberlist.php";
    public static String billcharges=baseurl+"/api/createbill.php";

    public static String paymentuserlist=baseurl+"/api/paymentuserlist.php";
    public static String memberpaymentdetail=baseurl+"/api/payment.php";

    public static String reporturl=baseurl+"/api/reports.php";
    public static String reportdetails = baseurl+"/api/reports_details.php";
    public static String memberreport = baseurl+"/api/reportmembersummary.php?page_no=";

    public static String profileimg=baseurl+"/api/image.php";

    public static String notificationlist=baseurl+"/api/notification.php";
    public static String usernotificationlist=baseurl+"/api/notification.php";
    public static String changestatus = baseurl + "/api/changestatus.php";

    //user api url
    public static String societyactivity=baseurl+"/apiuser/activitylist.php";
    public static String pastpayment=baseurl+"/apiuser/paymenthistory.php?user_id=";

    public static String userbulletineboard=baseurl+"/apiuser/bulletin.php";

    public static String societyrequestlist=baseurl+"/apiuser/requestlist.php";
    public static String requestSociety=baseurl+"/apiuser/request.php";
    public static String createbilldetails = baseurl+"/apiuser/billlist.php?user_id=";

    public static String MOM=baseurl+"/api/momlist.php";
    public static String userauditreport=baseurl+"/api/auditreportlist.php";

    public static String userchangepassword=baseurl+"/api/changepassword.php";
    public static String getprofile=baseurl+"/api/profile.php?user_id=";
    public static String getlabel = baseurl+"/api/buildingflatname.php?user_id=";

    public static String updatePayment = baseurl+"/api/update_payment.php";

}