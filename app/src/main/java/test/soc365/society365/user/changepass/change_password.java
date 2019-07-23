package test.soc365.society365.user.changepass;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class change_password extends AppCompatActivity
{
    EditText currentpass,newpass,reenterpass;
    Button submit;
    String curpass,npass,rpass,userid;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        currentpass=findViewById(R.id.pass1);
        newpass=findViewById(R.id.pass2);
        reenterpass=findViewById(R.id.pass3);
        submit=findViewById(R.id.submit);

        userid = sharedPreferences.getString("USERID","0" );

        //userid=getIntent().getStringExtra("userid");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(currentpass.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter current password",Toast.LENGTH_SHORT).show();
                }else if(newpass.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter new password",Toast.LENGTH_SHORT).show();
                }else if(reenterpass.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Re-enter new password",Toast.LENGTH_SHORT).show();
                } else if(newpass.getText().toString().trim().equals(reenterpass.getText().toString().trim()))
                {
                    changepass();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Sorry! Password mismatched, Please,try again.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void changepass()
    {
        try
        {
            curpass=URLEncoder.encode(currentpass.getText().toString(),"utf-8");
            rpass=URLEncoder.encode(reenterpass.getText().toString(),"utf-8");
            npass=newpass.getText().toString();

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        Log.d("curpass",curpass);
        Log.d("npass",npass);

        String target = null;
            target = StaticUrl.userchangepassword;
        if(Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest request=new StringRequest(Request.Method.POST, target, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        Log.d("passresponce",response);
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if(jsonObject.getString("status").equals("1"))
                        {
                            Log.d("message",jsonObject.getString("message"));
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            clear();
                        }
                        else
                        {
                            Log.d("messageelse",jsonObject.getString("message"));
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("user_id", userid);
                    /*
                    try {
                        map.put("password", URLEncoder.encode(currentpass.getText().toString(),"utf-8"));
                        map.put("new_password", URLEncoder.encode(reenterpass.getText().toString(),"utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    */
                    map.put("password", String.valueOf(Html.fromHtml(currentpass.getText().toString())));
                    map.put("new_password", String.valueOf(Html.fromHtml(reenterpass.getText().toString())));
                    return map;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    }

    private void clear()
    {
        currentpass.setText("");
        newpass.setText("");
        reenterpass.setText("");
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
