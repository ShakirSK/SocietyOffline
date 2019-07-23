package test.soc365.society365.maneger.Create_billCharges;

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
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.maneger.viewmember.member;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class CreateBill extends AppCompatActivity
{

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<member>memberArrayList ;
    private RecyclerView.LayoutManager layoutManager;
    SpotsDialog spotsDialog;
    String user_id,societyid,usertype,building,flat;
    String Buildingno,Flatno;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createbill_recycle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spotsDialog = new SpotsDialog(this, R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        memberArrayList=new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BillCharges_Adapter(memberArrayList,getApplicationContext());
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        usertype = sharedPreferences.getString("USERTYPE","0" );

        Intent intent=getIntent();
        //user_id=intent.getStringExtra("userid");
        //societyid=intent.getStringExtra("societyid");
        //usertype=intent.getStringExtra("usertype");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {
                        String bill_userid = memberArrayList.get(position).getMemberid();

                        Intent intent=new Intent(getApplicationContext(),BillCharges.class);
                        intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("societyid", societyid);
                        intent.putExtra("bill_userid", bill_userid);
                        intent.putExtra("userid", user_id);
                        intent.putExtra("usertype", usertype);
                        intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                    @Override
                    public void onLongClick(View view, int position)
                    {

                    }
                }));

        membercreatebill();
        }


    public void membercreatebill()
    {
        String membercreatebill= StaticUrl.createbilluserlist+"?society_id="+societyid;

        Log.d("membername",membercreatebill );

        if (Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();
            //memberArrayList.clear();
            StringRequest memberrequest=new StringRequest(Request.Method.GET,
                    membercreatebill,
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
                                    JSONArray jsonArray1=jsonObject.getJSONArray("member_name");

                                    for (int i=0; i< jsonArray1.length();i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        if (jsonObject1.getString("flat_status").equals("1"))
                                        {
                                            String firstname= jsonObject1.getString("first_name");
                                            String lastname = jsonObject1.getString("last_name");
                                            //mylist.setFirstname(jsonObject1.getString("first_name"));
                                            //mylist.setLastname(jsonObject1.getString("last_name"));
                                            String mobileno = jsonObject1.getString("mobile_no");
                                            //mylist.setPhonno(jsonObject1.getString("mobile_no"));
                                            String suserid = jsonObject1.getString("user_id");
                                            String memberid=jsonObject1.getString("user_id");
                                            //mylist.setFlatno(flat);
                                            //mylist.setImage(jsonObject1.getString("profile_pic"));
                                            //mylist.setBuildingno(building);
                                            //mylist.setCreatedon(jsonObject1.getString("created_on"));
                                            //mylist.setUpdatedon(jsonObject1.getString("updated_on"));
                                            //mylist.setFlatstatus(jsonObject1.getString("flat_status"));
                                            //mylist.setSocietyid(jsonObject1.getString("society_id"));
                                            String emailid = jsonObject1.getString("email");
                                            //mylist.setEmailid(jsonObject1.getString("email"));
                                            //mylist.setPassword(jsonObject1.getString("password"));
                                            //mylist.setNo_of_members(jsonObject1.getString("no_of_member"));
                                            //mylist.setUsertype(jsonObject1.getString("user_type"));
                                            String  buildingno=jsonObject1.getString("building_no");
                                            String flatno=jsonObject1.getString("flat_no");
                                            member mylist = new member();
                                            mylist.setFirstname(firstname);
                                            mylist.setLastname(lastname);
                                            mylist.setPhonno(mobileno);
                                            mylist.setBuildingno(buildingno);
                                            mylist.setFlatno(flatno);
                                            mylist.setMemberid(suserid);
                                           //getbuildingname(suserid,firstname,lastname,mobileno);

                                           memberArrayList.add(mylist);

                                        }
                                    }
                                adapter.notifyDataSetChanged();
                                }else
                                {
                                  //  Toast.makeText(getApplicationContext(),"No records found",Toast.LENGTH_SHORT ).show();
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
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Check internet Connection!",Toast.LENGTH_SHORT).show();
        }
    }

    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {

        private GestureDetector gestureDetector;
        private CreateBill.RecyclerTouchListener.ClickListener clickListener;

        public RecyclerTouchListener(Context applicationContext, final RecyclerView recyclerView, final CreateBill.RecyclerTouchListener.ClickListener clickListener)
        {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(applicationContext, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e)
                {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null)
                    {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
        {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e)
        {

        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
        {
        }
        public interface ClickListener
        {
            void onClick(View view, int position);
            void onLongClick(View view, int position);
        }
    }

    private void getbuildingname(final String suserid, final String firstname, final String lastname, final String mobileno)
    {
        //  String Buildingno=jsonObject.getString("building_no");
        //  String Flatno=jsonObject.getString("flat_no");

        String getbuidingurl=StaticUrl.getlabel+suserid;

        Log.d("getbuidingurl", getbuidingurl);
        memberArrayList.clear();

        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest memberrequest=new StringRequest(Request.Method.GET,
                    getbuidingurl,
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
                                    JSONArray jsonArray1=jsonObject.getJSONArray("list");

                                    for (int i=0; i< jsonArray1.length();i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        Buildingno=jsonObject1.getString("building_no");
                                        Flatno=jsonObject1.getString("flat_no");

                                        member mylist = new member();
                                        mylist.setFirstname(firstname);
                                        mylist.setLastname(lastname);
                                        mylist.setPhonno(mobileno);
                                        mylist.setBuildingno(Buildingno);
                                        mylist.setFlatno(Flatno);
                                        mylist.setMemberid(suserid);
                                        //getbuildingname(suserid);

                                        memberArrayList.add(mylist);
                                        adapter.notifyDataSetChanged();
                                    }

                                }else
                                {
                                  //  Toast.makeText(getApplicationContext(),"No records found",Toast.LENGTH_SHORT ).show();
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
        }
        else
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
