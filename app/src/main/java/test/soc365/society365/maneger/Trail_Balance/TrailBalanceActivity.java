package test.soc365.society365.maneger.Trail_Balance;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.partyledger.partydetailmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static test.soc365.society365.helper.DatabaseHelper.trailbalance;

public class TrailBalanceActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    String periodname;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;



    private TrailBalanceActivityAdapter adapter;

    //to store date name in array
    ArrayList<String> voucher_typebill,voucher_typeReceipt,voucher_typeJournal ;


    TextView dateendandstart,openingbalanceamount,closingbalanceamount;
    TextView totalamountgross;


    private List<partydetailmodel> movieList;


    //progress bar
    SpotsDialog spotsDialog;
    //ledger name from intent
    public  String type;
    String ledgerid,date1,date2;

    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid;
    //whole body visible after data
    CoordinatorLayout wholebody;
    ImageView leftarrow,rightarrow;
    TextView summaryname;
    String timeperiod1,timeperiod2;

    Calendar calendartp1;

    private SearchView searchView;
    String year1,year2;
    int day1,day2;
    String month1,month2;
    AlertDialog.Builder builder;
    // Database Helper
    DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_balance);
        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );


     builder = new AlertDialog.Builder(this);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        db = new DatabaseHelper(getApplicationContext());

        summaryname = (TextView)findViewById(R.id.summaryname);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);


        movieList = new ArrayList<>();
     //   adapter = new TrailBalanceActivityAdapter(getApplicationContext(),trailbalance);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        year1="2018";
        summaryname.setText(year1);
        getdatasqlite(year1);

     /*   if(type.equals("ledger")) {
            db.getdataforTrailBalance(year1);
            getSupportActionBar().setTitle("Trial Balance Ledger");
            adapter = new TrailBalanceActivityAdapter(getApplicationContext(),trailbalance);

        }
        else
        {
            db.getdataforAccountBalance(year1);
            getSupportActionBar().setTitle("Trial Balance Accounts");
            adapter = new TrailBalanceActivityAdapter(getApplicationContext(),trailbalance);

        }





        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        */





        spotsDialog = new SpotsDialog(this,R.style.Custom);




        //getData();

    }

    public void  getdatasqlite(String year)
    {
        if(type.equals("ledger")) {

            summaryname.setText(year);
            db.getdataforTrailBalance(year);
            getSupportActionBar().setTitle("Trial Balance Ledger");
            adapter = new TrailBalanceActivityAdapter(getApplicationContext(),trailbalance);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter);
        }
        else
        {

            summaryname.setText(year);
            db.getdataforAccountBalance(year);
            getSupportActionBar().setTitle("Trial Balance Accounts");
            adapter = new TrailBalanceActivityAdapter(getApplicationContext(),trailbalance);


            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(adapter);
        }
    }



    private void getData() {
        spotsDialog.show();


        String url = null;
        try {
            if(type.equals("ledger"))
            {
                url = "http://150.242.14.196:8012/society/service/app/get_trailbalanceledger.php?filter_year="
                        + URLEncoder.encode(year1, "UTF-8") + "&filter_company_id=" + stallyid+"&filter_party";

            }
            else
            {
                url = "http://150.242.14.196:8012/society/service/app/get_trailbalanceaccounts.php?filter_year="
                        + URLEncoder.encode(year1, "UTF-8") + "&filter_company_id=" + stallyid+"&filter_party";

            }


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

                                            summaryname.setText(year1);

/*
                                            timeperiod1=day1+"-"+month1+"-"+year1;


                                            timeperiod2=day2+"-"+month2+"-"+year2;


                                            dateendandstart.setText(timeperiod1+" - "+timeperiod2);*/




                                            JSONObject jsonObject= new JSONObject(response);

                                            JSONArray jsonArray1=jsonObject.getJSONArray("records");


                                             movieList.clear();

                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONArray jsonArray = jsonArray1.getJSONArray(i);
                                                for (int j=0;j<jsonArray.length();j++) {
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                                    partydetailmodel movie = new partydetailmodel();
                                                    movie.setAmount(jsonObject1.getString("opening_amount"));
                                                    movie.setDate(jsonObject1.getString("debit_amount"));
                                                    if(type.equals("ledger")) {
                                                        movie.setVoucher_number(jsonObject1.getString("ledger_name"));
                                                    }
                                                    else
                                                    {
                                                        movie.setVoucher_number(jsonObject1.getString("account_name"));
                                                    }
                                                    movie.setVoucher_type(jsonObject1.getString("credit_amount"));
                                                    // movie.setPrev_balance(jsonObject1.getString("balance_show"));
                                                    movie.setDr_cr(jsonObject1.getString("closing_amount"));
                                                    // movie.setYear(jsonObject1.getInt("releaseYear"));


                                                   /* amountplus = amountplus+jsonObject1.getInt("amount");
                                                    Log.d("amountplus", String.valueOf(amountplus));
*/

                                                    movieList.add(movie);
/*

                                                    if("Bill".equals(jsonObject1.getString("voucher_type")))
                                                    {
                                                        voucher_typebill.add(jsonObject1.getString("date"));
                                                        billamount.add(jsonObject1.getString("amount"));
                                                    }
                                                    else if("Receipt".equals(jsonObject1.getString("voucher_type")))
                                                    {
                                                        voucher_typeReceipt.add(jsonObject1.getString("date"));
                                                        Receiptamount.add(jsonObject1.getString("amount"));
                                                    }
                                                    else if("Journal".equals(jsonObject1.getString("voucher_type")))
                                                    {
                                                        voucher_typeJournal.add(jsonObject1.getString("date"));
                                                        Journalamount.add(jsonObject1.getString("amount"));
                                                    }

                                                    Log.d("partyledger3 ",voucher_typebill +"\n"+
                                                    voucher_typeReceipt +"\n"+
                                                    voucher_typeJournal );
*/

                                                }
                                            }
                                          /*  String amountall = String.valueOf(amountplus);
                                            totalamountgross.setText("\u20B9"+amountall);
*/
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


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TrailBalanceActivity.this,
                    android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("This Year");
            arrayAdapter.add("Last Year");/*
            arrayAdapter.add("Custom Date");*/

            builder.setTitle("Select Date")
                    .setCancelable(true);



            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                  /*  if(strName=="Custom Date"){
                    //    showcalender();
                    }
                    else */if(strName=="This Year")
                    {
                       adapter.notifyDataSetChanged();
                        trailbalance.clear();
                        getdatasqlite("2019");

                    //    getData();
                    }
                    else if(strName=="Last Year")
                    {

                       adapter.notifyDataSetChanged();
                        trailbalance.clear();
                        getdatasqlite("2018");
                  /*
                        int df=Integer.parseInt(year1)-1;
                        year1= String.valueOf(df);
*/
                      //  getData();

                    }

                    dialog.dismiss();
                }
            });
            builder.show();


        }



        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.share);
        MenuItem group = menu.findItem(R.id.group);

        group.setVisible(false);
        register.setVisible(false);

        return true;
    }
  /*  private void showcalender(){
     *//*   ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_date, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*//*

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


        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
*//*
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
*//*

        final DatePicker datePicker2 = (DatePicker) layout.findViewById(R.id.datePicker2);
        *//*datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {


            }
        });*//*
        builder = new AlertDialog.Builder(TrailBalanceActivity.this);

        builder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                day1 = datePicker.getDayOfMonth();
                                month1 = String.valueOf(datePicker.getMonth() + 1);
                                year1 = datePicker.getYear();


                                day2 = datePicker2.getDayOfMonth();
                                month2 = String.valueOf(datePicker2.getMonth() + 1);
                                year2 = datePicker2.getYear();


                                timeperiod1=day1+"-"+month1+"-"+year1;


                                timeperiod2=day2+"-"+month2+"-"+year2;


                                getData();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
       *//* builder.setTitle("Dialog with tabs");*//*
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();

    }
*/}
