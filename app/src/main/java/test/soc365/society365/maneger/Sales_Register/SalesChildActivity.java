package test.soc365.society365.maneger.Sales_Register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
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
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.Cash_Bank_Balance.CashBookDetailPage;
import test.soc365.society365.maneger.Outstanding_Receivable.ReceivableActivity;
import test.soc365.society365.maneger.Reciept.RecieptActivityAdapter;

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
import static test.soc365.society365.helper.DatabaseHelper.trailbalance;
import static test.soc365.society365.maneger.Outstanding_Receivable.ReceivableActivity.outstanding_rec_list;

public class SalesChildActivity extends AppCompatActivity {

    int year1,year2;
    String day1,day2;
    String month1,month2;

    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SalesChildActivityAdapter adapter;
    String snamesales,amount;
    String date1,date2;
    TextView dateendandstart,totalamountgross,totalsa,totalsd,totalcash,totalbankaccount;
    private List<sales_register_model> movieList;
    public static List<sales_register_model> receipt_innerlist;
    //progress bar
    SpotsDialog spotsDialog;


    private SearchView searchView;

    String stallyid;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    static String timeperiod1,timeperiod2;
    String defaultdate;

    Calendar calendartp1,calendartp2;

    TextView summaryname;

    LinearLayout calander;
    AlertDialog.Builder builder;


    ArrayList<String> partynametype;
    ArrayList<String> partyamount;
    JSONArray jsonArray1;
   static String type ;
   LinearLayout salesaccount,sundry,bankaccountlinear,cashlinear;

   //recycleview for sales account
   RecyclerView recyclerView2;
    private LinearLayoutManager linearLayoutManager2;
    private DividerItemDecoration dividerItemDecoration2;
    private RecieptActivityAdapter adapter2;

    // Database Helper
    DatabaseHelper db;
   public static List<sales_register_model> allToDos3,cashlist,banklist;
    int amountplus=0,amountplus2=0;
    String partynameusedfor_partysummary;
    int sum_cashamount,last_closing_amount;

    String bankaccount_click;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_child);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
       dateendandstart =(TextView)findViewById(R.id.summaryname);
        totalamountgross =(TextView)findViewById(R.id.totalamountgross);


        totalsa =(TextView)findViewById(R.id.totalsa);
        totalsd =(TextView)findViewById(R.id.totalsd);
        totalcash =(TextView)findViewById(R.id.totalcash);
        totalbankaccount =(TextView)findViewById(R.id.totalbankaccount);


        spotsDialog = new SpotsDialog(this,R.style.Custom);

        day1 = "01";
        day2 = "31";
        month1 = "04";
        month2 = "03";
        year1 =2018;
        year2 =2019;
/*

        timeperiod1=year1+"-"+month1+"-"+day1;

        timeperiod2=year2+"-"+month2+"-"+day2;
*/



        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        timeperiod1 = intent.getStringExtra("date1");
        timeperiod2 =  intent.getStringExtra("date2");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null, date2 = null;
        try {

            date1 = sdf.parse(timeperiod1);
            date2 = sdf.parse(timeperiod2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeperiod1 = sdf.format(date1);
        timeperiod2 =  sdf.format(date2);

        defaultdate=timeperiod1;

        sundry = (LinearLayout)findViewById(R.id.sundry);
        salesaccount = (LinearLayout)findViewById(R.id.salesaccount);
        cashlinear = (LinearLayout)findViewById(R.id.cashlinear);
        bankaccountlinear = (LinearLayout)findViewById(R.id.bankaccountlinear);

        recyclerView2 = (RecyclerView)findViewById(R.id.recycleview2);



       // dateendandstart.setText(date1+" - "+date2);
      //  totalamountgross.setText("3456");



        //recyclerView for sales ->sundry daptors and receipt
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        /*movieList = new ArrayList<>();*/
        partynametype =  new ArrayList<>();
        partyamount = new ArrayList<>();

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        db = new DatabaseHelper(getApplicationContext());
        movieList = new ArrayList<>();
        receipt_innerlist = new ArrayList<>();

        method_for_both(timeperiod1,timeperiod2);

    }

    public void method_for_both(String firstdate,String seconddate)
    {
        dateendandstart.setText(firstdate+" - "+seconddate);
            if(type.equals("from_ReceivableActivity"))
            {
               // dateendandstart.setText("2018");

                partynametype.clear();
                partyamount.clear();
                getSupportActionBar().setTitle("Receivable");
                sundry.setVisibility(View.VISIBLE);
                salesaccount.setVisibility(View.GONE);
                bankaccountlinear.setVisibility(View.GONE);
                cashlinear.setVisibility(View.GONE);

                cashlinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                  /*      Intent intent = new Intent(getApplicationContext(), ReceivableActivity.class);
                        intent.putExtra("partyledgertype","Cash");
                        intent.putExtra("date1",timeperiod1);
                        intent.putExtra("date2",timeperiod2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                        Intent intent = new Intent(getApplicationContext(),CashBookDetailPage.class);
                        intent.putExtra("cash_or_bank","Cash");
                        startActivity(intent);
                    }
                });
                bankaccountlinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* Intent intent = new Intent(getApplicationContext(), ReceivableActivity.class);
                        intent.putExtra("partyledgertype","T.J.S.B A/c" );
                        intent.putExtra("date1",timeperiod1);
                        intent.putExtra("date2",timeperiod2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
                        Intent intent = new Intent(getApplicationContext(),CashBookDetailPage.class);
                        intent.putExtra("cash_or_bank","Bank Accounts");
                        startActivity(intent);
                    }
                });
                sundry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ReceivableActivity.class);
                        intent.putExtra("partyledgertype", "from_mainpage");
                        intent.putExtra("date1",timeperiod1);
                        intent.putExtra("date2",timeperiod2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

               /* cashlist = db.getOutstanding_Rec_data("Receivable_cashorbank","2018","Cash");
                for (sales_register_model todo : cashlist) {

                    amountplus =amountplus+todo.getAmt_for_receipt();

                }
                totalcash.setText("\u20B9"+amountplus);


                banklist  = db.getOutstanding_Rec_data("Receivable_cashorbank","2018","T.J.S.B A/c");
                amountplus=0;
                for (sales_register_model todo : banklist) {

                    amountplus =amountplus+todo.getAmt_for_receipt();

                }
                totalbankaccount.setText("\u20B9"+amountplus);*/

                CashbankarrayDate("Cash",timeperiod1,timeperiod2);
                CashbankarrayDate("Bank Accounts",timeperiod1,timeperiod2);


                int amountplus=0;
               // allToDos3 = db.getOutstanding_Rec_data("preference","amountsort","Not_cashorbank","2018","DisplayPageRece");
                for (sales_register_model todo : outstanding_rec_list) {


                        partynametype.add(todo.getTest());


                    String sd= String.valueOf(todo.getAmt_for_receipt());
                    if(sd.contains("-"))
                    {

                    }
                    else{
                        amountplus = amountplus + todo.getAmt_for_receipt();

                    }
                  //  amountplus = amountplus + todo.getAmt_for_receipt();

                }
                totalsd.setText("\u20B9 "+amountplus);

                 /*  for all group*/
                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                partynametype.clear();
                partynametype.addAll(primesWithoutDuplicates);
                Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                Log.d("amountplusnew",String.valueOf(partynametype)+" "+amountplus);


                for (int j=0;j<partynametype.size();j++) {


                    int amountplus2=0;
                    for (sales_register_model todo : outstanding_rec_list) {

                        if (partynametype.get(j).equals(todo.getTest())) {

                            String sd= String.valueOf(todo.getAmt_for_receipt());
                            if(sd.contains("-"))
                            {

                            }
                            else{
                                 amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                            }

                         /*   amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                        */

                        }
                    }

                    partyamount.add(String.valueOf(amountplus2));
                }

                Log.d("amountplusnew",String.valueOf(partyamount));

                editor.putString("jsonStringReceipt", String.valueOf(trailbalance));
                editor.apply();

                adapter = new SalesChildActivityAdapter(getApplicationContext(),partynametype,partyamount, firstdate, seconddate);

            }
        else if(type.equals("from_receipt"))
        {

                partynametype.clear();
                partyamount.clear();
                getSupportActionBar().setTitle("Receipt");
                sundry.setVisibility(View.VISIBLE);
                salesaccount.setVisibility(View.GONE);
                bankaccountlinear.setVisibility(View.VISIBLE);
                cashlinear.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),firstdate+" "+seconddate,Toast.LENGTH_LONG).show();

                cashlinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SalesRegisterActivity.class);
                        intent.putExtra("type", "from_receipt");
                        intent.putExtra("partyledgertype","Cash");
                        intent.putExtra("banktype",type);
                        intent.putExtra("date1",timeperiod1);
                        intent.putExtra("date2",timeperiod2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
           /* bankaccountlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SalesRegisterActivity.class);
                    intent.putExtra("type", "from_receipt");
                      intent.putExtra("banktype",type);
                    intent.putExtra("partyledgertype","T.J.S.B A/c" );

                    intent.putExtra("date1",timeperiod1);
                    intent.putExtra("date2",timeperiod2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });*/
                bankaccountlinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bankaccount_click="true";
                        Intent intent = new Intent(getApplicationContext(), SalesChildActivity.class);
                        intent.putExtra("type", "bankaccount_click");
                        intent.putExtra("date1", timeperiod1);
                        intent.putExtra("date2", timeperiod2);
                        startActivity(intent);

                    }
                });
                sundry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SalesRegisterActivity.class);
                        intent.putExtra("type", "from_receipt");
                        intent.putExtra("partyledgertype", "from_mainpage");
                        intent.putExtra("banktype",type);
                        intent.putExtra("date1",timeperiod1);
                        intent.putExtra("date2",timeperiod2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                cashlist =    db.getReceiptexp(getApplicationContext(),"preference","amountsort","Receipt_cashorbank","Cash",firstdate,seconddate);
                for (sales_register_model todo : cashlist) {

                    amountplus =amountplus+todo.getAmt_for_receipt();

                }
                totalcash.setText("\u20B9"+amountplus);

                List<partydetailmodel> bankname = db.getBankName("Bank Accounts");
                for (partydetailmodel todo : bankname)
                {
                    banklist =    db.getReceiptexp(getApplicationContext(),"preference","amountsort","Receipt_cashorbank",todo.getLedger_name(),firstdate,seconddate);
                    amountplus=0;
                    for (sales_register_model todo2 : banklist) {

                        amountplus =amountplus+todo2.getAmt_for_receipt();

                    }
                    totalbankaccount.setText("\u20B9"+amountplus);
                }

          /*  banklist =    db.getReceiptexp(getApplicationContext(),"preference","amountsort","Receipt_cashorbank","T.J.S.B A/c",firstdate,seconddate);
            amountplus=0;
            for (sales_register_model todo : banklist) {

                amountplus =amountplus+todo.getAmt_for_receipt();

            }
            totalbankaccount.setText("\u20B9"+amountplus);*/


                allToDos3 =    db.getReceiptexp(getApplicationContext(),"preference","amountsort","Receipt","sundryAdapters",firstdate,seconddate);
                amountplus=0;
                for (sales_register_model todo : allToDos3) {

                    //partyname which is not there in ledger table will have flat wing in Others group
                    if(todo.getTest()==null)
                    {
                        partynametype.add("Others");
                    }
                    else
                    {
                        partynametype.add(todo.getTest());
                    }


                    amountplus =amountplus+todo.getAmt_for_receipt();

                }
                totalsd.setText("\u20B9"+amountplus);

/* for all group*/

                Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                partynametype.clear();
                partynametype.addAll(primesWithoutDuplicates);
                Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                Log.d("amountplusnew",String.valueOf(partynametype)+" "+amountplus);


                for (int j=0;j<partynametype.size();j++) {


                    int amountplus2=0;
                    for (sales_register_model todo : allToDos3) {

                        if(partynametype.get(j).equals("Others")&&todo.getTest()==null)
                        {
                            amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                        }

                        if (partynametype.get(j).equals(todo.getTest())) {


                            amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                        }
                    }
                    partyamount.add(String.valueOf(amountplus2));
                }

                Log.d("amountplusnew",String.valueOf(partyamount));

                editor.putString("jsonStringReceipt", String.valueOf(trailbalance));
                editor.apply();

                adapter = new SalesChildActivityAdapter(getApplicationContext(),partynametype,partyamount, firstdate, seconddate);




        }
            else if(type.equals("bankaccount_click"))
            {

                    partynametype.clear();
                    partyamount.clear();
                    getSupportActionBar().setTitle("Receipt");
                    sundry.setVisibility(View.VISIBLE);
                    salesaccount.setVisibility(View.GONE);
                    bankaccountlinear.setVisibility(View.GONE);
                    cashlinear.setVisibility(View.GONE);

                    List<partydetailmodel> bankname = db.getBankName("Bank Accounts");
                    amountplus=0;
                    for (partydetailmodel todo : bankname)
                    {
                        banklist =    db.getReceiptexp(getApplicationContext(),"preference","amountsort","Receipt_cashorbank",todo.getLedger_name(),firstdate,seconddate);
                        partynametype.add(todo.getLedger_name());
                        amountplus2=0;
                        for (sales_register_model todo2 : banklist) {

                            amountplus =amountplus+todo2.getAmt_for_receipt();
                            amountplus2= amountplus2+todo2.getAmt_for_receipt();
                        }
                        partyamount.add(String.valueOf(amountplus2));

                        totalsd.setText("\u20B9"+amountplus);
                    }
                adapter = new SalesChildActivityAdapter(getApplicationContext(),partynametype,partyamount, firstdate, seconddate);

                }
        else if(type.equals("from_sales"))
        {

            partynametype.clear();
            partyamount.clear();
            getSupportActionBar().setTitle("Sales");
            sundry.setVisibility(View.VISIBLE);
            salesaccount.setVisibility(View.VISIBLE);
            //sales account recycleview
            recyclerView2.setVisibility(View.VISIBLE);
            bankaccountlinear.setVisibility(View.GONE);
            cashlinear.setVisibility(View.GONE);


            int amountplus=0;
            allToDos3 =    db.getReceiptexp(getApplicationContext(),"preference","amountsort","Bill","forgetting_billinsales", firstdate, seconddate);

            for (sales_register_model todo : allToDos3) {

                //partyname which is not there in ledger table will have flat wing in Others group
                if(todo.getTest()==null)
                {
                    partynametype.add("Others");
                }
                else
                {
                    partynametype.add(todo.getTest());
                }


                amountplus =amountplus+todo.getAmt_for_receipt();

            }
            totalsd.setText("\u20B9"+amountplus);

                 /*  for all group*/
            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
            partynametype.clear();
            partynametype.addAll(primesWithoutDuplicates);
            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
            Log.d("amountplusnew",String.valueOf(partynametype)+" "+amountplus);


            for (int j=0;j<partynametype.size();j++) {


                int amountplus2=0;
                for (sales_register_model todo : allToDos3) {

                    if(partynametype.get(j).equals("Others")&&todo.getTest()==null)
                    {
                        amountplus2 = amountplus2 + todo.getAmt_for_receipt();
                    }

                    if (partynametype.get(j).equals(todo.getTest())) {

                        amountplus2 = amountplus2 + todo.getAmt_for_receipt();

                        Log.d("amountplus",partynametype.get(j)+ " "+todo.getTest()+amountplus2+" "+todo.getName()+" "+todo.getVn()+" "+todo.getLed_name());
                    }
                }
                partyamount.add(String.valueOf(amountplus2));
            }

            Log.d("amountplusnew",String.valueOf(partyamount));

            editor.putString("jsonStringReceipt", String.valueOf(trailbalance));
            editor.apply();

            adapter = new SalesChildActivityAdapter(getApplicationContext(),partynametype,partyamount, firstdate, seconddate);

            //for sales account

            linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());

            dividerItemDecoration2 = new DividerItemDecoration(recyclerView2.getContext(), linearLayoutManager2.getOrientation());

            List<sales_register_model> allToDos =   db.getBill_salesacc( firstdate, seconddate);

            int amtsales=0;
            for (sales_register_model todo : allToDos) {


                amtsales =amtsales+todo.getAmt_for_receipt();

            }
            totalsa.setText("\u20B9"+amtsales);
            adapter2 = new RecieptActivityAdapter(getApplicationContext(),salespaylist);

            recyclerView2.setHasFixedSize(true);
            recyclerView2.setLayoutManager(linearLayoutManager2);
            recyclerView2.addItemDecoration(dividerItemDecoration2);

            recyclerView2.setAdapter(adapter2);


        }

      /*  else
        {

            adapter = new SalesChildActivityAdapter(getApplicationContext(),partynametype,partyamount);
        }*/

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //settting for both sales and receipt
        recyclerView.setAdapter(adapter);

    }

    private void getData() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        spotsDialog.show();




       /* calendartp1 = Calendar.getInstance();
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
            if(type.equals("from_receipt"))
            {
                url = "http://150.242.14.196:8012/society/service/app/get_salesregister.php?" +
                        "filter_date="+ URLEncoder.encode(timeperiod1, "UTF-8")+
                        "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                        +"&company_id="+stallyid+"&type="+"receipt";
            }
            else {
                url = "http://150.242.14.196:8012/society/service/app/get_salesregister.php?" +
                        "filter_date="+ URLEncoder.encode(timeperiod1, "UTF-8")+
                        "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                        +"&company_id="+stallyid+"&type="+"bill";
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

                                            spotsDialog.dismiss();
                                            JSONObject jsonObject= new JSONObject(response);


                                            JSONArray jsonArray1=jsonObject.getJSONArray("records");

                                            Log.d("partyledger2 ", String.valueOf(jsonArray1));


                                            String jsonString = jsonArray1.toString();
                                            editor.putString("jsonStringSalesforallgroup", jsonString);
                                            editor.apply();

                                            partynametype.clear();
                                            partyamount.clear();



                                            int amountplus=0;
                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                              //  sales_register_model sales_register_model = new sales_register_model();
                                                //some party name as flatno with that only so below code to sepearate it
                                               /* if(jsonObject1.getString("name").contains("("))
                                                {
                                                    String[] arrOfStr = jsonObject1.getString("name").split("\\(");
                                                    sales_register_model.setName(arrOfStr[0]);
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    if(arrOfStr[1].contains(")"))
                                                    {
                                                        String[] arrOfStr2 = arrOfStr[1].split("\\)");
                                                        sales_register_model.setFlatno(arrOfStr2[0]);
                                                    }

                                                }
                                                else
                                                {
                                                    sales_register_model.setName(jsonObject1.getString("name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    sales_register_model.setFlatno(jsonObject1.getString("flat_no"));

                                                }
*/
                                                amountplus = amountplus+jsonObject1.getInt("amount");
                                                Log.d("amountplus", String.valueOf(amountplus));
                                            /*    sales_register_model.setDate1(timeperiod1);
                                                sales_register_model.setDate2(timeperiod2);
*/
                                                partynametype.add(jsonObject1.getString("parent_name_2"));


                                              //  movieList.add(sales_register_model);

                                            }

                                                  /*  for all group*/
                                            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                                            partynametype.clear();
                                            partynametype.addAll(primesWithoutDuplicates);
                                            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                                            Log.d("amountplus",String.valueOf(partynametype));



                                            String amountall = String.valueOf(amountplus);
                                            totalsd.setText("\u20B9"+amountall);


                                            for (int j=0;j<partynametype.size();j++) {


                                                int amountplus2=0;
                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                    if (partynametype.get(j).equals(jsonObject1.getString("parent_name_2"))) {


                                                        amountplus2 = amountplus2 + jsonObject1.getInt("amount");


                                                    }
                                                }
                                                partyamount.add(String.valueOf(amountplus2));
                                            }
                                            for (int i=0;i<partynametype.size();i++)
                                            {

                                                if (partynametype.get(i).equals(""))
                                                {
                                                    partynametype.set(i,"Others");
                                                }
                                            }
                                          /*  sales_register_model sales_register_model = new sales_register_model();
                                            sales_register_model.setTest("1");
                                          */  adapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            spotsDialog.dismiss();
                                        }

                                        adapter.notifyDataSetChanged();

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


    private void getDatasales_Account() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/


        String url = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/get_bill.php?" +
                    "filter_date="+ URLEncoder.encode(timeperiod1, "UTF-8")+
                    "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                    +"&company_id="+stallyid+"&type="+"bill";
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



                                            movieList.clear();

                                            int amountplus=0;
                                            for (int i=0;i<jsonArray1.length();i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                sales_register_model sales_register_model = new sales_register_model();

                                                    sales_register_model.setName(jsonObject1.getString("ledger_name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("amount"));
                                                    sales_register_model.setFlatno(jsonObject1.getString("flat_no"));


                                                amountplus = amountplus+jsonObject1.getInt("amount");
                                                Log.d("amountplus", String.valueOf(amountplus));

                                                sales_register_model.setDate1(timeperiod1);
                                                sales_register_model.setDate2(timeperiod2);


                                                movieList.add(sales_register_model);

                                            }


                                            String amountall = String.valueOf(amountplus);
                                            totalsa.setText("\u20B9"+amountall);
                                        //    adapter.notifyDataSetChanged();
                                            adapter2 = new RecieptActivityAdapter(getApplicationContext(),movieList);

                                            recyclerView2.setAdapter(adapter2);



                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }

                                        //adapter.notifyDataSetChanged();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
      /*  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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
        });*/
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
        else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();
            builder = new AlertDialog.Builder(SalesChildActivity.this);


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesChildActivity.this,
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
                        method_for_both(sdf.format(date1),sdf.format(date2));
                        // CashbankarrayDate("Cash",sdf.format(date1),sdf.format(date2));

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
                        method_for_both(sdf.format(date1),sdf.format(date2));

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

        MenuItem action_calender = menu.findItem(R.id.action_calender);
            MenuItem sharemenu = menu.findItem(R.id.share);
        MenuItem sharegroup = menu.findItem(R.id.group);
        MenuItem shareamount = menu.findItem(R.id.amount);
        MenuItem searchgroup = menu.findItem(R.id.action_search);

        if(type.equals("from_ReceivableActivity")) {
            action_calender.setVisible(false);
        }


        searchgroup.setVisible(false);
        sharegroup.setVisible(false);
        sharemenu.setVisible(false);
        shareamount.setVisible(false);
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
        builder = new AlertDialog.Builder(SalesChildActivity.this);

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
                                method_for_both(sdf.format(date1),sdf.format(date2));

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


    public void CashbankarrayDate(String cashbook,String startdate,String enddate)
    {
       /* int amountplus =  db.getCashBank_OB(cashbook);

        amountplus=(-amountplus);*/

        int amountplus =  check_date(cashbook,defaultdate,startdate,enddate);
        Toast.makeText(getApplicationContext(),String.valueOf(amountplus),Toast.LENGTH_SHORT).show();

        int credit_amount=0,debit_amount = 0;

        List<partydetailmodel> allToDos =  db.getCashBank_Balance("preference","amountsort",cashbook,startdate,enddate);
        for (partydetailmodel todo : allToDos) {
            if ("Credit".equals(todo.getDr_cr())) {
                Log.d("onCreate:amountplusdb ", todo.getDr_cr()+" "+todo.getDate() +"  " +String.valueOf(todo.getAmt_for_receipt()));
                credit_amount = credit_amount + todo.getAmt_for_receipt();
                Log.d("onCreate:amountplusdbC", String.valueOf(credit_amount));
            }
            else if ("Debit".equals(todo.getDr_cr()))
            {
                Log.d("onCreate:amountplusdb ", todo.getDr_cr()+" "+todo.getDate() +"  " +String.valueOf(todo.getAmt_for_receipt()));
                debit_amount = debit_amount + todo.getAmt_for_receipt();
                Log.d("onCreate:amountplusdbD ", String.valueOf(debit_amount));
            }
        }

        Log.d("onCreate:amountpluscd ", String.valueOf(credit_amount)+ " "+String.valueOf(debit_amount));
        Toast.makeText(getApplicationContext(), String.valueOf(credit_amount)+ " "+String.valueOf(debit_amount),Toast.LENGTH_SHORT).show();

        int closing_bal = debit_amount - credit_amount;
        sum_cashamount = closing_bal + amountplus;
        if(cashbook.equals("Cash")) {
            totalcash.setText("\u20B9"+sum_cashamount);
        }
        else
        {
            totalbankaccount.setText("\u20B9"+sum_cashamount);
        }
    }
    public Integer check_date(String cashbank,String defaultdate,String startdate,String enddate)
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

                int amountplus =  db.getCashBank_OB(cashbank);

                amountplus=(-amountplus);
                int credit_amount=0,debit_amount = 0;

                List<partydetailmodel> allToDos =  db.CheckDate_CashBank("preference","amountsort",cashbank,startdate);
                for (partydetailmodel todo : allToDos) {
                    if ("Credit".equals(todo.getDr_cr())) {
                        //Log.d("onCreate:amountplusdb ", todo.getDr_cr()+" "+todo.getDate() +"  " +String.valueOf(todo.getAmt_for_receipt()));
                        credit_amount = credit_amount + todo.getAmt_for_receipt();
                        //Log.d("onCreate:amountplusdbC", String.valueOf(credit_amount));
                    }
                    else if ("Debit".equals(todo.getDr_cr()))
                    {
                        //Log.d("onCreate:amountplusdb ", todo.getDr_cr()+" "+todo.getDate() +"  " +String.valueOf(todo.getAmt_for_receipt()));
                        debit_amount = debit_amount + todo.getAmt_for_receipt();
                        //Log.d("onCreate:amountplusdbD ", String.valueOf(debit_amount));
                    }
                }

                Log.d("onCreate:amountpluscd ", String.valueOf(credit_amount)+ " "+String.valueOf(debit_amount));

                int closing_bal = debit_amount - credit_amount;
                last_closing_amount = closing_bal + amountplus;

            }
            else if(date2.equals(date1)){

                last_closing_amount =  db.getCashBank_OB(cashbank);

                last_closing_amount=(-last_closing_amount);
                Toast.makeText(getApplicationContext()," else",Toast.LENGTH_SHORT).show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return last_closing_amount;
    }

    @Override
    public void onBackPressed()
    {
        if (type.equals("bankaccount_click")) {
            type="from_receipt";
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
