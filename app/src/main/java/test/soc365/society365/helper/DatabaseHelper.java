package test.soc365.society365.helper;






import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rollbar.android.Rollbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import test.soc365.society365.maneger.Daybook.daybookmodel;
import test.soc365.society365.maneger.Sales_Register.sales_register_model;
import test.soc365.society365.maneger.partyledger.partydetailmodel;
import test.soc365.society365.maneger.partyledger.partymodel;

public class DatabaseHelper extends SQLiteOpenHelper {


    // variable to hold context
    private Context context;

    //Rollbar
    Rollbar rollbar;

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "/sdcard/Testsoc365.db";

    // Table Names
    private static final String TABLE_COMPANY = "company";
    private static final String TABLE_DAYBOOK_DETAIL1 = "daybook_detail1";
    private static final String TABLE_DAYBOOK_DETAILS = "daybook_details";

    private static final String TABLE_DETAILS_GROUP = "daybook_details_group";
    private static final String TABLE_LEDGERS = "ledgers";
    private static final String TABLE_OC_COUNTRY = "oc_country";
    private static final String TABLE_OC_CURRENCY = "oc_currency";
    private static final String TABLE_OC_EXTENSION = "oc_extension";
    private static final String TABLE_OC_GEO_ZONE = "oc_geo_zone";
    private static final String TABLE_OC_LANGUAGE = "oc_language";
    private static final String TABLE_OC_LENGTH_CLASS = "oc_length_class";
    private static final String TABLE_OC_LENGTH_CLASS_DESCRIPTION = "oc_length_class_description";
    private static final String TABLE_OC_SETTING = "oc_setting";
    private static final String TABLE_OC_STORE = "oc_store";
    private static final String TABLE_OC_TAX_CLASS = "oc_tax_class";
    private static final String TABLE_OC_TAX_RATE = "oc_tax_rate";
    private static final String TABLE_OC_TAX_RATE_TO_CUSTOMAR_GROUP = "oc_tax_rate_to_customer_group";
    private static final String TABLE_OC_TAX_RULE = "oc_tax_rule";
    private static final String TABLE_OC_USER = "oc_user";
    private static final String TABLE_OC_USER_COMPANY = "oc_user_company";
    private static final String TABLE_OC_USER_GROUP = "oc_user_group";
    private static final String TABLE_OC_WEIGHT_CLASS = "oc_weight_class";
    private static final String TABLE_OC_WEIGHT_CLASS_DESCRIPTION = "oc_weight_class_description";
    private static final String TABLE_OC_ZONE = "oc_zone";
    private static final String TABLE_OC_ZONE_TO_GEO_ZONE = "oc_zone_to_geo_zone";
    private static final String TABLE_OC_TRIAL_BALANCE_ACCOUNTS = "trial_balance_accounts";
    private static final String TABLE_OC_TRIAL_BALANCE_LEDGER = "trial_balance_ledger";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // company TABLE_COMPANY - column nmaes
    private static final String KEY_CID = "company_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_UID = "user_id";
    private static final String KEY_TID = "tally_company_id";
    private static final String KEY_SID = "society_id";

    //  TABLE_DAYBOOK_DETAILS - column nmaes
    private static final String KEY_DID = "id";
    private static final String KEY_dr_cr = "dr_cr";
    //cid
    private static final String KEY_voucher_type = "voucher_type";
    private static final String KEY_voucher_number = "voucher_number";
    private static final String KEY_date = "date";
    private static final String KEY_amount = "amount";
    private static final String KEY_download_date = "download_date";

    private static final String KEY_narration = "narration";
    private static final String KEY_bank_name = "bank_name";
    private static final String KEY_is_payment = "is_payment";
    private static final String KEY_bank_date = "bank_date";


    private static final String KEY_branch = "branch";
    private static final String KEY_cheque_no = "cheque_no";
    private static final String KEY_cheque_date = "cheque_date";
    private static final String KEY_group_name = "group_name";


    //  CREATE_TABLE_LEDGERS - column nmaes
    private static final String KEY_LID = "id";
    private static final String KEY_ledger_name = "ledger_name";
    private static final String KEY_PID = "parent_id";
    private static final String KEY_party_name = "party_name";

    private static final String KEY_parent_name = "parent_name";
    private static final String KEY_parent_name_2 = "parent_name_2";
    //cid
    private static final String KEY_company_name = "company_name";
    private static final String KEY_type = "type";
    private static final String KEY_flat_no = "flat_no";
    private static final String KEY_mobile_number = "mobile_number";
    private static final String KEY_is_party = "is_party";
    private static final String KEY_opening_balance = "opening_balance";

    // company TABLE_OC_CURRENCY - column nmaes
    private static final String KEY_currency_id = "currency_id";
    private static final String KEY_title = "title";
    private static final String KEY_code = "code";
    private static final String KEY_symbol_left = "symbol_left";
    private static final String KEY_symbol_right = "symbol_right";
    private static final String KEY_decimal_place = "decimal_place";
    private static final String KEY_value = "value";
    private static final String KEY_status = "status";
    private static final String KEY_date_modified = "date_modified";


    // company TABLE_OC_GEO_ZONE - column nmaes
    private static final String KEY_geo_zone_id = "geo_zone_id";
    private static final String KEY_name = "name";
    private static final String KEY_description = "description";
    private static final String date_modified = "date_modified";
    private static final String KEY_date_added = "date_added";


    //  TABLE_OC_LANGUAGE - column nmaes
    private static final String KEY_language_id = "language_id";
    private static final String KEY_locale = "locale";
    private static final String KEY_directory = "directory";
    private static final String KEY_sort_order = "sort_order";

    //  TABLE_OC_LENGTH_CLASS - column nmaes
    private static final String KEY_length_class_id = "length_class_id";


    //  TABLE_OC_SETTING - column nmaes
    private static final String KEY_setting_id = "setting_id";
    private static final String KEY_store_id = "store_id";
    private static final String KEY_key = "key";
    private static final String KEY_serialized = "serialized";


    //  TABLE_OC_TAX_CLASS - column nmaes
    private static final String KEY_tax_class_id = "tax_class_id";

    //  TABLE_OC_TAX_RATE - column nmaes
    private static final String KEY_tax_rate_id = "tax_rate_id";
    private static final String KEY_rate = "rate";

    //  TABLE_OC_TAX_RATE_TO_CUSTOMAR_GROUP - column names
    private static final String KEY_customer_group_id = "customer_group_id";


    //  TABLE_OC_TAX_RULE - column nmaes
    private static final String KEY_tax_rule_id = "tax_rule_id";
    private static final String KEY_based = "based";
    private static final String KEY_priority = "priority";

    //  TABLE_OC_USER - column nmaes
    private static final String KEY_user_group_id = "user_group_id";
    private static final String KEY_username = "username";
    private static final String KEY_password = "password";
    private static final String KEY_salt = "salt";
    private static final String KEY_firstname = "firstname";
    //cid
    private static final String KEY_lastname = "lastname";
    private static final String KEY_email = "email";
    private static final String KEY_image = "image";
    private static final String KEY_ip = "ip";
    private static final String KEY_is_teacher = "is_teacher";
    private static final String KEY_is_account = "is_account";


    //  TABLE_OC_USER_COMPANY - column nmaes
    private static final String KEY_oc_user_company_id = "id";

    //  TABLE_OC_USER_GROUP - column nmaes
    private static final String KEY_permission = "permission";


    //  TABLE_OC_WEIGHT_CLASS - column nmaes
    private static final String KEY_weight_class_id = "weight_class_id";

    //  TABLE_OC_WEIGHT_CLASS_DESCRIPTION - column nmaes
    private static final String KEY_unit = "unit";

    SpotsDialog spotsDialog;

    //  TABLE_OC_TRIAL_BALANCE_ACCOUNTS - column nmaes
    private static final String KEY_account_name = "account_name";
    private static final String KEY_TRIAL_BALANCE_ACCOUNTS_id = "id";
    private static final String KEY_opening_amount = "opening_amount";
    private static final String KEY_credit_amount = "credit_amount";
    private static final String KEY_debit_amount = "debit_amount";
    private static final String KEY_closing_amount = "closing_amount";
    private static final String KEY_year = "year";

    //  TABLE_OC_TRIAL_BALANCE_LEDGER - column nmaes
    private static final String KEY_TRIAL_BALANCE_LEDGER_id = "id";

    //array party ledger
    public static List<partymodel> todos;
    public static List<partydetailmodel> trailbalance;
    public static List<sales_register_model> salespaylist;

    public static List<partydetailmodel> openingbalanceA;
    public static List<daybookmodel> daybookdetail;
    public static ArrayList<String> partyname2club;
    public static String flatn_Detail_Summ, drcr_ledgername, amount_rec, narration_rec, cqno_rec, cqdate_rec, bankdate_rec;
    public static String amount_payment;
    public static int amountplusdb,cashbook_ob_amount;
    sales_register_model newparty;
    SQLiteDatabase dbnew;
    String ledgername;

    //sharedPreferences for getting flatno
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Gson gson;

    private static final String CREATE_TABLE_COMPANY = "CREATE TABLE "
            + TABLE_COMPANY + "("
            + KEY_CID + " INTEGER,"
            + KEY_NAME + " TEXT,"
            + KEY_UID + " INTEGER,"
            + KEY_TID + " INTEGER,"
            + KEY_SID + " INTEGER"
            + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_DAYBOOK_DETAIL1 = "CREATE TABLE " + TABLE_DAYBOOK_DETAIL1
            + "(" + KEY_ID + " INTEGER PRIMARY KEY"
            + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_DAYBOOK_DETAILS = "CREATE TABLE "
            + TABLE_DAYBOOK_DETAILS + "(" + KEY_DID + " INTEGER,"
            + KEY_party_name + " TEXT,"
            + KEY_voucher_type + " TEXT,"
            + KEY_dr_cr + " TEXT,"
            + KEY_ledger_name + " TEXT,"
            + KEY_voucher_number + " INTEGER,"
            + KEY_date + " INTEGER,"
            + KEY_amount + " INTEGER,"
            + KEY_download_date + " INTEGER,"
            + KEY_company_name + " TEXT,"
            + KEY_CID + " INTEGER,"
            + KEY_narration + " TEXT,"
            + KEY_is_payment + " INTEGER,"
            + KEY_bank_name + " TEXT,"
            + KEY_bank_date + " TEXT,"
            + KEY_branch + " TEXT,"
            + KEY_cheque_no + " TEXT,"
            + KEY_cheque_date + " TEXT,"
            + KEY_group_name + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_DETAILS_GROUP = "CREATE TABLE "
            + TABLE_DETAILS_GROUP + "(" + KEY_ID + " INTEGER PRIMARY KEY"
            + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_LEDGERS = "CREATE TABLE "
            + TABLE_LEDGERS + "(" + KEY_LID + " INTEGER,"
            + KEY_ledger_name + " TEXT,"
            + KEY_PID + " INTEGER,"
            + KEY_parent_name + " TEXT,"
            + KEY_parent_name_2 + " TEXT,"
            + KEY_CID + " INTEGER,"
            + KEY_company_name + " TEXT,"
            + KEY_type + " TEXT,"
            + KEY_flat_no + " TEXT,"
            + KEY_mobile_number + " INTEGER,"
            + KEY_is_party + " INTEGER,"
            + KEY_opening_balance + " INTEGER"
            + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_COUNTRY = "CREATE TABLE "
            + TABLE_OC_COUNTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY"
            + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_CURRENCY = "CREATE TABLE "
            + TABLE_OC_CURRENCY + "(" + KEY_currency_id + " INTEGER,"
            + KEY_title + " TEXT,"
            + KEY_code + " TEXT,"
            + KEY_symbol_left + " TEXT,"
            + KEY_symbol_right + " TEXT,"
            + KEY_decimal_place + " INTEGER,"
            + KEY_value + " INTEGER,"
            + KEY_status + " INTEGER,"
            + KEY_date_modified + " TEXT" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_EXTENSION = "CREATE TABLE "
            + TABLE_OC_EXTENSION + "(" + KEY_ID + " INTEGER PRIMARY KEY"
            + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_GEO_ZONE = "CREATE TABLE "
            + TABLE_OC_GEO_ZONE + "(" + KEY_geo_zone_id + " INTEGER,"
            + KEY_name + " TEXT,"
            + KEY_description + " TEXT,"
            + date_modified + " TEXT,"
            + KEY_date_added + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_LANGUAGE = "CREATE TABLE "
            + TABLE_OC_LANGUAGE + "(" + KEY_language_id + " INTEGER,"
            + KEY_name + " TEXT,"
            + KEY_code + " TEXT,"
            + KEY_locale + " TEXT,"
            + KEY_image + " TEXT,"
            + KEY_directory + " TEXT,"
            + KEY_sort_order + " INTEGER,"
            + KEY_status + " INTEGER" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_LENGTH_CLASS = "CREATE TABLE "
            + TABLE_OC_LENGTH_CLASS + "(" + KEY_length_class_id + " INTEGER,"
            + KEY_value + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_LENGTH_CLASS_DESCRIPTION = "CREATE TABLE "
            + TABLE_OC_LENGTH_CLASS_DESCRIPTION + "(" + KEY_length_class_id + " INTEGER,"
            + KEY_language_id + " INTEGER,"
            + KEY_title + " TEXT,"
            + KEY_unit + " TEXT"
            + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_SETTING = "CREATE TABLE "
            + TABLE_OC_SETTING + "(" + KEY_setting_id + " INTEGER,"
            + KEY_store_id + " INTEGER,"
            + KEY_code + " TEXT,"
            + KEY_key + " TEXT,"
            + KEY_value + " INTEGER,"
            + KEY_serialized + " INTEGER" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_STORE = "CREATE TABLE "
            + TABLE_OC_STORE + "(" + KEY_ID + " INTEGER PRIMARY KEY"
            + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_TAX_CLASS = "CREATE TABLE "
            + TABLE_OC_TAX_CLASS + "(" + KEY_tax_class_id + " INTEGER,"
            + KEY_title + " TEXT,"
            + KEY_description + " TEXT,"
            + KEY_date_added + " TEXT,"
            + KEY_date_modified + " TEXT" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_TAX_RATE = "CREATE TABLE "
            + TABLE_OC_TAX_RATE + "(" + KEY_tax_rate_id + " INTEGER,"
            + KEY_geo_zone_id + " INTEGER,"
            + KEY_name + " TEXT,"
            + KEY_rate + " TEXT,"
            + KEY_type + " TEXT,"
            + KEY_date_added + " TEXT,"
            + KEY_date_modified + " TEXT" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_TAX_RATE_TO_CUSTOMAR_GROUP = "CREATE TABLE "
            + TABLE_OC_TAX_RATE_TO_CUSTOMAR_GROUP + "(" + KEY_tax_rate_id + " INTEGER,"
            + KEY_customer_group_id + " INTEGER"
            + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_TAX_RULE = "CREATE TABLE "
            + TABLE_OC_TAX_RULE + "(" + KEY_tax_rule_id + " INTEGER,"
            + KEY_tax_class_id + " INTEGER,"
            + KEY_tax_rate_id + " INTEGER,"
            + KEY_based + " TEXT,"
            + KEY_priority + " INTEGER" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_USER = "CREATE TABLE "
            + TABLE_OC_USER + "(" + KEY_UID + " INTEGER,"
            + KEY_user_group_id + " INTEGER,"
            + KEY_username + " TEXT,"
            + KEY_password + " TEXT,"
            + KEY_salt + " TEXT,"
            + KEY_firstname + " TEXT,"
            + KEY_lastname + " TEXT,"
            + KEY_email + " TEXT,"
            + KEY_image + " TEXT,"
            + KEY_code + " TEXT,"
            + KEY_ip + " TEXT,"
            + KEY_status + " INTEGER,"
            + KEY_date_added + " TEXT,"
            + KEY_is_teacher + " INTEGER,"
            + KEY_is_account + " INTEGER" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_USER_COMPANY = "CREATE TABLE "
            + TABLE_OC_USER_COMPANY + "(" + KEY_oc_user_company_id + " INTEGER,"
            + KEY_UID + " INTEGER,"
            + KEY_CID + " INTEGER" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_USER_GROUP = "CREATE TABLE "
            + TABLE_OC_USER_GROUP + "(" + KEY_user_group_id + " INTEGER,"
            + KEY_name + " TEXT,"
            + KEY_permission + " TEXT" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_WEIGHT_CLASS = "CREATE TABLE "
            + TABLE_OC_WEIGHT_CLASS + "(" + KEY_weight_class_id + " INTEGER,"
            + KEY_value + " TEXT" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_WEIGHT_CLASS_DESCRIPTION = "CREATE TABLE "
            + TABLE_OC_WEIGHT_CLASS_DESCRIPTION + "(" + KEY_weight_class_id + " INTEGER,"
            + KEY_language_id + " INTEGER,"
            + KEY_title + " TEXT,"
            + KEY_unit + " TEXT" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_ZONE = "CREATE TABLE "
            + TABLE_OC_ZONE + "(" + KEY_ID + " INTEGER PRIMARY KEY"
            + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_ZONE_TO_GEO_ZONE = "CREATE TABLE "
            + TABLE_OC_ZONE_TO_GEO_ZONE + "(" + KEY_ID + " INTEGER PRIMARY KEY"
            + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_TRIAL_BALANCE_ACCOUNTS = "CREATE TABLE "
            + TABLE_OC_TRIAL_BALANCE_ACCOUNTS + "(" + KEY_account_name + " TEXT,"
            + KEY_TRIAL_BALANCE_ACCOUNTS_id + " INTEGER,"
            + KEY_opening_amount + " TEXT,"
            + KEY_credit_amount + " TEXT,"
            + KEY_debit_amount + " TEXT,"
            + KEY_closing_amount + " TEXT,"
            + KEY_company_name + " TEXT,"
            + KEY_CID + " INTEGER,"
            + KEY_year + " INTEGER" + ")";
    // todo_tag table create statement
    private static final String CREATE_TABLE_OC_TRIAL_BALANCE_LEDGER = "CREATE TABLE "
            + TABLE_OC_TRIAL_BALANCE_LEDGER + "(" + KEY_TRIAL_BALANCE_ACCOUNTS_id + " INTEGER,"
            + KEY_ledger_name + " TEXT,"
            + KEY_opening_amount + " INTEGER,"
            + KEY_credit_amount + " INTEGER,"
            + KEY_debit_amount + " INTEGER,"
            + KEY_closing_amount + " INTEGER,"
            + KEY_company_name + " TEXT,"
            + KEY_download_date + " TEXT,"
            + KEY_year + " INTEGER,"
            + KEY_CID + " INTEGER" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_DAYBOOK_DETAILS);
        db.execSQL(CREATE_TABLE_LEDGERS);

        db.execSQL(CREATE_TABLE_OC_USER);
        db.execSQL(CREATE_TABLE_OC_USER_COMPANY);

        db.execSQL(CREATE_TABLE_OC_TRIAL_BALANCE_ACCOUNTS);
        db.execSQL(CREATE_TABLE_OC_TRIAL_BALANCE_LEDGER);


        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        Log.d(TAG, "Drop older table if existed");


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYBOOK_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEDGERS);

        db.execSQL("DROP TABLE IF EXISTS " +TABLE_OC_USER);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_OC_USER_COMPANY);

        db.execSQL("DROP TABLE IF EXISTS " +TABLE_OC_TRIAL_BALANCE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_OC_TRIAL_BALANCE_LEDGER);


        Log.d(TAG, "All Table Droped");

        // Create tables again
        onCreate(db);
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_DAYBOOK_DETAILS, null, null);
        db.delete(TABLE_LEDGERS, null, null);

        db.delete(TABLE_OC_USER, null, null);
        db.delete(TABLE_OC_USER_COMPANY, null, null);

        db.delete(TABLE_OC_TRIAL_BALANCE_ACCOUNTS, null, null);
        db.delete(TABLE_OC_TRIAL_BALANCE_LEDGER, null, null);

        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public int insertFromFile(Context context, String insertStmt) throws IOException {
        // Reseting Counter
        int result = 0;

        // Open the resource
     /*   InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
*/     rollbar  = Rollbar.instance();
        //BufferedReader insertReader = new BufferedReader(new FileReader(resourceId));
        // Log.d("Inserted Table 1 : ", String.valueOf(resourceId));
        // Iterate through lines (assuming each insert has its own line and theres no other stuff)

        SQLiteDatabase db = this.getWritableDatabase();

        rollbar.debug("Data Insert:new  "+insertStmt);
        db.execSQL(insertStmt);
        result++;

        // returning number of inserted rows
        return result;
    }

    public int insertFromDirectFile(Context context, File resourceId) throws IOException {
        // Reseting Counter
        int result = 0;

        // Open the resource
     /*   InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
*/     rollbar  = Rollbar.instance();
        BufferedReader insertReader = new BufferedReader(new FileReader(resourceId));
        rollbar.debug("Inserted Table 1 : "+resourceId);
        Log.d("Inserted Table 1 : ", String.valueOf(resourceId));
        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            SQLiteDatabase db = this.getWritableDatabase();

            String insertStmt = insertReader.readLine();
            Log.d("Inserted Table 1 Data:",insertStmt);
            rollbar.debug("Data :  "+insertStmt);
            db.execSQL(insertStmt);
            result++;
        }


        insertReader.close();

        // returning number of inserted rows
        return result;
    }
    /**
     * Storing user details in database
     */
    /*public void addUser(String uid, String email, String name, String mobileno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(MOBILENUMBER, mobileno); // Created At

        // Inserting Row
        long id = db.insert(TABLE_COMPANY, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
        String selectQuery = "SELECT  * FROM " + TABLE_COMPANY;
        Log.d(TAG, "TABLE sqlite: " + selectQuery);
    }*/

  /*  public boolean updateData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }*/

    /*public void updateUser(String uid, String email, String name, String mobileno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(MOBILENUMBER, mobileno); // Created At

        // Inserting Row

        db.update(TABLE_USER, values,KEY_ID+ "=?", new String[] { name });

        db.close(); // Closing database connection

        Log.d(TAG, "New user updated into sqlite: " );

    }*/
    //partyledger data
    public List<partymodel> getdata(Context context) {
      /*  sharedPreferences = context.getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        String  stringar = sharedPreferences.getString("Split_ledger_with_flatno",null );
        Type type = new TypeToken<List<sales_register_model>>(){}.getType();
        List<sales_register_model> Split_ledger_with_flatno = gson.fromJson(stringar, type);
*/
        List<sales_register_model>  Split_ledger_with_flatno =   getSplit_ledger_with_flatno();


        partyname2club = new ArrayList<>();
        todos = new ArrayList<partymodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LEDGERS
                + " WHERE "
                + KEY_type + " = " + "'Ledger'"
                + " ORDER BY " + KEY_ledger_name + " ASC";

        Cursor c = db.rawQuery(selectQuery, null);
        Log.e("queryresult", selectQuery + c);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                partymodel td = new partymodel();
                td.setledgerid((c.getString(c.getColumnIndex(KEY_ID))));

                    if (("").equals(c.getString(c.getColumnIndex(KEY_flat_no)))) {

                      /*  Log.d( "Viewsplit2: ",todo.getLed_name().replaceAll("\\s+","")+" "+todo.getFlatno()+ " "+c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+",""));
*/
                      for(sales_register_model todo:Split_ledger_with_flatno) {
                            if (todo.getLed_name().replaceAll("\\s+", "").equalsIgnoreCase(c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+", ""))) {
//                            Log.d( "Viewsplit3: ",todo.getLed_name().replaceAll("\\s+","")+ " "+c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+",""));
                                td.setIsparty("1");
                                td.setFlatnoremoved_SC(todo.getFlatno().replaceAll("-", ""));
                                td.setRating(todo.getFlatno());
                                td.setTitle(todo.getName());
                                Split_ledger_with_flatno.remove(todo);
                                break;
                            } else {
                                td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                                td.setIsparty((c.getString(c.getColumnIndex(KEY_is_party))));
                                td.setFlatnoremoved_SC("-");
                                td.setRating("-");
                            }
                        }
                    } else {


                        td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                        td.setFlatnoremoved_SC(c.getString(c.getColumnIndex(KEY_flat_no)).replaceAll("-", ""));
                        td.setRating(c.getString(c.getColumnIndex(KEY_flat_no)));
                        td.setIsparty((c.getString(c.getColumnIndex(KEY_is_party))));
                    }


                if(("").equals(c.getString(c.getColumnIndex(KEY_parent_name_2))))
                {

                }
                else
                {
                    partyname2club.add(c.getString(c.getColumnIndex(KEY_parent_name_2)));
                }


                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());

        }
            /*  for all group*/
        Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partyname2club);
        partyname2club.clear();
        partyname2club.addAll(primesWithoutDuplicates);
        Collections.sort(partyname2club, String.CASE_INSENSITIVE_ORDER);
        Log.d("partynametype", String.valueOf(partyname2club));
        if (c.moveToFirst()) {
            do {
        for(sales_register_model todo:Split_ledger_with_flatno)
        {


                    if (todo.getLed_name().replaceAll("\\s+", "").equalsIgnoreCase(c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+", "")) && todo.getFlatno().replaceAll("\\s+", "").equalsIgnoreCase(c.getString(c.getColumnIndex(KEY_flat_no)).replaceAll("\\s+", ""))) {
                        Split_ledger_with_flatno.remove(todo);
                        break;
                    } else {
                    }


        }
            } while (c.moveToNext());
        }
        for(sales_register_model todo:Split_ledger_with_flatno)
        {
                partymodel td = new partymodel();
                td.setIsparty("1");
                td.setFlatnoremoved_SC(todo.getFlatno().replaceAll("-", ""));
                td.setRating(todo.getFlatno());
                td.setTitle(todo.getName());
                todos.add(td);
        }

        return todos;
    }
    //partyledger data on basis of wing
    public List<partymodel> getdata_wing(Context context,String wing) {
        List<sales_register_model>  Split_ledger_with_flatno =   getSplit_ledger_with_flatno();

     /*   sharedPreferences = context.getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        String  stringar = sharedPreferences.getString("Split_ledger_with_flatno",null );
        Type type = new TypeToken<List<sales_register_model>>(){}.getType();
        List<sales_register_model> Split_ledger_with_flatno = gson.fromJson(stringar, type);

*/

        todos = new ArrayList<partymodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LEDGERS + " WHERE "
                + KEY_parent_name_2 + " = " +"'"+ wing+"'"+ " AND " +
                KEY_type + " = " + "'Ledger'" + " ORDER BY " + KEY_ledger_name + " ASC";

        Cursor c = db.rawQuery(selectQuery, null);
        Log.e("queryresult", selectQuery );
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                partymodel td = new partymodel();
                td.setledgerid((c.getString(c.getColumnIndex(KEY_ID))));

                if (("").equals(c.getString(c.getColumnIndex(KEY_flat_no)))) {

                      /*  Log.d( "Viewsplit2: ",todo.getLed_name().replaceAll("\\s+","")+" "+todo.getFlatno()+ " "+c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+",""));
*/
                    for(sales_register_model todo:Split_ledger_with_flatno) {
                        if (todo.getLed_name().replaceAll("\\s+", "").equalsIgnoreCase(c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+", ""))) {
//                            Log.d( "Viewsplit3: ",todo.getLed_name().replaceAll("\\s+","")+ " "+c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+",""));
                            td.setIsparty("1");
                            td.setFlatnoremoved_SC(todo.getFlatno().replaceAll("-", ""));
                            td.setRating(todo.getFlatno());
                            td.setTitle(todo.getName());
                            Split_ledger_with_flatno.remove(todo);
                            break;
                        } else {
                            td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setIsparty((c.getString(c.getColumnIndex(KEY_is_party))));
                            td.setFlatnoremoved_SC("-");
                            td.setRating("-");
                        }
                    }
                } else {


                    td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setFlatnoremoved_SC(c.getString(c.getColumnIndex(KEY_flat_no)).replaceAll("-", ""));
                    td.setRating(c.getString(c.getColumnIndex(KEY_flat_no)));
                    td.setIsparty((c.getString(c.getColumnIndex(KEY_is_party))));
                }

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());

        }

        if (c.moveToFirst()) {
            do {
                for(sales_register_model todo:Split_ledger_with_flatno)
                {


                    if (todo.getLed_name().replaceAll("\\s+", "").equalsIgnoreCase(c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+", "")) && todo.getFlatno().replaceAll("\\s+", "").equalsIgnoreCase(c.getString(c.getColumnIndex(KEY_flat_no)).replaceAll("\\s+", ""))) {
                        Split_ledger_with_flatno.remove(todo);
                        break;
                    } else {
                    }


                }
            } while (c.moveToNext());
        }
        for(sales_register_model todo:Split_ledger_with_flatno)
        {
            if(todo.getTest().equals(wing)){
                partymodel td = new partymodel();
                td.setIsparty("1");
                td.setFlatnoremoved_SC(todo.getFlatno().replaceAll("-", ""));
                td.setRating(todo.getFlatno());
                td.setTitle(todo.getName());
                todos.add(td);
            }
        }


        return todos;
    }

    public List<partydetailmodel> getdataforTrailBalance(String year) {
        trailbalance = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_OC_TRIAL_BALANCE_LEDGER
                + " WHERE "
                + KEY_year + " = " + year;

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                partydetailmodel td = new partydetailmodel();
                td.setAmount((c.getString(c.getColumnIndex(KEY_opening_amount))));
                td.setDate((c.getString(c.getColumnIndex(KEY_debit_amount))));
                td.setVoucher_number(c.getString(c.getColumnIndex(KEY_ledger_name)));
                td.setVoucher_type(c.getString(c.getColumnIndex(KEY_credit_amount)));
                td.setDr_cr(c.getString(c.getColumnIndex(KEY_closing_amount)));

                // adding to todo list
                trailbalance.add(td);
            } while (c.moveToNext());
        }

        return trailbalance;
    }

    public List<partydetailmodel> getdataforAccountBalance(String year) {
        trailbalance = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_OC_TRIAL_BALANCE_ACCOUNTS + " WHERE "
                + KEY_year + " = " + year;

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                partydetailmodel td = new partydetailmodel();
                td.setAmount((c.getString(c.getColumnIndex(KEY_opening_amount))));
                td.setDate((c.getString(c.getColumnIndex(KEY_debit_amount))));
                td.setVoucher_number(c.getString(c.getColumnIndex(KEY_account_name)));
                td.setVoucher_type(c.getString(c.getColumnIndex(KEY_credit_amount)));
                td.setDr_cr(c.getString(c.getColumnIndex(KEY_closing_amount)));

                // adding to todo list
                trailbalance.add(td);
            } while (c.moveToNext());
        }

        return trailbalance;
    }

    //used in party wise ledger inner page on click of ledger name
    public List<partydetailmodel> getdataforpartywisesummarydata(String preference,String amountsort,String isparty,String ledgername,String startdate,String enddate) {
        //vno used to compare voucher_number
        String vno = " ",vno_contra = " ";
        Log.d( "partywisesummarydata2: ",preference+" "+amountsort+" "+ledgername+" "+startdate+" "+enddate);
        //a is used as a string to add in second partydetailmodel in first if condition
        String a = " ";
        //second partydetailmodel for Bill vouchertype
        partydetailmodel td2 = null;
        //amountplusdb to add total amount for Bill vouchertype
        amountplusdb = 0;

        trailbalance = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
       /* if (preference.equals("preference")&&amountsort.equals("amountsort")) {
*/
            if(isparty.equals("0"))
            {
                selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                        + " WHERE "
                        + KEY_ledger_name + " like '%" + ledgername + "%'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + KEY_date + " ASC";
            }
            else if(isparty.equals("1"))
            {
                selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                        + " WHERE "
                        + KEY_party_name + " like '%" + ledgername + "%'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + KEY_date + " ASC";
            }

    /*   }
           else if (preference.equals("greaterthan"))
     {

            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " like '%" + ledgername + "%'" + " AND "
                    +
                    KEY_date + " >= '" + startdate + "'"
                    + " AND " + KEY_date + " <= '" + enddate + "'"
                    + " ORDER BY " + KEY_date + " ASC";
           *//* selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + ledgername + "'" + " AND "
                    +
                    KEY_date + " >= '" + startdate + "'"
                    + " AND " + KEY_date + " <= '" + enddate + "'"
                    + " AND "
                    +
                    KEY_amount + " >= '" + amountsort + "'"
                    + " ORDER BY " + KEY_date + " ASC";*//*
        }
        else if (preference.equals("equalto")) {
*//*
                selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                        + " WHERE "
                        + KEY_party_name + " = " + "'" + ledgername + "'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " AND "
                        +
                        KEY_amount + " = '" + amountsort + "'"
                        + " ORDER BY " + KEY_date + " ASC";

*//*

            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " like '%" + ledgername + "%'" + " AND "
                    +
                    KEY_date + " >= '" + startdate + "'"
                    + " AND " + KEY_date + " <= '" + enddate + "'"
                    + " ORDER BY " + KEY_date + " ASC";
            }*/
        Log.e("queryresult1", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                //first partydetailmodel for all vouchertype
                partydetailmodel td = new partydetailmodel();

                //for bill  main charges like Interest on Defaulted Dues and all
                if ((c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Bill"))&&(isparty.equals("0")))
                {
                    td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                    td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                    td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                    td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                    td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                    td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                    td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                    trailbalance.add(td);
                }
                //if the condition as vno different then the below will satisfied
                if (!c.getString(c.getColumnIndex(KEY_voucher_number)).equals(vno)) {
                    //in else condition where vno same ,then there we added all data in second partydetailmodel and here we just adding in to arraylist-trailbalance
                    if (a.equals("test")) {
                        // adding to trailbalance list
                        trailbalance.add(td2);
                        a = "";
                        amountplusdb = 0;
                    }
                    if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Bill")) {
                        amountplusdb = amountplusdb + c.getInt(c.getColumnIndex(KEY_amount));
                        vno = c.getString(c.getColumnIndex(KEY_voucher_number));

                    } else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Receipt")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Journal")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Payment")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Contra")) {
                        if (c.getString(c.getColumnIndex(KEY_ledger_name)).contains(ledgername)) {
                            //first partydetailmodel added
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        } else {


                        }
                    }

                 /*   else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Journal")) {

                        if (c.getString(c.getColumnIndex(KEY_ledger_name)).contains(ledgername)) {
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        }
                        else {


                        }
                    }
                    else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Payment")) {

                        if (c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Debit")) {
                            //not to insert anything
                        }
                        else {

                            //first partydetailmodel added
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr("Debit");
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        }
                    }*/
          /*          else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Contra")) {

                        if ((c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Credit"))&&(c.getString(c.getColumnIndex(KEY_party_name)).equals("T.J.S.B A/c"))) {
                            //first partydetailmodel added
                            vno_contra = c.getString(c.getColumnIndex(KEY_voucher_number));

                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr("Debit");
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);

                        }
                        else if (c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Credit")) {
                            //not to insert anything
                        }
                        else  if (c.getString(c.getColumnIndex(KEY_voucher_number)).equals(vno_contra)) {

                        }
                        else {

                            //first partydetailmodel added
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        }
                    }*/
                    else {

                        //first partydetailmodel added
                        td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setLedger_name((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                        td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                        td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                        td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                        trailbalance.add(td);
                    }
                }
                //if the condition as vno same then the below will satisfied
                else if ((c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Bill") || c.getString(c.getColumnIndex(KEY_voucher_number)).equals(vno))) {
                    a = "test";
                    td2 = new partydetailmodel();
                    amountplusdb = amountplusdb + c.getInt(c.getColumnIndex(KEY_amount));
                    vno = c.getString(c.getColumnIndex(KEY_voucher_number));

                    td2.setLedger_name((c.getString(c.getColumnIndex(KEY_party_name))));
                    td2.setAmount(String.valueOf(amountplusdb));
                    td2.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                    td2.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td2.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td2.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td2.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                    td2.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                    td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                    td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                    td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                    if (c.isLast()) {
                        trailbalance.add(td2);
                    }
                }

            } while (c.moveToNext());
        }

      /*  List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

      */  Log.d("wisesummarydata: ","outside");
        if (preference.equals("equalto")) {
             List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

            Log.d("wisesummarydata: ","inside");
            for (partydetailmodel todo : trailbalance) {

                td2 = new partydetailmodel();
                Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmount());
                if(amountsort.equals(todo.getAmount())) {
                    Log.d("wisesummarydata: ","insode "+todo.getAmount()+" "+todo.getVoucher_type());
                    td2.setPartyname(todo.getPartyname());
                    td2.setLedger_name(todo.getLedger_name());
                    td2.setAmount(todo.getAmount());
                    td2.setDate(todo.getDate());
                    td2.setVoucher_number(todo.getVoucher_number());
                    td2.setVoucher_type(todo.getVoucher_type());
                    td2.setDr_cr(todo.getDr_cr());
                    td2.setNarration(todo.getNarration());
                    td2.setCqno(todo.getCqno());
                    td2.setCqdate(todo.getCqdate());
                    td2.setBankdate(todo.getBankdate());
                    for_pref_list.add(td2);
                }
            }
            trailbalance.clear();
            trailbalance.addAll(for_pref_list);
        }
        else if (preference.equals("greaterthan")) {
             List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

            Log.d("wisesummarydata: ","inside");
            for (partydetailmodel todo : trailbalance) {

                td2 = new partydetailmodel();
                Float amt_userinput = Float.parseFloat(amountsort);
                Float amt_fromtable = Float.parseFloat(todo.getAmount());
                Log.d("wisesummarydata: ","insode"+amt_userinput+" "+amt_fromtable);

                if(amt_userinput <= amt_fromtable) {
                    Log.d("wisesummarydata: ","insode "+todo.getAmount()+" "+todo.getVoucher_type());
                    td2.setPartyname(todo.getPartyname());
                    td2.setLedger_name(todo.getLedger_name());
                    td2.setAmount(todo.getAmount());
                    td2.setDate(todo.getDate());
                    td2.setVoucher_number(todo.getVoucher_number());
                    td2.setVoucher_type(todo.getVoucher_type());
                    td2.setDr_cr(todo.getDr_cr());
                    td2.setNarration(todo.getNarration());
                    td2.setCqno(todo.getCqno());
                    td2.setCqdate(todo.getCqdate());
                    td2.setBankdate(todo.getBankdate());
                    for_pref_list.add(td2);
                }
            }
            trailbalance.clear();
            trailbalance.addAll(for_pref_list);
        }

        return trailbalance;
    }

    ////used in party wise ledger check date
    public List<partydetailmodel> CheckDate_partywisesummarydata(String preference,String amountsort,String isparty,String ledgername,String startdate) {
        //vno used to compare voucher_number
        String vno = " ",vno_contra = " ";
        Log.d( "partywisesummarydata: ",preference+" "+amountsort+" "+ledgername+" "+startdate);
        //a is used as a string to add in second partydetailmodel in first if condition
        String a = " ";
        //second partydetailmodel for Bill vouchertype
        partydetailmodel td2 = null;
        //amountplusdb to add total amount for Bill vouchertype
        amountplusdb = 0;

        trailbalance = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
   /*     if (preference.equals("preference")&&amountsort.equals("amountsort")) {
*/

        if(isparty.equals("0"))
        {
            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_ledger_name + " like '%" + ledgername + "%'" + " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " ORDER BY " + KEY_date + " ASC";

        }
        else if(isparty.equals("1"))
        {
            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " like '%" + ledgername + "%'" + " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " ORDER BY " + KEY_date + " ASC";

        }

/*
        selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " like '%" + ledgername + "%'" + " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " ORDER BY " + KEY_date + " ASC";*/
     /*   }
        else if (preference.equals("greaterthan"))
        {
            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " like '%" + ledgername + "%'" + " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " ORDER BY " + KEY_date + " ASC";
          *//*  selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + ledgername + "'" + " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " AND "
                    +
                    KEY_amount + " >= '" + amountsort + "'"
                    + " ORDER BY " + KEY_date + " ASC";*//*
        }
        else if (preference.equals("equalto")) {
           *//* selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + ledgername + "'" + " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " AND "
                    +
                    KEY_amount + " = '" + amountsort + "'"
                    + " ORDER BY " + KEY_date + " ASC";*//*

            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " like '%" + ledgername + "%'" + " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " ORDER BY " + KEY_date + " ASC";
        }*/


            Log.e("queryresult2", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                //first partydetailmodel for all vouchertype
                partydetailmodel td = new partydetailmodel();

                //if the condition as vno different then the below will satisfied
                if (!c.getString(c.getColumnIndex(KEY_voucher_number)).equals(vno)) {
                    //in else condition where vno same ,then there we added all data in second partydetailmodel and here we just adding in to arraylist-trailbalance
                    if (a.equals("test")) {
                        // adding to trailbalance list
                        trailbalance.add(td2);
                        a = "";
                        amountplusdb = 0;
                    }
                    if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Bill")) {
                        amountplusdb = amountplusdb + c.getInt(c.getColumnIndex(KEY_amount));
                        vno = c.getString(c.getColumnIndex(KEY_voucher_number));

                    } else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Receipt")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Journal")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Payment")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Contra")) {
                        if (c.getString(c.getColumnIndex(KEY_ledger_name)).contains(ledgername)) {
                            //first partydetailmodel added
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        } else {


                        }
                    }

                 /*   else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Journal")) {

                        if (c.getString(c.getColumnIndex(KEY_ledger_name)).contains(ledgername)) {
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        }
                        else {


                        }
                    }
                    else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Payment")) {

                        if (c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Debit")) {
                            //not to insert anything
                        }
                        else {

                            //first partydetailmodel added
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr("Debit");
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        }
                    }*/
          /*          else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Contra")) {

                        if ((c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Credit"))&&(c.getString(c.getColumnIndex(KEY_party_name)).equals("T.J.S.B A/c"))) {
                            //first partydetailmodel added
                            vno_contra = c.getString(c.getColumnIndex(KEY_voucher_number));

                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr("Debit");
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);

                        }
                        else if (c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Credit")) {
                            //not to insert anything
                        }
                        else  if (c.getString(c.getColumnIndex(KEY_voucher_number)).equals(vno_contra)) {

                        }
                        else {

                            //first partydetailmodel added
                            td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                            td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                            td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                            td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                            td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            trailbalance.add(td);
                        }
                    }*/
                    else {

                        //first partydetailmodel added
                        td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setLedger_name((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                        td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                        td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                        td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));
                        trailbalance.add(td);
                    }
                }
                //if the condition as vno same then the below will satisfied
                else if ((c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Bill") || c.getString(c.getColumnIndex(KEY_voucher_number)).equals(vno))) {
                    a = "test";
                    td2 = new partydetailmodel();
                    amountplusdb = amountplusdb + c.getInt(c.getColumnIndex(KEY_amount));
                    vno = c.getString(c.getColumnIndex(KEY_voucher_number));

                    td2.setLedger_name((c.getString(c.getColumnIndex(KEY_party_name))));
                    td2.setAmount(String.valueOf(amountplusdb));
                    td2.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                    td2.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td2.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td2.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td2.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                    td2.setNarration(c.getString(c.getColumnIndex(KEY_narration)));

                    if (c.isLast()) {
                        trailbalance.add(td2);
                    }
                }

            } while (c.moveToNext());
        }

      /*  List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();
*/
    /*    Log.d("wisesummarydata: ","outside");
        if (preference.equals("equalto")) {
            List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

            Log.d("wisesummarydata: ","inside");
            for (partydetailmodel todo : trailbalance) {

                td2 = new partydetailmodel();
                Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmount());
                if(amountsort.equals(todo.getAmount())) {
                    Log.d("wisesummarydata: ","insode "+todo.getAmount()+" "+todo.getVoucher_type());
                    td2.setPartyname(todo.getPartyname());
                    td2.setLedger_name(todo.getLedger_name());
                    td2.setAmount(todo.getAmount());
                    td2.setDate(todo.getDate());
                    td2.setVoucher_number(todo.getVoucher_number());
                    td2.setVoucher_type(todo.getVoucher_type());
                    td2.setDr_cr(todo.getDr_cr());
                    td2.setNarration(todo.getNarration());
                    td2.setCqno(todo.getCqno());
                    td2.setCqdate(todo.getCqdate());
                    td2.setBankdate(todo.getBankdate());
                    for_pref_list.add(td2);
                }
            }
            trailbalance.clear();
            trailbalance.addAll(for_pref_list);
        } else if (preference.equals("greaterthan")) {
            List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

            Log.d("wisesummarydata: ","inside");
            for (partydetailmodel todo : trailbalance) {

                td2 = new partydetailmodel();
                Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmount());
                int amt_userinput = Integer.parseInt(amountsort);
                int amt_fromtable = Integer.parseInt(todo.getAmount());
                if(amt_userinput <= amt_fromtable) {
                    Log.d("wisesummarydata: ","insode "+todo.getAmount()+" "+todo.getVoucher_type());
                    td2.setPartyname(todo.getPartyname());
                    td2.setLedger_name(todo.getLedger_name());
                    td2.setAmount(todo.getAmount());
                    td2.setDate(todo.getDate());
                    td2.setVoucher_number(todo.getVoucher_number());
                    td2.setVoucher_type(todo.getVoucher_type());
                    td2.setDr_cr(todo.getDr_cr());
                    td2.setNarration(todo.getNarration());
                    td2.setCqno(todo.getCqno());
                    td2.setCqdate(todo.getCqdate());
                    td2.setBankdate(todo.getBankdate());
                    for_pref_list.add(td2);
                }
            }
            trailbalance.clear();
            trailbalance.addAll(for_pref_list);
        }*/



        return trailbalance;
    }


    //used to get OpeningBalance in partywise ledger
    public Integer  getOpeningBalance(String ledgername) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_OC_TRIAL_BALANCE_LEDGER
                + " WHERE "
                + KEY_ledger_name + " = " + "'" + ledgername + "'" + " AND "
                + KEY_year + " = " + 2018;

        Cursor c = db.rawQuery(selectQuery, null);
        Log.d("queryresultob", selectQuery);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                cashbook_ob_amount = (c.getInt(c.getColumnIndex(KEY_opening_amount)));

            } while (c.moveToNext());
        }

        return cashbook_ob_amount;
    }

    public List<partydetailmodel> getdataDetailsummarydata(String partyname, String voucher_number, String voucher_type) {
        amountplusdb = 0;
        trailbalance = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        if (voucher_number.equals("0")) {
            String selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + partyname + "'"
                    + " AND "
                    + KEY_voucher_number + " IS NULL "
                    + " AND "
                    + KEY_voucher_type + " = " + "'" + voucher_type + "'";
            Log.e("queryresult", selectQuery);
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    partydetailmodel td = new partydetailmodel();
                    td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                    td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                    td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));

                    amountplusdb = amountplusdb + (c.getInt(c.getColumnIndex(KEY_amount)));
                    // adding to todo list
                    trailbalance.add(td);
                } while (c.moveToNext());
            }

            String sqledger = "SELECT  * FROM " + TABLE_LEDGERS + " WHERE "
                    + KEY_ledger_name + " = " + "'" + partyname + "'";

            Cursor cledger = db.rawQuery(sqledger, null);
            // looping through all rows and adding to list
            if (cledger.moveToFirst()) {
                do {

                    flatn_Detail_Summ = cledger.getString(cledger.getColumnIndex(KEY_flat_no));
                    Log.d("flatni", flatn_Detail_Summ);

                /*partymodel td = new partymodel();
                td.setledgerid((c.getString(c.getColumnIndex(KEY_ID))));
                td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                td.setRating(c.getString(c.getColumnIndex(KEY_flat_no)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_parent_name_2)));

                // adding to todo list
                todos.add(td);*/
                } while (cledger.moveToNext());
            }

            String sqReceipt = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + partyname + "'"
                    + " AND "
                    + KEY_voucher_number + " = " + voucher_number
                    + " AND "
                    + KEY_voucher_type + " = " + "'" + voucher_type + "'";
               /* + " AND "
                + KEY_dr_cr + " = "+ "'Debit'"*/


            Cursor cReceipt = db.rawQuery(sqReceipt, null);
            // looping through all rows and adding to list
            if (cReceipt.moveToFirst()) {
                do {

                    //for getting bsnk and Cash amount
                    if (voucher_type.equals("Payment")) {
                        drcr_ledgername = cReceipt.getString(cReceipt.getColumnIndex(KEY_party_name));
                    } else {
                        drcr_ledgername = cReceipt.getString(cReceipt.getColumnIndex(KEY_ledger_name));
                    }
                    amount_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_amount));
                    narration_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_narration));
                    cqno_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_cheque_no));
                    cqdate_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_cheque_date));
                    bankdate_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_bank_date));
                    Log.d("flatnidrcr_rec", drcr_ledgername);

                /*partymodel td = new partymodel();
                td.setledgerid((c.getString(c.getColumnIndex(KEY_ID))));
                td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                td.setRating(c.getString(c.getColumnIndex(KEY_flat_no)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_parent_name_2)));

                // adding to todo list
                todos.add(td);*/
                } while (cReceipt.moveToNext());
            }

        } else {
            String selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + partyname + "'"
                    + " AND "
                    + KEY_voucher_number + " = " + voucher_number
                    + " AND "
                    + KEY_voucher_type + " = " + "'" + voucher_type + "'";
            Log.e("queryresult", selectQuery);
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    partydetailmodel td = new partydetailmodel();
                    td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                    td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                    td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));

                    amountplusdb = amountplusdb + (c.getInt(c.getColumnIndex(KEY_amount)));
                    // adding to todo list
                    trailbalance.add(td);
                } while (c.moveToNext());
            }

            String sqledger = "SELECT  * FROM " + TABLE_LEDGERS + " WHERE "
                    + KEY_ledger_name + " = " + "'" + partyname + "'";

            Cursor cledger = db.rawQuery(sqledger, null);
            // looping through all rows and adding to list
            if (cledger.moveToFirst()) {
                do {

                    flatn_Detail_Summ = cledger.getString(cledger.getColumnIndex(KEY_flat_no));
                    Log.d("flatni", flatn_Detail_Summ);

                /*partymodel td = new partymodel();
                td.setledgerid((c.getString(c.getColumnIndex(KEY_ID))));
                td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                td.setRating(c.getString(c.getColumnIndex(KEY_flat_no)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_parent_name_2)));

                // adding to todo list
                todos.add(td);*/
                } while (cledger.moveToNext());
            }

            String sqReceipt = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + partyname + "'"
                    + " AND "
                    + KEY_voucher_number + " = " + voucher_number
                    + " AND "
                    + KEY_voucher_type + " = " + "'" + voucher_type + "'";
               /* + " AND "
                + KEY_dr_cr + " = "+ "'Debit'"*/


            Cursor cReceipt = db.rawQuery(sqReceipt, null);
            // looping through all rows and adding to list
            if (cReceipt.moveToFirst()) {
                do {

                    //for getting bsnk and Cash amount
                    if (voucher_type.equals("Payment")) {
                        drcr_ledgername = cReceipt.getString(cReceipt.getColumnIndex(KEY_party_name));
                    } else {
                        drcr_ledgername = cReceipt.getString(cReceipt.getColumnIndex(KEY_ledger_name));
                    }
                    amount_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_amount));
                    narration_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_narration));
                    cqno_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_cheque_no));
                    cqdate_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_cheque_date));
                    bankdate_rec = cReceipt.getString(cReceipt.getColumnIndex(KEY_bank_date));
                    Log.d("flatnidrcr_rec", drcr_ledgername);

                /*partymodel td = new partymodel();
                td.setledgerid((c.getString(c.getColumnIndex(KEY_ID))));
                td.setTitle((c.getString(c.getColumnIndex(KEY_ledger_name))));
                td.setRating(c.getString(c.getColumnIndex(KEY_flat_no)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_parent_name_2)));

                // adding to todo list
                todos.add(td);*/
                } while (cReceipt.moveToNext());
            }

        }


        return trailbalance;
    }


    public List<partydetailmodel> getinfo_ledgername(String ledgername) {
        openingbalanceA = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LEDGERS
                + " WHERE "
                + KEY_ledger_name + " = " + "'" + ledgername + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                partydetailmodel td = new partydetailmodel();
                td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                td.setCompanyname((c.getString(c.getColumnIndex(KEY_company_name))));
                td.setMobilenumber((c.getString(c.getColumnIndex(KEY_mobile_number))));
                td.setFlatno((c.getString(c.getColumnIndex(KEY_flat_no))));

                // adding to todo list
                openingbalanceA.add(td);
            } while (c.moveToNext());
        }

        return openingbalanceA;
    }

    //used in daybookdetail page
    public List<partydetailmodel> getdaybookdetail(String startdate,String enddate) {
        openingbalanceA = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                + " WHERE "+
                KEY_date +" >= '" + startdate + "'"
                + " AND "+ KEY_date +" <= '" + enddate + "'"
                + " ORDER BY " + KEY_date + " ASC";

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Receipt")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Journal")||c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Contra")) {
                    if (c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Debit")) {

                        partydetailmodel td = new partydetailmodel();
                        td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                        td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                        td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                        td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                        td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));
// adding to todo list
                        openingbalanceA.add(td);
                    }
                }
                else if (c.getString(c.getColumnIndex(KEY_voucher_type)).equals("Payment")){
                    if (c.getString(c.getColumnIndex(KEY_dr_cr)).equals("Credit")) {

                        partydetailmodel td = new partydetailmodel();
                        td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                        td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                        td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                        td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                        td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));
// adding to todo list
                        openingbalanceA.add(td);
                    }
                }
                else
                {
                    partydetailmodel td = new partydetailmodel();
                    td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                    td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                    td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                    td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                    td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                    td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                    td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));
// adding to todo list
                    openingbalanceA.add(td);
                }
            } while (c.moveToNext());
        }

        return openingbalanceA;
    }


    //used in PaymentDetail page
    public List<sales_register_model> getPaymentDetail(String startdate,String enddate) {

        amountplusdb = 0;
        salespaylist = new ArrayList<sales_register_model>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  " + KEY_ledger_name + ", SUM(" + KEY_amount + ") as Total FROM " + TABLE_DAYBOOK_DETAILS
                + " WHERE "
                + KEY_voucher_type + " = " + "'Payment'"+ " AND "
                +
                KEY_date +" >= '" + startdate + "'"
                + " AND "+ KEY_date +" <= '" + enddate + "'"
                + " GROUP BY " + KEY_ledger_name
                + " ORDER BY " + KEY_voucher_number + " ASC";

        Log.e("queryresult", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to
       List<partydetailmodel> bankname = getBankName("Bank Accounts");
       ArrayList<String> banklist = new ArrayList<>();
        for (partydetailmodel todo : bankname) {
            if (c.moveToFirst()) {
                do {
                  //  Log.d("getPaymentDetail: ",c.getInt(c.getColumnIndex("Total"))+" "+todo.getName()+" "+c.getString(c.getColumnIndex(KEY_ledger_name)));
                   if (c.getInt(c.getColumnIndex("Total"))==0) {


                        }
                        else if (todo.getLedger_name().equals((c.getString(c.getColumnIndex(KEY_ledger_name)))) && (c.getInt(c.getColumnIndex("Total"))!=0)) {
                       Log.d("getPaymentDetail:2 ",c.getInt(c.getColumnIndex("Total"))+" "+todo.getLedger_name()+" "+c.getString(c.getColumnIndex(KEY_ledger_name)));
                       banklist.add(todo.getLedger_name());
                       sales_register_model td = new sales_register_model();
                        td.setName((c.getString(c.getColumnIndex(KEY_ledger_name))));
                        td.setAmt_for_receipt(c.getInt(c.getColumnIndex("Total")));

                        td.setDate1(startdate);
                        td.setDate2(enddate);
                        // adding to todo list
                        salespaylist.add(td);

                    }

                }
                while (c.moveToNext());
            }
        }

        if (c.moveToFirst()) {
            do {
                if ("Cash".equals((c.getString(c.getColumnIndex(KEY_ledger_name))))) {
                    sales_register_model td = new sales_register_model();
                    td.setName((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setAmt_for_receipt(c.getInt(c.getColumnIndex("Total")));


                    td.setDate1(startdate);
                    td.setDate2(enddate);
                    // adding to todo list
                    salespaylist.add(td);
                }
                /*int amt = Integer.parseInt(c.getString(c.getColumnIndex("Total")));
                amountplusdb=amountplusdb+amt;*/

            } while (c.moveToNext());
        }



        if (c.moveToFirst()) {
            do {
                if ("Cash".equals((c.getString(c.getColumnIndex(KEY_ledger_name)))) || banklist.contains(c.getString(c.getColumnIndex(KEY_ledger_name)))) {

                    Toast.makeText(context,c.getString(c.getColumnIndex(KEY_ledger_name)),Toast.LENGTH_SHORT).show();
                } else {
                    sales_register_model td = new sales_register_model();
                    td.setName((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setAmt_for_receipt(c.getInt(c.getColumnIndex("Total")));


                    td.setDate1(startdate);
                    td.setDate2(enddate);
                    // adding to todo list
                    salespaylist.add(td);
                }

            } while (c.moveToNext());
        }

        return salespaylist;
    }

    //used in PaymentDetail Innerpage
    public List<partydetailmodel> getPaymentDetail_Innerpage(String ledgername, String voucher_type,String startdate,String enddate) {
        amountplusdb = 0;
        trailbalance = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                + " WHERE "
                + KEY_ledger_name + " = " + "'" + ledgername + "'"
                + " AND "
                + KEY_voucher_type + " = " + "'" + voucher_type + "'"+ " AND "
                +
                KEY_date +" >= '" + startdate + "'"
                + " AND "+ KEY_date +" <= '" + enddate + "'"
                + " ORDER BY " + KEY_voucher_number + " ASC";
        Log.e("queryresult", selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                partydetailmodel td = new partydetailmodel();
                td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                td.setDr_cr(c.getString(c.getColumnIndex(KEY_dr_cr)));
                td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                amountplusdb = amountplusdb + (c.getInt(c.getColumnIndex(KEY_amount)));
                // adding to todo list
                trailbalance.add(td);
            } while (c.moveToNext());
        }


        //for getting


        return trailbalance;
    }


    //used in Reciept page
    public List<sales_register_model> getReceiptdetail(String vtype) {
        salespaylist = new ArrayList<sales_register_model>();
        dbnew = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                + " WHERE "
                + KEY_voucher_type + " = " + "'" + vtype + "'"
                + " AND "
                + KEY_dr_cr + " = " + "'Credit'"
                + " ORDER BY " + KEY_voucher_number + " ASC";

        Cursor c = dbnew.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                newparty = new sales_register_model();
                newparty.setName((c.getString(c.getColumnIndex(KEY_party_name))));
                newparty.setVn((c.getString(c.getColumnIndex(KEY_voucher_number))));

                Log.d("getReceiptdetail: ", c.getString(c.getColumnIndex(KEY_party_name)) + c.getString(c.getColumnIndex(KEY_voucher_number)));
                getReceiptdetail2(c.getString(c.getColumnIndex(KEY_party_name)), c.getString(c.getColumnIndex(KEY_voucher_number)), vtype);

                salespaylist.add(newparty);
            } while (c.moveToNext());
        }

        return salespaylist;
    }

    //used in Reciept page for getting flatno from ledger
    public void getReceiptdetail2(String partyname, String vouchernumber, String vtype) {


        String sqledger = "SELECT  * FROM " + TABLE_LEDGERS + " WHERE "
                + KEY_ledger_name + " = " + "'" + partyname + "'";

        Cursor c = dbnew.rawQuery(sqledger, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                newparty.setFlatno((c.getString(c.getColumnIndex(KEY_flat_no))));
                String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                Log.d("getReceiptdetail2: ", pn2);

                newparty.setTest(pn2);
                salespaylist.add(newparty);
            /*    td.setVouchernumber((c.getString(c.getColumnIndex(KEY_voucher_number))));
                td.setVouchertype((c.getString(c.getColumnIndex(KEY_voucher_type))));
                td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                td.setAmount((c.getString(c.getColumnIndex(KEY_amount))));
                td.setLedgername((c.getString(c.getColumnIndex(KEY_ledger_name))));*/
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        String selectQuery;
        if(vtype.equals("Bill_from_sales")){


            //here vn is ledger name
            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_ledger_name + " = " + "'" + vouchernumber + "'" + " AND "
                    + KEY_voucher_type + " = " + "'Bill'" + " AND "
                    + KEY_dr_cr + " = " + "'Credit'"
                    + " ORDER BY " + KEY_voucher_number + " ASC";
        }
        else {

            selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = " + "'" + partyname + "'" + " AND "
                    + KEY_voucher_type + " = " + "'" + vtype + "'" + " AND "
                    + KEY_voucher_number + " = " + "'" + vouchernumber + "'" + " AND "
                    + KEY_dr_cr + " = " + "'Credit'"
                    + " ORDER BY " + KEY_voucher_number + " ASC";
        }

        Cursor c2 = dbnew.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c2.moveToFirst()) {
            do {
                newparty.setName((c2.getString(c2.getColumnIndex(KEY_party_name))));
                /*newparty.setVn((c2.getString(c2.getColumnIndex(KEY_voucher_number))));*/
                newparty.setType((c2.getString(c2.getColumnIndex(KEY_voucher_type))));
                newparty.setDate((c2.getString(c2.getColumnIndex(KEY_date))));
                newparty.setAmt_for_receipt((c2.getInt(c2.getColumnIndex(KEY_amount))));

                newparty.setLed_name((c2.getString(c2.getColumnIndex(KEY_ledger_name))));
                salespaylist.add(newparty);
                // adding to todo list
            } while (c2.moveToNext());
        }
        c2.close();

    }


    //used in Bill sales account
    public List<sales_register_model> getBill_salesacc(String startdate,String enddate) {


        salespaylist = new ArrayList<sales_register_model>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  " + KEY_ledger_name + ", SUM(" + KEY_amount + ") as Total FROM " + TABLE_DAYBOOK_DETAILS
                + " WHERE "
                + KEY_voucher_type + " = " + "'Bill'"+ " AND "
                +
                KEY_date +" >= '" + startdate + "'"
                + " AND "+ KEY_date +" <= '" + enddate + "'"
                + " GROUP BY " + KEY_ledger_name
                + " ORDER BY " + KEY_voucher_number + " ASC";

        Log.e("queryresult", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {

                sales_register_model td = new sales_register_model();
                td.setName((c.getString(c.getColumnIndex(KEY_ledger_name))));
                td.setAmt_for_receipt(c.getInt(c.getColumnIndex("Total")));
                td.setDate1(startdate);
                td.setDate2(enddate);


                // adding to todo list
                salespaylist.add(td);


            } while (c.moveToNext());
        }

        return salespaylist;
    }

    //used in Bill sales account
    public List<sales_register_model> getBill_salesacc_maindetails(String ledname) {


        salespaylist = new ArrayList<sales_register_model>();
        dbnew = this.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + TABLE_DAYBOOK_DETAILS
                + " WHERE "
                + KEY_ledger_name + " = " + "'" + ledname + "'" + " AND "
                + KEY_voucher_type + " = " + "'Bill'" + " AND "
                + KEY_dr_cr + " = " + "'Credit'"
                + " ORDER BY " + KEY_voucher_number + " ASC";

        Log.e("queryresult", selectQuery);

        Cursor c2 = dbnew.rawQuery(selectQuery, null);


        if (c2.moveToFirst()) {
            do {
                newparty = new sales_register_model();
                getReceiptdetail2(c2.getString(c2.getColumnIndex(KEY_party_name)), ledname, "Bill_from_sales");

            } while (c2.moveToNext());
        }

        return salespaylist;
    }

    //used in Reciept page
    public List<sales_register_model> getReceiptexp(Context context,String preference,String amountsort,String vtype,String ledname,String startdate,String enddate) {

        sharedPreferences = context.getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        String  stringar = sharedPreferences.getString("Split_ledger_with_flatno",null );
        Type type = new TypeToken<List<sales_register_model>>(){}.getType();
        List<sales_register_model> Split_ledger_with_flatno = gson.fromJson(stringar, type);


        salespaylist = new ArrayList<sales_register_model>();
        dbnew = this.getReadableDatabase();
        String selectQuery = null;
        if (preference.equals("preference")&&amountsort.equals("amountsort")) {

            //for sale -sales account - innerpage
            if (vtype.equals("Bill_from_sales")) {

                selectQuery = "SELECT a." + KEY_ledger_name +
                        ",a." + KEY_party_name +
                        ",a." + KEY_voucher_type +
                        ",a." + KEY_date +
                        ",a." + KEY_amount +
                        ",a." + KEY_voucher_number +
                        ",a." + KEY_narration +
                        ",a." + KEY_cheque_no +
                        ",a." + KEY_cheque_date +
                        ",a." + KEY_bank_date +
                        ",b." + KEY_flat_no +
                        ",b." + KEY_parent_name_2 +
                        " FROM " +
                        TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + "a." + KEY_ledger_name + " = " + "'" + ledname + "'" + " AND "
                        + KEY_voucher_type + " = " + "'Bill'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
            //for sale -sundry debtors - wingwise-KEY_ledger_name = others
            else if (ledname.equals("Others")) {

                selectQuery = "SELECT a." + KEY_ledger_name +
                        ",a." + KEY_party_name +
                        ",a." + KEY_voucher_type +
                        ",a." + KEY_date +
                        ",a." + KEY_amount +
                        ",a." + KEY_voucher_number +
                        ",a." + KEY_narration +
                        ",a." + KEY_cheque_no +
                        ",a." + KEY_cheque_date +
                        ",a." + KEY_bank_date +
                        ",b." + KEY_flat_no +
                        ",b." + KEY_parent_name_2 +
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + KEY_voucher_type + " = " + "'Bill'"
                        + " AND " +
                        "b." + KEY_ledger_name + " IS NULL "
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
            //for sale -sundry debtors - wingwise-KEY_ledger_name
            else if (vtype.equals("Bill_sending_wingname")) {
                selectQuery = "SELECT a." + KEY_ledger_name +
                        ",a." + KEY_party_name +
                        ",a." + KEY_voucher_type +
                        ",a." + KEY_date +
                        ",a." + KEY_amount +
                        ",a." + KEY_voucher_number +
                        ",a." + KEY_narration +
                        ",a." + KEY_cheque_no +
                        ",a." + KEY_cheque_date +
                        ",a." + KEY_bank_date +
                        ",b." + KEY_flat_no +
                        ",b." + KEY_parent_name_2 +
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " + "'Bill'"
                        + " AND " +
                        "b." + KEY_parent_name_2 + " = " + "'" + ledname + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }

            //from receipt page
            else if (vtype.equals("Receipt")) {
              /*  selectQuery = "SELECT *" +
                        " FROM " + TABLE_DAYBOOK_DETAILS  +
                        " WHERE "
                        + KEY_voucher_type + " = " + "'" + vtype + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Debit'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " +  KEY_date + " ASC";*/
                selectQuery = "SELECT a." + KEY_ledger_name +
                        ",a." + KEY_party_name +
                        ",a." + KEY_voucher_type +
                        ",a." + KEY_date +
                        ",a." + KEY_amount +
                        ",a." + KEY_voucher_number +
                        ",a." + KEY_narration +
                        ",a." + KEY_cheque_no +
                        ",a." + KEY_cheque_date +
                        ",a." + KEY_bank_date +
                        ",b." + KEY_flat_no +
                        ",b." + KEY_parent_name_2 +
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " + "'" + vtype + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Debit'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + "a." + KEY_amount + " DESC";
/*
                + " ORDER BY " + "a." + KEY_amount + " DESC";*/
            }
            //from receipt page -> cash and bankacc
            else if (vtype.equals("Receipt_cashorbank")) {
                selectQuery = "SELECT a." + KEY_ledger_name +
                        ",a." + KEY_party_name +
                        ",a." + KEY_voucher_type +
                        ",a." + KEY_date +
                        ",a." + KEY_amount +
                        ",a." + KEY_voucher_number +
                        ",a." + KEY_narration +
                        ",a." + KEY_cheque_no +
                        ",a." + KEY_cheque_date +
                        ",a." + KEY_bank_date +
                        ",b." + KEY_flat_no +
                        ",b." + KEY_parent_name_2 +
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " + "'Receipt'"
                        + " AND "
                        + "a." + KEY_ledger_name + " = " + "'" + ledname + "'" + " AND "
                        + KEY_dr_cr + " = " + "'Debit'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + "a." + KEY_amount + " DESC";
            }
            //from Bill-sundry daptor main page
            else {
                selectQuery = "SELECT a." + KEY_ledger_name +
                        ",a." + KEY_party_name +
                        ",a." + KEY_voucher_type +
                        ",a." + KEY_date +
                        ",a." + KEY_amount +
                        ",a." + KEY_voucher_number +
                        ",a." + KEY_narration +
                        ",a." + KEY_cheque_no +
                        ",a." + KEY_cheque_date +
                        ",a." + KEY_bank_date +
                        ",b." + KEY_flat_no +
                        ",b." + KEY_parent_name_2 +
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " + "'" + vtype + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'" + " AND "
                        +
                        KEY_date + " >= '" + startdate + "'"
                        + " AND " + KEY_date + " <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
        }
        else if (preference.equals("greaterthan")) {

            //for sale -sales account - innerpage
            if(vtype.equals("Bill_from_sales")) {

                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " +
                        TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + "a." + KEY_ledger_name + " = " + "'" + ledname + "'"+ " AND "
                        + KEY_voucher_type + " = " +"'Bill'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
            //for sale -sundry debtors - wingwise-KEY_ledger_name = others
            else if(ledname.equals("Others"))
            {

                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + KEY_voucher_type + " = " +"'Bill'"
                        + " AND "+
                        "b."+ KEY_ledger_name + " IS NULL "
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
            //for sale -sundry debtors - wingwise-KEY_ledger_name
            else if(vtype.equals("Bill_sending_wingname"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " +"'Bill'"
                        + " AND "+
                        "b."+ KEY_parent_name_2 + " = " + "'"+ledname+"'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }

            //from receipt page
            else if(vtype.equals("Receipt"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " + "'" + vtype + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Debit'"   + " AND "
                        +
                        KEY_amount + " >= '" + amountsort + "'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + "a." +  KEY_amount + " DESC";
            }
            //from receipt page -> cash and bankacc
            else if(vtype.equals("Receipt_cashorbank"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " + "'Receipt'"
                        + " AND "
                        + "a."+ KEY_ledger_name + " = " + "'"+ledname+"'" + " AND "
                        + KEY_dr_cr + " = " + "'Debit'"
                        + " AND "
                        +
                        KEY_amount + " >= '" + amountsort + "'"+ " AND "+
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + "a." +  KEY_amount + " DESC";
            }
            //from Bill-sundry daptor main page
            else
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + KEY_voucher_type + " = " + "'" + vtype + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
        }
        else if (preference.equals("equalto")) {

            //for sale -sales account - innerpage
            if(vtype.equals("Bill_from_sales")) {

                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " +
                        TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + "a." + KEY_ledger_name + " = " + "'" + ledname + "'"+ " AND "
                        + KEY_voucher_type + " = " +"'Bill'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
            //for sale -sundry debtors - wingwise-KEY_ledger_name = others
            else if(ledname.equals("Others"))
            {

                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + KEY_voucher_type + " = " +"'Bill'"
                        + " AND "+
                        "b."+ KEY_ledger_name + " IS NULL "
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
            //for sale -sundry debtors - wingwise-KEY_ledger_name
            else if(vtype.equals("Bill_sending_wingname"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_party_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + KEY_voucher_type + " = " +"'Bill'"
                        + " AND "+
                        "b."+ KEY_parent_name_2 + " = " + "'"+ledname+"'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }

            //from receipt page
            else if(vtype.equals("Receipt"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + KEY_voucher_type + " = " + "'" + vtype + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Debit'"   + " AND "
                        +
                        KEY_amount + " = '" + amountsort + "'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + "a." +  KEY_amount + " DESC";
            }
            //from receipt page -> cash and bankacc
            else if(vtype.equals("Receipt_cashorbank"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + KEY_voucher_type + " = " + "'Receipt'"
                        + " AND "
                        + "a."+ KEY_ledger_name + " = " + "'"+ledname+"'" + " AND "
                        + KEY_dr_cr + " = " + "'Debit'"
                        + " AND "
                        +
                        KEY_amount + " = '" + amountsort + "'"+ " AND "+
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + "a." +  KEY_amount + " DESC";
            }
            //from Bill-sundry daptor main page
            else
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_party_name+
                        ",a."+KEY_voucher_type+
                        ",a."+KEY_date+
                        ",a."+KEY_amount+
                        ",a."+KEY_voucher_number+
                        ",a."+KEY_narration+
                        ",a."+KEY_cheque_no+
                        ",a."+KEY_cheque_date+
                        ",a."+KEY_bank_date+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        " FROM " + TABLE_DAYBOOK_DETAILS + " a INNER JOIN " +
                        TABLE_LEDGERS + " b ON " + "a." + KEY_party_name + "= b." + KEY_ledger_name
                        + " WHERE "
                        + KEY_voucher_type + " = " + "'" + vtype + "'"
                        + " AND "
                        + KEY_dr_cr + " = " + "'Credit'"+ " AND "
                        +
                        KEY_date +" >= '" + startdate + "'"
                        + " AND "+ KEY_date +" <= '" + enddate + "'"
                        + " ORDER BY " + KEY_voucher_number + " ASC";
            }
        }

        Cursor c = dbnew.rawQuery(selectQuery, null);
        Log.d("getsqlitequery ",selectQuery);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                if(("0").equals(c.getString(c.getColumnIndex(KEY_amount))))
                {

                }
                else
                {
                    if(vtype.equals("Receipt"))
                    {
                        sales_register_model td = new sales_register_model();

                        if(ledname.equals("sundryAdapters"))
                        {
                            if((("").equals(c.getString(c.getColumnIndex(KEY_flat_no))))||(("").equals(c.getString(c.getColumnIndex(KEY_parent_name_2)))))
                            {
                                                  /*  td.setFlatno("-");
                                                    String pn2 = "-";
                                                    td.setTest(pn2);*/
                            }
                            else
                            {

                                    if(c.getString(c.getColumnIndex(KEY_party_name)).contains("("))
                                    {
                                        String[] arrOfStr = c.getString(c.getColumnIndex(KEY_party_name)).split("\\(");
                                        td.setLed_name(arrOfStr[0]);
                                        String[] arrOfStr2 = null;
                                        if (arrOfStr[1].contains(")")) {
                                          arrOfStr2 = arrOfStr[1].split("\\)");
                                            td.setFlatno(arrOfStr2[0]);

                                        }

                                        for(sales_register_model todo:Split_ledger_with_flatno) {

                                            if (todo.getFlatno().equalsIgnoreCase(arrOfStr2[0])) {
                                                Toast.makeText(context, todo.getTest() + " " + todo.getLed_name().replaceAll("\\s+", "") + " " + todo.getFlatno() + " " + c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+", ""), Toast.LENGTH_LONG).show();
                                                Log.d("Viewsplit2: ", todo.getTest() + " " + todo.getLed_name().replaceAll("\\s+", "") + " " + todo.getFlatno() + " " + c.getString(c.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+", ""));
                                                //  String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                                                td.setTest(todo.getTest());
                                                break;

                                            }
                                        }



                                        td.setName((c.getString(c.getColumnIndex(KEY_party_name))));
                                        /*newparty.setVn((c2.getString(c2.getColumnIndex(KEY_voucher_number))));*/
                                        td.setType((c.getString(c.getColumnIndex(KEY_voucher_type))));
                                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                                        td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                                        td.setVn((c.getString(c.getColumnIndex(KEY_voucher_number))));
                                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                                        // adding to todo list
                                        salespaylist.add(td);


                                    }
                                            else
                                            {
                                                td.setFlatno((c.getString(c.getColumnIndex(KEY_flat_no))));
                                                String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                                                td.setTest(pn2);
                                                td.setName((c.getString(c.getColumnIndex(KEY_party_name))));
                                        /*newparty.setVn((c2.getString(c2.getColumnIndex(KEY_voucher_number))));*/
                                                td.setType((c.getString(c.getColumnIndex(KEY_voucher_type))));
                                                td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                                                td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                                                td.setVn((c.getString(c.getColumnIndex(KEY_voucher_number))));
                                                td.setLed_name((c.getString(c.getColumnIndex(KEY_ledger_name))));

                                                td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                                                td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                                                td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                                                td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                                                // adding to todo list
                                                salespaylist.add(td);

                                            }


                            }
                        }
                        else
                        {
                            if((("").equals(c.getString(c.getColumnIndex(KEY_flat_no))))||(("").equals(c.getString(c.getColumnIndex(KEY_parent_name_2)))))
                            {
                                                    td.setFlatno("-");
                                                    String pn2 = "-";
                                                    td.setTest(pn2);

                            }
                            else {
                                td.setFlatno((c.getString(c.getColumnIndex(KEY_flat_no))));
                                String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                                td.setTest(pn2);
                                 }
                            td.setName((c.getString(c.getColumnIndex(KEY_party_name))));
                                        /*newparty.setVn((c2.getString(c2.getColumnIndex(KEY_voucher_number))));*/
                            td.setType((c.getString(c.getColumnIndex(KEY_voucher_type))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                            td.setVn((c.getString(c.getColumnIndex(KEY_voucher_number))));
                            td.setLed_name((c.getString(c.getColumnIndex(KEY_ledger_name))));

                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            // adding to todo list
                            salespaylist.add(td);


                        }




                        //  getReceiptdetail2(c.getString(c.getColumnIndex(KEY_party_name)), c.getString(c.getColumnIndex(KEY_voucher_number)), vtype);

                    }
                    else
                    {
                        Log.d("getReceip: ",  c.getString(c.getColumnIndex(KEY_party_name)) + c.getString(c.getColumnIndex(KEY_ledger_name)));
                        sales_register_model td = new sales_register_model();
                    /*    if((("").equals(c.getString(c.getColumnIndex(KEY_flat_no))))||(("").equals(c.getString(c.getColumnIndex(KEY_parent_name_2)))))
                        {
                            td.setFlatno("-");
                            String pn2 = "-";
                            td.setTest(pn2);
                        }
                        else
                        {
                            td.setFlatno((c.getString(c.getColumnIndex(KEY_flat_no))));
                            String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                            td.setTest(pn2);
                        }*/

                    /*
                        td.setFlatno((c.getString(c.getColumnIndex(KEY_flat_no))));
                        String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                        td.setTest(pn2);
                        td.setName((c.getString(c.getColumnIndex(KEY_party_name))));
                *//*newparty.setVn((c2.getString(c2.getColumnIndex(KEY_voucher_number))));*//*
                        td.setType((c.getString(c.getColumnIndex(KEY_voucher_type))));
                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                        td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                        td.setVn((c.getString(c.getColumnIndex(KEY_voucher_number))));
                        td.setLed_name((c.getString(c.getColumnIndex(KEY_ledger_name))));

                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                        // adding to todo list
                        salespaylist.add(td);
                    */    //  getReceiptdetail2(c.getString(c.getColumnIndex(KEY_party_name)), c.getString(c.getColumnIndex(KEY_voucher_number)), vtype);

                        if(c.getString(c.getColumnIndex(KEY_party_name)).contains("("))
                        {
                            String[] arrOfStr = c.getString(c.getColumnIndex(KEY_party_name)).split("\\(");
                            td.setName(arrOfStr[0]);
                            String[] arrOfStr2 = null;
                            if (arrOfStr[1].contains(")")) {
                                arrOfStr2 = arrOfStr[1].split("\\)");
                                td.setFlatno(arrOfStr2[0]);

                            }

                            for(sales_register_model todo:Split_ledger_with_flatno) {

                                if (todo.getFlatno().equalsIgnoreCase(arrOfStr2[0])) {
                                    //  String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                                    td.setTest(todo.getTest());
                                    break;

                                }
                            }



                            td.setLed_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                                        /*newparty.setVn((c2.getString(c2.getColumnIndex(KEY_voucher_number))));*/
                            td.setType((c.getString(c.getColumnIndex(KEY_voucher_type))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                            td.setVn((c.getString(c.getColumnIndex(KEY_voucher_number))));
                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            // adding to todo list
                            salespaylist.add(td);


                        }
                        else
                        {
                            td.setFlatno((c.getString(c.getColumnIndex(KEY_flat_no))));
                            String pn2 = c.getString(c.getColumnIndex(KEY_parent_name_2));
                            td.setTest(pn2);
                            td.setName((c.getString(c.getColumnIndex(KEY_party_name))));
                                        /*newparty.setVn((c2.getString(c2.getColumnIndex(KEY_voucher_number))));*/
                            td.setType((c.getString(c.getColumnIndex(KEY_voucher_type))));
                            td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                            td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                            td.setVn((c.getString(c.getColumnIndex(KEY_voucher_number))));
                            td.setLed_name((c.getString(c.getColumnIndex(KEY_ledger_name))));

                            td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                            td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                            td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                            td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                            // adding to todo list
                            salespaylist.add(td);

                        }

                    }
                }
            } while (c.moveToNext());
        }

        return salespaylist;
    }


    //used in PaymentDetail page
    public void getTotalAmount(String vtype) {
        amountplusdb = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        if(vtype.equals("Receipt"))
        {
            selectQuery = "SELECT SUM(" + KEY_amount + ") as Total FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_voucher_type + " = " + "'"+vtype+"'"
                    + " AND "
                    + KEY_dr_cr + " = " + "'Debit'"
                    + " ORDER BY " + KEY_voucher_number + " ASC";
        }
        else
        {
            selectQuery = "SELECT SUM(" + KEY_amount + ") as Total FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_voucher_type + " = " + "'"+vtype+"'"
                    + " GROUP BY " + KEY_ledger_name
                    + " ORDER BY " + KEY_voucher_number + " ASC";
        }


        Log.e("queryresult", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to
        if (c.moveToFirst()) {
            do {
                amountplusdb = amountplusdb+  c.getInt(c.getColumnIndex("Total"));

            } while (c.moveToNext());
        }
        amount_payment = String.valueOf(amountplusdb);
    }


    //used in CASH BANK openinf balance page
    public Integer getCashBank_OB(String vtype) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;


            selectQuery = "SELECT  * FROM " + TABLE_OC_TRIAL_BALANCE_ACCOUNTS
                    + " WHERE "
                    + KEY_account_name + " = " + "'" + vtype + "'" + " AND "
                    + KEY_year + " = " + 2018;


            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to
            if (c.moveToFirst()) {
                do {

                    cashbook_ob_amount = (c.getInt(c.getColumnIndex(KEY_opening_amount)));
                    Log.d("onCreate:amountdbwala ", String.valueOf(cashbook_ob_amount));
                } while (c.moveToNext());
            }
            c.close();

        //amount_payment = String.valueOf(amountplusdb);
        return cashbook_ob_amount;
    }

    //used in CASH BANK
    public List<partydetailmodel> getCashBank_Balance(String preference,String amountsort,String cashbook,String startdate,String enddate) {
        openingbalanceA = new ArrayList<partydetailmodel>();
        openingbalanceA.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        String  selectQuery;
/*
        //from cash bank balance
        if("Cash".equals(cashbook)) {*/
            selectQuery = "SELECT * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_ledger_name + " like '%"+cashbook+"%'"+ " AND "
                    +
                    KEY_date +" >= '" + startdate + "'"
                    + " AND "+ KEY_date +" <= '" + enddate + "'"
                    + " ORDER BY " + KEY_date + " ASC";

/*
            KEY_date +"BETWEEN "+"'" + startdate + "'"+ "AND"+"'" + enddate + "'"
*/

            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    partydetailmodel td = new partydetailmodel();
                    td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                    td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                    td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                    td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                    td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                    td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                    td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                    // adding to todo list
                    openingbalanceA.add(td);
                } while (c.moveToNext());
            }


            Log.d("wisesummarydata: ","outside");
            if (preference.equals("equalto")) {
                List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

                Log.d("wisesummarydata: ","inside");
                for (partydetailmodel todo : openingbalanceA) {
                    int amt_userinput = Integer.parseInt(amountsort);

                    Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmt_for_receipt());
                    if(amt_userinput==todo.getAmt_for_receipt()) {
                        partydetailmodel td2 = new partydetailmodel();
                        Log.d("wisesummarydata: ","insode "+todo.getAmt_for_receipt()+" "+todo.getVoucher_type());
                        td2.setPartyname(todo.getPartyname());
                        td2.setLedger_name(todo.getLedger_name());
                        td2.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td2.setDate(todo.getDate());
                        td2.setVoucher_number(todo.getVoucher_number());
                        td2.setVoucher_type(todo.getVoucher_type());
                        td2.setDr_cr(todo.getDr_cr());
                        td2.setNarration(todo.getNarration());
                        td2.setCqno(todo.getCqno());
                        td2.setCqdate(todo.getCqdate());
                        td2.setBankdate(todo.getBankdate());
                        for_pref_list.add(td2);
                    }
                }
                openingbalanceA.clear();
                openingbalanceA = new ArrayList<partydetailmodel>(for_pref_list);
          /*      openingbalanceA.addAll(for_pref_list);*/
            } else if (preference.equals("greaterthan")) {
                List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

                Log.d("wisesummarydata: ","inside");
                for (partydetailmodel todo : openingbalanceA) {

                    partydetailmodel td2 = new partydetailmodel();
                    Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmt_for_receipt());
                    int amt_userinput = Integer.parseInt(amountsort);
                    int amt_fromtable = todo.getAmt_for_receipt();
                    if(amt_userinput <= amt_fromtable) {
                        Log.d("wisesummarydata: ","insode "+todo.getAmt_for_receipt()+" "+todo.getVoucher_type());
                        td2.setPartyname(todo.getPartyname());
                        td2.setLedger_name(todo.getLedger_name());
                        td2.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td2.setDate(todo.getDate());
                        td2.setVoucher_number(todo.getVoucher_number());
                        td2.setVoucher_type(todo.getVoucher_type());
                        td2.setDr_cr(todo.getDr_cr());
                        td2.setNarration(todo.getNarration());
                        td2.setCqno(todo.getCqno());
                        td2.setCqdate(todo.getCqdate());
                        td2.setBankdate(todo.getBankdate());
                        for_pref_list.add(td2);
                    }
                }
                openingbalanceA.clear();
                openingbalanceA.addAll(for_pref_list);
            }




       /* }
        //bank accounts
        else*/
        /*{
            //first getting account name(ledgername) from below query
            selectQuery = "SELECT  * FROM " + TABLE_LEDGERS
                    + " WHERE "
                    + KEY_parent_name_2 + " = " + "'" + cashbook + "'" ;

            Cursor c2 = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to
            if (c2.moveToFirst()) {
                do {

                    ledgername = (c2.getString(c2.getColumnIndex(KEY_ledger_name)));
                    Log.d("onCreate:dbledgername", String.valueOf(ledgername));
                } while (c2.moveToNext());
            }
            c2.close();

            //after getting account name(ledgername) we get all details from daybook details of particular bank name from below query
            selectQuery = "SELECT * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_ledger_name + " like '%"+ledgername+"%'"
                    + " AND "
                    +
                    KEY_date +" >= '" + startdate + "'"
                    + " AND "+ KEY_date +" <= '" + enddate + "'"
                    + " ORDER BY " + KEY_date + " ASC";

            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    partydetailmodel td = new partydetailmodel();
                    td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                    td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                    td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                    td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                     td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                    td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                    td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                    td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));
                    // adding to todo list
                    openingbalanceA.add(td);
                } while (c.moveToNext());
            }

            c.close();

            Log.d("wisesummarydata: ","outside");
            if (preference.equals("equalto")) {
                List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();


                Log.d("wisesummarydata: ","inside");
                for (partydetailmodel todo : openingbalanceA) {
                    int amt_userinput = Integer.parseInt(amountsort);

                    partydetailmodel td2 = new partydetailmodel();
                    Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmt_for_receipt());
                    if(amt_userinput==todo.getAmt_for_receipt()) {
                        Log.d("wisesummarydata: ","insode "+todo.getAmt_for_receipt()+" "+todo.getVoucher_type());
                        td2.setPartyname(todo.getPartyname());
                        td2.setLedger_name(todo.getLedger_name());
                        td2.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td2.setDate(todo.getDate());
                        td2.setVoucher_number(todo.getVoucher_number());
                        td2.setVoucher_type(todo.getVoucher_type());
                        td2.setDr_cr(todo.getDr_cr());
                        td2.setNarration(todo.getNarration());
                        td2.setCqno(todo.getCqno());
                        td2.setCqdate(todo.getCqdate());
                        td2.setBankdate(todo.getBankdate());
                        for_pref_list.add(td2);
                    }
                }
                openingbalanceA.clear();
                openingbalanceA.addAll(for_pref_list);
            } else if (preference.equals("greaterthan")) {
                List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

                Log.d("wisesummarydata: ","inside");
                for (partydetailmodel todo : openingbalanceA) {

                    partydetailmodel td2 = new partydetailmodel();
                    Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmt_for_receipt());
                    int amt_userinput = Integer.parseInt(amountsort);
                    int amt_fromtable = todo.getAmt_for_receipt();
                    if(amt_userinput <= amt_fromtable) {
                        Log.d("wisesummarydata: ","insode "+todo.getAmt_for_receipt()+" "+todo.getVoucher_type());
                        td2.setPartyname(todo.getPartyname());
                        td2.setLedger_name(todo.getLedger_name());
                        td2.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td2.setDate(todo.getDate());
                        td2.setVoucher_number(todo.getVoucher_number());
                        td2.setVoucher_type(todo.getVoucher_type());
                        td2.setDr_cr(todo.getDr_cr());
                        td2.setNarration(todo.getNarration());
                        td2.setCqno(todo.getCqno());
                        td2.setCqdate(todo.getCqdate());
                        td2.setBankdate(todo.getBankdate());
                        for_pref_list.add(td2);
                    }
                }
                openingbalanceA.clear();
                openingbalanceA.addAll(for_pref_list);
            }

        }*/
        return openingbalanceA;
    }

    //used in CASH BANK check date
    public List<partydetailmodel> CheckDate_CashBank(String preference,String amountsort,String cashbook,String startdate)
    {
        openingbalanceA = new ArrayList<partydetailmodel>();
        openingbalanceA.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        String  selectQuery;

        //from cash bank balance

            selectQuery = "SELECT * FROM " + TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_ledger_name + " like '%"+cashbook+"%'"+ " AND "
                    +
                    KEY_date +" < '" + startdate + "'"
                    + " ORDER BY " + KEY_date + " ASC";

/*
            KEY_date +"BETWEEN "+"'" + startdate + "'"+ "AND"+"'" + enddate + "'"
*/

            Cursor c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    partydetailmodel td = new partydetailmodel();
                    td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                    td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                    td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                    td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                    td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                    td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                    td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                    td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                    td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                    td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                    td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                    // adding to todo list
                    openingbalanceA.add(td);
                } while (c.moveToNext());
            }



        /*    Log.d("wisesummarydata: ","outside");
            if (preference.equals("equalto")) {
                List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

                Log.d("wisesummarydata: ","inside");
                for (partydetailmodel todo : openingbalanceA) {
                    int amt_userinput = Integer.parseInt(amountsort);

                    partydetailmodel td = new partydetailmodel();
                    Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmt_for_receipt());
                    if(amt_userinput==todo.getAmt_for_receipt()) {
                        Log.d("wisesummarydata: ","insode "+todo.getAmt_for_receipt()+" "+todo.getVoucher_type());
                        td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                        td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                        td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                        td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                        td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                        for_pref_list.add(td);
                    }
                }
                openingbalanceA.clear();
                openingbalanceA.addAll(for_pref_list);
            } else if (preference.equals("greaterthan")) {
                List<partydetailmodel> for_pref_list  = new ArrayList<partydetailmodel>();

                Log.d("wisesummarydata: ","inside");
                for (partydetailmodel todo : openingbalanceA) {

                    partydetailmodel td = new partydetailmodel();
                    Log.d("wisesummarydata: ","insode"+amountsort+" "+todo.getAmt_for_receipt());
                    int amt_userinput = Integer.parseInt(amountsort);
                    int amt_fromtable = todo.getAmt_for_receipt();
                    if(amt_userinput <= amt_fromtable) {
                        Log.d("wisesummarydata: ","insode "+todo.getAmt_for_receipt()+" "+todo.getVoucher_type());
                        td.setAmt_for_receipt((c.getInt(c.getColumnIndex(KEY_amount))));
                        td.setDr_cr((c.getString(c.getColumnIndex(KEY_dr_cr))));
                        td.setDate((c.getString(c.getColumnIndex(KEY_date))));
                        td.setPartyname((c.getString(c.getColumnIndex(KEY_party_name))));
                        td.setLedger_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                        td.setVoucher_number(c.getString(c.getColumnIndex(KEY_voucher_number)));
                        td.setVoucher_type(c.getString(c.getColumnIndex(KEY_voucher_type)));
                        td.setNarration(c.getString(c.getColumnIndex(KEY_narration)));
                        td.setCqno(c.getString(c.getColumnIndex(KEY_cheque_no)));
                        td.setCqdate(c.getString(c.getColumnIndex(KEY_cheque_date)));
                        td.setBankdate(c.getString(c.getColumnIndex(KEY_bank_date)));

                        for_pref_list.add(td);
                    }
                }
                openingbalanceA.clear();
                openingbalanceA.addAll(for_pref_list);
            }*/


        return openingbalanceA;
    }

    //OR
    public List<sales_register_model> getOutstanding_Rec_data(String preference,String amountsort,String Receivable_cashorbank,String year,String wing) {
        salespaylist = new ArrayList<sales_register_model>();
        SQLiteDatabase db = this.getReadableDatabase();

      /*  String selectQuery = "SELECT  * FROM " + TABLE_OC_TRIAL_BALANCE_LEDGER  + " WHERE "
                + KEY_year + " = " + 2018;
*/
        String  selectQuery = null;
        if (preference.equals("preference")&&amountsort.equals("amountsort")) {
            if(wing.equals("DisplayPageRece"))
            {

                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_opening_amount+
                        ",a."+KEY_closing_amount+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        ",b."+KEY_is_party+
                        " FROM " +
                        TABLE_OC_TRIAL_BALANCE_LEDGER + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_ledger_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + "b." + KEY_is_party + " = "+"'1'"+ " AND "
                        + KEY_year + " = " + year
                        + " ORDER BY " + "b." +  KEY_flat_no + " ASC";
            }
            else if(Receivable_cashorbank.equals("Receivable_cashorbank")){
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_opening_amount+
                        ",a."+KEY_closing_amount+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        ",b."+KEY_is_party+
                        ",c."+KEY_voucher_number+
                        " FROM " +
                        TABLE_LEDGERS +
                        " b INNER JOIN " +
                        TABLE_OC_TRIAL_BALANCE_LEDGER +
                        " a ON " + "a." + KEY_ledger_name + "= b." + KEY_ledger_name+
                        " INNER JOIN "+ TABLE_DAYBOOK_DETAILS +
                        " c ON " + "c." + KEY_party_name + "= a." + KEY_ledger_name
                        + " WHERE "
                        + "b." + KEY_is_party + " = "+"'1'"+ " AND "
                        + "a." + KEY_year + " = " + year+ " AND "
                        + "c." + KEY_ledger_name + " = '" + wing + "'"
                        + " ORDER BY " + "a." +  KEY_closing_amount + " ASC";
            }
            else
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_opening_amount+
                        ",a."+KEY_closing_amount+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_parent_name_2+
                        ",b."+KEY_is_party+
                        " FROM " +
                        TABLE_OC_TRIAL_BALANCE_LEDGER + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_ledger_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + "b." + KEY_is_party + " = "+"'1'"+ " AND "
                        + KEY_year + " = " + year+ " AND "
                        + "b." + KEY_parent_name_2 + " = '" + wing + "'"
                        + " ORDER BY " + "a." +  KEY_closing_amount + " ASC";
            }

        }
        else if (preference.equals("greaterthan")) {
            if(wing.equals("DisplayPageRece"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_opening_amount+
                        ",a."+KEY_closing_amount+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_is_party+
                        ",b."+KEY_parent_name_2+
                        " FROM " +
                        TABLE_OC_TRIAL_BALANCE_LEDGER + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_ledger_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + "b." + KEY_is_party + " = "+"'1'"+ " AND "
                        +
                        KEY_closing_amount + " <= '" + amountsort + "'"+ " AND "
                        + KEY_year + " = " + year
                        + " ORDER BY " + "a." +  KEY_closing_amount + " ASC";
            }
            else
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_opening_amount+
                        ",a."+KEY_closing_amount+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_is_party+
                        ",b."+KEY_parent_name_2+
                        " FROM " +
                        TABLE_OC_TRIAL_BALANCE_LEDGER + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_ledger_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + "b." + KEY_is_party + " = "+"'1'"+ " AND "
                        +
                        KEY_closing_amount + " <= '" + amountsort + "'"+ " AND "
                        + KEY_year + " = " + year+ " AND "
                        + "b." + KEY_parent_name_2 + " = '" + wing + "'"
                        + " ORDER BY " + "a." +  KEY_closing_amount + " ASC";
            }

        }
        else if (preference.equals("equalto")) {
            if(wing.equals("DisplayPageRece"))
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_opening_amount+
                        ",a."+KEY_closing_amount+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_is_party+
                        ",b."+KEY_parent_name_2+
                        " FROM " +
                        TABLE_OC_TRIAL_BALANCE_LEDGER + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_ledger_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + "b." + KEY_is_party + " = "+"'1'"+ " AND "
                        +
                        KEY_closing_amount + " = '" + amountsort + "'"+ " AND "
                        + KEY_year + " = " + year
                        + " ORDER BY " + "a." +  KEY_closing_amount + " ASC";
            }
            else
            {
                selectQuery = "SELECT a."+KEY_ledger_name+
                        ",a."+KEY_opening_amount+
                        ",a."+KEY_closing_amount+
                        ",b."+KEY_flat_no+
                        ",b."+KEY_is_party+
                        ",b."+KEY_parent_name_2+
                        " FROM " +
                        TABLE_OC_TRIAL_BALANCE_LEDGER + " a LEFT OUTER JOIN " +
                        TABLE_LEDGERS + " b ON " + " REPLACE( a." + KEY_ledger_name + ", ' ', '')= REPLACE( b." + KEY_ledger_name
                        + " , ' ', '') WHERE "
                        + "b." + KEY_is_party + " = "+"'1'"+ " AND "
                        +
                        KEY_closing_amount + " = '" + amountsort + "'"+ " AND "
                        + KEY_year + " = " + year+ " AND "
                        + "b." + KEY_parent_name_2 + " = '" + wing + "'"
                        + " ORDER BY " + "a." +  KEY_closing_amount + " ASC";
            }

        }
        
          
        Cursor c = db.rawQuery(selectQuery, null);
        Log.e("queryresult", selectQuery + c);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                if(("0".equals(c.getString(c.getColumnIndex(KEY_opening_amount))))&&("0".equals(c.getString(c.getColumnIndex(KEY_closing_amount)))))
                {

                }
                else
                {
                    if(("0").equals(c.getString(c.getColumnIndex(KEY_closing_amount))))
                    {

                    }
                    else {
                        //for getting voucher in Receivable_cashorbank
                          if(Receivable_cashorbank.equals("Receivable_cashorbank")){
                              sales_register_model td = new sales_register_model();
                              int amt = (-(c.getInt(c.getColumnIndex(KEY_closing_amount))));
                              td.setAmt_for_receipt(amt);
                              td.setAmount(String.valueOf(amt));
                              td.setLed_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                              td.setTest(c.getString(c.getColumnIndex(KEY_parent_name_2)));
                              td.setFlatno(c.getString(c.getColumnIndex(KEY_flat_no)));
                              td.setVn(c.getString(c.getColumnIndex(KEY_voucher_number)));
                              td.setIsparty("1");
                              // adding to todo list
                              salespaylist.add(td);

                          }
                          else {
                              sales_register_model td = new sales_register_model();
                              int amt = (-(c.getInt(c.getColumnIndex(KEY_closing_amount))));
                              td.setAmt_for_receipt(amt);
                              td.setAmount(String.valueOf(amt));
                              td.setLed_name((c.getString(c.getColumnIndex(KEY_ledger_name))));
                              td.setTest(c.getString(c.getColumnIndex(KEY_parent_name_2)));
                              td.setFlatno(c.getString(c.getColumnIndex(KEY_flat_no)));
                              td.setIsparty("1");
                              // adding to todo list
                              salespaylist.add(td);

                          }
                       }
                }

            } while (c.moveToNext());
             /*  for all group*/



            }

            c.close();

        sharedPreferences = context.getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        String  stringar = sharedPreferences.getString("Split_ledger_with_flatno",null );
        Type type = new TypeToken<List<sales_register_model>>(){}.getType();
        List<sales_register_model> Split_ledger_with_flatno = gson.fromJson(stringar, type);


        for (sales_register_model todo : Split_ledger_with_flatno) {

            String selectQuerylast=null;

              selectQuerylast = "SELECT a." + KEY_ledger_name +
                      ",a." + KEY_opening_amount +
                      ",a." + KEY_closing_amount +
                      ",b." + KEY_flat_no +
                      ",b." + KEY_parent_name_2 +
                      ",c." + KEY_voucher_number +
                      " FROM " +
                      TABLE_OC_TRIAL_BALANCE_LEDGER +
                      " a INNER JOIN " +
                      TABLE_LEDGERS +
                      " b ON " + " REPLACE( a." + KEY_ledger_name + ", ' ', '')= REPLACE(  " + "'"+todo.getFlatno()+"' , ' ', '')" +
                      " INNER JOIN " + TABLE_DAYBOOK_DETAILS +
                      " c ON " + " REPLACE( b." + KEY_ledger_name + ", ' ', '')= REPLACE(   " + "'"+todo.getLed_name()+"' , ' ', '')" +
                      " WHERE "
                      + "a." +  KEY_year + " = " + year;



            Cursor c3 = db.rawQuery(selectQuerylast, null);
            Log.e("queryresult", selectQuerylast );

            // looping through all rows and adding to list
            if (c3.moveToFirst()) {
                do {

                  if(wing.equals("DisplayPageRece")) {

                        if(("0".equals(c3.getString(c3.getColumnIndex(KEY_opening_amount))))&&("0".equals(c3.getString(c3.getColumnIndex(KEY_closing_amount)))))
                        {

                        }
                        else
                        {
                            if(("0").equals(c3.getString(c3.getColumnIndex(KEY_closing_amount))))
                            {

                            }
                            else {
                                //for getting voucher in Receivable_cashorbank
                                if(Receivable_cashorbank.equals("Receivable_cashorbank")){
                                    sales_register_model td = new sales_register_model();
                                    int amt = (-(c3.getInt(c3.getColumnIndex(KEY_closing_amount))));
                                    td.setAmt_for_receipt(amt);
                                    td.setAmount(String.valueOf(amt));
                                    td.setLed_name(todo.getLed_name());
                                    td.setTest(todo.getTest());
                                    td.setFlatno(todo.getFlatno());
                                    td.setVn(c3.getString(c3.getColumnIndex(KEY_voucher_number)));
                                    td.setIsparty("1");
                                    // adding to todo list
                                    salespaylist.add(td);
                                    break;

                                }
                                else {
                                    sales_register_model td = new sales_register_model();
                                    int amt = (-(c3.getInt(c3.getColumnIndex(KEY_closing_amount))));
                                    td.setAmt_for_receipt(amt);
                                    td.setAmount(String.valueOf(amt));
                                    td.setLed_name(todo.getLed_name());
                                    td.setTest(todo.getTest());
                                    td.setFlatno(todo.getFlatno());
                                    td.setIsparty("1");
                                    // adding to todo list
                                    salespaylist.add(td);
                                    break;

                                }
                            }
                        }
                    }
                    else{
                        if(todo.getTest().equals(wing))
                        {
                            if(("0".equals(c3.getString(c3.getColumnIndex(KEY_opening_amount))))&&("0".equals(c3.getString(c3.getColumnIndex(KEY_closing_amount)))))
                            {

                            }
                            else
                            {
                                if(("0").equals(c3.getString(c3.getColumnIndex(KEY_closing_amount))))
                                {

                                }
                                else {
                                    //for getting voucher in Receivable_cashorbank
                                    if(Receivable_cashorbank.equals("Receivable_cashorbank")){
                                        sales_register_model td = new sales_register_model();
                                        int amt = (-(c3.getInt(c3.getColumnIndex(KEY_closing_amount))));
                                        td.setAmt_for_receipt(amt);
                                        td.setAmount(String.valueOf(amt));
                                        td.setLed_name(todo.getLed_name());
                                        td.setIsparty("1");
                                        td.setTest(todo.getTest());
                                        td.setFlatno(todo.getFlatno());
                                        td.setVn(c3.getString(c3.getColumnIndex(KEY_voucher_number)));
                                        // adding to todo list
                                        salespaylist.add(td);
                                        break;

                                    }
                                    else {
                                        sales_register_model td = new sales_register_model();
                                        int amt = (-(c3.getInt(c3.getColumnIndex(KEY_closing_amount))));
                                        td.setAmt_for_receipt(amt);
                                        td.setAmount(String.valueOf(amt));
                                        td.setLed_name(todo.getLed_name());
                                        td.setTest(todo.getTest());
                                        td.setFlatno(todo.getFlatno());
                                        td.setIsparty("1");

                                        // adding to todo list
                                        salespaylist.add(td);
                                        break;

                                    }
                                }
                            }
                        }

                    }

                 /*   Log.e("getRec_datenter: ", todo.getFlatno()+" "+todo.getLed_name() + " "+c3.getString(c3.getColumnIndex(KEY_ledger_name)));
*/


                      /*  if(c3.getString(c2.getColumnIndex(KEY_party_name)).contains("("))
                        {
                            String[] arrOfStr = c2.getString(c2.getColumnIndex(KEY_party_name)).split("\\(");
                      *//*  sales_register_model.setName(arrOfStr[0]);
                        partynameusedfor_partysummary = arrOfStr[0];*//*
                            if (arrOfStr[1].contains(")")) {
                                String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                arrayList_flatnosort.add(arrOfStr2[0]);
                                Log.d("getRec_data: ", c2.getString(c2.getColumnIndex(KEY_party_name)) +"  " +arrOfStr2[0]);
                            }

                        }*/



                } while (c3.moveToNext());
            }
/*            selectQuery = "SELECT *"+
                    " FROM " +
    for C3               TABLE_OC_TRIAL_BALANCE_LEDGER
                    + " WHERE "
                    + KEY_year + " = " + year*/
                   ;/*
            +  KEY_closing_amount + " != "+"'0'"+ " AND "
                    +  KEY_opening_amount + " != "+"'0'"+ " AND "*/
        /*    Cursor c3 = db.rawQuery(selectQuery, null);
            Log.e("queryresult", selectQuery );

            // looping through all rows and adding to list
            if (c3.moveToFirst()) {
                do {
                    Log.d("getRec_datasecodn: ", arrayList_flatnosort.size()+ " "+c3.getString(c3.getColumnIndex(KEY_ledger_name)));

                    for (String todo : arrayList_flatnosort)
                    {
                        Log.d("getRec_datasecodn: ", todo + " "+c3.getString(c3.getColumnIndex(KEY_ledger_name)));

                        if(todo.equals(c3.getString(c3.getColumnIndex(KEY_ledger_name))))
                        {
                            Log.d("getRec_datenter: ", todo + " "+c3.getString(c3.getColumnIndex(KEY_ledger_name)));

                            if(("0".equals(c3.getString(c3.getColumnIndex(KEY_opening_amount))))&&("0".equals(c3.getString(c3.getColumnIndex(KEY_closing_amount)))))
                            {

                            }
                            else
                            {
                                if(("0").equals(c3.getString(c3.getColumnIndex(KEY_closing_amount))))
                                {

                                }
                                else {
                                    //for getting voucher in Receivable_cashorbank
                                    if(Receivable_cashorbank.equals("Receivable_cashorbank")){
                                        sales_register_model td = new sales_register_model();
                                        int amt = (-(c3.getInt(c3.getColumnIndex(KEY_closing_amount))));
                                        td.setAmt_for_receipt(amt);
                                        td.setAmount(String.valueOf(amt));
                                        td.setLed_name((c3.getString(c3.getColumnIndex(KEY_ledger_name))));
                                        td.setTest(todo);
                                        td.setFlatno(todo);
                                        td.setVn(c3.getString(c3.getColumnIndex(KEY_voucher_number)));
                                        // adding to todo list
                                        salespaylist.add(td);

                                    }
                                    else {
                                        sales_register_model td = new sales_register_model();
                                        int amt = (-(c3.getInt(c3.getColumnIndex(KEY_closing_amount))));
                                        td.setAmt_for_receipt(amt);
                                        td.setAmount(String.valueOf(amt));
                                        td.setLed_name((c3.getString(c3.getColumnIndex(KEY_ledger_name))));
                                        td.setTest(todo);
                                        td.setFlatno(todo);
                                        // adding to todo list
                                        salespaylist.add(td);

                                    }
                                }
                            }
                        }
                      *//*  if(c3.getString(c2.getColumnIndex(KEY_party_name)).contains("("))
                        {
                            String[] arrOfStr = c2.getString(c2.getColumnIndex(KEY_party_name)).split("\\(");
                      *//**//*  sales_register_model.setName(arrOfStr[0]);
                        partynameusedfor_partysummary = arrOfStr[0];*//**//*
                            if (arrOfStr[1].contains(")")) {
                                String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                arrayList_flatnosort.add(arrOfStr2[0]);
                                Log.d("getRec_data: ", c2.getString(c2.getColumnIndex(KEY_party_name)) +"  " +arrOfStr2[0]);
                            }

                        }*//*
                    }


                } while (c3.moveToNext());
            }*/
            c3.close();
        }

        return salespaylist;
    }

    //split legder name and flatno
    public List<sales_register_model> getSplit_ledger_with_flatno() {
        salespaylist = new ArrayList<sales_register_model>();
        SQLiteDatabase db = this.getReadableDatabase();

       String selectQuery = "SELECT "+KEY_party_name+
               " FROM " +
               TABLE_DAYBOOK_DETAILS
               + " GROUP BY " +  KEY_party_name ;
        Cursor c2 = db.rawQuery(selectQuery, null);
        Log.e("queryresult", selectQuery + c2);

        // looping through all rows and adding to list
        if (c2.moveToFirst()) {
            do {
                if(c2.getString(c2.getColumnIndex(KEY_party_name)).contains("("))
                {
                    sales_register_model sales_register_model = new sales_register_model();
                    sales_register_model.setName(c2.getString(c2.getColumnIndex(KEY_party_name)));

                    String[] arrOfStr = c2.getString(c2.getColumnIndex(KEY_party_name)).split("\\(");
                    String[] arrOfStr2= null;
                    sales_register_model.setLed_name(arrOfStr[0]);
                    if (arrOfStr[1].contains(")")) {
                       arrOfStr2 = arrOfStr[1].split("\\)");
                        sales_register_model.setFlatno(arrOfStr2[0]);
                        Log.d("getRec_data: ", c2.getString(c2.getColumnIndex(KEY_party_name)) +"  " +arrOfStr2[0]);
                    }

                    String wing =arrOfStr2[0].replaceAll("\\s+","");
                    char first = wing.charAt(0);

                    switch (first) {
                        case 'A':
                            wing = "A Wing";
                            break;
                        case 'B':
                            wing = "B Wing";
                            break;
                        case 'C':
                            wing = "C Wing";
                            break;
                        default:
                              String selectQuery2 = "SELECT *"+
                            " FROM " +
                            TABLE_LEDGERS ;
                    Cursor c3 = db.rawQuery(selectQuery2, null);
                    if (c3.moveToFirst()) {
                        do {
                            if((arrOfStr[0].replaceAll("\\s+","").equalsIgnoreCase(c3.getString(c3.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+",""))))
                            {
                                wing = c3.getString(c3.getColumnIndex(KEY_parent_name_2));

                            }

                        } while (c3.moveToNext());
                    }
                    c3.close();

                            break;
                    }
                    sales_register_model.setTest(wing);

                   /* String selectQuery2 = "SELECT *"+
                            " FROM " +
                            TABLE_LEDGERS ;
                    Cursor c3 = db.rawQuery(selectQuery2, null);
                    if (c3.moveToFirst()) {
                        do {
                            if((arrOfStr[0].replaceAll("\\s+","").equalsIgnoreCase(c3.getString(c3.getColumnIndex(KEY_ledger_name)).replaceAll("\\s+",""))))
                            {
                                String pn2 = c3.getString(c3.getColumnIndex(KEY_parent_name_2));
                                sales_register_model.setTest(pn2);
                            }

                        } while (c3.moveToNext());
                    }
                    c3.close();
                     */ /*  sales_register_model.setName(arrOfStr[0]);
                        partynameusedfor_partysummary = arrOfStr[0];*/


                    salespaylist.add(sales_register_model);
                }

            } while (c2.moveToNext());
        }
        Log.d("getRec_data: ", String.valueOf(salespaylist.size()));



        return salespaylist;

    }
    public void array_ini()
    {
        openingbalanceA = new ArrayList<partydetailmodel>();
    }

    public ArrayList<String> getPartyledger_ReceiptDebit(String ledgername,String vtype,String vnum) {
        partyname2club = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

      /*  String selectQuery = "SELECT  * FROM " + TABLE_OC_TRIAL_BALANCE_LEDGER  + " WHERE "
                + KEY_year + " = " + 2018;
*/
        String selectQuery;
        if(vtype.equals("Receipt"))
        {
            selectQuery = "SELECT * FROM " +
                    TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_party_name + " = "+"'"+ledgername+"'"+ " AND "
                    + KEY_voucher_number + " = "+"'"+vnum+"'"+ " AND "
                    + KEY_voucher_type + " = "+"'"+vtype+"'"+ " AND "
                    + KEY_dr_cr + " = "+"'Debit'";
        }
        else if(vtype.equals("Paymentincash"))
        {
            selectQuery = "SELECT * FROM " +
                    TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_date + " = "+"'"+ledgername+"'"+ " AND "
                    + KEY_amount + " = "+"'"+vnum+"'"+ " AND "
                    + KEY_voucher_type + " = "+"'Payment'"+ " AND "
                    + KEY_dr_cr + " = "+"'Credit'";
        }
        else
        {/*
          + " AND "
                  + KEY_amount + " = "+"'"+vnum+"'"*/

            selectQuery = "SELECT * FROM " +
                    TABLE_DAYBOOK_DETAILS
                    + " WHERE "
                    + KEY_date + " = "+"'"+ledgername+"'"+ " AND "
                    + KEY_voucher_type + " = "+"'Payment'"+ " AND "
                    + KEY_narration + " LIKE "+"'%"+vtype+"%'"+ " AND "
                    + KEY_dr_cr + " = "+"'Credit'";
        }


        Cursor c = db.rawQuery(selectQuery, null);
        Log.e("queryresultreturn", selectQuery + c);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                partyname2club.add(c.getString(c.getColumnIndex(KEY_narration)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_cheque_no)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_cheque_date)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_bank_date)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_ledger_name)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_party_name)));
                partyname2club.add(c.getString(c.getColumnIndex(KEY_amount)));

            } while (c.moveToNext());
             /*  for all group*/
        }

        return partyname2club;
    }
    public List<partydetailmodel> getBankName(String cashbook){
        trailbalance = new ArrayList<partydetailmodel>();
        SQLiteDatabase db = this.getReadableDatabase();
        //first getting account name(ledgername) from below query
      String  selectQuery = "SELECT  * FROM " + TABLE_LEDGERS
                + " WHERE "
                + KEY_parent_name_2 + " = " + "'" + cashbook + "'" ;

        Cursor c2 = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to
        if (c2.moveToFirst()) {
            do {

                partydetailmodel sales_register_model = new partydetailmodel();
                sales_register_model.setLedger_name(c2.getString(c2.getColumnIndex(KEY_ledger_name)));
                trailbalance.add(sales_register_model);
                Log.d("onCreate:dbledgername", String.valueOf(ledgername));
            } while (c2.moveToNext());
        }
        c2.close();
        return trailbalance;
    }

    /**
     * Getting user data from database
     * */
 /*   public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
       String selectQuery = "SELECT  * FROM " + TABLE_LEDGERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("mobileno", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }*/

    /**
     * Re crate database Delete all tables and create them again
     * */





}