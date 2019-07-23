package test.soc365.society365.maneger.Cash_Bank_Balance;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import test.soc365.society365.maneger.partyledger.partydetailmodel;

import static test.soc365.society365.helper.DatabaseHelper.amount_payment;
import static test.soc365.society365.helper.DatabaseHelper.amountplusdb;
import static test.soc365.society365.helper.DatabaseHelper.cashbook_ob_amount;

public class CashBankBalanceActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid,amountTo;
    TextView amountcash,amountbank;

    //progress bar
    SpotsDialog spotsDialog;
    TextView dateendandstart;
    // Database Helper
    DatabaseHelper db;
    RelativeLayout cashlayout,banklayout;
    AlertDialog.Builder builder;
    private SearchView searchView;
    int year1,year2;
    String day1,day2;
    String month1,month2;
    String timeperiod1,timeperiod2,defaultdate;
    int sum_cashamount,last_closing_amount;
    ArrayList<String> partynametype;
    ArrayList<String> partyamount;
    private Bank_name_Adapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_bank_balance);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        db = new DatabaseHelper(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cash / Bank Balance");

        cashlayout = (RelativeLayout)findViewById(R.id.cashlayout);
        banklayout = (RelativeLayout)findViewById(R.id.banklayout);
        amountcash = (TextView)findViewById(R.id.amountcash);
        amountbank = (TextView)findViewById(R.id.amountbank);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );
        spotsDialog = new SpotsDialog(this,R.style.Custom);

        dateendandstart =(TextView)findViewById(R.id.summaryname);


        day1 = "01";
        day2 = "31";
        month1 = "04";
        month2 = "03";
        year1 =2018;

        year2 =2019;

        timeperiod1=year1+"-"+month1+"-"+day1;

        timeperiod2=year2+"-"+month2+"-"+day2;
        defaultdate=timeperiod1;

        dateendandstart.setText(timeperiod1+" - "+timeperiod2);
      /*  db.getCashBank_Balance("cash");*/


        CashbankarrayDate("Cash",timeperiod1,timeperiod2);

            //amountcash.setText("\u20B9"+sum_cashamount);

        cashlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(),CashBookDetailPage.class);
                    intent.putExtra("cash_or_bank","Cash");
                    intent.putExtra("date1",timeperiod1);
                    intent.putExtra("date2",timeperiod2);
                    startActivity(intent);
                }
            });

        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        partynametype =  new ArrayList<>();
        partyamount = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //settting for both sales and receipt

       CashbankarrayDate("Bank Accounts",timeperiod1,timeperiod2);



     //   amountbank.setText("\u20B9"+sum_cashamount);

        /*banklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CashBookDetailPage.class);
                intent.putExtra("cash_or_bank","Bank Accounts");
                startActivity(intent);
            }
        });*/


        String dsd;
        String dsd2;

        final Calendar calendartp= Calendar.getInstance();
        //calander to choose custom date
        builder = new AlertDialog.Builder(this);
        //setting calender to custom date




        dsd2=calendartp.get(Calendar.YEAR)+"-"+"04"+"-"+"01";

        calendartp.add(Calendar.YEAR, -1);
        dsd= calendartp.get(Calendar.YEAR)+"-"+"03"+"-"+"31";

        //dateendandstart.setText(dsd+" - "+dsd2);
        Log.d("datedate",dsd+" "+dsd2);

      /*    String url=null,url2 = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/test_cb1.php?company_id="
                    + stallyid+ "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8");

            url2 = "http://150.242.14.196:8012/society/service/app/test_cb2.php?company_id="
                    + stallyid+ "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        spotsDialog.show();
        Log.d("onResponse:summary2 ", url);

      StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {
                                            spotsDialog.dismiss();
                                            JSONObject jsonObject= new JSONObject(response);
                                            amountTo=jsonObject.getString("sub_total_amount");
                                            amountcash.setText(amountTo);
                                            //      Toast.makeText(getContext(),amountTo,Toast.LENGTH_SHORT).show();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            spotsDialog.dismiss();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                spotsDialog.dismiss();
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

        StringRequest jsonArrayRequest2 =
                new StringRequest
                        (url2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);
                                            amountTo=jsonObject.getString("sub_total_amount");
                                            amountbank.setText(amountTo);
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
        jsonArrayRequest2.setRetryPolicy(new RetryPolicy() {
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(jsonArrayRequest2);*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
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
        else if (id == R.id.action_search) {
            //searchbar edittext
            return true;
        }
        else if (id == R.id.action_calender) {
            //info BS
            //    information_bottomsheet();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CashBankBalanceActivity.this,
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
                    else if(strName=="Last Year")
                    {


//                        final Calendar calendartp= Calendar.getInstance();
                        //setting calender to custom date


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
                        CashbankarrayDate("Cash",sdf.format(date1),sdf.format(date2));
                        CashbankarrayDate("Bank Accounts",sdf.format(date1),sdf.format(date2));

                    }
                    else if(strName=="This Year")
                    {

                      /*  final Calendar calendartp= Calendar.getInstance();
                        //setting calender to custom date
                        calendartp.add(Calendar.YEAR, -1);*/
                     //   calendartp.add(Calendar.YEAR, -1);


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
                        CashbankarrayDate("Cash",sdf.format(date1),sdf.format(date2));
                        CashbankarrayDate("Bank Accounts",sdf.format(date1),sdf.format(date2));

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

        MenuItem searchmenu = menu.findItem(R.id.action_search);
        MenuItem amount = menu.findItem(R.id.amount);

        amount.setVisible(false);
        searchmenu.setVisible(false);
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
        builder = new AlertDialog.Builder(CashBankBalanceActivity.this);

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
                                CashbankarrayDate("Cash",sdf.format(date1),sdf.format(date2));
                                CashbankarrayDate("Bank Accounts",sdf.format(date1),sdf.format(date2));


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

    public void CashbankarrayDate(String cashbook,String startdate,String enddate)
    {
       /* int amountplus =  db.getCashBank_OB(cashbook);

        amountplus=(-amountplus);*/


        if(cashbook.equals("Cash")) {
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

            amountcash.setText("\u20B9"+sum_cashamount);
        }
        else
        {
            sum_cashamount=0;
            partynametype.clear();
            partyamount.clear();


            List<partydetailmodel> bankname = db.getBankName("Bank Accounts");

            for (partydetailmodel todomain : bankname) {

               /* Toast.makeText(getApplicationContext(),String.valueOf(amountplus),Toast.LENGTH_SHORT).show();
*/
                int credit_amount=0,debit_amount = 0;
                List<partydetailmodel> allToDos = db.getCashBank_Balance("preference", "amountsort", todomain.getLedger_name(), startdate, enddate);
                if(allToDos.isEmpty())
                {

                }
                else {
                    int amountplus =  check_date(todomain.getLedger_name(),defaultdate,startdate,enddate);
                    Toast.makeText(getApplicationContext(), amountplus+ " "+todomain.getLedger_name(),Toast.LENGTH_SHORT).show();

                    partynametype.add(todomain.getLedger_name());
                    for (partydetailmodel todo : allToDos) {
                        if ("Credit".equals(todo.getDr_cr())) {
                            Log.d("onCreate:amountplusdb ", todo.getDr_cr() + " " + todo.getDate() + "  " + String.valueOf(todo.getAmt_for_receipt()));
                            credit_amount = credit_amount + todo.getAmt_for_receipt();
                        } else if ("Debit".equals(todo.getDr_cr())) {
                            Log.d("onCreate:amountplusdb ", todo.getDr_cr() + " " + todo.getDate() + "  " + String.valueOf(todo.getAmt_for_receipt()));
                            debit_amount = debit_amount + todo.getAmt_for_receipt();
                            Log.d("onCreate:amountplusdbD ", String.valueOf(debit_amount));
                        }
                    }

                    int closing_bal = debit_amount - credit_amount;
                    partyamount.add(String.valueOf(closing_bal + amountplus));

                    sum_cashamount = sum_cashamount + closing_bal + amountplus;
                }
            }
            Toast.makeText(getApplicationContext(), partynametype+ " "+partyamount,Toast.LENGTH_SHORT).show();

            adapter = new Bank_name_Adapter(getApplicationContext(),partynametype,partyamount, startdate, enddate);
            recyclerView.setAdapter(adapter);


            amountbank.setText("\u20B9"+sum_cashamount);
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
                Toast.makeText(getApplicationContext()," else"+cashbank,Toast.LENGTH_SHORT).show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
return last_closing_amount;
    }
}
