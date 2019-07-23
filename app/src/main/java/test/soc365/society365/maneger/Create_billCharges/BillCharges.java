package test.soc365.society365.maneger.Create_billCharges;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
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

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BillCharges extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    Button button;
    EditText selectdeudate;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String userid,societyid,bill_userid,usertype;

    EditText duedate,service,electricity,watercharge,repairfund,sinkingfund,
            insurancecharge,cablecharge,occurancycharge,income,parkingcharge,intrest;

    TextView totaledittxt;

    Float servicecharge,electricitycharge,watercharge1,repairfundcharge,sinkingfundcharge,
            insurancecharge1,cablecharge1,occurancycharge1,incomecharge,parkingcharge1,intrestcharge,totalamount=0.0f;

    String servicestr="00",electricitystr="00",waterstr="00",repairstr="00",sinkingstr="00",insurancestr="00",cablestr="00",
    occurancystr="00",incomestr="00",parkingstr="00",intreststr="00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_charges);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectdeudate=findViewById(R.id.datea);
        button=findViewById(R.id.submitbtn);


        service=findViewById(R.id.service);
        electricity=findViewById(R.id.service2);
        watercharge=findViewById(R.id.service3);
        repairfund=findViewById(R.id.service4);
        sinkingfund=findViewById(R.id.service5);
        insurancecharge=findViewById(R.id.service6);
        cablecharge=findViewById(R.id.service7);
        occurancycharge=findViewById(R.id.service8);
        income=findViewById(R.id.service9);
        parkingcharge=findViewById(R.id.serviceA);
        intrest=findViewById(R.id.serviceB);
        totaledittxt=findViewById(R.id.total);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        usertype = sharedPreferences.getString("USERTYPE","0" );

        Intent intent=getIntent();
        //userid=intent.getStringExtra("userid");
        //societyid=intent.getStringExtra("societyid");
        bill_userid=intent.getStringExtra("bill_userid");
        //usertype=intent.getStringExtra("usertype");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        selectdeudate.setInputType(InputType.TYPE_NULL);
       /* selectdeudate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDate();
                }
            }
        });*/
        selectdeudate.setInputType(InputType.TYPE_NULL);
        selectdeudate.setFocusable(false);
        selectdeudate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               setDate();
           }
       });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             validation();
            }
        });

        service.addTextChangedListener(serviceWatcher);
        electricity.addTextChangedListener(electricityWatcher);
        watercharge.addTextChangedListener(waterchargeWatcher);
        repairfund.addTextChangedListener(repairfundWatcher);
        sinkingfund.addTextChangedListener(sinkingfundWatcher);
        insurancecharge.addTextChangedListener(insurancechargeWatcher);
        cablecharge.addTextChangedListener(cablechargeWatcher);
        occurancycharge.addTextChangedListener(occurancychargeWatcher);
        income.addTextChangedListener(incomeWatcher);
        parkingcharge.addTextChangedListener(parkingchargeWatcher);
        intrest.addTextChangedListener(intrestWatcher);

    }


    private void popup(String billmesg)
    {
        final Dialog dialog = new Dialog(BillCharges.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_memberadd);

        TextView text =  dialog.findViewById(R.id.skill);
        text.setText(billmesg);

        Button dialogYes = dialog.findViewById(R.id.donebtn);
        dialogYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(BillCharges.this,CreateBill.class);
                intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                clear();
            }
        });


        dialog.show();
        /*LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dailog_billadded, null);

        TextView textViewmesg = alertLayout.findViewById(R.id.skill);

        AlertDialog.Builder alert = new AlertDialog.Builder(BillCharges.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        textViewmesg.setText(billmesg);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
              alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                // String user = etUsername.getText().toString();
                // String pass = etEmail.getText().toString();
                //Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
        */

    }



    TextWatcher serviceWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String servicestring =service.getText().toString();
            if (servicestring.equals(""))
            {
                servicestr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }else
            {
                servicestr=servicestring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }
        }
    };


    TextWatcher electricityWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String electricitystring=electricity.getText().toString();
            if (electricitystring.equals(""))
            {
                electricitystr="0";
               total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                       parkingstr,intreststr);
            }else
            {
                electricitystr= electricitystring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };


  TextWatcher waterchargeWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String waterchargestring=watercharge.getText().toString();
            if (waterchargestring.equals(""))
            {
                waterstr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }else
            {
                waterstr= waterchargestring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }
        }
    };


  TextWatcher repairfundWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String repairfundstring=repairfund.getText().toString();
            if (repairfundstring.equals(""))
            {
                repairstr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }else
            {
                repairstr= repairfundstring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };


  TextWatcher sinkingfundWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String sinkingfundstring=sinkingfund.getText().toString();
            if (sinkingfundstring.equals(""))
            {
                sinkingstr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }else
            {
                sinkingstr= sinkingfundstring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };

  TextWatcher insurancechargeWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String insurancechargestring=insurancecharge.getText().toString();
            if (insurancechargestring.equals(""))
            {
                insurancestr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);


            }else
            {
                insurancestr= insurancechargestring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };

      TextWatcher cablechargeWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String cablechargestring=cablecharge.getText().toString();
            if (cablechargestring.equals(""))
            {
                cablestr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }else
            {
                cablestr= cablechargestring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }
        }
    };

    TextWatcher occurancychargeWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String occurancychargestring=occurancycharge.getText().toString();
            if (occurancychargestring.equals(""))
            {
                occurancystr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }else
            {
                occurancystr= occurancychargestring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };

   TextWatcher incomeWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String incomestring=income.getText().toString();
            if (incomestring.equals(""))
            {
                incomestr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }else
            {
                incomestr= incomestring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };


     TextWatcher parkingchargeWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String parkingchargestring=parkingcharge.getText().toString();
            if (parkingchargestring.equals(""))
            {
                parkingstr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }else
            {
                parkingstr= parkingchargestring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };

    TextWatcher intrestWatcher=new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String intreststring=intrest.getText().toString();
            if (intreststring.equals(""))
            {
                intreststr="0";
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);

            }else
            {
                intreststr= intreststring;
                total(servicestr, electricitystr,waterstr,repairstr,sinkingstr,insurancestr,cablestr,occurancystr,incomestr,
                        parkingstr,intreststr);
            }
        }
    };


    public void total(String service,String electricity,String watercharge,String repairfund,String sinkingfund,String insurancecharge,
                      String cablecharge,String occurancycharge,String income,String parkingcharge,String intrest)
    {
        Log.d("alltotal",service+electricity+watercharge);
        Float servicefl= Float.valueOf(service);
        Float electrifl= Float.valueOf(electricity);
        Float waterfl=Float.valueOf(watercharge);
        Float repairfl=Float.valueOf(repairfund);
        Float sinkingfl=Float.valueOf(sinkingfund);
        Float insurancefl=Float.valueOf(insurancecharge);
        Float cablefl=Float.valueOf(cablecharge);
        Float occurancyfl=Float.valueOf(occurancycharge);
        Float incomefl= Float.valueOf(income);
        Float parkingfl=Float.valueOf(parkingcharge);
        Float intrestfl=Float.valueOf(intrest);

        Float total= servicefl+electrifl+waterfl+repairfl+sinkingfl+insurancefl+cablefl +occurancyfl+incomefl+parkingfl+intrestfl;
        Log.d("total", String.valueOf(total));

        totaledittxt.setText(String.valueOf(total));
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


   /* private void addition(Float servicecharge,Float electricitycharge,Float watercharge1,Float repairfundcharge,Float sinkingfundcharge,
                          Float insurancecharge1,Float cablecharge1,Float occurancycharge1,
                          Float incomecharge,Float parkingcharge1,Float intrestcharge)
    {
      Float  totalamount=servicecharge+electricitycharge+watercharge1+repairfundcharge+sinkingfundcharge+
                insurancecharge1+cablecharge1+occurancycharge1+incomecharge+parkingcharge1+intrestcharge;

      Log.d("totalamount", String.valueOf(totalamount));

    }*/


    public void setDate()
    {
        showDialog(999);
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
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day)
    {
        selectdeudate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }


    private void billchargecal()
    {
        String billcharge = StaticUrl.billcharges;

        Log.d("billurl", billcharge);
        Log.d("useridbill", userid);
        Log.d("useridbillvalue",  "service_charge" +Html.fromHtml(service.getText().toString()).toString()
                +"electricity_charge"+ Html.fromHtml(electricity.getText().toString()).toString()+
                "water_charges"+ Html.fromHtml(watercharge.getText().toString()).toString()+
                "building_repair_fund"+ Html.fromHtml(repairfund.getText().toString()).toString()+
                        "cable_charges"+Html.fromHtml(cablecharge.getText().toString()).toString()+
                                "sinking_fund"+ Html.fromHtml(sinkingfund.getText().toString()).toString()+
                "building_insurance_charges"+ Html.fromHtml(insurancecharge.getText().toString()).toString()+
        "nonoccupancy_charges"+ Html.fromHtml(occurancycharge.getText().toString()).toString()+
                "miscellaneous_income"+ Html.fromHtml(income.getText().toString()).toString()+
        "parking_charge"+Html.fromHtml(parkingcharge.getText().toString()).toString()+
       "interest_on_due"+ Html.fromHtml(intrest.getText().toString()).toString()+
        "user_id"+ bill_userid+
        "sender_id"+ userid+
        "society_id"+ societyid+
        "due_date"+ Html.fromHtml(selectdeudate.getText().toString()).toString()+
       "total"+ totaledittxt.getText().toString());

        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    billcharge,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {

                            Log.d("response", response);

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {
                                    String mesg= jsonObject.getString("status");
                                    //Toast.makeText(getApplicationContext(),"message",Toast.LENGTH_SHORT).show();
                                    popup("Bill Added Successfully");
                                    //Intent intent = new Intent(BillCharges.this,CreateBill.class);
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    //startActivity(intent);

                                }else {
                                    String mesg= jsonObject.getString("status");
                                    popup("Bill Already Created");
                                }

                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("service_charge", Html.fromHtml(service.getText().toString()).toString());
                    param.put("electricity_charge", Html.fromHtml(electricity.getText().toString()).toString());
                    param.put("water_charges", Html.fromHtml(watercharge.getText().toString()).toString());
                    param.put("building_repair_fund", Html.fromHtml(repairfund.getText().toString()).toString());
                    param.put("cable_charges", Html.fromHtml(cablecharge.getText().toString()).toString());
                    param.put("sinking_fund", Html.fromHtml(sinkingfund.getText().toString()).toString());
                    param.put("building_insurance_charges", Html.fromHtml(insurancecharge.getText().toString()).toString());
                    param.put("nonoccupancy_charges", Html.fromHtml(occurancycharge.getText().toString()).toString());
                    param.put("miscellaneous_income", Html.fromHtml(income.getText().toString()).toString());
                    param.put("parking_charge", Html.fromHtml(parkingcharge.getText().toString()).toString());
                    param.put("interest_on_due", Html.fromHtml(intrest.getText().toString()).toString());
                    param.put("user_id", bill_userid);
                    param.put("sender_id", userid);
                    param.put("society_id", societyid);
                    param.put("due_date", Html.fromHtml(selectdeudate.getText().toString()).toString());
                    param.put("total", totaledittxt.getText().toString());
                    return param;
                }


                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response.headers == null) {
                        // cant just set a new empty map because the member is final.
                        response = new NetworkResponse(
                                response.statusCode,
                                response.data,
                                Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                response.notModified,
                                response.networkTimeMs);


                    }

                    return super.parseNetworkResponse(response);
                }
            };


            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void validation()
    {
        if (selectdeudate.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_SHORT).show();
        } else
        if (service.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Service Charges", Toast.LENGTH_SHORT).show();
        } else if (electricity.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Cont.Electricity Charges", Toast.LENGTH_SHORT).show();
        } else if (watercharge.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Cont.Water Charges", Toast.LENGTH_SHORT).show();
        } else if (repairfund.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Cont.Bldg Repaire Fund", Toast.LENGTH_SHORT).show();
        } else if (sinkingfund.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Enter Cont.Sinking Fund", Toast.LENGTH_SHORT).show();
        } else if(insurancecharge.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Enter Bldg Insurance Charges", Toast.LENGTH_SHORT).show();
        } else if(cablecharge.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Enter Cable Charges", Toast.LENGTH_SHORT).show();
        } else if(occurancycharge.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Enter Non Occupancy Charges", Toast.LENGTH_SHORT).show();
        } else if(income.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Enter Miscellaneous Income", Toast.LENGTH_SHORT).show();
        } else if(parkingcharge.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Enter Parking Charges", Toast.LENGTH_SHORT).show();
        } else if(intrest.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Intrest on Dues", Toast.LENGTH_SHORT).show();
        }
        else
        {
            billchargecal();
        }
    }

    private void clear()
    {
        //EditText duedate,service,electricity,watercharge,repairfund,sinkingfund,
        //            insurancecharge,cablecharge,occurancycharge,income,parkingcharge,intrest;
        selectdeudate.setText("");
        service.setText("");
        electricity.setText("");
        watercharge.setText("");
        repairfund.setText("");
        sinkingfund.setText("");
        insurancecharge.setText("");
        cablecharge.setText("");
        occurancycharge.setText("");
        income.setText("");
        parkingcharge.setText("");
        intrest.setText("");

    }

}


