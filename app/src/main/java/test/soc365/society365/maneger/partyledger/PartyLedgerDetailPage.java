package test.soc365.society365.maneger.partyledger;

import android.Manifest;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import test.soc365.society365.BuildConfig;
import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.CheckNetwork;
import test.soc365.society365.maneger.MarshMallowPermission;
import test.soc365.society365.maneger.Sales_Register.sales_register_model;
import test.soc365.society365.maneger.showpdftest_file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static test.soc365.society365.helper.DatabaseHelper.trailbalance;
import static test.soc365.society365.maneger.partyledger.PartyLedgerDetailPageAdapter.cb;

public class PartyLedgerDetailPage extends AppCompatActivity {


    private static final String SHARED_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".myfileprovider";

    MenuItem menuItem;
    RecyclerView recyclerView;
    String periodname;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;



    private PartyLedgerDetailPageAdapter adapter;

    //to store date name in array
    ArrayList<String> voucher_typebill,voucher_typeReceipt,voucher_typeJournal ;


    TextView dateendandstart,openingbalanceamount,closingbalanceamount;

    LinearLayout closingbalance,openingbalance,calander;
    //to store amount name in array
    ArrayList<String> billamount,Receiptamount,Journalamount ;
    private List<partydetailmodel> movieList;

    int lastindexposition;

    //progress bar
    SpotsDialog spotsDialog,spotsDialog2;
    //ledger name from intent
    static String sname,isparty;
    String ledgerid,showpdfstatus,snamefor_OB,flatno;

    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Gson gson;

    String stallyid,companyname;
    //whole body visible after data
    CoordinatorLayout wholebody;
    ImageView leftarrow,rightarrow;
    String timeperiod1,timeperiod2,defaultdate;

    Calendar calendartp1;

    private SearchView searchView;
    int year1,year2;
    String day1,day2;
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
    static String openbalance;
    int last_closing_amount;
    List<partydetailmodel> trailbal;
    AlertDialog alertDialog;

    RadioGroup radioGroupPreference ;
    RadioButton radioButton_equalto ;
    RadioButton radioButton_greaterthan;
    String radiocheckbutton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_ledger_detail_page);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        db = new DatabaseHelper(getApplicationContext());

        //getting partyledger user details from partyadapter.class
        Intent intent = getIntent();
        sname = intent.getStringExtra("partyledgername");
        flatno = intent.getStringExtra("flatno");
        ledgerid = intent.getStringExtra("ledgerid");
        showpdfstatus= intent.getStringExtra("showpdfstatus");
        isparty=intent.getStringExtra("isparty");

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        stallyid = sharedPreferences.getString("stallyid","0" );
        companyname= sharedPreferences.getString("sname","0");


        String  stringar = sharedPreferences.getString("Split_ledger_with_flatno",null );
        Type type = new TypeToken<List<sales_register_model>>(){}.getType();
        List<sales_register_model> Split_ledger_with_flatno = gson.fromJson(stringar, type);

        snamefor_OB=sname;
        for(sales_register_model todo:Split_ledger_with_flatno)
        {
           // Log.d( "Viewsplit: ",todo.getLed_name()+" "+todo.getFlatno());
            if(todo.getName().contains(sname)&&todo.getFlatno().contains(flatno))
            {
                Log.d( "Viewsplit: ",todo.getLed_name()+" "+todo.getFlatno()+" "+sname+" "+flatno);
                Toast.makeText(getApplicationContext(),todo.getLed_name()+" "+todo.getFlatno()+" "+sname+" "+flatno,Toast.LENGTH_LONG).show();
                sname=todo.getName();
                snamefor_OB=todo.getFlatno();
            }
        }


        //toolbar name
        getSupportActionBar().setTitle(sname);


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



        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PartyLedgerDetailPage.this,
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

                            timeperiod1 = sdf.format(date1);
                            timeperiod2 = sdf.format(date2);
                            dateendandstart.setText(timeperiod1+" - "+timeperiod2);

                            party_ledger_arrayDate("preference","amountsort",timeperiod1,timeperiod2);

                         /*   dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));
                            party_ledger_arrayDate("preference","amountsort",sdf.format(date1),sdf.format(date2));*/

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

                            timeperiod1 = sdf.format(date1);
                            timeperiod2 = sdf.format(date2);
                            dateendandstart.setText(timeperiod1+" - "+timeperiod2);

                            party_ledger_arrayDate("preference","amountsort",timeperiod1,timeperiod2);


                        }
                        Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                        toast.show();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });



        calendartp1 = Calendar.getInstance();
        //setting calender to custom date
        calendartp1.set(2018, 4, 1);


        day1 = "01";
        day2 = "31";
        month1 = "04";
        month2 = "03";
        year1 =2018;

        year2 =2019;


        timeperiod2=year2+"-"+month2+"-"+day2;

//                        calendartp.add(Calendar.YEAR, -1);
        timeperiod1=year1+"-"+month1+"-"+day1;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null,date2 = null;
        try {

            date1 = sdf.parse(timeperiod1);
            date2 = sdf.parse(timeperiod2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeperiod1 = sdf.format(date1);
        timeperiod2 = sdf.format(date2);

        //monthstring();

        defaultdate=timeperiod1;

        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                year1=year1-1;
                year2=year2-1;


                timeperiod1=day1+"-"+month1+"-"+year1;


                timeperiod2=day2+"-"+month2+"-"+year2;

                openingbalanceamount.setText("\u20B9"+"0");

                closingbalanceamount.setText("\u20B9"+"0");




               // monthstring();
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



                openingbalanceamount.setText("\u20B9"+"0");

                closingbalanceamount.setText("\u20B9"+"0");

            /*    calendartp1.add(Calendar.YEAR, 1);


                //tp1 date
                timeperiod1=(calendartp1.get(Calendar.MONTH)) +
                        "-" + calendartp1.get(Calendar.DATE) + "-" + calendartp1.get(Calendar.YEAR);

                //tp2 date and add year by plus 1
                calendartp2.add(Calendar.YEAR, 1);
                timeperiod2=(calendartp2.get(Calendar.MONTH)) +
                        "-" + calendartp2.get(Calendar.DATE) + "-" + calendartp2.get(Calendar.YEAR);
*/
                //monthstring();
                getData();

            }
        });


        trailbal = new ArrayList<partydetailmodel>();

        adapter = new PartyLedgerDetailPageAdapter(getApplicationContext(),trailbalance,trailbal);


        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        movieList = new ArrayList<>();

        adapter = new PartyLedgerDetailPageAdapter(getApplicationContext(),trailbalance,trailbal);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);


        party_ledger_arrayDate("preference","amountsort",timeperiod1,timeperiod2);

       // partydetailmodel partydetailmodel = new partydetailmodel();





        spotsDialog = new SpotsDialog(this,R.style.Custom);
        spotsDialog2 = new SpotsDialog(this,R.style.Custompdf);

        //monthstring();
        //getData();

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

        urlstring =null;
        String url=null;
        try {
            url = "http://150.242.14.196:8012/society/service/party_ledger_pdfapi.php/export?filter_company_id="
                    + stallyid+"&filter_party="+URLEncoder.encode(sname, "UTF-8")+"&filter_party_id="+ledgerid+"&filter_type="+"Ledger"+
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
                                share.putExtra("sname", sname);
                                share.putExtra("timeperiod1", timeperiod1);
                                share.putExtra("timeperiod2", timeperiod2);
                                share.putExtra("companyname", companyname);


                                startActivity(share);
                            }
                         /*   else {


                                Intent share = new Intent();
                                // set flag to give temporary permission to external app to use your FileProvider
                                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri apkURI = FileProvider.getUriForFile(
                                        context,
                                        BuildConfig.APPLICATION_ID + ".myfileprovider", d);
                                share.setDataAndType(apkURI, "application/pdf");
                                share.setType("plain/text");
                                share.putExtra(Intent.EXTRA_STREAM, apkURI);
                                share.putExtra(Intent.EXTRA_EMAIL, " ");
                                share.putExtra(android.content.Intent.EXTRA_SUBJECT, sname + " legder report for period " + timeperiod1 + " to " + timeperiod2);

                                final Calendar calendartp = Calendar.getInstance();
                                //setting calender to custom date


                                date_In_Email_Doc = calendartp.get(Calendar.DAY_OF_MONTH) + "-" + calendartp.get(Calendar.MONTH) + "-" + calendartp.get(Calendar.YEAR);


                                share.putExtra(android.content.Intent.EXTRA_TEXT, "\n" +
                                        "Hello!\n" +
                                        "\n" +
                                        "Ledger report of " + sname + " for period " + timeperiod1 + " to " + timeperiod2
                                        + ".Please find the attached PDF for your reference.\n" +
                                        "\n" +
                                        "\n" +
                                        "\n" +
                                        "Regards,\n" + companyname +
                                        "\n" +
                                        "Date :" + date_In_Email_Doc + "\n" +
                                        "\n" +
                                        "Sent through Society365 App\n" +
                                        "http://society365.in/");
                                share.setAction(Intent.ACTION_SEND);

                                startActivity(share);
                            }
*/
                        }
                    }
                }
            }
        };

        Intent intent1 = registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        closingbalanceamount.setText("\u20B9"+cb);

    }

    private void getData() {
        spotsDialog.show();


         String url = null;
        try {
            if(ledgerid.equals("from_sales"))
            {
                url = "http://150.242.14.196:8012/society/service/app/get_sales_details.php?ledger_name="
                        + URLEncoder.encode(sname, "UTF-8").replaceAll("\\+", "%20")
                        + "&filter_date=" + URLEncoder.encode(timeperiod1, "UTF-8") +
                        "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8") + "&company_id=" + stallyid;

            }
            else {
                url = "http://150.242.14.196:8012/society/service/party_ledger_summery_api.php/getPartyledger_data?ledger_name=" + URLEncoder.encode(sname, "UTF-8").replaceAll("\\+", "%20") + "&filter_date=" + URLEncoder.encode(timeperiod1, "UTF-8") +
                        "&filter_date_end=" + URLEncoder.encode(timeperiod2, "UTF-8") + "&company_id=" + stallyid;
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



                                        /*    timeperiod1=day1+"-"+month1+"-"+year1;


                                            timeperiod2=day2+"-"+month2+"-"+year2;

*/
                                            dateendandstart.setText(timeperiod1+" - "+timeperiod2);


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
                                            //openingbalanceamount.setText("\u20B9"+jsonObject.getString("opening_amount"));

                                            JSONArray jsonArray1=jsonObject.getJSONArray("tran_datas");

                                                Log.d("partyledger2 ", String.valueOf(jsonArray1));

                                           voucher_typebill = new ArrayList<>();
                                            voucher_typeReceipt = new ArrayList<>();
                                            voucher_typeJournal = new ArrayList<>();



                                            billamount = new ArrayList<>();
                                            Receiptamount = new ArrayList<>();
                                            Journalamount = new ArrayList<>();


                                           /* int a = jsonArray1.length();
                                             lastindexposition = a-1;
                                             lastindexposition = lastindexposition+1;
                                            JSONObject jsonObjectpb = jsonArray1.getJSONObject(lastindexposition);

                                                closingbalanceamount.setText("\u20B9"+jsonObjectpb.getString("prev_balance"));*/
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
                                                            movie.setNarration(jsonObject1.getString("narration"));

                                                            movie.setPeriod(jsonObject.getString("Period"));
                                                            amountplus = amountplus+jsonObject1.getInt("amount");
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

                                                            movieList.add(movie);
                                                        }
                                                    }
                                                        else if(ledgerid.equals("from_payment"))
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

                                                                movieList.add(movie);
                                                            }
                                                        }
                                                    else{
                                                        partydetailmodel movie = new partydetailmodel();
                                                        movie.setAmount(jsonObject1.getString("amount"));
                                                        movie.setDate(jsonObject1.getString("date"));
                                                        movie.setVoucher_number(jsonObject1.getString("voucher_number"));
                                                        movie.setVoucher_type(jsonObject1.getString("voucher_type"));
                                                        movie.setPrev_balance(jsonObject1.getString("balance_show"));
                                                        movie.setDr_cr(jsonObject1.getString("dr_cr"));
                                                        movie.setNarration(jsonObject1.getString("narration"));
                                                        movie.setPeriod(jsonObject.getString("Period"));
                                                        amountplus = amountplus+jsonObject1.getInt("amount");

                                                        // movie.setYear(jsonObject1.getInt("releaseYear"));

                                                        movieList.add(movie);
                                                    }


                                                    int a = jsonArray1.length();
                                                    lastindexposition = a-1;
                                                    if(i==lastindexposition)
                                                    {
                                                        JSONObject jsonObjectpb = jsonArray1.getJSONObject(i);

                                          //              closingbalanceamount.setText("\u20B9"+jsonObjectpb.getString("balance_show"));

                                                    }
                                                   // lastindexposition = lastindexposition+1;

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

          getMenuInflater().inflate(R.menu.dashboardmenu, menu);
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
        else if (id == R.id.profile) {
            //info BS
         information_bottomsheet();

        }
        else if(id==R.id.amount)
      {
          show_amountdialog();
      }
        else if (id==R.id.share)
      {
          if(CheckNetwork.isInternetAvailable(PartyLedgerDetailPage.this)) //returns true if internet available
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
          else {
              ViewGroup viewGroup = findViewById(android.R.id.content);

              //then we will inflate the custom alert dialog xml that we created
              View dialogView = LayoutInflater.from(this).inflate(R.layout.customdialog_main, viewGroup, false);
              int SHARE_PERMISSION_CODE = 223;

              ImageView imageView = (ImageView) dialogView.findViewById(R.id.pdffile);
              imageView.setVisibility(View.GONE);
              TextView textView = (TextView)dialogView.findViewById(R.id.info);

              textView.setText("No Internet Connection Please Connect To Internet TO Generate PDF");



              //Now we need an AlertDialog.Builder object
              AlertDialog.Builder builder = new AlertDialog.Builder(this);

              //setting the view of the builder to our custom view that we already inflated
              builder.setView(dialogView);

              //finally creating the alert dialog and displaying it
              AlertDialog alertDialog = builder.create();
              alertDialog.show();


          }

      }
        return super.onOptionsItemSelected(item);
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


        List<partydetailmodel> allToDos =     db.getinfo_ledgername(sname);
        for (partydetailmodel todo : allToDos) {

            Log.d("ToDogetAmount", todo.getCompanyname());
            group.setText( todo.getCompanyname());
            mobilenumber.setText( todo.getMobilenumber());
            Address.setText( todo.getFlatno());
            partyusername.setText( todo.getLedger_name());


        }


     /*   String url = "http://150.242.14.196:8012/society/service/getledger_detailapp.php?ledger_id="+ledgerid;
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
*//*

                                            for (int i=0;i<jsonArray1.length();i++) {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);


                                                group.setText(jsonObject1.getString("company_name"));
                                                mobilenumber.setText(jsonObject1.getString("mobile_number"));
                                                Address.setText(jsonObject1.getString("flat_no"));
                                                partyusername.setText(jsonObject1.getString("ledger_name"));
                                            }*//*
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
        requestQueue.add(jsonArrayRequest);*/

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
        builder = new AlertDialog.Builder(PartyLedgerDetailPage.this);

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

                                timeperiod1 = sdf.format(date1);
                                timeperiod2 = sdf.format(date2);
                                dateendandstart.setText(timeperiod1+" - "+timeperiod2);

                                party_ledger_arrayDate("preference","amountsort",timeperiod1,timeperiod2);

                               /* dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));
                                party_ledger_arrayDate("preference","amountsort",sdf.format(date1),sdf.format(date2));*/


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
        int SHARE_PERMISSION_CODE = 223;

        if (MarshMallowPermission.checkMashMallowPermissions(this, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                SHARE_PERMISSION_CODE)) {
        }
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.pdffile);
        TextView textView = (TextView)dialogView.findViewById(R.id.info);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Please Wait",Toast.LENGTH_SHORT).show();
                        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        String DownloadUrl = "http://150.242.14.196:8012//society//system//storage//download//partyreport//Nitin%20D.%20Shigwan_11-02-2019.pdf";
                        String sdsds = "http:\\/\\/society365.in\\/sms\\/images\\/Profile.png";
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(urlstring)).setDestinationInExternalPublicDir("/Society365", sname+" - "+timeperiod2+".pdf");

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
               party_ledger_arrayDate(radiocheckbutton,edit,timeperiod1,timeperiod2);
                alertDialog.dismiss();
            }
        });
    }
  /*  public void downloadFile(){
        String DownloadUrl = "http://150.242.14.196:8012//society//system//storage//download//partyreport//Nitin%20D.%20Shigwan_11-02-2019.pdf";
        DownloadManager.Request request = new DownloadManager.Request(parse(DownloadUrl));
        request.setDescription("http://150.242.14.196:8012//society//system//storage//download//partyreport//Nitin%20D.%20Shigwan_11-02-2019.pdf");   //appears the same in Notification bar while downloading
        request.setTitle("Sample.pdf");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalFilesDir(getApplicationContext(),null, "sample.pdf");
        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }*/

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

    //two methods get data respective to thier dates

    public void party_ledger_arrayDate(String preference,String amountsort,String startdate,String enddate)
    {
        /*
            int amountplus = db.getCashBank_OB(cashbook);

            amountplus = (-amountplus);
*/
            int ob =  check_date(preference,amountsort,defaultdate,startdate,enddate);
            Toast.makeText(getApplicationContext(),isparty+" "+sname+" "+startdate+" "+enddate,Toast.LENGTH_LONG).show();

            openingbalanceamount.setText("\u20B9" + ob);



        List<partydetailmodel> allToDoss =    db.getdataforpartywisesummarydata(preference,amountsort,isparty,sname,startdate,enddate);
       trailbal = new ArrayList<partydetailmodel>();


        for (partydetailmodel todo : allToDoss) {
            partydetailmodel td = new partydetailmodel();

            if(todo.getVoucher_type().equals("Bill"))
            {
                Float a = Float.parseFloat(todo.getAmount());
                Float sd = ob + a;

                ob=Math.round(sd);

                td.setPrev_balance(String.valueOf(sd));

           /*     holder.preamount.setText("("+"\u20B9 "+sd+")");
                holder.preamount.setTextColor(Color.parseColor("#42C0FB"));
           */     cb=Math.round(sd);

            }
            else if((todo.getVoucher_type().equals("Journal"))||(todo.getVoucher_type().equals("Payment"))||(todo.getVoucher_type().equals("Contra")))
            {

                if(todo.getDr_cr().equals("Credit"))
                {
                   /* int a = Integer.parseInt(todo.getAmount());
                    int sd = ob - a;

                    ob=sd;

                    td.setPrev_balance(String.valueOf(sd));

           *//*     holder.preamount.setText("("+"\u20B9 "+sd+")");
                holder.preamount.setTextColor(Color.parseColor("#42C0FB"));
           *//*     cb=sd;*/


                    Float a = Float.parseFloat(todo.getAmount());
                    Float sd = ob - a;

                    ob =Math.round(sd);
                    td.setPrev_balance(String.valueOf(sd));
                    cb=Math.round(sd);
                }
                else
                {

                    Float a = Float.parseFloat(todo.getAmount());
                    Float sd = ob + a;

                   ob =Math.round(sd);
                    td.setPrev_balance(String.valueOf(sd));
                    cb=Math.round(sd);

                }
            }
            else if((todo.getVoucher_type().equals("Receipt")))
            {
                if(todo.getDr_cr().equals("Credit")) {
                    Float a = Float.parseFloat(todo.getAmount());
                    Float sd = ob - a;

                    ob = Math.round(sd);
                    td.setPrev_balance(String.valueOf(sd));
                    cb = Math.round(sd);
                }else
                {
                    Float a = Float.parseFloat(todo.getAmount());
                    Float sd = ob + a;

                    ob = Math.round(sd);
                    td.setPrev_balance(String.valueOf(sd));
                    cb = Math.round(sd);
                }
            }
            trailbal.add(td);
          /*  openingbalanceamount.setText("\u20B9"+todo.getAmount());
            openbalance=todo.getAmount();
       */ }

      //  db.getdataforpartywisesummarydata(preference,amountsort,sname,startdate,enddate);

            closingbalanceamount.setText("\u20B9"+cb);
            adapter.notifyDataSetChanged();
        adapter = new PartyLedgerDetailPageAdapter(getApplicationContext(),trailbalance,trailbal);
        recyclerView.setAdapter(adapter);


    }

    public Integer check_date(String preference,String amountsort,String defaultdate,String startdate,String enddate)
    {
        try {
            //Toast.makeText(getApplicationContext(),startdate +" - "+enddate,Toast.LENGTH_SHORT).show();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(defaultdate);
            Date date2 = sdf.parse(startdate);
            Date date3 = sdf.parse(enddate);
            //Toast.makeText(getApplicationContext(),sdf.format(date2),Toast.LENGTH_SHORT).show();


            if(date2.after(date1)){
                Toast.makeText(getApplicationContext()," if",Toast.LENGTH_SHORT).show();

                int ob =  db.getOpeningBalance(snamefor_OB);

                ob=(-ob);
                List<partydetailmodel> allToDoss =    db.CheckDate_partywisesummarydata(preference,amountsort,isparty,sname,startdate);
                for (partydetailmodel todo : allToDoss) {

                    if (todo.getVoucher_type().equals("Bill")) {
                        Float a = Float.parseFloat(todo.getAmount());
                        Float sd = ob + a;

                        ob = Math.round(sd);
                        cb = Math.round(sd);

                    } else if ((todo.getVoucher_type().equals("Journal"))||(todo.getVoucher_type().equals("Payment"))||(todo.getVoucher_type().equals("Contra"))) {

                        if (todo.getDr_cr().equals("Credit")) {
                           /* int a = Integer.parseInt(todo.getAmount());
                            int sd = ob - a;

                            ob = sd;
                            cb = sd;*/
                            Float a = Float.parseFloat(todo.getAmount());
                            Float sd = ob - a;

                            ob = Math.round(sd);
                            cb = Math.round(sd);
                        } else {
                            Float a = Float.parseFloat(todo.getAmount());
                            Float sd = ob + a;

                            ob = Math.round(sd);
                            cb = Math.round(sd);

                        }
                    } else if ((todo.getVoucher_type().equals("Receipt"))) {
                        if (todo.getDr_cr().equals("Credit")) {

                            Float a = Float.parseFloat(todo.getAmount());
                        Float sd = ob - a;

                        ob = Math.round(sd);
                        cb = Math.round(sd);
                        }
                        else
                        {
                            Float a = Float.parseFloat(todo.getAmount());
                            Float sd = ob + a;

                            ob = Math.round(sd);
                            cb = Math.round(sd);
                        }
                    }
                }

                last_closing_amount = cb;

            }
            else if(date2.equals(date1)){

                last_closing_amount =  db.getOpeningBalance(snamefor_OB);
                last_closing_amount=(-last_closing_amount);
                Toast.makeText(getApplicationContext()," else",Toast.LENGTH_SHORT).show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return last_closing_amount;
    }
}
