package test.soc365.society365.maneger.bulletin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
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
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class BulletineFragment extends AppCompatActivity {

   Button submit;
   SpotsDialog spotsDialog;
   private RecyclerView.Adapter adapter;
   private RecyclerView recyclerView;
   private ArrayList<bulletinemodel> bulletinemodelArrayList;
   private RecyclerView.LayoutManager layoutManager;
   EditText addbulletin,editdate;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String sid,usrid,utype,senderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletinefragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addbulletin=findViewById(R.id.requried);
        editdate=findViewById(R.id.date2);
        submit=findViewById(R.id.submitbtn);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        bulletinemodelArrayList=new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BulletinAdapter(this,bulletinemodelArrayList);
        recyclerView.setAdapter(adapter);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        usrid = sharedPreferences.getString("USERID","0" );
        sid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );

        Intent intent=getIntent();
        //sid=intent.getStringExtra("societyid");
        //usrid=intent.getStringExtra("user_id");
        //utype=intent.getStringExtra("usertype");
        senderid=intent.getStringExtra("user_id");

        editdate.setInputType(InputType.TYPE_NULL);

       editdate.setFocusable(false);

       editdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               setDate();
           }
       });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

        bulletinlist();

    }

    public void validation()
    {
        if (addbulletin.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Bulletin Message", Toast.LENGTH_SHORT).show();
        }else if (editdate.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Select Date" ,Toast.LENGTH_SHORT).show();
        }else
        {
            bulletinadd();
        }

    }

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
        editdate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }

   public void popup()
   {
       final Dialog dialog = new Dialog(BulletineFragment.this);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       dialog.setCancelable(false);
       dialog.setContentView(R.layout.dialog_memberadd);

       TextView mesgtxt = dialog.findViewById(R.id.skill);
       mesgtxt.setText("Bulletin Added Successfully");
       Button dialogButton = dialog.findViewById(R.id.donebtn);
       dialogButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
               bulletinlist();
               addbulletin.setText("");
               editdate.setText("");
           }
       });
       dialog.show();
   }

   private void bulletinadd()
   {
       //URLEncoder.encode(addbulletin.getText().toString(),"utf-8")
       String   bulletinadd = StaticUrl.bulletinboard;

       Log.d("bulletinaddurl", bulletinadd);

       if (Connectivity.isConnected(getApplicationContext()))
       {
           spotsDialog.show();

            StringRequest stringRequest=new StringRequest(Request.Method.POST,
                   bulletinadd,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           Log.d("response", response);

                           spotsDialog.dismiss();
                           try {
                               JSONArray jsonArray=new JSONArray(response);
                               JSONObject jsonObject=jsonArray.getJSONObject(0);

                               if (jsonObject.getString("status").equals("1"))
                               {
                                   Log.d("msg",jsonObject.getString("message"));
                                popup();
                               }else {
                                   Log.d("msg",jsonObject.getString("message"));
                               }
                               spotsDialog.dismiss();
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }
                   }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {

               }
           }
           ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params=new HashMap<String,String>();

                        params.put("activity", Html.fromHtml(addbulletin.getText().toString()).toString());
                        params.put("publish_date", editdate.getText().toString());
                        params.put("society_id",sid);
                        params.put("sender_id", senderid);

                    return params;
                }
            };
           MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

       }else {
           Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

       }
   }


        private void bulletinlist()
        {

            String targeturl = StaticUrl.bulletinlist+"?society_id="+sid;

            Log.d("bulletinlist_targeturl", targeturl);

            if (Connectivity.isConnected(getApplicationContext())) {

                bulletinemodelArrayList.clear();
                spotsDialog.show();

                StringRequest bulletin = new StringRequest(Request.Method.GET,
                        targeturl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("bulletinlist_response", response);

                                spotsDialog.dismiss();
                                try {

                                    JSONArray jsonArray = new JSONArray(response);

                                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                                    if (jsonObject.getString("status").equals("1")) {
                                        Log.d("bulletin_status", jsonObject.getString("status"));

                                        JSONArray jsonArray1 = jsonObject.getJSONArray("bulletin list");

                                        Log.d("bulletin_jsonArray1", String.valueOf(jsonObject.getJSONArray("bulletin list")));

                                        for (int i = 0; i < jsonArray1.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                            bulletinemodel blist = new bulletinemodel();

                                            if (jsonObject1.getString("is_delete").equals("0")) {

                                                blist.setBulletin_id(jsonObject1.getString("bulletin_id"));

                                                Log.d("bulletin_id", jsonObject1.getString("activity"));

                                                blist.setActivity(jsonObject1.getString("activity"));
                                                blist.setPublish_date(jsonObject1.getString("publish_date"));
                                                blist.setCreated_at(jsonObject1.getString("created_at"));
                                                blist.setIs_delete(jsonObject1.getString("is_delete"));

                                                bulletinemodelArrayList.add(blist);
                                            }
                                        }
                                        Log.d("bulletin_arraylist", bulletinemodelArrayList.toString());

                                        adapter.notifyDataSetChanged();
                                        Log.d("arrlistsizebul",""+bulletinemodelArrayList.size());
                                    } else {
                                      //  Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
                                        spotsDialog.dismiss();
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
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(bulletin);

            } else {
                Toast.makeText(getApplicationContext(), "Check internet Connection!", Toast.LENGTH_SHORT).show();
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
