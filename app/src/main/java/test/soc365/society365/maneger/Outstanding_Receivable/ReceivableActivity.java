package test.soc365.society365.maneger.Outstanding_Receivable;


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
        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.LinearLayout;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
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
        import test.soc365.society365.maneger.Sales_Register.SalesChildActivity;
        import test.soc365.society365.maneger.Sales_Register.sales_register_model;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Collections;
        import java.util.LinkedHashSet;
        import java.util.List;
        import java.util.Set;

        import dmax.dialog.SpotsDialog;
        import test.soc365.society365.maneger.partyledger.PartyLedgerDetailPage;

        import static test.soc365.society365.helper.DatabaseHelper.salespaylist;

        import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.banklist;
        import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.cashlist;
        import static test.soc365.society365.maneger.Sales_Register.SalesChildActivityAdapter.wing;

public class ReceivableActivity extends AppCompatActivity {

 

    private SearchView searchView;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private ReceivableActivityAdapter adapter;

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
    int day1,day2;
    String month1,month2;
    String datefordb;

    TextView partynamecash,amountcash,partynamebank,amountbank;

    LinearLayout calander;
    AlertDialog.Builder builder;

    ArrayList<String> partynametype;
    JSONArray jsonArray1;

    // Database Helper
    DatabaseHelper db;
    String partyledgertype;
    int amountplus;
    RadioGroup radioGroupPreference ;
    RadioButton radioButton_equalto ;
    RadioButton radioButton_greaterthan;
    String radiocheckbutton;
    AlertDialog alertDialog;
    public static List<sales_register_model> outstanding_rec_list;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable);
        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Receivable");
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
        summaryname =(TextView)findViewById(R.id.summaryname);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        totalamountgross =(TextView)findViewById(R.id.totalamountgross);

        movieList = new ArrayList<>();

        Intent intent = getIntent();
        partyledgertype = intent.getStringExtra("partyledgertype");
        timeperiod1 = intent.getStringExtra("date1");
        timeperiod2 = intent.getStringExtra("date2");



        spotsDialog = new SpotsDialog(this,R.style.Custom);


        db = new DatabaseHelper(getApplicationContext());
        datefordb="2018";
        Method_Receivable("preference","amountsort",datefordb);



        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);


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

        day1 = 1;
        day2 = 31;
        month1 = "4";
        month2 = "3";
        year1 =2018;

        year2 =2019;

      /*  timeperiod1=day1+"-"+month1+"-"+year1;

        timeperiod2=day2+"-"+month2+"-"+year2;*/

      //  getData();

    }

    public void  Method_Receivable(String preference,String amountsort,String year)
    {
        summaryname.setText(timeperiod1 + " - " + timeperiod2);


        if(preference.equals("equalto")||preference.equals("greaterthan"))
        {
            List<sales_register_model> salespaylist  = new ArrayList<sales_register_model>();
            for (sales_register_model todo : outstanding_rec_list) {
                String sd= String.valueOf(todo.getAmt_for_receipt());
                int amt_userinput = Integer.parseInt(amountsort);
                if(preference.equals("equalto")&&sd.equals(amountsort))
                {
                    if(partyledgertype.equals(todo.getTest()))
                    {
                        //amount sort for wing wise
                        sales_register_model td = new sales_register_model();
                        //int amt = (-(c.getInt(c.getColumnIndex(KEY_closing_amount))));
                        td.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td.setAmount(String.valueOf(todo.getAmt_for_receipt()));
                        td.setLed_name(todo.getLed_name());
                        td.setTest(todo.getTest());
                        td.setFlatno(todo.getFlatno());
                        td.setIsparty(todo.getIsparty());
                        // adding to todo list
                        salespaylist.add(td);
                    }
                    else if(partyledgertype.equals("from_mainpage"))
                    {
                        //amount sort for main chargers
                        sales_register_model td = new sales_register_model();
                        //int amt = (-(c.getInt(c.getColumnIndex(KEY_closing_amount))));
                        td.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td.setAmount(String.valueOf(todo.getAmt_for_receipt()));
                        td.setLed_name(todo.getLed_name());
                        td.setTest(todo.getTest());
                        td.setFlatno(todo.getFlatno());
                        td.setIsparty(todo.getIsparty());
                        // adding to todo list
                        salespaylist.add(td);
                    }


                }
                else if(preference.equals("greaterthan")&& todo.getAmt_for_receipt() >= amt_userinput)
                {
                    if(partyledgertype.equals(todo.getTest()))
                    {
                        //amount sort for wing wise
                        sales_register_model td = new sales_register_model();
                        //int amt = (-(c.getInt(c.getColumnIndex(KEY_closing_amount))));
                        td.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td.setAmount(String.valueOf(todo.getAmt_for_receipt()));
                        td.setLed_name(todo.getLed_name());
                        td.setTest(todo.getTest());
                        td.setFlatno(todo.getFlatno());
                        td.setIsparty(todo.getIsparty());
                        // adding to todo list
                        salespaylist.add(td);
                    }
                    else if(partyledgertype.equals("from_mainpage"))
                    {
                        //amount sort for main chargers
                        sales_register_model td = new sales_register_model();
                        //int amt = (-(c.getInt(c.getColumnIndex(KEY_closing_amount))));
                        td.setAmt_for_receipt(todo.getAmt_for_receipt());
                        td.setAmount(String.valueOf(todo.getAmt_for_receipt()));
                        td.setLed_name(todo.getLed_name());
                        td.setTest(todo.getTest());
                        td.setFlatno(todo.getFlatno());
                        td.setIsparty(todo.getIsparty());
                        // adding to todo list
                        salespaylist.add(td);
                    }
                }
            }
            adapter = new ReceivableActivityAdapter(getApplicationContext(), salespaylist);
            recyclerView.setAdapter(adapter);
        }
        else if(partyledgertype.equals("from_mainpage")) {
           // summaryname.setText(year);

            outstanding_rec_list = db.getOutstanding_Rec_data(preference,amountsort,"Not_cashorbank",year, "DisplayPageRece");
           /* if(!outstanding_rec_list.isEmpty())
            {
                spotsDialog.dismiss();
            }*/
            for (sales_register_model todo : outstanding_rec_list) {

                String sd= String.valueOf(todo.getAmt_for_receipt());
                if(sd.contains("-"))
                {

                }
                else{
                    amountplus = amountplus + todo.getAmt_for_receipt();

                }
            }
            totalamountgross.setText( "\u20B9 " + amountplus);

            adapter = new ReceivableActivityAdapter(getApplicationContext(), salespaylist);
            recyclerView.setAdapter(adapter);
        }
        else
        {
            //summaryname.setText(year);

            if(partyledgertype.equals("Cash"))
            {
                cashlist = db.getOutstanding_Rec_data(preference,amountsort,"Receivable_cashorbank","2018","Cash");
                for (sales_register_model todo : cashlist) {

                    amountplus = amountplus + todo.getAmt_for_receipt();

                }
                totalamountgross.setText( "\u20B9 " + amountplus);

                adapter = new ReceivableActivityAdapter(getApplicationContext(), salespaylist);
                recyclerView.setAdapter(adapter);

            }else  if(partyledgertype.equals("T.J.S.B A/c"))
            {
                banklist  = db.getOutstanding_Rec_data(preference,amountsort,"Receivable_cashorbank","2018","T.J.S.B A/c");
                for (sales_register_model todo : banklist) {

                    amountplus = amountplus + todo.getAmt_for_receipt();

                }
                totalamountgross.setText( "\u20B9 " + amountplus);

                adapter = new ReceivableActivityAdapter(getApplicationContext(), salespaylist);
                recyclerView.setAdapter(adapter);
            }else {
                List<sales_register_model> allToDos3 = db.getOutstanding_Rec_data(preference,amountsort,"Not_cashorbank",year, partyledgertype);
                for (sales_register_model todo : allToDos3) {

                    String sd= String.valueOf(todo.getAmt_for_receipt());
                    if(sd.contains("-"))
                    {

                    }
                    else{
                        amountplus = amountplus + todo.getAmt_for_receipt();

                    }

                }
                totalamountgross.setText( "\u20B9 " + amountplus);

                adapter = new ReceivableActivityAdapter(getApplicationContext(), salespaylist);
                recyclerView.setAdapter(adapter);
            }

        }

    }
    private void getData() {
      /*  final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        spotsDialog.show();




 /*       switch(month1) {
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


        }*/


       summaryname.setText("As of "+timeperiod2);
        String url = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/get_receivables.php?filter_company_id=" +stallyid+
                    "&filter_year="+ URLEncoder.encode(String.valueOf(year2), "UTF-8")+
                    "&filter_date_end="+URLEncoder.encode(timeperiod2, "UTF-8")
                    +"&filter_amount="+0+"&filter_type="+2+"&once="+1;
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


                                                    Log.d("onResponse:ledger_name ",jsonObject1.getString("party_name"));
                                                    sales_register_model sales_register_model = new sales_register_model();

                                                    sales_register_model.setName(jsonObject1.getString("party_name"));
                                                    sales_register_model.setAmount(jsonObject1.getString("total_credit_amount_1"));

                                                    sales_register_model.setDate1(timeperiod1);
                                                    sales_register_model.setDate2(timeperiod2);

                                                    partynametype.add(jsonObject1.getString("party_name"));

                                                    movieList.add(sales_register_model);


                                                amountplus = amountplus+jsonObject1.getInt("total_credit_amount_1");
                                                Log.d("amountplus", String.valueOf(amountplus));
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
        //jsonArrayRequest.setRetryPolicy(new MyRetryPolicyWithoutRetry());
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 200000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 200000;
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


            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReceivableActivity.this,
                    android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("This Year");
            arrayAdapter.add("Last Year");
         //   arrayAdapter.add("Custom Date");

            builder.setTitle("Select Time")
                    .setCancelable(true);



            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                   /* if(strName=="Custom Date"){
                        showcalender();
                    }
                    else */if(strName=="This Year")
                    {
                        final Calendar calendartp= Calendar.getInstance();
                        //setting calender to custom date
                        adapter.notifyDataSetChanged();
                        salespaylist.clear();
                        Method_Receivable("preference","amountsort","2019");
                    }
                    else if(strName=="Last Year")
                    {
                        adapter.notifyDataSetChanged();
                        salespaylist.clear();
                        Method_Receivable("preference","amountsort","2018");
                    }
                    Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                }
            });
            builder.show();


        }
    /*    else if (id == R.id.group)
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
          *//*  arrayAdapter.add("This Year");
            arrayAdapter.add("Last Year");
            arrayAdapter.add("Custom Date");*//*

            builder.setTitle("Select Group")
                    .setCancelable(true);



            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                 *//*     if(strName==partynametype.get(which)){

                    }
                    else if(strName=="This Year")
                    {

                    }
                    else *//*
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

                   *//*    JSONObject json = jsonArray.getJSONObject(which);
                        testid = json.getString(product_id);
                        testcategory = json.getString(category_id);
                        Log.d("ResponseHADtest", testid);
                        Log.d("ResponseHADcategory", testcategory);*//*

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

               *//*     Toast toast=Toast.makeText(getContext(),strName,Toast.LENGTH_SHORT);
                    toast.show();*//*
                    dialog.dismiss();
                }
            });
            builder.show();

        }*/
        else if (id == R.id.group) {
            Intent intent = new Intent(getApplicationContext(),SalesChildActivity.class);
            intent.putExtra("type","from_ReceivableActivity");
            intent.putExtra("date1",timeperiod1);
            intent.putExtra("date2",timeperiod2);
            startActivity(intent);
            finish();
        } else if(id==R.id.amount)
        {
            show_amountdialog();
        }


        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sharemenu = menu.findItem(R.id.share);
        MenuItem sortgrp = menu.findItem(R.id.group);
        MenuItem calandar =  menu.findItem(R.id.action_calender);

        calandar.setVisible(false);
        sharemenu.setVisible(false);
      //  sortgrp.setVisible(false);
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
        builder = new AlertDialog.Builder(ReceivableActivity.this);

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
       /* builder.setTitle("Dialog with tabs");*/
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();

    }



    public class ReceivableActivityAdapter extends RecyclerView.Adapter<ReceivableActivityAdapter.ViewHolder> implements Filterable {

        private Context context;
        private List<sales_register_model> list;

        private List<sales_register_model> contactListFiltered;

        public ReceivableActivityAdapter(Context context, List<sales_register_model> list) {
            this.context = context;
            this.list = list;
            this.contactListFiltered = list;
        }


        @Override
        public ReceivableActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_salesregister, parent, false);
            return new ReceivableActivityAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ReceivableActivityAdapter.ViewHolder holder, int position) {
            final sales_register_model movie = contactListFiltered.get(position);

            holder.textTitle.setText(movie.getLed_name());
            if(movie.getVn()==null)
            {
                holder.flatno.setText(movie.getFlatno());
            }
            else
            {
                holder.flatno.setText(movie.getVn()+" | "+movie.getFlatno());
            }

          /*  int amt =(-movie.getAmt_for_receipt());*/
            holder.amount.setText("\u20B9 "+movie.getAmt_for_receipt());


            holder.partylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String partname = movie.getLed_name();
                    Intent intent = new Intent(context,PartyLedgerDetailPage.class);
                    intent.putExtra("partyledgername",partname);
                    intent.putExtra("flatno",movie.getFlatno());
                    intent.putExtra("showpdfstatus","0");
                    intent.putExtra("isparty",movie.getIsparty());
                    intent.putExtra("yearforReceivable",datefordb);
                    intent.putExtra("ledgerid","2");
                    context.startActivity(intent);
                }
            });

    /*        holder.partylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String partname = movie.getName();
                    Intent intent = new Intent(context,SalesRegisterDetailPage.class);
                    intent.putExtra("partyledgername",partname);
                    intent.putExtra("ledgerid","from_payment");
                    intent.putExtra("date1",movie.getDate1());
                    intent.putExtra("date2",movie.getDate2());
                    context.startActivity(intent);

   *//*                 SharedPreferences settings = getSharedPreferences("pref", 0);
                    String jsonString = settings
                            .getString("jsonStringPaymentforallgroup", null);


                    try {
                        jsonArray1 = new JSONArray(jsonString);
                        Log.d("ResponseHADtestArray", String.valueOf(jsonArray1));

                   *//**//*    JSONObject json = jsonArray.getJSONObject(which);
                        testid = json.getString(product_id);
                        testcategory = json.getString(category_id);
                        Log.d("ResponseHADtest", testid);
                        Log.d("ResponseHADcategory", testcategory);*//**//*

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
                    finish();*//*
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return contactListFiltered.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textTitle,flatno,amount,date;
            RelativeLayout partylayout;
            public ViewHolder(View itemView) {
                super(itemView);

                textTitle = itemView.findViewById(R.id.partyname);
                flatno = itemView.findViewById(R.id.flatno);
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
                            if (row.getLed_name().toLowerCase().contains(charString.toLowerCase())
                                    ||row.getAmount().contains(charSequence)
                                    ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())
                                    ) {
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
                int ex =  Integer.parseInt(editText.getText().toString());
                String edit = String.valueOf(ex);
                Toast.makeText(getApplicationContext(),edit,Toast.LENGTH_SHORT).show();
               Method_Receivable(radiocheckbutton,edit,"2018");
                alertDialog.dismiss();
            }
        });
    }
}

