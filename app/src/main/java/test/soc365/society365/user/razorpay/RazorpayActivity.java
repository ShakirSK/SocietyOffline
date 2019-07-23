package test.soc365.society365.user.razorpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener{

    String razorpayPaymentID,userid,amount,useremailid,usermobile,billid,username,firstname,lastname;
    private static final String TAG = RazorpayActivity.class.getSimpleName();
    Button btnpayment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView paymentid,mesgtxt,billidtxt,amottxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        billidtxt = findViewById(R.id.billidtxt);
        amottxt= findViewById(R.id.amottxt);
        mesgtxt = findViewById(R.id.skill);
        paymentid = findViewById(R.id.paymentid);

         /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        sharedPreferences = getApplicationContext().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        firstname = sharedPreferences.getString("firstname","0");
        lastname = sharedPreferences.getString("lastname","0");

        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        useremailid = intent.getStringExtra("useremailid");
        usermobile = intent.getStringExtra("usermobile");
        billid = intent.getStringExtra("billid");

        Log.d("usrename",firstname+lastname);
        username = firstname+" "+lastname;

        billidtxt.setText("Bill Id: "+billid);
        amottxt.setText("INR: "+amount);

        Checkout.preload(getApplicationContext());
        // Payment button created by you in XML layout
        btnpayment = findViewById(R.id.btn_pay);
        btnpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
    }

    public void startPayment()
    {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", username);
            //options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            float amountstr = Float.parseFloat(amount);
            options.put("amount", amountstr*100);
            //Log.d("testamount", String.valueOf(amountstr*100));
            //amount,useremailid,usermobile
            JSONObject preFill = new JSONObject();
            preFill.put("email", useremailid);
            preFill.put("contact", usermobile);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void updatepayment(final String razorpayPaymentID)
    {
             if(Connectivity.isConnected(getApplicationContext()))
            {
                String targetUrl = StaticUrl.updatePayment +
                        "?razorpay_payment_id=" + razorpayPaymentID +
                        "&payment_no="+billid+
                        "&paid_amount="+amount;

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

                                    if(status.equals("1"))
                                    {
                                        //Toast.makeText(getApplicationContext(),mesg,Toast.LENGTH_SHORT).show();
                                        popup(razorpayPaymentID);
                                    }else {
                                       // Toast.makeText(getApplicationContext(),mesg,Toast.LENGTH_SHORT).show();
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
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }else {
                Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
    }
    private void popup(String razorpayPaymentID)
    {
        mesgtxt.setText("Payment Success");
        paymentid.setText("Payment Id: "+ razorpayPaymentID);
        /*
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_sucess);

        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        // text.setText(msg);
        TextView mesgtxt = dialog.findViewById(R.id.skill);
        TextView paymentid = dialog.findViewById(R.id.paymentid);

        mesgtxt.setText("Payment Success");
        paymentid.setText("Payment Id: "+ razorpayPaymentID);
        Log.d("c", razorpayPaymentID);
        Button dialogButton = dialog.findViewById(R.id.donebtn);
        dialogButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        */
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
}
