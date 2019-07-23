package test.soc365.society365.maneger.Cash_Bank_Balance;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import test.soc365.society365.BuildConfig;
import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.CheckNetwork;
import test.soc365.society365.maneger.partyledger.PartyLedgerDetailPageAdapter;
import test.soc365.society365.maneger.partyledger.partydetailmodel;

import static test.soc365.society365.helper.DatabaseHelper.openingbalanceA;
import static test.soc365.society365.helper.DatabaseHelper.trailbalance;

public class CashBookDetailPage extends AppCompatActivity {


    private static final String SHARED_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".myfileprovider";

    MenuItem menuItem;
    RecyclerView recyclerView;
    String periodname;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;



    private CashBookDetailPageAdapter adapter;

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
    static String sname;
    String ledgerid,showpdfstatus;

    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
    String cash_or_bank;
    List<partydetailmodel> trailbal;

    // Database Helper
    DatabaseHelper db;
    int cb,last_closing_amount;
    RadioGroup radioGroupPreference ;
    RadioButton radioButton_equalto ;
    RadioButton radioButton_greaterthan;
    String radiocheckbutton;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_book_report);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        db = new DatabaseHelper(getApplicationContext());

        //toolbar name
        getSupportActionBar().setTitle("Cash Report");

        //Initialize
        closingbalance = (LinearLayout)findViewById(R.id.closingbalance);
        openingbalance = (LinearLayout)findViewById(R.id.openingbalance);
        dateendandstart =(TextView)findViewById(R.id.summaryname);
        openingbalanceamount =(TextView)findViewById(R.id.openingbalanceamount);
        closingbalanceamount=(TextView)findViewById(R.id.closingbalanceamount);


        Intent intent = getIntent();
        cash_or_bank = intent.getStringExtra("cash_or_bank");


        day1 = "01";
        day2 = "31";
        month1 = "04";
        month2 = "03";
        year1 =2018;

        year2 =2019;

        timeperiod1=year1+"-"+month1+"-"+day1;

        timeperiod2=year2+"-"+month2+"-"+day2;

        defaultdate=timeperiod1;

        timeperiod1 = intent.getStringExtra("date1");
        timeperiod2 = intent.getStringExtra("date2");
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
        calander = (LinearLayout) findViewById(R.id.calander);
        builder = new AlertDialog.Builder(this);

  /*      timeperiod1 ="2018-04-01";
        timeperiod2 ="2019-03-31";*/


        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CashBookDetailPage.this,
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

                            CashbankarrayDate("preference","amountsort",cash_or_bank,timeperiod1,timeperiod2);
                          /*  dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));
                            CashbankarrayDate("preference","amountsort",cash_or_bank,sdf.format(date1),sdf.format(date2));*/

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

                            CashbankarrayDate("preference","amountsort",cash_or_bank,timeperiod1,timeperiod2);
                          /*  dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));

                            CashbankarrayDate("preference","amountsort",cash_or_bank,sdf.format(date1),sdf.format(date2));*/

                        }
                        Toast toast=Toast.makeText(getApplicationContext(),strName,Toast.LENGTH_SHORT);
                        toast.show();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        trailbal = new ArrayList<partydetailmodel>();
     //   db.array_ini();
        adapter = new CashBookDetailPageAdapter(getApplicationContext(),openingbalanceA,trailbal);

        recyclerView = (RecyclerView)findViewById(R.id.recycleview);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        CashbankarrayDate("preference","amountsort",cash_or_bank,timeperiod1,timeperiod2);



    }

    public void CashbankarrayDate(String preference,String amountsort,String cashbook,String startdate,String enddate)
    {

            getSupportActionBar().setTitle(cashbook);
            /*
            int amountplus = db.getCashBank_OB(cashbook);

            amountplus = (-amountplus);
*/
            int amountplus =  check_date(preference,amountsort,cashbook,defaultdate,startdate,enddate);
            Toast.makeText(getApplicationContext(),String.valueOf(amountplus)+cashbook,Toast.LENGTH_SHORT).show();

            openingbalanceamount.setText("\u20B9" + amountplus);


            List<partydetailmodel> allToDoss = db.getCashBank_Balance(preference,amountsort,cashbook,startdate,enddate);
            trailbal = new ArrayList<partydetailmodel>();


            for (partydetailmodel todo : allToDoss) {
                partydetailmodel td = new partydetailmodel();

                if ("Credit".equals(todo.getDr_cr())) {
                    int a = todo.getAmt_for_receipt();
                    int sd = amountplus - a;

                    amountplus = sd;

                    td.setPrev_balance(String.valueOf(sd));
                    cb=sd;
                } else if ("Debit".equals(todo.getDr_cr())) {
                    int a = todo.getAmt_for_receipt();
                    int sd = amountplus + a;

                    amountplus = sd;

                    td.setPrev_balance(String.valueOf(sd));
                    cb=sd;
                }
                trailbal.add(td);
          /*  openingbalanceamount.setText("\u20B9"+todo.getAmount());
            openbalance=todo.getAmount();
       */
            }
           // Toast.makeText(getApplicationContext(), "size "+openingbalanceA.size()+ " "+ trailbal.size(), Toast.LENGTH_SHORT).show();
           // db.getCashBank_Balance(preference,amountsort,cashbook,startdate,enddate);
            closingbalanceamount.setText("\u20B9"+cb);
            adapter.notifyDataSetChanged();
            adapter = new CashBookDetailPageAdapter(getApplicationContext(),openingbalanceA,trailbal);
            recyclerView.setAdapter(adapter);


      }

    public Integer check_date(String preference,String amountsort,String cashbank,String defaultdate,String startdate,String enddate)
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

                List<partydetailmodel> allToDos =  db.CheckDate_CashBank(preference,amountsort,cashbank,startdate);
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
        builder = new AlertDialog.Builder(CashBookDetailPage.this);

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

                                CashbankarrayDate("preference","amountsort",cash_or_bank,timeperiod1,timeperiod2);

                               /* dateendandstart.setText(sdf.format(date1)+" - "+sdf.format(date2));
                                CashbankarrayDate("preference","amountsort",cash_or_bank,sdf.format(date1),sdf.format(date2));*/


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

        if (id ==  android.R.id.home) {
            onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.search) {
            //searchbar edittext
            return true;
        }
        else if(id==R.id.amount)
        {
            show_amountdialog();
        }
     /*   else if (id==R.id.share)
        {
            if(CheckNetwork.isInternetAvailable(CashBookDetailPage.this)) //returns true if internet available
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
     */   return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sharemenu = menu.findItem(R.id.share);
        MenuItem profile = menu.findItem(R.id.profile);
        MenuItem amount = menu.findItem(R.id.amount);

       // amount.setVisible(false);
        sharemenu.setVisible(false);
        profile.setVisible(false);
        return true;
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
                CashbankarrayDate(radiocheckbutton,edit,cash_or_bank,timeperiod1,timeperiod2);

                alertDialog.dismiss();
            }
        });
    }

}
