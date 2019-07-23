package test.soc365.society365.user.billpayment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.staticurl.StaticUrl;
import test.soc365.society365.user.razorpay.RazorpayActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class BillPayment extends Fragment implements PaymentResultListener
{
    private static final String TAG = RazorpayActivity.class.getSimpleName();
    String useremailid,usermobile,totalstr,uid,sid,utype,billid;
    TextView txtbillstatus,txtbillid,DueDate,service_charge,electricity_charge,water_charges,building_repair_fund,sinking_fund,building_insurance_charges,cable_charges,nonoccupancy_charges,miscellaneous_income,parking_charge,interest_on_due,total,bill_amt;//due_date,
    Button paymentgetway;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.billpayment, container, false);

        service_charge = view.findViewById(R.id.ServiceCharge);
        electricity_charge = view.findViewById(R.id.Electricitycharge);
        water_charges = view.findViewById(R.id.WaterCharges);
        building_repair_fund = view.findViewById(R.id.BuildingRepairFund);
        sinking_fund = view.findViewById(R.id.SinkingFund);
        building_insurance_charges = view.findViewById(R.id.BuildingInsuranceCharges);
        cable_charges = view.findViewById(R.id.CableCharges);
        nonoccupancy_charges = view.findViewById(R.id.NonoccupancyCharges);
        miscellaneous_income = view.findViewById(R.id.MiscellaneousIncome);
        parking_charge = view.findViewById(R.id.ParkingCharge);
        interest_on_due = view.findViewById(R.id.interestondue);
        total = view.findViewById(R.id.total);
        //due_date = view.findViewById(R.id.payment);
        bill_amt=view.findViewById(R.id.billamount);
        DueDate=view.findViewById(R.id.DueDate);
        paymentgetway=view.findViewById(R.id.paymentgetway);
        txtbillid=view.findViewById(R.id.txtbillid);
        txtbillstatus=view.findViewById(R.id.billstatus);

        sharedPreferences = getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        uid = sharedPreferences.getString("USERID","0" );
        sid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );

        Bundle arg=getArguments();
        usermobile=arg.getString("usermobile");
        useremailid=arg.getString("useremailid");

        /*Bundle arg=getArguments();
        uid=arg.getString("userid");
        sid=arg.getString("societyid");
        utype=arg.getString("usertype");*/

        //bill_amt.setInputType(InputType.TYPE_CLASS_NUMBER);
        //bill_amt.setFocusable(false);
        /*due_date.setInputType(InputType.TYPE_NULL);
        due_date.setFocusable(false);
        due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clickevent", "ok");
                FragmentManager transaction = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment =  new DatePickerFragment();
                datePickerFragment.show(transaction,"Select Date");
            }
        });
        */
        getpaymentdetails();

        Checkout.preload(getActivity());
        paymentgetway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RazorpayActivity.class);
                intent.putExtra("amount",totalstr);
                intent.putExtra("useremailid",useremailid);
                intent.putExtra("usermobile",usermobile);
                intent.putExtra("billid",billid);
                startActivity(intent);
               /// startPayment();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        getpaymentdetails();
        super.onResume();
    }

    private void getpaymentdetails()
    {
        String targeturl = StaticUrl.createbilldetails+uid;
        Log.d("paymentdetails", targeturl);
        if (Connectivity.isConnected(getActivity()))
        {
            StringRequest bulletin = new StringRequest(Request.Method.GET,
                    targeturl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("bulletinlist_response", response);

                            //spotsDialog.dismiss();
                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                JSONArray billstatus = jsonObject.getJSONArray("status11");
                                JSONObject billstatusObject =  billstatus.getJSONObject(0);
                                String bill_status = billstatusObject.getString("status");

                                if(bill_status.equals("1"))
                                {
                                    txtbillstatus.setText("Paid");
                                }else {
                                    txtbillstatus.setText("");
                                }


                                if (jsonObject.getString("status").equals("1"))
                                {
                                    Log.d("bulletin_status", jsonObject.getString("status"));

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("invoice");
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                                    service_charge.setText(jsonObject1.getString("service_charge"));
                                    electricity_charge.setText(jsonObject1.getString("electricity_charge"));
                                    water_charges.setText(jsonObject1.getString("water_charges"));
                                    building_repair_fund.setText(jsonObject1.getString("building_repair_fund"));
                                    sinking_fund.setText(jsonObject1.getString("sinking_fund"));
                                    building_insurance_charges.setText(jsonObject1.getString("building_insurance_charges"));
                                    cable_charges.setText(jsonObject1.getString("cable_charges"));
                                    nonoccupancy_charges.setText(jsonObject1.getString("nonoccupancy_charges"));
                                    miscellaneous_income.setText(jsonObject1.getString("miscellaneous_income"));
                                    parking_charge.setText(jsonObject1.getString("parking_charge"));
                                    interest_on_due.setText(jsonObject1.getString("interest_on_due"));
                                    total.setText(jsonObject1.getString("total"));
                                    totalstr = jsonObject1.getString("total");
                                    Log.d("totalstr",totalstr);
                                    DueDate.setText("Due Date: "+jsonObject1.getString("due_date"));
                                    bill_amt.setText("Bill Amount: "+jsonObject1.getString("total"));
                                    billid=jsonObject1.getString("bill_id");
                                    txtbillid.setText("Bill ID: "+billid);

                                } else {
                                    //Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
                                    //spotsDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            MySingleton.getInstance(getActivity()).addToRequestQueue(bulletin);

        } else {
            Toast.makeText(getActivity(), "Check internet Connection!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Bill Payment");
    }

    public void startPayment()
    {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity  activity = getActivity();

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
           // options.put("name", "Razorpay Corp");
           // options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
           // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", totalstr);

            JSONObject preFill = new JSONObject();
            preFill.put("email", useremailid);
            preFill.put("contact", usermobile);

            options.put("prefill", preFill);

            co.open((Activity) getContext().getApplicationContext(),options);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Exception",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(getActivity(), "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            updatepayment(razorpayPaymentID);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(getActivity(), "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void updatepayment(final String razorpayPaymentID)
    {
        if(Connectivity.isConnected(getActivity()))
        {
            String targetUrl = StaticUrl.updatePayment +
                    "?razorpay_payment_id=" + razorpayPaymentID +
                    "&payment_no="+billid+
                    "&id="+uid;

            Log.d("updatePayment",targetUrl);

            final StringRequest  stringRequest = new StringRequest(Request.Method.GET,
                    targetUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                String status = jsonObject.getString("status");
                                String mesg = jsonObject.getString("message");
                                Log.d("mesg",status + mesg);
                                if(status.equals("1"))
                                {
                                    Toast.makeText(getActivity(),mesg,Toast.LENGTH_SHORT).show();
                                    popup(razorpayPaymentID);
                                }else {
                                    Toast.makeText(getActivity(),mesg,Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void popup(String razorpayPaymentID)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_sucess);

        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        // text.setText(msg);
        TextView mesgtxt = dialog.findViewById(R.id.skill);
        TextView paymentid = dialog.findViewById(R.id.paymentid);

        mesgtxt.setText("Payment Success");
        paymentid.setText("Payment Id: "+razorpayPaymentID);
        Log.d("razorpayPaymentID",razorpayPaymentID);
        Button dialogButton = dialog.findViewById(R.id.donebtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }
/*
    public void setDate()
    {
        //showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }
   // @Override
    protected Dialog onCreateDialog(int id)
    {
        // TODO Auto-generated method stub
        if (id == 999)
        {
            return new DatePickerDialog(getContext(), myDateListener, year, month, day);
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
        due_date.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }
    */


}
