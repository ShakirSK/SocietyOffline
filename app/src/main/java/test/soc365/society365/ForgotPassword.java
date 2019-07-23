package test.soc365.society365;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.login.Login;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ForgotPassword extends AppCompatActivity {

    EditText emailid;
    SpotsDialog spotsDialog;
    Button reset;
    TextView back,errortxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        emailid=findViewById(R.id.emailidforgotpass);
        reset=findViewById(R.id.submitforgotpass);
        back=findViewById(R.id.txtlogin);
        errortxt=findViewById(R.id.forgotpassmesg);

        spotsDialog = new SpotsDialog(this, R.style.Custom);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgotPassword.this,Login.class);
                startActivity(intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            validation();
            }
        });



    }

    private void validation()
    {
        if (emailid.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Enter Emailid",Toast.LENGTH_SHORT).show();
            Log.d("msg", "messge");

        }else
        {
            String emailidstr=emailid.getText().toString();
            Log.d("emailid",emailidstr );
            forgotpassword();

        }
        //strtemailid = emailid.getText().toString();
        // strtpassword = password.getText().toString();
    }

    public void forgotpassword()
    {
        String forgotpassword= StaticUrl.forgotpassword;//+ "?email="+emailid.getText().toString();

        Log.d("forgotpassurl",forgotpassword );

        if (Connectivity.isConnected(getApplicationContext())){
            spotsDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    forgotpassword,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            spotsDialog.dismiss();

                            try{
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {

                                    Log.d("msg", jsonObject.getString("message"));
                                    //jsonObject.getString("message"
                                    errortxt.setText("Check your inbox for a Password reset Email.");
                                }else {
                                    Log.d("msg", jsonObject.getString("message"));
                                    errortxt.setText(jsonObject.getString("message"));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("Res_errorList", error.getMessage());
                    spotsDialog.dismiss();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params=new HashMap<String,String>();

                        params.put("email",Html.fromHtml(emailid.getText().toString()).toString());

                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }


    }
