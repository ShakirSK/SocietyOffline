package test.soc365.society365.maneger.viewmember;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

public class ViewFragment extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<member> memberArrayList;
    SpotsDialog spotsDialog;
    SearchView searchview;
    String usrid,sid,utype,sname,saddress;;
    private SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

          searchview = findViewById(R.id.searchview);
          swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
          spotsDialog=new SpotsDialog(this,R.style.Custom);
          recyclerView=findViewById(R.id.recycleview);
          recyclerView.setHasFixedSize(true);
          memberArrayList = new ArrayList<>();
          layoutManager = new LinearLayoutManager(getApplicationContext());
          recyclerView.setLayoutManager(layoutManager);
          recyclerView.setItemAnimator(new DefaultItemAnimator());
          adapter = new MemberAdapter(this,memberArrayList);
          recyclerView.setAdapter(adapter);


        usrid = sharedPreferences.getString("USERID","0" );

        sid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );


        Intent intent=getIntent();
       // usrid=intent.getStringExtra("user_id");
        //sid=intent.getStringExtra("societyid");
        //utype=intent.getStringExtra("usertype");
       // sname=getIntent().getStringExtra("sname");
        //saddress=getIntent().getStringExtra("saddress");

         searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener()
         {
            public boolean onQueryTextSubmit(String query) {
                searchview.clearFocus();     // Close keyboard on pressing IME_ACTION_SEARCH option
                Log.d( "QueryTextSubmit : ", query);
                //load search query
                String targeturl = StaticUrl.memberlist+"?fullname="+query+"&society_id="+sid;
                memberlist(targeturl);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d( "QueryTextChange: ", newText);
                String targeturl = StaticUrl.memberlist+"?fullname="+newText+"&society_id="+sid;
                memberlist(targeturl);
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                //getmylaundrylist(status);
                String targeturl = StaticUrl.memberlist+"?society_id="+sid;
                memberlist(targeturl);
            }
        });
    }



    private void memberlist(String targeturl)
    {
        Log.d("targeturl", targeturl);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();

            memberArrayList.clear();

              StringRequest memberrequest=new StringRequest(Request.Method.GET,
                    targeturl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("response", response);

                            spotsDialog.dismiss();
                            try
                            {
                                swipeRefreshLayout.setRefreshing(true);

                                JSONArray jsonArray=new JSONArray(response);

                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                 if (jsonObject.getString("status").equals("1"))
                                {
                                    JSONArray jsonArray1=jsonObject.getJSONArray("member_name");

                                    for (int i=0; i< jsonArray1.length();i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        member mylist = new member();

                                        if (jsonObject1.getString("flat_status").equals("1"))
                                        {

                                            mylist.setFirstname(jsonObject1.getString("first_name"));
                                            mylist.setLastname(jsonObject1.getString("last_name"));
                                            mylist.setPhonno(jsonObject1.getString("mobile_no"));
                                            mylist.setFlatno(jsonObject1.getString("flat_no"));
                                            mylist.setBuildingno(jsonObject1.getString("building_no"));
                                            mylist.setImage(jsonObject1.getString("profile_pic_url"));
                                            mylist.setMemberid(jsonObject1.getString("user_id"));
                                            mylist.setJoindate(jsonObject1.getString("join_date"));
                                            //  mylist.setCreatedon(jsonObject1.getString("created_on"));
                                            // mylist.setUpdatedon(jsonObject1.getString("updated_on"));
                                            mylist.setFlatstatus(jsonObject1.getString("flat_status"));
                                            mylist.setSocietyid(jsonObject1.getString("society_id"));
                                            mylist.setEmailid(jsonObject1.getString("email"));
                                            // mylist.setPassword(jsonObject1.getString("password"));
                                            // mylist.setNo_of_members(jsonObject1.getString("no_of_member"));
                                            mylist.setUsertype(jsonObject1.getString("user_type"));
                                            memberArrayList.add(mylist);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                    spotsDialog.dismiss();
                                }
                                else
                                {
                                  //  Toast.makeText(getApplicationContext(),"No records found",Toast.LENGTH_SHORT ).show();
                                    spotsDialog.dismiss();
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
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

    @Override
    public void onRefresh() {
        String targeturl = StaticUrl.memberlist+"?society_id="+sid;
        memberlist(targeturl);
    }
}
