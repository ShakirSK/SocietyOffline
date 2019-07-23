package test.soc365.society365.user.userprofile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import test.soc365.society365.maneger.ManagerProfile;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class edit_profile extends AppCompatActivity
{

    EditText firstname,lastname,mobile;
    String fname,lname,userid,utype;
    Button submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String emailid,pass,building,flat,firstname_str,lastname_str,mobile_str,dateofjoining_str,emid_str;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );

        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        mobile=findViewById(R.id.mobile);
        submit=findViewById(R.id.submit);

      /* Intent intent=getIntent();
        fname=intent.getStringExtra("firstname");
        lname=intent.getStringExtra("lastname");
        mobileno=intent.getStringExtra("mobileno");
        building=intent.getStringExtra("buildingno");
        flat=intent.getStringExtra("flatno");
        userid=intent.getStringExtra("userid");
        utype=intent.getStringExtra("usertype");
        Log.d("memberuserid", userid);
*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validation();
            }
        });
        getprofile();
    }

    private void popup(String message)
    {
        final Dialog dialog = new Dialog(edit_profile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_memberadd);

        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        // text.setText(msg);
        TextView mesgtxt = dialog.findViewById(R.id.skill);
        mesgtxt.setText(message);
        Button dialogButton = dialog.findViewById(R.id.donebtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getprofile();
               // clear();
            }
        });
       /*
        Button dialogYes = dialog.findViewById(R.id.deleteyes);
        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
         */
        dialog.show();
    }

    private void editprofile()
    {
        String editurl= StaticUrl.editmember;
        Log.d("editurl", editurl);
        if(Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest request=new StringRequest(Request.Method.POST, editurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        Log.d("editprofresponse", response);
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if(jsonObject.getString("status").equals("1"))
                        {
                           // Toast.makeText(getApplicationContext(),jsonObject.getString("message") ,Toast.LENGTH_SHORT ).show();
                           //clear();
                            popup("Update Profile Successfully");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message") ,Toast.LENGTH_SHORT ).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error",error.getMessage());
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("first_name", Html.fromHtml(firstname.getText().toString()).toString());
                    map.put("last_name", Html.fromHtml(lastname.getText().toString()).toString());
                    map.put("mobile_no", Html.fromHtml(mobile.getText().toString()).toString());
                    map.put("building_no", building);
                    map.put("flat_no",flat);
                    map.put("user_id",userid);
                    return map;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check Internet Connection" ,Toast.LENGTH_SHORT).show();
        }
    }

    private void clear()
    {
        //firstname.setText("");
       // lastname.setText("");
      // mobile.setText("");
        Intent intent = new Intent(edit_profile.this, ManagerProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void getprofile()
    {
        String profileurl=StaticUrl.getprofile+userid;

        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest stringRequest=new StringRequest(Request.Method.GET,
                    profileurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("profileurl", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {
                                    Log.d("audit_status", jsonObject.getString("status"));

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("profile");
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                                    building=jsonObject1.getString("building_id");
                                    flat=jsonObject1.getString("flat_id");
                                    firstname_str=jsonObject1.getString("first_name");
                                    lastname_str=jsonObject1.getString("last_name");
                                    mobile_str =jsonObject1.getString("mobile_no");
                                    dateofjoining_str=jsonObject1.getString("join_date");
                                    emid_str=jsonObject1.getString("email");

                                    /*if (jsonObject1.getString("flat_status").equals("1"))
                                        flatstatus.setText("Active");
                                    else
                                        flatstatus.setText("Inactive");*/

                                    //userid=jsonObject1.getString("user_id");

                                    //societyname.setText(arg.getString("societyname"));
                                    String profurl = jsonObject1.getString("profile_pic_url");

                                  //  Picasso.with(getApplicationContext()).load(profurl).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.ic_person_black_24dp).into(profile);

                                    firstname.setText(firstname_str);
                                    lastname.setText(lastname_str);
                                    mobile.setText(mobile_str);

                                } else {
                               //     Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();

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

        }else {
            Toast.makeText(getApplicationContext(), "Check internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void validation()
    {
            isValidMobile(mobile.getText().toString());
    }
    private boolean isValidMobile(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() == 10)// || phone.length() > 0
            {
                //if(phone.length() != 10)
                // {
                check = true;
                editprofile();
                Log.d("motest", "false");

                //txtPhone.setError("Not Valid Number");
                // }
            } else {
                Toast.makeText(getApplicationContext(), "Mobile Number Is Not Valid", Toast.LENGTH_SHORT).show();
                Log.d("motest", "true");
                check = false;
            }
        } else
        {
            check=false;
        }
        return check;
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
