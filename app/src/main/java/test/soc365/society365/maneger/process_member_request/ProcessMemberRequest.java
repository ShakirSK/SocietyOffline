package test.soc365.society365.maneger.process_member_request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

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

import dmax.dialog.SpotsDialog;


public class ProcessMemberRequest extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<processrequestmodel> processrequestmodelArrayList;
    SpotsDialog spotsDialog;

    String usrid,sid,utype;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button act,act2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_member_rquest);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spotsDialog=new SpotsDialog(this,R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        processrequestmodelArrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new process_request_Adapter(ProcessMemberRequest.this,processrequestmodelArrayList);
        recyclerView.setAdapter(adapter);

        Intent intent=getIntent();
        /*usrid=intent.getStringExtra("user_id");
        sid=intent.getStringExtra("societyid");
        utype=intent.getStringExtra("usertype");*/

        sharedPreferences=getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        usrid=sharedPreferences.getString("USERID", "0");
        sid=sharedPreferences.getString("SOCIETYID","0" );
        utype=sharedPreferences.getString("USERTYPE","0" );

        processrequestlist();

    }

    private void processrequestlist()
    {
        String processmember= StaticUrl.processrequestlist+"?society_id="+sid;

        processrequestmodelArrayList.clear();

        Log.d("processmemberlist", processmember);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();

            StringRequest memberrequest=new StringRequest(Request.Method.GET,
                    processmember,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("response", response);

                            spotsDialog.dismiss();
                            try
                            {
                                JSONArray jsonArray=new JSONArray(response);
                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {
                                    JSONArray jsonArray1=jsonObject.getJSONArray("requestlist");
                                    for (int i=0; i< jsonArray1.length();i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        processrequestmodel listmember=new processrequestmodel();

                                        listmember.setRequest_id(jsonObject1.getString("request_id"));
                                        listmember.setRequest_from_id(jsonObject1.getString("member_name"));
                                        listmember.setRequest_date(jsonObject1.getString("request_date"));
                                        listmember.setRequest_type(jsonObject1.getString("request_type"));
                                        listmember.setUser_message(jsonObject1.getString("user_message"));
                                        listmember.setResponse_message(jsonObject1.getString("response_message"));
                                        listmember.setStatus(jsonObject1.getString("status"));
                                        listmember.setCreated_on(jsonObject1.getString("created_on"));
                                        processrequestmodelArrayList.add(listmember);
                                    }
                                    adapter.notifyDataSetChanged();

                                }
                                else
                                {
                                   // Toast.makeText(getApplicationContext(),"No records found",Toast.LENGTH_SHORT ).show();
                                    spotsDialog.dismiss();
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
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(memberrequest);

        }else
        {
            Toast.makeText(getApplicationContext(),"Check internet Connection!",Toast.LENGTH_SHORT).show();
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
