package test.soc365.society365.maneger.Sales_Register;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import test.soc365.society365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import test.soc365.society365.helper.DatabaseHelper;

import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.allToDos3;
import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.banklist;
import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.cashlist;
import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.receipt_innerlist;
import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.timeperiod1;
import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.timeperiod2;
import static test.soc365.society365.maneger.Sales_Register.SalesChildActivityAdapter.wing;

public class SalesRegisterActivity extends AppCompatActivity {

    private SearchView searchView;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SalesRegisterActivityAdapter adapter;
    private SalesAdapterforwingwiseSummary adapter_wingwise;
    private List<sales_register_model> movieList;

    String stallyid;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    Calendar calendartp1, calendartp2;

    TextView summaryname, totalamountgross;

    int year1, year2;
    String day1, day2;
    String month1, month2;

    //progress bar
    SpotsDialog spotsDialog;

    LinearLayout calander;
    AlertDialog.Builder builder;


    ArrayList<String> partynametype;
    ArrayList<String> partyamount;
    JSONArray jsonArray1;
    String partyledgertype,banktype, timeperiod1, timeperiod2;
    static String ledgertype;
    public static String partynameusedfor_partysummary;
    // Database Helper
    DatabaseHelper db;
    AlertDialog alertDialog;

    RadioGroup radioGroupPreference;
    RadioButton radioButton_equalto;
    RadioButton radioButton_greaterthan;
    String radiocheckbutton;
    public static List<sales_register_model> wingwiselist_db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_register);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Sales");
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        db = new DatabaseHelper(getApplicationContext());
        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid", "0");


        Intent intent = getIntent();
        partyledgertype = intent.getStringExtra("partyledgertype");
        banktype = intent.getStringExtra("banktype");
        ledgertype = intent.getStringExtra("type");
        timeperiod1 = intent.getStringExtra("date1");
        timeperiod2 = intent.getStringExtra("date2");

      /*  day1 = "01";
        day2 = "31";
        month1 = "04";
        month2 = "03";
        year1 =2018;
        year2 =2019;

        timeperiod1=year1+"-"+month1+"-"+day1;

        timeperiod2=year2+"-"+month2+"-"+day2;*/


        //calander to choose custom date
        calander = (LinearLayout) findViewById(R.id.calander);
        builder = new AlertDialog.Builder(this);

        totalamountgross = (TextView) findViewById(R.id.totalamountgross);
        summaryname = (TextView) findViewById(R.id.summaryname);
        summaryname.setText(timeperiod1 + " - " + timeperiod2);

        partynametype = new ArrayList<>();
        partyamount = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        movieList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);


        spotsDialog = new SpotsDialog(this, R.style.Custom);

        Log.d("partyledgertype", partyledgertype);
        if (ledgertype.equals("from_sales_account")) {
            //getData_Account();
            //for sale -sales account - innerpage
            get_Bill_sales_innerdata();
            adapter = new SalesRegisterActivityAdapter(getApplicationContext(), receipt_innerlist);
            recyclerView.setAdapter(adapter);
        } else if (ledgertype.equals("from_sales_wingwise")) {
            //sundry details wingwise sale
            Log.d("onCreateledgertype", ledgertype);
            getData_wingwise();
            adapter_wingwise = new SalesAdapterforwingwiseSummary(getApplicationContext(), partynametype, partyamount,timeperiod1,timeperiod2);
            recyclerView.setAdapter(adapter_wingwise);
        } else if ((ledgertype.equals("from_receipt"))) {


            Receipt_method("preference", "amountsort", timeperiod1, timeperiod2);


        } else if ((ledgertype.equals("from_sales_wingwise_summary"))) {
            //sundry details inner page sale -->from_sales_wingwise_summary
            Log.d("onCreateledgertype", ledgertype);
            get_Bill_sundrydetails_innerdata();
            adapter = new SalesRegisterActivityAdapter(getApplicationContext(), receipt_innerlist);
            recyclerView.setAdapter(adapter);
        }
    }

    private void getData_wingwise() {

        int amountplus = 0;
       /* wingwiselist_db = db.getReceiptexp(getApplicationContext(), "preference", "amountsort", "Bill_sending_wingname", partyledgertype, timeperiod1, timeperiod2);
*/
     //   wingwiselist_db =  db.getReceiptexp(getApplicationContext(),"preference","amountsort","Bill","forgetting_billinsales", timeperiod1, timeperiod2);

        for (sales_register_model todo : allToDos3) {

            if(todo.getTest().equals(partyledgertype))
            {
                //partyname which is not there in ledger table will have flat wing in Others group
                partynametype.add(todo.getLed_name());


                amountplus = amountplus + todo.getAmt_for_receipt();
            }


        }
        totalamountgross.setText("\u20B9" + amountplus);

                 /*  for all group*/
        Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
        partynametype.clear();
        partynametype.addAll(primesWithoutDuplicates);
        Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
        Log.d("amountplusnew", String.valueOf(partynametype) + " " + amountplus);


        for (int j = 0; j < partynametype.size(); j++) {


            int amountplus2 = 0;
            for (sales_register_model todo : allToDos3) {

                if(todo.getTest().equals(partyledgertype)) {

                    if (partynametype.get(j).equals(todo.getLed_name())) {


                        amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                    }
                }
            }
            partyamount.add(String.valueOf(amountplus2));
        }


    }

    private void getData() {
    /*    final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/


        //c summaryname.setText(timeperiod1+" - "+timeperiod2);


        if (ledgertype.equals("from_receipt")) {

            String jsonString = sharedPreferences
                    .getString("jsonStringReceipt", "0");


            try {
                jsonArray1 = new JSONArray(jsonString);
                Log.d("ResponseHADtestArray", String.valueOf(jsonArray1));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            movieList.clear();

            int amountplus = 0;
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = jsonArray1.getJSONObject(i);

                    if (partyledgertype.equals("Others")) {
                        partyledgertype = "";
                    }

                    if (partyledgertype.equals(jsonObject1.getString("parent_name_2"))) {
                        sales_register_model sales_register_model = new sales_register_model();

                        if (jsonObject1.getString("name").contains("(")) {
                            String[] arrOfStr = jsonObject1.getString("name").split("\\(");
                            sales_register_model.setName(arrOfStr[0]);
                            partynameusedfor_partysummary = arrOfStr[0];
                            sales_register_model.setAmount(jsonObject1.getString("amount"));
                            sales_register_model.setVn(jsonObject1.getString("voucher_number"));
                            sales_register_model.setDate(jsonObject1.getString("date"));
                            sales_register_model.setType(jsonObject1.getString("type"));
                            if (arrOfStr[1].contains(")")) {
                                String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                sales_register_model.setFlatno(arrOfStr2[0]);
                            }

                        } else {
                            sales_register_model.setName(jsonObject1.getString("name"));
                            partynameusedfor_partysummary = jsonObject1.getString("name");
                            sales_register_model.setAmount(jsonObject1.getString("amount"));
                            sales_register_model.setFlatno(jsonObject1.getString("flat_no"));
                            sales_register_model.setVn(jsonObject1.getString("voucher_number"));
                            sales_register_model.setDate(jsonObject1.getString("date"));
                            sales_register_model.setType(jsonObject1.getString("type"));
                        }
                        amountplus = amountplus + jsonObject1.getInt("amount");
                        Log.d("amountplus", String.valueOf(amountplus));

                        sales_register_model.setDate1(timeperiod1);
                        sales_register_model.setDate2(timeperiod2);


                        movieList.add(sales_register_model);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            String amountall = String.valueOf(amountplus);
            totalamountgross.setText("\u20B9" + amountall);

        } else {

            String jsonString = sharedPreferences
                    .getString("jsonStringSalesforWingWise", "0");


            try {
                jsonArray1 = new JSONArray(jsonString);
                Log.d("ResponseHADtestArray", String.valueOf(jsonArray1));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            movieList.clear();

            int amountplus = 0;
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = jsonArray1.getJSONObject(i);

                    if (partyledgertype.equals("Others")) {
                        partyledgertype = "";
                    }

                    if (partyledgertype.equals(jsonObject1.getString("ledger_name"))) {
                        sales_register_model sales_register_model = new sales_register_model();

                        if (jsonObject1.getString("name").contains("(")) {
                            String[] arrOfStr = jsonObject1.getString("name").split("\\(");
                            sales_register_model.setName(arrOfStr[0]);
                            sales_register_model.setAmount(jsonObject1.getString("amount"));
                            if (arrOfStr[1].contains(")")) {
                                String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                sales_register_model.setFlatno(arrOfStr2[0]);
                            }

                        } else {
                            sales_register_model.setName(jsonObject1.getString("name"));
                            sales_register_model.setAmount(jsonObject1.getString("amount"));
                            sales_register_model.setFlatno(jsonObject1.getString("flat_no"));

                        }
                        amountplus = amountplus + jsonObject1.getInt("amount");
                        Log.d("amountplus", String.valueOf(amountplus));

                        sales_register_model.setDate1(timeperiod1);
                        sales_register_model.setDate2(timeperiod2);


                        movieList.add(sales_register_model);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            String amountall = String.valueOf(amountplus);
            totalamountgross.setText("\u20B9" + amountall);

        }

    }


    private void getData_Account() {

        spotsDialog.show();
        summaryname.setText(timeperiod1 + " - " + timeperiod2);
        String url = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/get_maindetails.php?"
                    + "&filter_date=" + URLEncoder.encode(timeperiod1, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8") + "&company_id=" + stallyid + "&type=" + "bill"
                    + "&ledger_name="
                    + URLEncoder.encode(partyledgertype, "UTF-8").replaceAll("\\+", "%20");
            Log.d("onResponse:partyledger ", url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:party ", response);

                                        try {

                                            JSONObject jsonObject = new JSONObject(response);

                                            spotsDialog.dismiss();
                                            JSONArray jsonArray1 = jsonObject.getJSONArray("records");

                                            Log.d("partyledger2 ", String.valueOf(jsonArray1));

                                            movieList.clear();

                                            partynametype = new ArrayList<>();

                                            int amountplus = 0;
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                sales_register_model sales_register_model = new sales_register_model();
                                                //some party name as flatno with that only so below code to sepearate it
                                                if (jsonObject1.getString("name").contains("(")) {
                                                    String[] arrOfStr = jsonObject1.getString("name").split("\\(");
                                                    sales_register_model.setName(arrOfStr[0]);
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    if (arrOfStr[1].contains(")")) {
                                                        String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                                        sales_register_model.setFlatno(arrOfStr2[0]);
                                                    }

                                                } else {
                                                    sales_register_model.setName(jsonObject1.getString("name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    sales_register_model.setFlatno(jsonObject1.getString("flat_no"));

                                                }

                                                amountplus = amountplus + jsonObject1.getInt("amount");
                                                Log.d("amountplus", String.valueOf(amountplus));
                                                sales_register_model.setDate1(timeperiod1);
                                                sales_register_model.setDate2(timeperiod2);


                                                partynametype.add(jsonObject1.getString("parent_name_2"));


                                                movieList.add(sales_register_model);

                                            }

                                                  /*  for all group*/
                                            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                                            partynametype.clear();
                                            partynametype.addAll(primesWithoutDuplicates);
                                            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                                            Log.d("partynametype", String.valueOf(partynametype));

                                            String amountall = String.valueOf(amountplus);
                                            totalamountgross.setText("\u20B9" + amountall);
                                            adapter.notifyDataSetChanged();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            spotsDialog.dismiss();
                                        }

                                        adapter.notifyDataSetChanged();
                                        spotsDialog.dismiss();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());
                                spotsDialog.dismiss();
                            }
                        });
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search)
        );
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed

                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        //noinspection Simp lifiableIfStatement
        else if (id == R.id.search) {
            //searchbar edittext
            return true;
        } else if (id == R.id.amount) {
            show_amountdialog();
        } else if (id == R.id.group) {
            Intent intent = new Intent(getApplicationContext(), SalesChildActivity.class);
            intent.putExtra("type", "from_receipt");
            intent.putExtra("date1", timeperiod1);
            intent.putExtra("date2", timeperiod2);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();
            builder = new AlertDialog.Builder(SalesRegisterActivity.this);


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesRegisterActivity.this,
                    android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("This Year");
            arrayAdapter.add("Last Year");
            arrayAdapter.add("Custom Date");

            builder.setTitle("Select Time")
                    .setCancelable(true);


            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    if (strName == "Custom Date") {
                        showcalender();
                    } else if (strName == "This Year") {
                        day1 = "01";
                        day2 = "31";
                        month1 = "01";
                        month2 = "03";
                        year1 = 2019;
                        year2 = 2019;

                        timeperiod2 = year2 + "-" + month2 + "-" + day2;

//                        calendartp.add(Calendar.YEAR, -1);
                        timeperiod1 = year1 + "-" + month1 + "-" + day1;
                        // monthstring();
                        //getData();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = null, date2 = null;
                        try {

                            date1 = sdf.parse(timeperiod1);
                            date2 = sdf.parse(timeperiod2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        summaryname.setText(sdf.format(date1) + " - " + sdf.format(date2));
                        Receipt_method("preference", "amountsort", sdf.format(date1), sdf.format(date2));
                        // CashbankarrayDate("Cash",sdf.format(date1),sdf.format(date2));

                    } else if (strName == "Last Year") {
                        day1 = "01";
                        day2 = "31";
                        month1 = "04";
                        month2 = "12";
                        year1 = 2018;

                        year2 = 2018;

                        timeperiod2 = year2 + "-" + month2 + "-" + day2;

//                        calendartp.add(Calendar.YEAR, -1);
                        timeperiod1 = year1 + "-" + month1 + "-" + day1;
                        // monthstring();
                        //getData();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = null, date2 = null;
                        try {

                            date1 = sdf.parse(timeperiod1);
                            date2 = sdf.parse(timeperiod2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        summaryname.setText(sdf.format(date1) + " - " + sdf.format(date2));
                        Receipt_method("preference", "amountsort", sdf.format(date1), sdf.format(date2));

                    }
                    Toast toast = Toast.makeText(getApplicationContext(), strName, Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }
            });
            builder.show();


        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem sharemenu = menu.findItem(R.id.share);
        MenuItem sharegroup = menu.findItem(R.id.group);
        MenuItem action_calender = menu.findItem(R.id.action_calender);

        if (ledgertype.equals("from_sales_wingwise")) {
            MenuItem search = menu.findItem(R.id.action_search);
            search.setVisible(false);
            MenuItem amount = menu.findItem(R.id.amount);
            amount.setVisible(false);
           action_calender.setVisible(false);
            sharegroup.setVisible(false);
        } else if ((ledgertype.equals("from_sales_wingwise_summary")))
        {

            MenuItem amount = menu.findItem(R.id.amount);
            amount.setVisible(false);
            action_calender.setVisible(false);
            sharegroup.setVisible(false);
        }
        else if ((ledgertype.equals("from_sales_account")))
        {
            MenuItem amount = menu.findItem(R.id.amount);
            amount.setVisible(false);
            action_calender.setVisible(false);
            sharegroup.setVisible(false);
        }

            //  action_calender.setVisible(false);
        // sharegroup.setVisible(false);
        sharemenu.setVisible(false);
        return true;
    }

    private void showcalender() {
     /*   ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_date, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_date,
                (ViewGroup) findViewById(R.id.tabhost));

        TabHost tabs = (TabHost) layout.findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec tabpage1 = tabs.newTabSpec("FROM");
        tabpage1.setContent(R.id.ScrollView01);
        tabpage1.setIndicator("FROM");
        TabHost.TabSpec tabpage2 = tabs.newTabSpec("TO");
        tabpage2.setContent(R.id.ScrollView02);
        tabpage2.setIndicator("TO");
        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null, date2 = null;
        try {

            date1 = sdf.parse(timeperiod1);
            date2 = sdf.parse(timeperiod2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String[] splited = sdf.format(date1).split("-");

        int y = Integer.parseInt(splited[0]);
        int m = Integer.parseInt(splited[1]);
        int d = Integer.parseInt(splited[2]);

        Toast.makeText(getApplicationContext(), String.valueOf(m) + String.valueOf(d), Toast.LENGTH_LONG).show();
        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);

        datePicker.updateDate(y, m, d);

        Calendar currentDate = Calendar.getInstance();
        currentDate.set(currentDate.get(Calendar.YEAR) - 1, Calendar.APRIL, 1);
        datePicker.setMinDate(currentDate.getTimeInMillis());

        Calendar currentDate2 = Calendar.getInstance();
        currentDate2.set(currentDate2.get(Calendar.YEAR), Calendar.MARCH, 31);
        datePicker.setMaxDate(currentDate2.getTimeInMillis());
/*
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
*/

        String[] splited2 = sdf.format(date2).split("-");

        int y2 = Integer.parseInt(splited2[0]);
        int m2 = Integer.parseInt(splited2[1]);
        int d2 = Integer.parseInt(splited2[2]);

        final DatePicker datePicker2 = (DatePicker) layout.findViewById(R.id.datePicker2);
        datePicker2.updateDate(y2, m2, d2);


        datePicker2.setMinDate(currentDate.getTimeInMillis());
        datePicker2.setMaxDate(currentDate2.getTimeInMillis());

        /*datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {


            }
        });*/
        builder = new AlertDialog.Builder(SalesRegisterActivity.this);

        builder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                day1 = String.valueOf(datePicker.getDayOfMonth());
                                month1 = String.valueOf(datePicker.getMonth() + 1);
                                year1 = datePicker.getYear();


                                day2 = String.valueOf(datePicker2.getDayOfMonth());
                                month2 = String.valueOf(datePicker2.getMonth() + 1);
                                year2 = datePicker2.getYear();


                                timeperiod1 = year1 + "-" + month1 + "-" + day1;

                                timeperiod2 = year2 + "-" + month2 + "-" + day2;

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = null, date2 = null;
                                try {

                                    date1 = sdf.parse(timeperiod1);
                                    date2 = sdf.parse(timeperiod2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                summaryname.setText(sdf.format(date1) + " - " + sdf.format(date2));
                                Receipt_method("preference", "amountsort", sdf.format(date1), sdf.format(date2));

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
       /* builder.setTitle("Dialog with tabs");*/
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();

    }

    //receipt method
    public void Receipt_method(String preference, String amountsort, String startdate, String enddate) {
        if (partyledgertype.equals("from_mainpage")) {
            getSupportActionBar().setTitle("Receipt");

            int amountplus = 0;
            receipt_innerlist = db.getReceiptexp(getApplicationContext(), preference, amountsort, "Receipt", "forgetting_billinsales", startdate, enddate);
            for (sales_register_model todo : receipt_innerlist) {

                amountplus = amountplus + todo.getAmt_for_receipt();

            }
            totalamountgross.setText("\u20B9" + amountplus);
            adapter = new SalesRegisterActivityAdapter(getApplicationContext(), receipt_innerlist);
            recyclerView.setAdapter(adapter);
        } else {
            getSupportActionBar().setTitle("Receipt - " + partyledgertype);

            //getData();
            get_receipt_innerdata(preference, amountsort, startdate, enddate);
            adapter = new SalesRegisterActivityAdapter(getApplicationContext(), receipt_innerlist);
            recyclerView.setAdapter(adapter);
        }
    }

    //receipt detail inner page method
    public void get_receipt_innerdata(String preference, String amountsort, String startdate, String enddate) {
        receipt_innerlist.clear();
        cashlist.clear();
        banklist.clear();
        allToDos3.clear();
        int amountplus2 = 0;

        List<sales_register_model> newList;

        if (partyledgertype.equals("Cash")) {
            cashlist = db.getReceiptexp(getApplicationContext(), preference, amountsort, "Receipt_cashorbank", "Cash", startdate, enddate);

            newList = new ArrayList<sales_register_model>(cashlist);
            for (sales_register_model todo : newList) {

                if (partyledgertype.equals("Others") && todo.getTest() == null) {
                    amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                    //making arraylist for inner page
                    sales_register_model sales_register_model = new sales_register_model();

                    sales_register_model.setName(todo.getName());
                    partynameusedfor_partysummary = todo.getName();
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    sales_register_model.setFlatno(todo.getFlatno());
                    if (todo.getVn() == null) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());
                    sales_register_model.setType(todo.getType());
                    sales_register_model.setLed_name(todo.getLed_name());

                    sales_register_model.setDate1(timeperiod1);
                    sales_register_model.setDate2(timeperiod2);
                    sales_register_model.setNarration(todo.getNarration());
                    sales_register_model.setCqdate(todo.getCqdate());
                    sales_register_model.setCqno(todo.getCqno());
                    sales_register_model.setBankdate(todo.getBankdate());


                    receipt_innerlist.add(sales_register_model);
                }

                amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                //making arraylist for inner page
                sales_register_model sales_register_model = new sales_register_model();
/*                if (todo.getTest().contains("(")) {
                    String[] arrOfStr = todo.getTest().split("\\(");
                    sales_register_model.setName(arrOfStr[0]);
                    partynameusedfor_partysummary = arrOfStr[0];
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    Toast.makeText(getApplicationContext(), todo.getVn(), Toast.LENGTH_SHORT).show();
                    Log.d("receipt_innerdata: ", todo.getVn());
                    if (todo.getVn() == null) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());

                    sales_register_model.setLed_name(todo.getLed_name());
                    sales_register_model.setType(todo.getType());
                    sales_register_model.setNarration(todo.getNarration());
                    sales_register_model.setCqdate(todo.getCqdate());
                    sales_register_model.setCqno(todo.getCqno());
                    sales_register_model.setBankdate(todo.getBankdate());
                    if (arrOfStr[1].contains(")")) {
                        String[] arrOfStr2 = arrOfStr[1].split("\\)");
                        sales_register_model.setFlatno(arrOfStr2[0]);
                    }

                }*/ /*else {*/
                    sales_register_model.setName(todo.getName());
                    partynameusedfor_partysummary = todo.getName();
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    sales_register_model.setFlatno(todo.getFlatno());
                    Log.d("innerdata2: ", todo.getVn());
                    if (todo.getVn().equals("")) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());

                    sales_register_model.setLed_name(todo.getLed_name());
                    sales_register_model.setType(todo.getType());
               /* }*/
                sales_register_model.setDate1(timeperiod1);
                sales_register_model.setDate2(timeperiod2);
                sales_register_model.setNarration(todo.getNarration());
                sales_register_model.setCqdate(todo.getCqdate());
                sales_register_model.setCqno(todo.getCqno());
                sales_register_model.setBankdate(todo.getBankdate());


                receipt_innerlist.add(sales_register_model);

            }
            totalamountgross.setText("\u20B9" + amountplus2);

        } else if (banktype.equals("bankaccount_click")) {
            banklist = db.getReceiptexp(getApplicationContext(), preference, amountsort, "Receipt_cashorbank", partyledgertype, startdate, enddate);

            newList = new ArrayList<sales_register_model>(banklist);

            for (sales_register_model todo : newList) {

                if (partyledgertype.equals("Others") && todo.getTest() == null) {
                    amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                    //making arraylist for inner page
                    sales_register_model sales_register_model = new sales_register_model();

                    sales_register_model.setName(todo.getName());
                    partynameusedfor_partysummary = todo.getName();
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    sales_register_model.setFlatno(todo.getFlatno());
                    if (todo.getVn() == null) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());
                    sales_register_model.setType(todo.getType());
                    sales_register_model.setLed_name(todo.getLed_name());

                    sales_register_model.setDate1(timeperiod1);
                    sales_register_model.setDate2(timeperiod2);
                    sales_register_model.setNarration(todo.getNarration());
                    sales_register_model.setCqdate(todo.getCqdate());
                    sales_register_model.setCqno(todo.getCqno());
                    sales_register_model.setBankdate(todo.getBankdate());


                    receipt_innerlist.add(sales_register_model);
                }

                    amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                    //making arraylist for inner page
                    sales_register_model sales_register_model = new sales_register_model();
                        sales_register_model.setName(todo.getName());
                        partynameusedfor_partysummary = todo.getName();
                        sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                        sales_register_model.setFlatno(todo.getFlatno());
                        Log.d("innerdata2: ", todo.getVn());
                        if (todo.getVn().equals("")) {
                            sales_register_model.setVn("0");
                        } else {
                            sales_register_model.setVn(todo.getVn());
                        }

                        sales_register_model.setDate(todo.getDate());

                        sales_register_model.setLed_name(todo.getLed_name());
                        sales_register_model.setType(todo.getType());

                    sales_register_model.setDate1(timeperiod1);
                    sales_register_model.setDate2(timeperiod2);
                    sales_register_model.setNarration(todo.getNarration());
                    sales_register_model.setCqdate(todo.getCqdate());
                    sales_register_model.setCqno(todo.getCqno());
                    sales_register_model.setBankdate(todo.getBankdate());


                    receipt_innerlist.add(sales_register_model);

            }
            totalamountgross.setText("\u20B9" + amountplus2);

        } else {
            allToDos3 = db.getReceiptexp(getApplicationContext(), preference, amountsort, "Receipt", "sundryAdapters", startdate, enddate);

            newList = new ArrayList<sales_register_model>(allToDos3);


      /*      for (sales_register_model todo : allToDos3) {

                if (partyledgertype.equals("Others") && todo.getTest() == null) {
                    amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                    //making arraylist for inner page
                    sales_register_model sales_register_model = new sales_register_model();

                    sales_register_model.setName(todo.getName());
                    partynameusedfor_partysummary = todo.getName();
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    sales_register_model.setFlatno(todo.getFlatno());
                    if (todo.getVn() == null) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());
                    sales_register_model.setType(todo.getType());
                    sales_register_model.setLed_name(todo.getLed_name());

                    sales_register_model.setDate1(timeperiod1);
                    sales_register_model.setDate2(timeperiod2);
                    sales_register_model.setNarration(todo.getNarration());
                    sales_register_model.setCqdate(todo.getCqdate());
                    sales_register_model.setCqno(todo.getCqno());
                    sales_register_model.setBankdate(todo.getBankdate());


                    receipt_innerlist.add(sales_register_model);
                }

                if (partyledgertype.equals(todo.getTest())) {


                    amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                    //making arraylist for inner page
                    sales_register_model sales_register_model = new sales_register_model();
                    if (todo.getTest().contains("(")) {
                        String[] arrOfStr = todo.getTest().split("\\(");
                        sales_register_model.setName(arrOfStr[0]);
                        partynameusedfor_partysummary = arrOfStr[0];
                        sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                        Toast.makeText(getApplicationContext(), todo.getVn(), Toast.LENGTH_SHORT).show();
                        Log.d("receipt_innerdata: ", todo.getVn());
                        if (todo.getVn() == null) {
                            sales_register_model.setVn("0");
                        } else {
                            sales_register_model.setVn(todo.getVn());
                        }

                        sales_register_model.setDate(todo.getDate());

                        sales_register_model.setLed_name(todo.getLed_name());
                        sales_register_model.setType(todo.getType());
                        sales_register_model.setNarration(todo.getNarration());
                        sales_register_model.setCqdate(todo.getCqdate());
                        sales_register_model.setCqno(todo.getCqno());
                        sales_register_model.setBankdate(todo.getBankdate());
                        if (arrOfStr[1].contains(")")) {
                            String[] arrOfStr2 = arrOfStr[1].split("\\)");
                            sales_register_model.setFlatno(arrOfStr2[0]);
                        }

                    } else {
                        sales_register_model.setName(todo.getName());
                        partynameusedfor_partysummary = todo.getName();
                        sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                        sales_register_model.setFlatno(todo.getFlatno());
                        Log.d("innerdata2: ", todo.getVn());
                        if (todo.getVn().equals("")) {
                            sales_register_model.setVn("0");
                        } else {
                            sales_register_model.setVn(todo.getVn());
                        }

                        sales_register_model.setDate(todo.getDate());

                        sales_register_model.setLed_name(todo.getLed_name());
                        sales_register_model.setType(todo.getType());
                    }
                    sales_register_model.setDate1(timeperiod1);
                    sales_register_model.setDate2(timeperiod2);
                    sales_register_model.setNarration(todo.getNarration());
                    sales_register_model.setCqdate(todo.getCqdate());
                    sales_register_model.setCqno(todo.getCqno());
                    sales_register_model.setBankdate(todo.getBankdate());


                    receipt_innerlist.add(sales_register_model);
                }
            }
        }*/

        for (sales_register_model todo : newList) {

            if (partyledgertype.equals("Others") && todo.getTest() == null) {
                amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                //making arraylist for inner page
                sales_register_model sales_register_model = new sales_register_model();

                sales_register_model.setName(todo.getName());
                partynameusedfor_partysummary = todo.getName();
                sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                sales_register_model.setFlatno(todo.getFlatno());
                if (todo.getVn() == null) {
                    sales_register_model.setVn("0");
                } else {
                    sales_register_model.setVn(todo.getVn());
                }

                sales_register_model.setDate(todo.getDate());
                sales_register_model.setType(todo.getType());
                sales_register_model.setLed_name(todo.getLed_name());

                sales_register_model.setDate1(timeperiod1);
                sales_register_model.setDate2(timeperiod2);
                sales_register_model.setNarration(todo.getNarration());
                sales_register_model.setCqdate(todo.getCqdate());
                sales_register_model.setCqno(todo.getCqno());
                sales_register_model.setBankdate(todo.getBankdate());


                receipt_innerlist.add(sales_register_model);
            }
            if (partyledgertype.equals(todo.getTest())) {

                amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                //making arraylist for inner page
                sales_register_model sales_register_model = new sales_register_model();
                if (todo.getTest().contains("(")) {
                    String[] arrOfStr = todo.getTest().split("\\(");
                    sales_register_model.setName(arrOfStr[0]);
                    partynameusedfor_partysummary = arrOfStr[0];
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    Toast.makeText(getApplicationContext(), todo.getVn(), Toast.LENGTH_SHORT).show();
                    Log.d("receipt_innerdata: ", todo.getVn());
                    if (todo.getVn() == null) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());

                    sales_register_model.setLed_name(todo.getLed_name());
                    sales_register_model.setType(todo.getType());
                    sales_register_model.setNarration(todo.getNarration());
                    sales_register_model.setCqdate(todo.getCqdate());
                    sales_register_model.setCqno(todo.getCqno());
                    sales_register_model.setBankdate(todo.getBankdate());
                    if (arrOfStr[1].contains(")")) {
                        String[] arrOfStr2 = arrOfStr[1].split("\\)");
                        sales_register_model.setFlatno(arrOfStr2[0]);
                    }

                } else {
                    sales_register_model.setName(todo.getName());
                    partynameusedfor_partysummary = todo.getName();
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    sales_register_model.setFlatno(todo.getFlatno());
                    Log.d("innerdata2: ", todo.getVn());
                    if (todo.getVn().equals("")) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());

                    sales_register_model.setLed_name(todo.getLed_name());
                    sales_register_model.setType(todo.getType());
                }
                sales_register_model.setDate1(timeperiod1);
                sales_register_model.setDate2(timeperiod2);
                sales_register_model.setNarration(todo.getNarration());
                sales_register_model.setCqdate(todo.getCqdate());
                sales_register_model.setCqno(todo.getCqno());
                sales_register_model.setBankdate(todo.getBankdate());


                receipt_innerlist.add(sales_register_model);
            }
        }
        totalamountgross.setText("\u20B9" + amountplus2);
    }

}

    public void get_Bill_sundrydetails_innerdata(){
        receipt_innerlist.clear();
        int amountplus2 = 0;
        for (sales_register_model todo : allToDos3) {


            if (partyledgertype.equals(todo.getLed_name())&&wing.equals(todo.getTest())) {


                amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                //making arraylist for inner page
                sales_register_model sales_register_model = new sales_register_model();
                    sales_register_model.setName(todo.getName());
                    partynameusedfor_partysummary = todo.getName();
                    sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
                    sales_register_model.setFlatno(todo.getFlatno());
                    Log.d("innerdata2: ", todo.getVn());
                    if (todo.getVn().equals("")) {
                        sales_register_model.setVn("0");
                    } else {
                        sales_register_model.setVn(todo.getVn());
                    }

                    sales_register_model.setDate(todo.getDate());

                    sales_register_model.setLed_name(todo.getLed_name());
                    sales_register_model.setType(todo.getType());

                sales_register_model.setDate1(timeperiod1);
                sales_register_model.setDate2(timeperiod2);


                receipt_innerlist.add(sales_register_model);
            }
        }
        totalamountgross.setText("\u20B9" + amountplus2);
    }

    public void get_Bill_sales_innerdata() {

        
        List<sales_register_model> allToDos= db.getReceiptexp(getApplicationContext(),"preference","amountsort","Bill_from_sales",partyledgertype,timeperiod1,timeperiod2);

        receipt_innerlist.clear();
        int amountplus2 = 0;
        for (sales_register_model todo : allToDos) {


            amountplus2 = amountplus2 + todo.getAmt_for_receipt();

            //making arraylist for inner page
            sales_register_model sales_register_model = new sales_register_model();

            sales_register_model.setName(todo.getName());
            partynameusedfor_partysummary = todo.getName();
            sales_register_model.setAmt_for_receipt(todo.getAmt_for_receipt());
            sales_register_model.setFlatno(todo.getFlatno());
            Log.d("innerdata2: ", todo.getVn());
            if (todo.getVn().equals("")) {
                sales_register_model.setVn("0");
            } else {
                sales_register_model.setVn(todo.getVn());
            }

            sales_register_model.setDate(todo.getDate());

            sales_register_model.setLed_name(todo.getLed_name());
            sales_register_model.setType(todo.getType());


            receipt_innerlist.add(sales_register_model);

            totalamountgross.setText("\u20B9" + amountplus2);
        }

    }
    private void show_amountdialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.customdialog_amount_partyledger, viewGroup, false);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edittext);

          radioGroupPreference = (RadioGroup) dialogView.findViewById(R.id.radioGroupPreference);
         radioButton_equalto = (RadioButton)dialogView.findViewById(R.id.radioButton_equalto);
         radioButton_greaterthan = (RadioButton) dialogView.findViewById(R.id.radioButton_greaterthan);

         radioButton_equalto.setChecked(true);
        if(radioButton_equalto.isChecked())
        {
            // is checked
            radiocheckbutton = "equalto";
        }
        else
        {
            // is checked
            radiocheckbutton = "greaterthan";
            // not checked
        }

        radioButton_equalto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroupPreference.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton_equalto = (RadioButton) findViewById(selectedId);
                radiocheckbutton = "equalto";
             /*   Toast.makeText(MyAndroidAppActivity.this,
                        radioButton_equalto.getText(), Toast.LENGTH_SHORT).show();*/
            }

        });


        radioButton_greaterthan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroupPreference.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton_greaterthan = (RadioButton) findViewById(selectedId);

                radiocheckbutton = "greaterthan";
            }

        });


        Button button =(Button)dialogView.findViewById(R.id.submit);



        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit = editText.getText().toString();
                Toast.makeText(getApplicationContext(),edit,Toast.LENGTH_SHORT).show();
                Receipt_method(radiocheckbutton,edit,timeperiod1,timeperiod2);
                alertDialog.dismiss();
            }
        });
    }
}
