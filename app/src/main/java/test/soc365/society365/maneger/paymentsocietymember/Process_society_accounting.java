package test.soc365.society365.maneger.paymentsocietymember;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Process_society_accounting extends AppCompatActivity
{
    CardView cardView;
    ImageView search;
    EditText caldate,years;
    Spinner months;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ProcessAcouutingModel> processAcouutingModelArrayList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SpotsDialog spotsDialog;
    Dialog dialog;
    //Dialog d ;
    int yearpicker = Calendar.getInstance().get(Calendar.YEAR);

    EditText membername, amount,paidbill, depositedate,depositeamount,refno;
    Spinner spinner,paymentstatus;
    Button submit;
    EditText caldates,spinnerbank;

    String pmode,pstat,payment_username,statuss,bills,uids,userid,societyid,usertype,smonth,payment_status,payment_bill_amt,payment_userid,checkmonth,url;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_society_accounting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        years=findViewById(R.id.year);
        months=findViewById(R.id.month);
        search=findViewById(R.id.search);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        final List<String> mon=new ArrayList<String>();
        mon.add("January");
        mon.add("February");
        mon.add("March");
        mon.add("April");
        mon.add("May");
        mon.add("June");
        mon.add("July");
        mon.add("August");
        mon.add("September");
        mon.add("October");
        mon.add("November");
        mon.add("December");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mon);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        months.setAdapter(dataAdapter);

        months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                checkmonth=months.getSelectedItem().toString();
                Log.d("months",checkmonth);
                switch (checkmonth)
                {
                    case "January":
                        smonth="01";
                        break;
                    case "February":
                        smonth="02";
                        break;
                    case "March":
                        smonth="03";
                        break;
                    case "April":
                        smonth="04";
                        break;
                    case "May":
                        smonth="05";
                        break;
                    case "June":
                        smonth="06";
                        break;
                    case "July":
                        smonth="07";
                        break;
                    case "August":
                        smonth="08";
                        break;
                    case "September":
                        smonth="09";
                        Log.d("september", smonth);
                        break;
                    case "October":
                        smonth="10";
                        break;
                    case "November":
                        smonth="11";
                        break;
                    case "December":
                        smonth="12";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spotsDialog=new SpotsDialog(this,R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        processAcouutingModelArrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ProcessAcoountingAdapter(getApplicationContext(),processAcouutingModelArrayList);
        recyclerView.setAdapter(adapter);

        years.setInputType(InputType.TYPE_NULL);
        years.setFocusable(false);
        years.setClickable(false);
        years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showYearDialog();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {

                        if(processAcouutingModelArrayList.get(position).getStatus().equals("1"))
                        {
                            Toast.makeText(getApplicationContext(),"Already Paid" , Toast.LENGTH_LONG).show();
                        }
                        else {
                            payment_username = processAcouutingModelArrayList.get(position).getUsername();
                            payment_bill_amt = processAcouutingModelArrayList.get(position).getBill_amount();
                            payment_status = processAcouutingModelArrayList.get(position).getStatus();
                            payment_userid = processAcouutingModelArrayList.get(position).getUser_id();
                            showdialogAcc();
                        }
                        /*FragmentManager manager=getSupportFragmentManager();
                        MemberPaymentDetails payment=new MemberPaymentDetails();
                        Bundle arg=new Bundle();
                        arg.putString("paymentid", processAcouutingModelArrayList.get(position).getPayment_id());
                        arg.putString("username", processAcouutingModelArrayList.get(position).getUsername());
                        arg.putString("billamount", processAcouutingModelArrayList.get(position).getBill_amount());
                        arg.putString("status", processAcouutingModelArrayList.get(position).getStatus());
                        arg.putString("userid", processAcouutingModelArrayList.get(position).getUser_id());
                        payment.setArguments(arg);
                        payment.show(manager, "Payment");*/

                    }
                    @Override
                    public void onLongClick(View view, int position)
                    {

                    }
                }));

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //userid = sharedPreferences.getString("USERID","0" );
       // societyid = sharedPreferences.getString("SOCIETYID","0" );
       // usertype = sharedPreferences.getString("USERTYPE","0" );

        Intent intent=getIntent();
        userid=intent.getStringExtra("user_id");
        usertype=intent.getStringExtra("usertype");
        societyid=intent.getStringExtra("societyid");

        Log.d("senderid",userid);

        memberaccountlist("?sender_id="+userid);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                url= "?year="+years.getText().toString()+ "&month="+smonth+ "&sender_id="+userid;
                memberaccountlist(url);
                Log.d("url", url);
            }
        });
    }



    private void memberaccountlist(String surl){

        processAcouutingModelArrayList.clear();

        String accountinglist= StaticUrl.paymentuserlist+surl; //?year=2018&month=08&sender_id=29";//StaticUrl.processaccountinglist+surl;

        Log.d("accontingmemberlist", accountinglist);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();

            StringRequest stringRequest= new StringRequest(Request.Method.GET,
                    accountinglist,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("response", response);
                            try
                            {
                                JSONArray jsonArray=new JSONArray(response);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {
                                    spotsDialog.dismiss();

                                    JSONArray jsonArray1=jsonObject.getJSONArray("paymentuser");

                                    for (int i=0; i< jsonArray1.length();i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        ProcessAcouutingModel acclistmember=new ProcessAcouutingModel();

                                        acclistmember.setPayment_id(jsonObject1.getString("payment_id"));
                                        acclistmember.setUsername(jsonObject1.getString("username"));
                                        acclistmember.setPayment_mode(jsonObject1.getString("payment_mode"));
                                        acclistmember.setUser_id(jsonObject1.getString("user_id"));
                                        acclistmember.setPayment_no(jsonObject1.getString("payment_no"));
                                        acclistmember.setCharge(jsonObject1.getString("charge"));
                                        acclistmember.setBill_amount(jsonObject1.getString("bill_amount"));
                                        acclistmember.setPaid_amount(jsonObject1.getString("paid_amount"));
                                        acclistmember.setBank_account(jsonObject1.getString("bank_account"));
                                        acclistmember.setRefrence_no(jsonObject1.getString("refrence_no"));
                                        acclistmember.setPayment_received_date(jsonObject1.getString("payment_received_date"));
                                        acclistmember.setPayment_deposit_amount(jsonObject1.getString("payment_deposit_amount"));
                                        acclistmember.setPayment_deposit_date(jsonObject1.getString("payment_deposit_date"));
                                        acclistmember.setStatus(jsonObject1.getString("status"));

                                        processAcouutingModelArrayList.add(acclistmember);
                                    }
                                    adapter.notifyDataSetChanged();
                                }else
                                {
                                    spotsDialog.dismiss();
                                   // Toast.makeText(getApplicationContext(),"No records found",Toast.LENGTH_SHORT ).show();
                                    adapter.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }else
        {
            Toast.makeText(getApplicationContext(),"Check internet Connection!",Toast.LENGTH_SHORT).show();
        }

    }

    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {

        private GestureDetector gestureDetector;
        private Process_society_accounting.RecyclerTouchListener.ClickListener clickListener;

        public RecyclerTouchListener(Context applicationContext, final RecyclerView recyclerView, final Process_society_accounting.RecyclerTouchListener.ClickListener clickListener)
        {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(applicationContext, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e)
                {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null)
                    {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
        {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e)
        {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
        {
        }
        public interface ClickListener
        {
            void onClick(View view, int position);
            void onLongClick(View view, int position);
        }
    }

    private void showdialogAcc()
    {
        dialog = new Dialog(Process_society_accounting.this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        //dialog.setTitle("Member Payment Details");
        dialog.setContentView(R.layout.activity_member_payment_details);

        //TextView text =  dialog.findViewById(R.id.text_dialog);
        // text.setText(msg);

        spinner=dialog.findViewById(R.id.payment);
        spinnerbank=dialog.findViewById(R.id.bank);
        caldate=dialog.findViewById(R.id.date);
        membername=dialog.findViewById(R.id.lucky);
        amount=dialog.findViewById(R.id.amount);
        paidbill=dialog.findViewById(R.id.amount2);
        //depositeamount=dialog.findViewById(R.id.amount4);
        depositedate=dialog.findViewById(R.id.amount3);
        refno=dialog.findViewById(R.id.rno);
        paymentstatus=dialog.findViewById(R.id.paymentstatusselected);

        membername.setText(payment_username);
        membername.setClickable(false);
        membername.setFocusable(false);
        membername.setInputType(InputType.TYPE_NULL);
        amount.setText(payment_bill_amt);
        amount.setClickable(false);
        amount.setInputType(InputType.TYPE_NULL);
        amount.setFocusable(false);
        paidbill.setText(payment_bill_amt);
        paidbill.setClickable(false);
        paidbill.setInputType(InputType.TYPE_NULL);
        paidbill.setFocusable(false);


        depositedate.setInputType(InputType.TYPE_NULL);
        depositedate.setFocusable(false);
        depositedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(999);
            }
        });

        caldate.setInputType(InputType.TYPE_NULL);
        caldate.setFocusable(false);
        caldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(666);
            }
        });


        /*spinnerbank.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("HDFC");
        categories.add("SBI");
        categories.add("BOI");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,categories);
        spinnerbank.setAdapter(adapter);

        spinnerbank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String item =adapterView.getItemAtPosition(i).toString();
                Log.d("selected", item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/
        List<String> categories2 = new ArrayList<String>();
        categories2.add("Online");
        categories2.add("Cheque");
        categories2.add("Cash");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,categories2);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String item =adapterView.getItemAtPosition(i).toString();

                switch (item)
                {
                    case "Online":
                        pmode="1";
                        break;
                    case "Cheque":
                        pmode="2";
                        break;
                    case "Cash":
                        pmode="0";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> categories3 = new ArrayList<String>();
        categories3.add("Paid");
        categories3.add("Unpaid");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,categories3);
        paymentstatus.setAdapter(adapter2);

        paymentstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String item =adapterView.getItemAtPosition(i).toString();

                switch (item)
                {
                    case "Paid":
                        pstat="1";
                        break;
                    case "Unpaid":
                        pstat="0";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button dialogButton = dialog.findViewById(R.id.cancelbtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialogYes = dialog.findViewById(R.id.submitbtn);
        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validation();
               // clear();
            }
        });
        dialog.show();
    }

    private void clear()
    {
        //EditText membername, amount,paidbill, depositedate,depositeamount,refno;
        //    Spinner spinner,paymentstatus;
        //    Button submit;
        //    EditText caldates,spinnerbank;

        membername.setText("");
        amount.setText("");
        paidbill.setText("");
       // depositedate.setText("YYYY-DD-MM");
        refno.setText("");
        //caldates.setText("YYYY-DD-MM");
    }
    private void validation()
    {
        if (membername.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
        } else if (amount.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Select Bill Amount", Toast.LENGTH_SHORT).show();
        } else if (paidbill.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Paid Amount", Toast.LENGTH_SHORT).show();
        } else if (spinnerbank.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Bank Account", Toast.LENGTH_SHORT).show();
        } else if (depositedate.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Deposite Date", Toast.LENGTH_SHORT).show();
        } else
        {
            paymentdetails();
          //  Toast.makeText(getApplicationContext(), "Enter Bank Name", Toast.LENGTH_SHORT).show();
        }
    }

    public void paymentdetails()
    {

        String paymentdetail = StaticUrl.memberpaymentdetail;

        Log.d("memberpayment",paymentdetail);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest request=new StringRequest(Request.Method.POST,
                    paymentdetail,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    Log.d("memberpayment",response);
                    try
                    {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if(jsonObject.getString("status").equals("1"))
                        {
                            String mesg = jsonObject.getString("message");
                            //Toast.makeText(Process_society_accounting.this,mesg, Toast.LENGTH_LONG).show();
                            Toast.makeText(Process_society_accounting.this,"Payment is Successfully Done", Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                            String url="?sender_id="+userid;
                            memberaccountlist(url);

                        }
                        else
                        {
                            String mesg = jsonObject.getString("message");
                            Toast.makeText(Process_society_accounting.this,mesg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                //http://makonlinesolutions.com/aditi/societymgt/api/payment.php?paid_amount=1300&bank_account=SBI
                // &payment_mode=1&refrence_no=a1234&payment_received_date=2016-01-02&payment_deposit_amount=134
                // &payment_deposit_date=2015-02-12&status=1&society_id=1&user_id=110
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String,String> map=new HashMap<String, String>();
                        map.put("user_id", payment_userid);
                        map.put("society_id",societyid);//societyid
                        map.put("bill_amount", payment_bill_amt);
                        map.put("paid_amount", Html.fromHtml(paidbill.getText().toString()).toString());
                        map.put("bank_account", Html.fromHtml(spinnerbank.getText().toString()).toString());
                        map.put("payment_mode", pmode);
                        map.put("refrence_no", Html.fromHtml(refno.getText().toString()).toString());
                        map.put("payment_received_date", Html.fromHtml(caldate.getText().toString()).toString());
                        map.put("payment_deposit_amount",Html.fromHtml(paidbill.getText().toString()).toString());
                        map.put("payment_deposit_date", Html.fromHtml(depositedate.getText().toString()).toString());
                        map.put("status", pstat);

                    return map;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check Internet Connection" , Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setDate(int id)
    {
        showDialog(id);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected Dialog onCreateDialog(int id)
    {
        // TODO Auto-generated method stub
        if (id == 999)
        {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        else if (id == 666)
        {
            return new DatePickerDialog(this, myDateListenerd, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
        {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3,depositedate);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListenerd = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
        {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3, caldate);
        }
    };

    private void showDate(int year, int month, int day, EditText depositedate)
    {
        depositedate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }

    public void showYearDialog()
    {

        final Dialog d = new Dialog(Process_society_accounting.this);
        //d.setTitle("Year Picker");
        d.setCancelable(false);
        d.setContentView(R.layout.yearchooser);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        TextView year_text=(TextView)d.findViewById(R.id.year_text);
        year_text.setText("Select Year");
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+150);
        nopicker.setMinValue(year-150);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);

        //nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                years.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

}


