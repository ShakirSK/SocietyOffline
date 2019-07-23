package test.soc365.society365.maneger.partyledger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import test.soc365.society365.helper.DatabaseHelper;

import static test.soc365.society365.helper.DatabaseHelper.amount_rec;
import static test.soc365.society365.helper.DatabaseHelper.amountplusdb;
import static test.soc365.society365.helper.DatabaseHelper.bankdate_rec;
import static test.soc365.society365.helper.DatabaseHelper.cqdate_rec;
import static test.soc365.society365.helper.DatabaseHelper.cqno_rec;
import static test.soc365.society365.helper.DatabaseHelper.drcr_ledgername;
import static test.soc365.society365.helper.DatabaseHelper.flatn_Detail_Summ;
import static test.soc365.society365.helper.DatabaseHelper.narration_rec;
import static test.soc365.society365.helper.DatabaseHelper.trailbalance;
import static test.soc365.society365.maneger.Sales_Register.SalesRegisterDetailPage.snamesales;
import static test.soc365.society365.maneger.partyledger.PartyLedgerDetailPage.sname;
import static test.soc365.society365.maneger.partyledger.PartyLedgerDetailPageAdapter.drcr;

public class PartySummary extends AppCompatActivity {


    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.Adapter adapter;
    private List<partydetailmodel> movieList;
    TextView date,partysummaryname,TOtalamount,summaryname;
    //for receipt data vochure type
    TextView BAtotal,Narrationvalue,chequeno,chequedate,bankdate,Narrationvalueforjournal,TOtalamountCash;
    TextView chequeno_contra
            ,chequedate_contra,bankdate_contra,BAtotal_contra;
    View lineview;


    //whole body visible after data
    RelativeLayout wholebody,grosstotal_layout,bankacc_layout,bankacc_layout_contra,bankacc_layoutforcash;
    LinearLayout receiptbody,receiptbodyjournal;
    String vtype,vnumber,ledgername,partyname,Period,cqno,cqdate,bkdate,amount,amtotal,narration;


    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid;

    //progress bar
    SpotsDialog spotsDialog;
    String dsd ;
    String dsd2 ;
    // Database Helper
    DatabaseHelper db;
    String narration_jou;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_summary);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        //Initialize

        lineview = (View)findViewById(R.id.lineview);

        date =(TextView)findViewById(R.id.date);
        summaryname = (TextView)findViewById(R.id.summaryname);
        partysummaryname = (TextView)findViewById(R.id.partysummaryname);
        TOtalamount = (TextView)findViewById(R.id.TOtalamount);

        //for receipt data vochure type
        BAtotal = (TextView)findViewById(R.id.BAtotal);
        Narrationvalue  = (TextView)findViewById(R.id.Narrationvalue);
        chequeno = (TextView)findViewById(R.id.chequeno);
        chequedate = (TextView)findViewById(R.id.chequedate);
        bankdate = (TextView)findViewById(R.id.bankdate);
        Narrationvalueforjournal= (TextView)findViewById(R.id.Narrationvalueforjournal);
        TOtalamountCash= (TextView)findViewById(R.id.TOtalamountCash);

        //for contra
        chequeno_contra = (TextView)findViewById(R.id.chequeno_contra);
        chequedate_contra = (TextView)findViewById(R.id.chequedate_contra);
        bankdate_contra = (TextView)findViewById(R.id.bankdate_contra);


        wholebody = (RelativeLayout)findViewById(R.id.wholebody);
        grosstotal_layout= (RelativeLayout)findViewById(R.id.grosstotal_layout);
        bankacc_layout = (RelativeLayout)findViewById(R.id.bankacc_layout);
        bankacc_layout_contra = (RelativeLayout)findViewById(R.id.bankacc_layout_contra);
        bankacc_layoutforcash = (RelativeLayout)findViewById(R.id.bankacc_layoutforcash);
        receiptbody = (LinearLayout)findViewById(R.id.receiptbody);
        receiptbodyjournal = (LinearLayout)findViewById(R.id.receiptbodyjournal);
        recyclerView = (RecyclerView)findViewById(R.id.recycleviewsummary);


        Intent intent = getIntent();
        vtype = intent.getStringExtra("vouchertype");
        vnumber = intent.getStringExtra("vouchernumber");
        if(vnumber.equals(""))
        {
            vnumber="0";
        }
            ledgername = intent.getStringExtra("ledgername");
        partyname = intent.getStringExtra("partyname");
        Period = intent.getStringExtra("Date");
        Period=formatdate(Period);
        cqno = intent.getStringExtra("ChequeNo");
        cqdate = intent.getStringExtra("ChequeDate");
        if(cqdate==null)
        {

        }else
        {
            cqdate=formatdate(cqdate);
        }
        bkdate = intent.getStringExtra("BankDate");
        if(bkdate==null)
        {

        }else
        {
            bkdate=formatdate(bkdate);
        }
        amount = intent.getStringExtra("Amount");
        narration = intent.getStringExtra("Narration");
        amtotal = intent.getStringExtra("amtotal");



       if(vtype.equals("Bill")||vtype.equals("Journal"))
        {
            //toolbar name
            getSupportActionBar().setTitle("Voucher no: "+vnumber);

            db = new DatabaseHelper(getApplicationContext());

            List<partydetailmodel> allToDos =    db.getdataDetailsummarydata(partyname,vnumber,vtype);
            for (partydetailmodel todo : allToDos) {

                Log.d("ToDogetAmount", todo.getNarration());
                narration_jou=todo.getNarration();
            }
            //db.getdataDetailsummarydata(sname,vnumber,vtype);


            date.setText(Period+" | "+vtype);

            if(flatn_Detail_Summ==null)
            {
                partysummaryname.setText(partyname);
            }
            else
            {
                partysummaryname.setText(flatn_Detail_Summ+" - "+partyname);

            }


        }
        else
       {
           //toolbar name
           getSupportActionBar().setTitle("Voucher no: "+vnumber);


           date.setText(Period+" | "+vtype);

           partysummaryname.setText(partyname);

       }

        offline_datasum();





        movieList = new ArrayList<>();
        adapter = new partySummaryAdapter(getApplicationContext(),trailbalance);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      /*  dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                wholebody.setVisibility(View.VISIBLE);
            }
        }, 5000);



      //  Log.d("perioddate",Period+partynameusedfor_partysummary);
       /* String[] splited = Period.split("-");

        dsd = splited[0];

        dsd2 =splited[1];*/


        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );

        spotsDialog = new SpotsDialog(this,R.style.Custom);

        //getData();

    }


    private void getData() {
        spotsDialog.show();
        String name = "Kamat Pratap Waman";



        String url = null;


        try {
            if (ledgername.equals("test"))
            {
                url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/" +
                        "getpartyledgermobiledata?ledger_name=" + URLEncoder.encode(sname, "UTF-8").replaceAll("\\+", "%20") +
                        "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                        URLEncoder.encode(dsd, "UTF-8") +
                        "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;
        }

        else if (ledgername.equals("from_sales"))
            {
                if(vtype.equals("Bill"))
                {
                    url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/" +
                            "getpartyledgermobiledata?ledger_name=" + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20") +
                            "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                            URLEncoder.encode(dsd, "UTF-8") +
                            "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;

                }
                else
                {/*
                    url = "http://150.242.14.196:8012/society/service/app/get_sales_details1.php/getpartyledgermobiledata?ledger_name="
                            + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20") +
                            "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                            URLEncoder.encode(dsd, "UTF-8") +
                            "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;
                    */
                    url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/getpartyledgermobiledata?ledger_name="
                            + URLEncoder.encode(snamesales, "UTF-8").replaceAll("\\+", "%20") +
                            "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                            URLEncoder.encode(dsd, "UTF-8") +
                            "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;
                }

            }
        else
        {
            url = "http://150.242.14.196:8012/society/service/Party_ledger_detail_appapi.php/" +
                    "getpartyledgermobiledata?ledger_name=" + URLEncoder.encode(ledgername, "UTF-8").replaceAll("\\+", "%20") +
                    "&voucher_number=" + vnumber + "&company_id=" + stallyid + "&voucher_type=" + vtype + "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8") + "&add_prev_bal=" + 0;

        }

            Log.d("onResponse:summary ", url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);


                                            //toolbar name
                                            getSupportActionBar().setTitle("Voucher no: "+jsonObject.getString("voucher_number"));

                                            date.setText(jsonObject.getString("Period")+" | "+jsonObject.getString("voucher_type"));
                                            partysummaryname.setText(jsonObject.getString("party_name"));

                                            TOtalamount.setText("\u20B9 "+jsonObject.getString("grand_total"));

                                            if(jsonObject.getString("voucher_type").equals("Bill")||jsonObject.getString("voucher_type").equals("Journal"))
                                            {
                                                if(jsonObject.getString("voucher_type").equals("Journal")) {
                                                    summaryname.setText("ENTRIES");
                                                    grosstotal_layout.setVisibility(View.GONE);
                                                    receiptbodyjournal.setVisibility(View.VISIBLE);
                                                    Narrationvalueforjournal.setText(jsonObject.getString("narration"));
                                                }
                                                else
                                                {
                                                    summaryname.setText("SUMMARY");
                                                    receiptbodyjournal.setVisibility(View.GONE);
                                                }
                                                receiptbody.setVisibility(View.GONE);
                                                JSONArray jsonArray1=jsonObject.getJSONArray("final_datas");

                                                Log.d("partyledger2 ", String.valueOf(jsonArray1));

                                                for (int i=0;i<jsonArray1.length();i++)
                                                {
                                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                    partydetailmodel movie = new partydetailmodel();
                                                    movie.setAmount(jsonObject1.getString("amount"));
                                                    movie.setVoucher_number(jsonObject1.getString("ledger_name"));
                                                    // movie.setYear(jsonObject1.getInt("releaseYear"));

                                                    movieList.add(movie);

                                                }
                                            }
                                            else
                                            {
                                                lineview.setVisibility(View.GONE);
                                                summaryname.setVisibility(View.GONE);
                                                recyclerView.setVisibility(View.GONE);


                                                if(jsonObject.getString("cheque_no").equals("")) {
                                                   /* receiptbody.setVisibility(View.GONE);
                                                    receiptbodyjournal.setVisibility(View.VISIBLE);
                                                    Narrationvalueforjournal.setText(jsonObject.getString("narration"));
*/

                                                            bankacc_layout.setVisibility(View.GONE);
                                                    bankacc_layoutforcash.setVisibility(View.VISIBLE);
                                                    TOtalamountCash.setText("\u20B9 "+jsonObject.getString("grand_total"));
                                                    Narrationvalue.setText(jsonObject.getString("narration"));
                                                }
                                                else {

                                                    bankacc_layoutforcash.setVisibility(View.GONE);
                                                    bankacc_layout.setVisibility(View.VISIBLE);
                                                    BAtotal.setText("\u20B9 " + jsonObject.getString("bank_ac"));
                                                    Narrationvalue.setText(jsonObject.getString("narration"));
                                                    chequeno.setText("Cheque/DD | No:" + jsonObject.getString("cheque_no"));
                                                    chequedate.setText("Chq Date:" + jsonObject.getString("cheque_date"));
                                                    bankdate.setText("Bank Date:" + jsonObject.getString("bank_date"));
                                                }
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

   public void offline_datasum()
    {

        if(vtype.equals("Bill")||vtype.equals("Journal"))
        {
            if(vtype.equals("Journal")) {
                summaryname.setText("ENTRIES");
                grosstotal_layout.setVisibility(View.GONE);
                receiptbodyjournal.setVisibility(View.VISIBLE);


                Narrationvalueforjournal.setText(narration_jou);
            }
            else
            {

                TOtalamount.setText("\u20B9 "+amountplusdb);

                summaryname.setText("SUMMARY");
                receiptbodyjournal.setVisibility(View.GONE);
            }
            receiptbody.setVisibility(View.GONE);
            /*JSONArray jsonArray1=jsonObject.getJSONArray("final_datas");

            Log.d("partyledger2 ", String.valueOf(jsonArray1));

            for (int i=0;i<jsonArray1.length();i++)
            {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                partydetailmodel movie = new partydetailmodel();
                movie.setAmount(jsonObject1.getString("amount"));
                movie.setVoucher_number(jsonObject1.getString("ledger_name"));
                // movie.setYear(jsonObject1.getInt("releaseYear"));

                movieList.add(movie);

            }*/
        }
        else if(vtype.equals("Contra"))
        {
            lineview.setVisibility(View.GONE);
            summaryname.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            if(ledgername.equals("Cash")) {

                bankacc_layout.setVisibility(View.GONE);
                bankacc_layoutforcash.setVisibility(View.VISIBLE);
                TOtalamountCash.setText("\u20B9 "+amount);
                Narrationvalue.setText(narration);
                TOtalamount.setText("\u20B9 "+amount);

            }
            else {

                if(ledgername.equals(partyname))
                {
                    TOtalamount.setText("\u20B9 "+amount);
                    TOtalamountCash.setText("\u20B9 "+amount);

                    bankacc_layoutforcash.setVisibility(View.VISIBLE);
                    bankacc_layout.setVisibility(View.GONE);
                    bankacc_layout_contra.setVisibility(View.VISIBLE);
                    BAtotal.setText("\u20B9 " +amount);
                    Narrationvalue.setText(narration);
                    chequeno_contra.setText("Cheque/DD | No:" + cqno);
                    chequedate_contra.setText("Chq Date:" + cqdate);
                    bankdate_contra.setText("Bank Date:" + bkdate);

                }
                else
                {
                    TOtalamount.setText("\u20B9 "+amount);
                    /*TOtalamountCash.setText("\u20B9 "+amount);

                    bankacc_layoutforcash.setVisibility(View.VISIBLE);*/
                    bankacc_layout.setVisibility(View.VISIBLE);
                    bankacc_layout_contra.setVisibility(View.GONE);
                    BAtotal.setText("\u20B9 " +amount);
                    Narrationvalue.setText(narration);
                    chequeno.setText("Cheque/DD | No:" + cqno);
                    chequedate.setText("Chq Date:" + cqdate);
                    bankdate.setText("Bank Date:" + bkdate);

                }
                 }
        }
        else
        {
            lineview.setVisibility(View.GONE);
            summaryname.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            if(ledgername.equals("Cash")) {
                                            /*       receiptbody.setVisibility(View.GONE);
                                                    receiptbodyjournal.setVisibility(View.VISIBLE);
                                                    Narrationvalueforjournal.setText(narration_rec);*/


                bankacc_layout.setVisibility(View.GONE);
                bankacc_layoutforcash.setVisibility(View.VISIBLE);
                TOtalamountCash.setText("\u20B9 "+amount);
                Narrationvalue.setText(narration);
                TOtalamount.setText("\u20B9 "+amount);

            }
            else {

                TOtalamount.setText("\u20B9 "+amount);
               /* TOtalamountCash.setText("\u20B9 "+amount);

                bankacc_layoutforcash.setVisibility(View.VISIBLE);*/
                bankacc_layout_contra.setVisibility(View.GONE);
                bankacc_layout.setVisibility(View.VISIBLE);
                BAtotal.setText("\u20B9 " +amount);
                Narrationvalue.setText(narration);
                chequeno.setText("Cheque/DD | No:" + cqno);
                chequedate.setText("Chq Date:" + cqdate);
                bankdate.setText("Bank Date:" + bkdate);
                if(vtype.equals("Payment"))
                {
                    BAtotal.setText("\u20B9 " +amtotal);
                }
            }
        }
    }
    public  String formatdate(String period)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat( "yyyy-MM-dd" , Locale.ENGLISH ).parse(period);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat( "dd/MMM/yyyy" , Locale.getDefault() );
        return formatter.format(date);
    }
}
