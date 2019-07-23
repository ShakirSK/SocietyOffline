package test.soc365.society365.maneger.process_member_request;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MemberRequestWrite extends AppCompatActivity {

    Button submit;
    String usertype,request_id,societyid,userid;
    EditText actionhere;
    TextView date,id,status,name,usermesg;
    String managerresponse;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_request_write);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        usertype = sharedPreferences.getString("USERTYPE","0" );

        submit=findViewById(R.id.sumbmitbtn);
        actionhere=findViewById(R.id.actionhere);
        date=findViewById(R.id.req_date);
        name=findViewById(R.id.lucky);
        id=findViewById(R.id.req_id);
        status=findViewById(R.id.here);
        usermesg=findViewById(R.id.usermesg);

        request_id=getIntent().getStringExtra("request_id");
        Log.d("request_id",request_id);
        date.setText(getIntent().getStringExtra("request_date"));
        name.setText(getIntent().getStringExtra("Member_name"));
        usermesg.setText(getIntent().getStringExtra("reqmesg"));
        managerresponse=getIntent().getStringExtra("respmesg");

        if(getIntent().getStringExtra("status").equals("1"))
        {
            status.setText("Open");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    validate();
                }
            });
        }
        else
            {
            status.setText("Close");
            actionhere.setText(managerresponse);
            actionhere.setClickable(false);
            actionhere.setInputType(InputType.TYPE_NULL);
            actionhere.setFocusable(false);
            submit.setOnClickListener(null);
            submit.setVisibility(View.INVISIBLE);
        }
        id.setText(getIntent().getStringExtra("request_id"));

        /*
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MemberRequestWrite.this,ProcessMemberRequest.class);
                startActivity(intent);
            }
        });
        */

           }
    private void validate()
    {
        if(actionhere.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Enter Response", Toast.LENGTH_SHORT).show();
        }
        else{
                String responsemessage  = Html.fromHtml(actionhere.getText().toString()).toString();
                Log.d("responsemessage", responsemessage);
                replymember(responsemessage);
        }
    }

    private void replymember(final String responsemessage)
    {
        // + "?request_id=1&response_message=hello

        final String replymember= StaticUrl.replymember;

        Log.d("member_replyurl",replymember +responsemessage);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest stringRequest= new StringRequest(Request.Method.POST,
                    replymember,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            Log.d("responce",response);
                            try {

                                JSONArray jsonArray=new JSONArray(response);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {
                                    Log.d("msg",jsonObject.getString("message"));

                                    final Dialog dialog = new Dialog(MemberRequestWrite.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.dialog_memberadd);

                                    TextView mesgtxt = dialog.findViewById(R.id.skill);
                                    mesgtxt.setText("Message Sent Successfully");

                                    Button dialogYes = dialog.findViewById(R.id.donebtn);
                                    dialogYes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            actionhere.setText("");
                                            Intent intent = new Intent(MemberRequestWrite.this,ProcessMemberRequest.class);
                                            intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            startActivity(intent);
                                        }
                                    });

                                    dialog.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Log.d("error",error.getMessage());
                        }
            })
            {///replymember.php?request_id=73&response_message=hello&manager_id=29
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<String,String>();
                    params.put("request_id", request_id);
                    params.put("response_message",responsemessage);
                    params.put("manager_id",userid);
                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

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

}
