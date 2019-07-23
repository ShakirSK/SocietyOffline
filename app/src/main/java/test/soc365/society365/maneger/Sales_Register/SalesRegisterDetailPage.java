package test.soc365.society365.maneger.Sales_Register;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import test.soc365.society365.BuildConfig;
import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.partyledger.partydetailmodel;
import test.soc365.society365.maneger.showpdftest_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static test.soc365.society365.helper.DatabaseHelper.amountplusdb;
import static test.soc365.society365.helper.DatabaseHelper.trailbalance;

public class SalesRegisterDetailPage extends AppCompatActivity {

    private static final String SHARED_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".myfileprovider";

    MenuItem menuItem;
    RecyclerView recyclerView;
    String periodname;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;



    private SalesRegisterDetailPageAdapter adapter;

    //to store date name in array
    ArrayList<String> voucher_typebill,voucher_typeReceipt,voucher_typeJournal ;


    TextView dateendandstart,openingbalanceamount,closingbalanceamount;
    TextView totalamountgross;

    LinearLayout closingbalance,openingbalance,calander;
    //to store amount name in array
    ArrayList<String> billamount,Receiptamount,Journalamount ;
    private List<partydetailmodel> movieList;

    int lastindexposition;

    //progress bar
    SpotsDialog spotsDialog;
    //ledger name from intent
    public static String snamesales;
    String date1,date2,showpdfstatus;
    static String ledgerid;


    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid,companyname;
    //whole body visible after data
    CoordinatorLayout wholebody;
    ImageView leftarrow,rightarrow;
    String timeperiod1,timeperiod2;

    Calendar calendartp1;

    private SearchView searchView;
    int year1,year2;
    int day1,day2;
    String month1,month2;
    AlertDialog.Builder builder;

    //pdf share
    private long enqueue;
    private DownloadManager dm;
    BroadcastReceiver receiver;
    String urlstring;
    String date_In_Email_Doc;

    // Database Helper
    DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_register_detail_page);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        //getting partyledger user details from partyadapter.class
        Intent intent = getIntent();
        snamesales = intent.getStringExtra("partyledgername");
        ledgerid = intent.getStringExtra("ledgerid");
        showpdfstatus= intent.getStringExtra("showpdfstatus");
        date1 = intent.getStringExtra("date1");
        date2 = intent.getStringExtra("date2");


       /* String[] splited = date1.split("-");

        day1 = 1;
        day2 = 31;
        month1 = "4";
        month2 = "3";
        year1 = Integer.parseInt(splited[2]);

        String[] splited2 = date2.split("-");
        year2 =Integer.parseInt(splited2[2]);

        Log.d("legdrname",splited+" "+date1+" "+year1);
*/
        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );

        companyname= sharedPreferences.getString("sname","0");




        //toolbar name
        getSupportActionBar().setTitle(snamesales);

        //total amount
        totalamountgross =(TextView)findViewById(R.id.totalamountgross);

        closingbalance = (LinearLayout)findViewById(R.id.closingbalance);
        openingbalance = (LinearLayout)findViewById(R.id.openingbalance);
        dateendandstart =(TextView)findViewById(R.id.summaryname);
        openingbalanceamount =(TextView)findViewById(R.id.openingbalanceamount);
        closingbalanceamount=(TextView)findViewById(R.id.closingbalanceamount);

        //arrows for date change
        leftarrow =(ImageView)findViewById(R.id.leftarrow);
        rightarrow =(ImageView)findViewById(R.id.rightarrow);

        calander = (LinearLayout) findViewById(R.id.calander);
        builder = new AlertDialog.Builder(this);


        //date calender and closing opening balance hide if it is not party ledger
        if(!ledgerid.equals("Bill"))
        {
            openingbalance.setVisibility(View.GONE);
            closingbalance.setVisibility(View.GONE);
            rightarrow.setVisibility(View.GONE);
            leftarrow.setVisibility(View.GONE);
        }

/*
        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesRegisterDetailPage.this,
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



        calendartp1 = Calendar.getInstance();
        //setting calender to custom date
        calendartp1.set(2018, 4, 1);


        timeperiod1=date1;

        timeperiod2=date2;

/*

        final Calendar calendartp2 = Calendar.getInstance();
        //setting calender to custom date


        calendartp2.set(Calendar.DATE, 31);
        calendartp2.set(Calendar.MONTH, Calendar.MARCH);
        calendartp2.set(Calendar.YEAR, 2019);
  */
/*      calendartp2.set(2019, 3, 31);*//*


        //tp1 date
        timeperiod1=(calendartp1.get(Calendar.DAY_OF_MONTH)) +
                "-" + calendartp1.get(Calendar.MONTH) + "-" + calendartp1.get(Calendar.YEAR);


        //tp2 date and add year by plus 1

        timeperiod2=(calendartp2.get(Calendar.DAY_OF_MONTH)) +
                "-" + calendartp2.get(Calendar.MONTH) + "-" + calendartp2.get(Calendar.YEAR);

*/

        // substract year by  1 to get actual year when we click on left right arrow

        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                year1=year1-1;
                year2=year2-1;


                timeperiod1=day1+"-"+month1+"-"+year1;


                timeperiod2=day2+"-"+month2+"-"+year2;




/*
                calendartp1.add(Calendar.YEAR, -1);
                //tp1 date
                timeperiod1=(calendartp1.get(Calendar.MONTH)) +
                        "-" + calendartp1.get(Calendar.DATE) + "-" + calendartp1.get(Calendar.YEAR);

                //tp2 date and add year by plus 1
                calendartp2.add(Calendar.YEAR, -1);
                timeperiod2=(calendartp2.get(Calendar.MONTH)) +
                        "-" + calendartp2.get(Calendar.DATE) + "-" + calendartp2.get(Calendar.YEAR);

                 Toast.makeText(getApplicationContext(),timeperiod1 +
                        "-"+timeperiod2,Toast.LENGTH_SHORT).show();*/


                dateendandstart.setText(timeperiod1+" - "+timeperiod2);
                getData();
            }
        });

        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                year1=year1+1;
                year2=year2+1;


                timeperiod1=day1+"-"+month1+"-"+year1;


                timeperiod2=day2+"-"+month2+"-"+year2;



            /*    calendartp1.add(Calendar.YEAR, 1);


                //tp1 date
                timeperiod1=(calendartp1.get(Calendar.MONTH)) +
                        "-" + calendartp1.get(Calendar.DATE) + "-" + calendartp1.get(Calendar.YEAR);

                //tp2 date and add year by plus 1
                calendartp2.add(Calendar.YEAR, 1);
                timeperiod2=(calendartp2.get(Calendar.MONTH)) +
                        "-" + calendartp2.get(Calendar.DATE) + "-" + calendartp2.get(Calendar.YEAR);
*/


                dateendandstart.setText(timeperiod1+" - "+timeperiod2);
                getData();

            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        movieList = new ArrayList<>();

        db = new DatabaseHelper(getApplicationContext());
        dateendandstart.setText(date1+" - "+date2);

        method_payment_innerpage(date1,date2);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);



        spotsDialog = new SpotsDialog(this,R.style.Custom);


     //   getData();

        closingbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(lastindexposition);
            }
        });
        openingbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerView.smoothScrollToPosition(0);

            }
        });

        wholebody = (CoordinatorLayout)findViewById(R.id.wholebody);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                wholebody.setVisibility(View.VISIBLE);
            }
        }, 5000);


        //pdf share
        urlstring = null;
        String url=null;
        try {
            url = "http://150.242.14.196:8012/society/service/party_ledger_pdfapi.php/export?filter_company_id="
                    + stallyid+"&filter_party="+URLEncoder.encode(snamesales, "UTF-8")+"&filter_party_id="+ledgerid+"&filter_type="+"Ledger"+
                    "&filter_is_party="+1+
                    "&filter_date=" +URLEncoder.encode(timeperiod1, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8")+"&year="+1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("onurlpdf",url);
        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);
                                            urlstring=jsonObject.getString("url");
                                            //      Toast.makeText(getContext(),amountTo,Toast.LENGTH_SHORT).show();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());
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


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            Uri a = Uri.parse(uriString);
                            File d = new File(a.getPath());
                            // copy file from external to internal will esaily avalible on net use google.
                            //     view.setImageURI(a);


                            if(showpdfstatus.equals("0"))
                            {
                                // showpdfstatus="1";
                                Intent share = new Intent(context,showpdftest_file.class);
                                // set flag to give temporary permission to external app to use your FileProvider
                                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri apkURI = FileProvider.getUriForFile(
                                        context,
                                        BuildConfig.APPLICATION_ID  + ".myfileprovider", d);
                                Log.d("pdfmain2", apkURI.toString());
                                share.putExtra("URL", apkURI.toString());
                                share.putExtra("urlstring",urlstring);
                                share.putExtra("sname", snamesales);
                                share.putExtra("timeperiod1", timeperiod1);
                                share.putExtra("timeperiod2", timeperiod2);
                                share.putExtra("companyname", companyname);


                                startActivity(share);
                            }

                          /*  Intent share = new Intent();
                            // set flag to give temporary permission to external app to use your FileProvider
                            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri apkURI = FileProvider.getUriForFile(
                                    context,
                                    BuildConfig.APPLICATION_ID  + ".myfileprovider", d);
                            share.setDataAndType(apkURI, "application/pdf");
                            share.setType("plain/text");
                            share.putExtra(Intent.EXTRA_STREAM, apkURI);
                            share.putExtra(Intent.EXTRA_EMAIL, " ");
                            share.putExtra(android.content.Intent.EXTRA_SUBJECT,  snamesales+" legder report for period "+timeperiod1+" to "+timeperiod2);

                            final Calendar calendartp= Calendar.getInstance();
                            //setting calender to custom date




                            date_In_Email_Doc=calendartp.get(Calendar.DAY_OF_MONTH)+"-"+calendartp.get(Calendar.MONTH)+"-"+calendartp.get(Calendar.YEAR);


                            share.putExtra(android.content.Intent.EXTRA_TEXT, "\n" +
                                    "Hello!\n" +
                                    "\n" +
                                    "Ledger report of "+snamesales+ " for period "+ timeperiod1+" to "+ timeperiod2
                                    +".Please find the attached PDF for your reference.\n"  +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "Regards,\n" +companyname+
                                    "\n" +
                                    "Date :"+date_In_Email_Doc+"\n" +
                                    "\n" +
                                    "Sent through Society365 App\n" +
                                    "http://society365.in/");
                            share.setAction(Intent.ACTION_SEND);

                            startActivity(share);*/

                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void method_payment_innerpage(String startdate,String enddate) {
        db.getPaymentDetail_Innerpage(snamesales,ledgerid,startdate,enddate);
        totalamountgross.setText("\u20B9 "+amountplusdb);
        adapter = new SalesRegisterDetailPageAdapter(getApplicationContext(),trailbalance);
        recyclerView.setAdapter(adapter);

    }
    private void getData() {
        spotsDialog.show();


        String url = null;
        try {
            if(ledgerid.equals("Payment"))
            {
                url = "http://150.242.14.196:8012/society/service/app/get_sales_details.php?ledger_name="
                        + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20")
                        + "&filter_date=" + URLEncoder.encode(timeperiod1, "UTF-8") +
                        "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8") + "&company_id=" + stallyid;

               }
            else {
                url = "http://150.242.14.196:8012/society/service/party_ledger_summery_api.php/getPartyledger_data?ledger_name=" + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20") + "&filter_date=" + URLEncoder.encode(timeperiod1, "UTF-8") +
                        "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8") + "&company_id=" + stallyid;

            }
            Log.d("onResponse:partyledger ", url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        dateendandstart.setText(timeperiod1+" - "+timeperiod2);

        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:party ", response);

                                        try {


/*
                                            timeperiod1=day1+"-"+month1+"-"+year1;


                                            timeperiod2=day2+"-"+month2+"-"+year2;


                                            dateendandstart.setText(timeperiod1+" - "+timeperiod2);*/


                                            if(!response.contains("Period"))
                                            {
                                                movieList.clear();
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_SHORT).show();
                                            }

                                            JSONObject jsonObject= new JSONObject(response);


                                            //    JSONArray jsonPeriod=jsonObject.getJSONArray("Period");

                                            periodname = jsonObject.getString("Period");

                                            //    dateendandstart.setText(periodname);
                                            openingbalanceamount.setText("\u20B9"+jsonObject.getString("opening_amount"));

                                            JSONArray jsonArray1=jsonObject.getJSONArray("tran_datas");

                                            Log.d("partyledger2 ", String.valueOf(jsonArray1));

                                            voucher_typebill = new ArrayList<>();
                                            voucher_typeReceipt = new ArrayList<>();
                                            voucher_typeJournal = new ArrayList<>();



                                            billamount = new ArrayList<>();
                                            Receiptamount = new ArrayList<>();
                                            Journalamount = new ArrayList<>();


                                            int a = jsonArray1.length();
                                            lastindexposition = a-1;
                                            JSONObject jsonObjectpb = jsonArray1.getJSONObject(lastindexposition);

                                            closingbalanceamount.setText("\u20B9"+jsonObjectpb.getString("prev_balance"));
                                            movieList.clear();

                                            int amountplus=0;
                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);


                                                if(ledgerid.equals("from_sales"))
                                                {
                                                    if("Bill".equals(jsonObject1.getString("voucher_type")))
                                                    {
                                                        Log.d("onResponse:a","A");
                                                        partydetailmodel movie = new partydetailmodel();
                                                        movie.setAmount(jsonObject1.getString("amount"));
                                                        movie.setDate(jsonObject1.getString("date"));
                                                        movie.setVoucher_number(jsonObject1.getString("voucher_number"));
                                                        movie.setVoucher_type(jsonObject1.getString("voucher_type"));
                                                        movie.setPrev_balance(jsonObject1.getString("balance_show"));
                                                        movie.setDr_cr(jsonObject1.getString("dr_cr"));

                                                        movie.setPeriod(jsonObject.getString("Period"));
                                                        amountplus = amountplus+jsonObject1.getInt("amount");
                                                        Log.d("amountplus",   jsonObject.getString("Period"));

                                                        movieList.add(movie);
                                                    }

                                                }
                                                else if(ledgerid.equals("from_receipt"))
                                                {
                                                    if("Receipt".equals(jsonObject1.getString("voucher_type")))
                                                    {
                                                        Log.d("onResponse:a","B");
                                                        partydetailmodel movie = new partydetailmodel();
                                                        movie.setAmount(jsonObject1.getString("amount"));
                                                        movie.setDate(jsonObject1.getString("date"));
                                                        movie.setVoucher_number(jsonObject1.getString("voucher_number"));
                                                        movie.setVoucher_type(jsonObject1.getString("voucher_type"));
                                                        movie.setPrev_balance(jsonObject1.getString("balance_show"));
                                                        movie.setDr_cr(jsonObject1.getString("dr_cr"));

                                                        movie.setPeriod(jsonObject.getString("Period"));

                                                        amountplus = amountplus+jsonObject1.getInt("amount");
                                                        Log.d("amountplus", String.valueOf(amountplus));


                                                        movieList.add(movie);
                                                    }
                                                }
                                                else if(ledgerid.equals("Payment"))
                                                {
                                                    if("Payment".equals(jsonObject1.getString("voucher_type")))
                                                    {
                                                        partydetailmodel movie = new partydetailmodel();
                                                        movie.setAmount(jsonObject1.getString("amount"));
                                                        movie.setDate(jsonObject1.getString("date"));
                                                        movie.setVoucher_number(jsonObject1.getString("voucher_number"));
                                                        movie.setVoucher_type(jsonObject1.getString("voucher_type"));
                                                        movie.setPrev_balance(jsonObject1.getString("balance_show"));
                                                        movie.setDr_cr(jsonObject1.getString("dr_cr"));
                                                        movie.setPeriod(jsonObject.getString("Period"));


                                                        amountplus = amountplus+jsonObject1.getInt("amount");
                                                        Log.d("amountplus", String.valueOf(amountplus));


                                                        movieList.add(movie);
                                                    }
                                                }
                                                else{
                                                    Log.d("onResponse:a","C");
                                                    partydetailmodel movie = new partydetailmodel();
                                                    movie.setAmount(jsonObject1.getString("amount"));
                                                    movie.setDate(jsonObject1.getString("date"));
                                                    movie.setVoucher_number(jsonObject1.getString("voucher_number"));
                                                    movie.setVoucher_type(jsonObject1.getString("voucher_type"));
                                                    movie.setPrev_balance(jsonObject1.getString("balance_show"));
                                                    movie.setDr_cr(jsonObject1.getString("dr_cr"));
                                                    // movie.setYear(jsonObject1.getInt("releaseYear"));
                                                    movie.setPeriod(jsonObject.getString("Period"));


                                                    amountplus = amountplus+jsonObject1.getInt("amount");
                                                    Log.d("amountplus", String.valueOf(amountplus));


                                                    movieList.add(movie);
                                                }
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            //searchbar edittext
            return true;
        }
        else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesRegisterDetailPage.this,
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



                        dateendandstart.setText(timeperiod1+" - "+timeperiod2);
                        monthstring();
                        getData();
                    }
                    else if(strName=="Last Year")
                    {

                        final Calendar calendartp= Calendar.getInstance();
                        //setting calender to custom date
                        calendartp.add(Calendar.YEAR, -1);
                        timeperiod2=day2+"-"+month2+"-"+calendartp.get(Calendar.YEAR);

                        calendartp.add(Calendar.YEAR, -1);


                        timeperiod1=day1+"-"+month1+"-"+calendartp.get(Calendar.YEAR);

                        monthstring();
                        getData();

                    }
                    Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }
            });
            builder.show();


        }

        else if (id==R.id.share)
        {
            {
                if(urlstring==null)
                {
                    Toast.makeText(getApplicationContext(),"Generating Pdf Please Wait !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showcustomedialog();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sortgrp = menu.findItem(R.id.group);
        MenuItem calander = menu.findItem(R.id.action_calender);
        MenuItem share = menu.findItem(R.id.share);
        MenuItem amount = menu.findItem(R.id.amount);

        amount.setVisible(false);

        calander.setVisible(false);
        sortgrp.setVisible(false);
        share.setVisible(false);
        return true;
    }

    public void information_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.party_info_bottom_sheet, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        //startbottomsheetsizecolor

        final TextView partyusername = (TextView) modalbottomsheet.findViewById(R.id.partyusername);
        final EditText group = (EditText) modalbottomsheet.findViewById(R.id.groupname);
        final EditText mobilenumber = (EditText) modalbottomsheet.findViewById(R.id.mobilenumber);
        final EditText Address = (EditText) modalbottomsheet.findViewById(R.id.Address);
        final EditText period = (EditText) modalbottomsheet.findViewById(R.id.period);
        EditText limit = (EditText) modalbottomsheet.findViewById(R.id.limit);
        ImageView imageView = (ImageView)modalbottomsheet.findViewById(R.id.shareicon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextUrl();
            }
        });
        imageView.setVisibility(View.GONE);


        group.setEnabled(false);
      /*  mobilenumber.setEnabled(false);*/
        Address.setEnabled(false);
        period.setEnabled(false);
        limit.setEnabled(false);

        String url = "http://150.242.14.196:8012/society/service/getledger_detailapp.php?ledger_id="+ledgerid;
        Log.d("onResponse:partyledger ", url);

        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:partyinfo", response);

                                        try {

                                            JSONArray jsonArray= new JSONArray(response);
                                            Log.d("partyinfo2 ", String.valueOf(jsonArray));

                                            //    JSONArray jsonPeriod=jsonObject.getJSONArray("Period");

                                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                                            Log.d("partyinfo2 ", String.valueOf(jsonObject));


                                            JSONObject jsonArray1=jsonObject.getJSONObject("message");
                                            Log.d("partyinfo2 ", String.valueOf(jsonArray1));

                                            group.setText(jsonArray1.getString("company_name"));
                                            mobilenumber.setText(jsonArray1.getString("mobile_number"));
                                            Address.setText(jsonArray1.getString("flat_no"));
                                            partyusername.setText(jsonArray1.getString("ledger_name"));
/*

                                            for (int i=0;i<jsonArray1.length();i++) {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);


                                                group.setText(jsonObject1.getString("company_name"));
                                                mobilenumber.setText(jsonObject1.getString("mobile_number"));
                                                Address.setText(jsonObject1.getString("flat_no"));
                                                partyusername.setText(jsonObject1.getString("ledger_name"));
                                            }*/
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

        mobilenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();

               /* Log.d("onClick: abc",mobilenumber.getText().toString());
                // Use format with "tel:" and phoneNumber created is
                // stored in u.
                Uri u = Uri.parse("tel:" + mobilenumber.getText().toString());

                // Create the intent and set the data for the
                // intent as the phone number.
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try
                {
                    startActivity(i);
                }
                catch (SecurityException s)
                {
                }
            */}
        });

        dialog.show();


        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
    }
    private void shareTextUrl() {
        String lookupKey = null;
       /* Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

        startActivity(Intent.createChooser(share, "Share link!"));*/

        // String [] PROJECTION = new String [] {  ContactsContract.Contacts.LOOKUP_KEY };
/*
        Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts.LOOKUP_KEY }, ContactsContract.Contacts._ID + " = " + contactId, null, null);
        if (cur.moveToFirst()) {
             lookupKey = cur.getString(0);
        }
        lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));


        Uri vcardUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ContactsContract.Contacts.CONTENT_VCARD_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, vcardUri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bob Dylan"); // put the name of the contact here
        startActivity(intent);*/
    }

    @TargetApi(Build.VERSION_CODES.O)
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


        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
/*
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
*/

        final DatePicker datePicker2 = (DatePicker) layout.findViewById(R.id.datePicker2);
        /*datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {


            }
        });*/
        builder = new AlertDialog.Builder(SalesRegisterDetailPage.this);

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

                                monthstring();

                                timeperiod1=day1+"-"+month1+"-"+year1;


                                timeperiod2=day2+"-"+month2+"-"+year2;


                                dateendandstart.setText(timeperiod1+" - "+timeperiod2);
                                getData();

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

    private void showcustomedialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.customdialog_main, viewGroup, false);

        ImageView imageView = (ImageView) dialogView.findViewById(R.id.pdffile);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Please Wait",Toast.LENGTH_SHORT).show();
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
               /* String DownloadUrl = "http://150.242.14.196:8012//society//system//storage//download//partyreport//Nitin%20D.%20Shigwan_11-02-2019.pdf";
                String sdsds = "http:\\/\\/society365.in\\/sms\\/images\\/Profile.png";
              */  DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(urlstring)).setDestinationInExternalPublicDir("/Society365", snamesales+" - "+timeperiod2+".pdf");

                request.setDescription(urlstring);   //appears the same in Notification bar while downloading

                enqueue = dm.enqueue(request);
            }
        });
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
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
