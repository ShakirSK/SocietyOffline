package test.soc365.society365.maneger.PaymentReports;

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
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.Sales_Register.SalesRegisterDetailPage;
import test.soc365.society365.maneger.Sales_Register.sales_register_model;

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
import test.soc365.society365.maneger.partyledger.partydetailmodel;

import static test.soc365.society365.helper.DatabaseHelper.salespaylist;

public class PaymentReportActivity extends AppCompatActivity {

    private SearchView searchView;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private PaymentReportActivityAdapter adapter;

    static List<sales_register_model> movieList;

    String stallyid;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String timeperiod1,timeperiod2;

    Calendar calendartp1,calendartp2;

    TextView summaryname,totalamountgross;

    //progress bar
    SpotsDialog spotsDialog;

    int year1,year2;
    String day1,day2;
    String month1,month2;

    TextView partynamecash,amountcash,partynamebank,amountbank;

    LinearLayout calander;
    AlertDialog.Builder builder;

    ArrayList<String> partynametype;
    ArrayList<String> partyamount;
    JSONArray jsonArray1;
    // Database Helper
    DatabaseHelper db;
    List<partydetailmodel> allToDos3;
    int amountplus=0;
    TextView dateendandstart;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_report);
        Toolbar toolbar = findViewById(R.id.tool);
        totalamountgross =(TextView)findViewById(R.id.totalamountgross);
        summaryname =(TextView)findViewById(R.id.summaryname);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dateendandstart =(TextView)findViewById(R.id.summaryname);


        day1 = "01";
        day2 = "31";
        month1 = "04";
        month2 = "03";
        year1 =2018;

        year2 =2019;


        monthstring();

        timeperiod1=day1+"-"+month1+"-"+year1;

        timeperiod2=day2+"-"+month2+"-"+year2;


        getSupportActionBar().setTitle("Payment");
        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );

        //calander to choose custom date
        calander = (LinearLayout) findViewById(R.id.calander);
        builder = new AlertDialog.Builder(this);


        //cash and bank initialize

        partynamecash =(TextView)findViewById(R.id.partynamecash);
        amountcash =(TextView)findViewById(R.id.amountcash);
        partynamebank =(TextView)findViewById(R.id.partynamebank);
        amountbank=(TextView)findViewById(R.id.amountbank);

        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        movieList = new ArrayList<>();

        db = new DatabaseHelper(getApplicationContext());

        method_payment(timeperiod1,timeperiod2);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);



        spotsDialog = new SpotsDialog(this,R.style.Custom);

/*
        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentReportActivity.this,
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
                        if(strName=="Custom Date"){
                            showcalender();
                        }
                        else if(strName=="This Year")
                        {
                            final Calendar calendartp= Calendar.getInstance();
                            //setting calender to custom date

                            timeperiod2=day2+"-"+month2+"-"+calendartp.get(Calendar.YEAR);

                            calendartp.add(Calendar.YEAR, -1);
                            timeperiod1=day1+"-"+month1+"-"+calendartp.get(Calendar.YEAR);
                            getData();
                        }
                        else if(strName=="Last Year")
                        {

                            year1=year1-1;
                            year2=year2-1;


                            timeperiod1=day1+"-"+month1+"-"+year1;


                            timeperiod2=day2+"-"+month2+"-"+year2;

                            getData();

                        }
                        Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                        toast.show();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
*/

        //done for receipt after completion of main receipt ..will cut this
      /*  partynametype =  new ArrayList<>();
        partyamount = new ArrayList<>();
        int amountplus=0;
       allToDos3 =    db.getReceiptdetail();
        for (partydetailmodel todo : allToDos3) {

            //partyname which is not there in ledger table will have flat wing in Others group
            if(todo.getCompanyname()==null)
            {
                partynametype.add("Others");
            }
            else
            {
                partynametype.add(todo.getCompanyname());
            }


               amountplus =amountplus+todo.getAmt_for_receipt();

        }


                 *//*  for all group*//*
                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                partynametype.clear();
                partynametype.addAll(primesWithoutDuplicates);
        Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                Log.d("amountplusnew",String.valueOf(partynametype)+" "+amountplus);


        for (int j=0;j<partynametype.size();j++) {


            int amountplus2=0;
            for (partydetailmodel todo : allToDos3) {

                if(partynametype.get(j).equals("Others")&&todo.getCompanyname()==null)
                {
                    amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                }

                if (partynametype.get(j).equals(todo.getCompanyname())) {


                    amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                }
            }
            partyamount.add(String.valueOf(amountplus2));
        }

        Log.d("amountplusnew",String.valueOf(partyamount));

                editor.putString("jsonStringReceipt", String.valueOf(trailbalance));
                editor.apply();
*/


        //  getData();

    }

    private void getData() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        spotsDialog.show();



        summaryname.setText(timeperiod1+" - "+timeperiod2);
        String url = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/get_salesregister.php?" +
                    "filter_date="+ URLEncoder.encode(timeperiod1, "UTF-8")+
                    "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                    +"&company_id="+stallyid+"&type="+"payment";
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

                                            JSONObject jsonObject= new JSONObject(response);


                                            JSONArray jsonArray1=jsonObject.getJSONArray("records");

                                            Log.d("partyledger2 ", String.valueOf(jsonArray1));

                                            String jsonString = jsonArray1.toString();
                                            SharedPreferences settings =getSharedPreferences(
                                                    "pref", 0);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putString("jsonStringPaymentforallgroup", jsonString);
                                            editor.apply();

                                            movieList.clear();
                                            partynametype =  new ArrayList<>();

                                            int amountplus=0;

                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                              /*  if("Cash".equals(jsonObject1.getString("ledger_name")))
                                                {
                                                    partynamecash.setText(jsonObject1.getString("ledger_name"));
                                                    amountcash.setText(jsonObject1.getString("amount"));
                                                }
                                                else if("T.J.S.B A/c".equals(jsonObject1.getString("ledger_name")))
                                                {
                                                    partynamebank.setText(jsonObject1.getString("ledger_name"));
                                                    amountbank.setText(jsonObject1.getString("amount"));
                                                }
                                                else
                                                {*/

                                                if("Cash-in-hand".equals(jsonObject1.getString("parent_name_2"))||"Bank Accounts".equals(jsonObject1.getString("parent_name_2"))) {
                                                    Log.d("onResponse:ledger_name ",jsonObject1.getString("ledger_name"));
                                                    sales_register_model sales_register_model = new sales_register_model();

                                                    sales_register_model.setName(jsonObject1.getString("ledger_name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));

                                                    sales_register_model.setFlatno(jsonObject1.getString("flat_no"));


                                                    sales_register_model.setDate1(timeperiod1);
                                                    sales_register_model.setDate2(timeperiod2);

                                                    partynametype.add(jsonObject1.getString("parent_name_2"));

                                                    movieList.add(sales_register_model);
                                                }

                                                amountplus = amountplus+jsonObject1.getInt("amount");
                                                Log.d("amountplus", String.valueOf(amountplus));
                                            }

                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                                if("Cash-in-hand".equals(jsonObject1.getString("parent_name_2"))||"Bank Accounts".equals(jsonObject1.getString("parent_name_2"))) {
                                                }
                                                else {
                                                    sales_register_model sales_register_model = new sales_register_model();

                                                    sales_register_model.setName(jsonObject1.getString("ledger_name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));

                                                    sales_register_model.setDate1(timeperiod1);
                                                    sales_register_model.setDate2(timeperiod2);
                                                    movieList.add(sales_register_model);
                                                }


                                                   }



                                            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                                            partynametype.clear();
                                            partynametype.addAll(primesWithoutDuplicates);
                                            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                                            Log.d("partynametype",String.valueOf(partynametype));

                                            String amountall = String.valueOf(amountplus);
                                            totalamountgross.setText("\u20B9"+amountall);
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

    public void method_payment(String startdate,String enddate) {
        List<sales_register_model> allToDos =    db.getPaymentDetail(startdate,enddate);
        for (sales_register_model todo : allToDos) {

            amountplus = amountplus+ todo.getAmt_for_receipt();
        }
        totalamountgross.setText("\u20B9"+amountplus);
        adapter = new PaymentReportActivityAdapter(getApplicationContext(),salespaylist);

        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView( menu.findItem(R.id.action_search)
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
        if (id ==  android.R.id.home) {
            onBackPressed();
            return true;
        }
        //noinspection Simp lifiableIfStatement
       else if (id == R.id.search) {
            //searchbar edittext
            return true;
        }
        else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();


                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentReportActivity.this,
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
                            if(strName=="Custom Date"){
                                showcalender();
                            }
                            else if(strName=="This Year")
                            {
                                day1 = "01";
                                day2 = "31";
                                month1 = "01";
                                month2 = "03";
                                year1 =2019;
                                year2 =2019;

                                timeperiod2=year2+"-"+month2+"-"+day2;

//                        calendartp.add(Calendar.YEAR, -1);
                                timeperiod1=year1+"-"+month1+"-"+day1;
                                // monthstring();
                                //getData();

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = null,date2 = null;
                                try {

                                    date1 = sdf.parse(timeperiod1);
                                    date2 = sdf.parse(timeperiod2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));
                                method_payment(sdf.format(date1),sdf.format(date2));

                            }
                            else if(strName=="Last Year")
                            {
                                day1 = "01";
                                day2 = "31";
                                month1 = "04";
                                month2 = "12";
                                year1 =2018;

                                year2 =2018;

                                timeperiod2=year2+"-"+month2+"-"+day2;

//                        calendartp.add(Calendar.YEAR, -1);
                                timeperiod1=year1+"-"+month1+"-"+day1;
                                // monthstring();
                                //getData();

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = null,date2 = null;
                                try {

                                    date1 = sdf.parse(timeperiod1);
                                    date2 = sdf.parse(timeperiod2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));
                                method_payment(sdf.format(date1),sdf.format(date2));


                            }
                            Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                            toast.show();
                            dialog.dismiss();
                        }
                    });
                    builder.show();


        }
        else if (id == R.id.group)
        {
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("All Groups");
            for (int i=0;i<partynametype.size();i++)
            {

                if (partynametype.get(i).equals(""))
                {
                    partynametype.set(i,"others");
                }
            }
            arrayAdapter.addAll(partynametype);
          /*  arrayAdapter.add("This Year");
            arrayAdapter.add("Last Year");
            arrayAdapter.add("Custom Date");*/

            builder.setTitle("Select Group")
                    .setCancelable(true);



            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                 /*     if(strName==partynametype.get(which)){

                    }
                    else if(strName=="This Year")
                    {

                    }
                    else */
                    if(strName=="All Groups")
                    {
                        getData();
                    }
                    else {

                        SharedPreferences settings = getSharedPreferences("pref", 0);
                        String jsonString = settings
                                .getString("jsonStringPaymentforallgroup", null);


                        try {
                            jsonArray1 = new JSONArray(jsonString);
                            Log.d("ResponseHADtestArray", String.valueOf(jsonArray1));

                   /*    JSONObject json = jsonArray.getJSONObject(which);
                        testid = json.getString(product_id);
                        testcategory = json.getString(category_id);
                        Log.d("ResponseHADtest", testid);
                        Log.d("ResponseHADcategory", testcategory);*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        movieList.clear();

                        int amountplus=0;
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject1 = null;
                            try {
                                jsonObject1 = jsonArray1.getJSONObject(i);

                                if(strName.equals("others"))
                                {
                                    strName="";
                                }

                                if (strName.equals(jsonObject1.getString("parent_name_2"))) {
                                    sales_register_model sales_register_model = new sales_register_model();
                                    sales_register_model.setName(jsonObject1.getString("ledger_name"));
                                    sales_register_model.setAmount(jsonObject1.getString("amount"));

                                    amountplus = amountplus+jsonObject1.getInt("amount");
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
                        totalamountgross.setText("\u20B9"+amountall);
                        adapter.notifyDataSetChanged();
                    }

               /*     Toast toast=Toast.makeText(getContext(),strName,Toast.LENGTH_SHORT);
                    toast.show();*/
                    dialog.dismiss();
                }
            });
            builder.show();

        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sharemenu = menu.findItem(R.id.share);
        MenuItem sortgrp = menu.findItem(R.id.group);
        MenuItem amount = menu.findItem(R.id.amount);

        amount.setVisible(false);
        sharemenu.setVisible(false);
        sortgrp.setVisible(false);
        return true;
    }
    private void showcalender(){
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
        Date date1 = null,date2 = null;
        try {

            date1 = sdf.parse(timeperiod1);
            date2 = sdf.parse(timeperiod2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String[] splited = sdf.format(date1).split("-");

        int  y = Integer.parseInt(splited[0]);
        int  m = Integer.parseInt(splited[1]);
        int  d = Integer.parseInt(splited[2]);

        Toast.makeText(getApplicationContext(),String.valueOf(m)+String.valueOf(d),Toast.LENGTH_LONG).show();
        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);

        datePicker.updateDate(y,m,d);

        Calendar currentDate = Calendar.getInstance();
        currentDate.set(currentDate.get(Calendar.YEAR)-1,Calendar.APRIL,1);
        datePicker.setMinDate(currentDate.getTimeInMillis());

        Calendar currentDate2 = Calendar.getInstance();
        currentDate2.set(currentDate2.get(Calendar.YEAR),Calendar.MARCH,31);
        datePicker.setMaxDate(currentDate2.getTimeInMillis());
/*
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
*/

        String[] splited2 = sdf.format(date2).split("-");

        int  y2 = Integer.parseInt(splited2[0]);
        int  m2 = Integer.parseInt(splited2[1]);
        int  d2 = Integer.parseInt(splited2[2]);

        final DatePicker datePicker2 = (DatePicker) layout.findViewById(R.id.datePicker2);
        datePicker2.updateDate(y2,m2,d2);


        datePicker2.setMinDate(currentDate.getTimeInMillis());
        datePicker2.setMaxDate(currentDate2.getTimeInMillis());

        /*datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {


            }
        });*/
        builder = new AlertDialog.Builder(PaymentReportActivity.this);

        builder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                day1 = String.valueOf(datePicker.getDayOfMonth());
                                month1 = String.valueOf(datePicker.getMonth() +1);
                                year1 = datePicker.getYear();


                                day2 = String.valueOf(datePicker2.getDayOfMonth());
                                month2 = String.valueOf(datePicker2.getMonth()+1);
                                year2 = datePicker2.getYear();



                                timeperiod1=year1+"-"+month1+"-"+day1;

                                timeperiod2=year2+"-"+month2+"-"+day2;

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = null,date2 = null;
                                try {

                                    date1 = sdf.parse(timeperiod1);
                                    date2 = sdf.parse(timeperiod2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));
                                method_payment(sdf.format(date1),sdf.format(date2));

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

    public class PaymentReportActivityAdapter extends RecyclerView.Adapter<PaymentReportActivityAdapter.ViewHolder> implements Filterable {

        private Context context;
        private List<sales_register_model> list;

        private List<sales_register_model> contactListFiltered;

        public PaymentReportActivityAdapter(Context context, List<sales_register_model> list) {
            this.context = context;
            this.list = list;
            this.contactListFiltered = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_salesregister, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final sales_register_model movie = contactListFiltered.get(position);
/*
            if(movie.getAmt_for_receipt()==0)
            {
                holder.partylayout.setVisibility(View.GONE);
            }
            else
            {*/
                holder.textTitle.setText(movie.getName());
                holder.amount.setText("\u20B9"+movie.getAmt_for_receipt());

            /*}*/




            holder.partylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context,SalesRegisterDetailPage.class);
                    intent.putExtra("partyledgername", movie.getName());
                    intent.putExtra("ledgerid","Payment");
                    intent.putExtra("date1",movie.getDate1());
                    intent.putExtra("date2",movie.getDate2());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

   /*                 SharedPreferences settings = getSharedPreferences("pref", 0);
                    String jsonString = settings
                            .getString("jsonStringPaymentforallgroup", null);


                    try {
                        jsonArray1 = new JSONArray(jsonString);
                        Log.d("ResponseHADtestArray", String.valueOf(jsonArray1));

                   *//*    JSONObject json = jsonArray.getJSONObject(which);
                        testid = json.getString(product_id);
                        testcategory = json.getString(category_id);
                        Log.d("ResponseHADtest", testid);
                        Log.d("ResponseHADcategory", testcategory);*//*

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    movieList.clear();

                    int amountplusforchild=0;
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = jsonArray1.getJSONObject(i);

                            if("Cash-in-hand".equals(jsonObject1.getString("parent_name_2"))||"Bank Accounts".equals(jsonObject1.getString("parent_name_2"))) {

                            }
else if (movie.getName().equals(jsonObject1.getString("name"))) {
                                Log.d("onResponse:ledger_name ",jsonObject1.getString("ledger_name"));
                                sales_register_model sales_register_model = new sales_register_model();
                                sales_register_model.setName(jsonObject1.getString("ledger_name"));
                                sales_register_model.setAmount(jsonObject1.getString("amount"));

                                amountplusforchild = amountplusforchild+jsonObject1.getInt("amount");
                                sales_register_model.setDate1(timeperiod1);
                                sales_register_model.setDate2(timeperiod2);
                                movieList.add(sales_register_model);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    String amountall = String.valueOf(amountplusforchild);

                    Intent intent = new Intent(context,PaymentChildActivity.class);
                    intent.putExtra("amount",amountall);
                    intent.putExtra("partyledgername",movie.getName());
                    intent.putExtra("date1",movie.getDate1());
                    intent.putExtra("date2",movie.getDate2());
                    context.startActivity(intent);
                    finish();*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return contactListFiltered.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textTitle, vouchernumber,vochurtype,amount,date;
            RelativeLayout partylayout;
            public ViewHolder(View itemView) {
                super(itemView);

                textTitle = itemView.findViewById(R.id.partyname);
                amount = itemView.findViewById(R.id.amount);


                partylayout = itemView.findViewById(R.id.partylayout);

            }
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        contactListFiltered = list;
                    } else {
                        List<sales_register_model> filteredList = new ArrayList<>();
                        for (sales_register_model row : list) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        contactListFiltered = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = contactListFiltered;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    contactListFiltered = (ArrayList<sales_register_model>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }



    }

    public void monthstring()
    {
        switch(month1) {
            case "1":
                month1="Jan";
                break;
            case "2":
                month1="Feb";
                break;
            case "3":
                month1="Mar";
                break;
            case "4":
                month1="Apr";
                break;
            case "5":
                month1="May";
                break;
            case "6":
                month1="Jun";
                break;
            case "7":
                month1="July";
                break;
            case "8":
                month1="Aug";
                break;
            case "9":
                month1="Sept";
                break;
            case "10":
                month1="Oct";
                break;
            case "11":
                month1="Nov";
                break;
            case "12":
                month1="Dec";
                break;

        }
        switch(month2) {
            case "1":
                month2="Jan";
                break;
            case "2":
                month2="Feb";
                break;
            case "3":
                month2="Mar";
                break;
            case "4":
                month2="Apr";
                break;

            case "5":
                month2="May";
                break;
            case "6":
                month2="Jun";
                break;
            case "7":
                month2="July";
                break;
            case "8":
                month2="Aug";
                break;
            case "9":
                month2="Sept";
                break;
            case "10":
                month2="Oct";
                break;
            case "11":
                month2="Nov";
                break;
            case "12":
                month2="Dec";
                break;


        }
    }
}
