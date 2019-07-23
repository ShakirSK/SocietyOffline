package test.soc365.society365.maneger.Daybook;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
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
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import test.soc365.society365.helper.DatabaseHelper;

import static test.soc365.society365.helper.DatabaseHelper.daybookdetail;
import static test.soc365.society365.helper.DatabaseHelper.openingbalanceA;

public class DayBookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private SearchView searchView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private DayBookActivityAdapter adapter;

    private List<daybookmodel> movieList;

    String stallyid;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String timeperiod1,timeperiod2;

    Calendar calendartp1,calendartp2;

    TextView summaryname;

    //progress bar
    SpotsDialog spotsDialog;

    int year1,year2;
    String day1,day2;
    String month1,month2;

    TextView dateendandstart;

    LinearLayout calander;
    AlertDialog.Builder builder;
    // Database Helper
    DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_book);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("DayBook");
        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        dateendandstart =(TextView)findViewById(R.id.summaryname);


        day1 = "01";
        day2 = "31";
        month1 = "04";
        month2 = "03";
        year1 =2018;

        year2 =2019;


        monthstring();
        timeperiod1=year1+"-"+month1+"-"+day1;

        timeperiod2=year2+"-"+month2+"-"+day2;

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        movieList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);

       // adapter = new DayBookActivityAdapter(getApplicationContext(),daybookdetail);

        db = new DatabaseHelper(getApplicationContext());

        method_daybook(timeperiod1,timeperiod2);

        //  stallyid = sharedPreferences.getString("stallyid","0" );




        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);



        spotsDialog = new SpotsDialog(this,R.style.Custom);

//calander to choose custom date
        calander = (LinearLayout) findViewById(R.id.calander);
        builder = new AlertDialog.Builder(this);



      //  getData();




    }
    private void getData() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        spotsDialog.show();

        String name = "Kamat Pratap Waman";

/*


        calendartp1 = Calendar.getInstance();
        //setting calender to custom date
        calendartp1.set(2018, 1, 1);

        //tp1 date
        timeperiod1=(calendartp1.get(Calendar.MONTH)) +
                "-" + calendartp1.get(Calendar.DATE) + "-" + calendartp1.get(Calendar.YEAR);


        calendartp2 = Calendar.getInstance();
        //setting calender to custom date
        calendartp2.set(2019, 31, 1);


        //tp2 date and add year by plus 1

        timeperiod2=(calendartp2.get(Calendar.MONTH)) +
                "-" + calendartp2.get(Calendar.DATE) + "-" + calendartp2.get(Calendar.YEAR);*/




        summaryname =(TextView)findViewById(R.id.summaryname);
        summaryname.setText(timeperiod1+" - "+timeperiod2);
        String url = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/get_daybook.php?" +
                    "filter_date="+URLEncoder.encode(timeperiod1, "UTF-8")+
                    "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                    +"&company_id="+stallyid;
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





                                            movieList.clear();

                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                daybookmodel daybookmodel = new daybookmodel();
                                                daybookmodel.setPartyname(jsonObject1.getString("party_name"));
                                                daybookmodel.setDate(jsonObject1.getString("date"));
                                                daybookmodel.setVouchernumber(jsonObject1.getString("voucher_number"));
                                                daybookmodel.setVouchertype(jsonObject1.getString("voucher_type"));
                                                daybookmodel.setAmount(jsonObject1.getString("amount"));
                                                // movie.setYear(jsonObject1.getInt("releaseYear"));


                                                daybookmodel.setDate1(timeperiod1);
                                                daybookmodel.setDate2(timeperiod2);

                                                movieList.add(daybookmodel);

                                            }
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

    public void method_daybook(String startdate,String enddate) {

        db.getdaybookdetail(startdate,enddate);
        adapter = new DayBookActivityAdapter(getApplicationContext(),openingbalanceA);
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
        else  if (id == R.id.search) {
            //searchbar edittext
            return true;
        }
        else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DayBookActivity.this,
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
                        method_daybook(sdf.format(date1),sdf.format(date2));
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
                        method_daybook(sdf.format(date1),sdf.format(date2));


                    }
                    Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                    toast.show();
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
        builder = new AlertDialog.Builder(DayBookActivity.this);

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
                                method_daybook(sdf.format(date1),sdf.format(date2));

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
