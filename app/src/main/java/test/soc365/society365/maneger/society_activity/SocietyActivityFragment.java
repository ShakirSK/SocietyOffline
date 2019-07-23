package test.soc365.society365.maneger.society_activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class SocietyActivityFragment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Calendar calendar;
    private DatePicker datePicker;
    private int year;
    private int month;
    private int day;
    EditText calenderdate;
    String startdate;
    TextView select;
    //MultiSelectSpinner selectMember;
    Button submit;
    EditText addactivity;//,date
    SpotsDialog spotsDialog;
    CheckBox checkBox;
    Conversion conversion;
    StringBuffer br;

    String fname,lname,memberid;
    String userid;
    int length;

    String societyid,usrid,utype,senderid;

    public static ArrayList<String> userarrayList;
    public static ArrayList<String> finaluserarrayList=new ArrayList<String>();
    public static ArrayList<String> usernames;

    private String listitems,names;
    private boolean[] checkeditems;
    private boolean check=false;
    private int pos,cnt=0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<societyactivitymodel> societyactivitymodelArrayList;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView.Adapter madapter;
    private RecyclerView mrecyclerView;
    private ArrayList<users_model> memberArrayList;// = new ArrayList<>();
    private ArrayList<users_model> membername;
    private RecyclerView.LayoutManager mlayoutManager;

    //private SocietyActmemberAdapter sadapter=new SocietyActmemberAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.societyactivityfragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        select=findViewById(R.id.select);
        //selectMember=findViewById(R.id.selectMember);
        calenderdate=findViewById(R.id.selectdate);
        submit=findViewById(R.id.submitbtn);
        addactivity=findViewById(R.id.requried);
        //date=findViewById(R.id.date2);


        spotsDialog = new SpotsDialog(this, R.style.Custom);

        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        societyactivitymodelArrayList=new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        userarrayList=new ArrayList<String>();


        // add elements to al, including duplicates


        usernames=new ArrayList<String>();
        memberArrayList = new ArrayList<>();

        membername = new ArrayList<>();

        adapter = new SocietyAdapter(this,societyactivitymodelArrayList,conversion);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        usrid = sharedPreferences.getString("USERID","0" );
        Log.d("useridactivity", usrid);
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );
        senderid=sharedPreferences.getString("USERID","0" );

        Intent intent=getIntent();
       /*sid=intent.getStringExtra("societyid");
        usrid=intent.getStringExtra("user_id");
        utype=intent.getStringExtra("usertype");*/
        //senderid=intent.getStringExtra("user_id");
        Log.d("useridactivity", usrid);

        activitylist();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        calenderdate.setInputType(InputType.TYPE_NULL);
        calenderdate.setFocusable(false);
        calenderdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validation();
            }
        });
        checkeditems=new boolean[memberArrayList.size()];

        /* */
        select.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userarrayList.clear();
                usernames.clear();
                listitems="";
                select.setText("");

             //userarrayList.clear();
             LayoutInflater inflater = getLayoutInflater();
             View alertLayout = inflater.inflate(R.layout.societyacty_recycleview, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(SocietyActivityFragment.this);
                alert.setTitle("");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     }
                });
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        for (int i = 0; i < userarrayList.size(); i++)
                        {
                            if (listitems == null && names == null)
                            {
                                listitems = userarrayList.get(i) + ",";

                            } else if (i != userarrayList.size() - 1)
                            {
                                listitems = listitems + userarrayList.get(i) + ",";

                            } else
                                {

                                listitems = listitems + userarrayList.get(i);
                            }

                        }
                        Log.d("user", Arrays.toString(new ArrayList[]{userarrayList}));
                        Log.d("listitems", listitems);

                        Log.d("usernames", Arrays.toString(new ArrayList[]{usernames}));

                        for (int i=0;i<usernames.size();i++)
                        {
                            if(select.getText().equals(""))
                            {
                                select.setText(usernames.get(i).toString());
                            }
                            else
                            {
                                select.setText(select.getText()+ ", "+usernames.get(i).toString());
                            }
                        }

                    }
                });

                mrecyclerView=alertLayout.findViewById(R.id.recycleview);
                mlayoutManager=new LinearLayoutManager(getApplicationContext());
                checkBox=alertLayout.findViewById(R.id.checkbox);

                mrecyclerView.setLayoutManager(mlayoutManager);
                //memberArrayList = new ArrayList<>();
                madapter=new SocietyActmemberAdapter(getApplicationContext(),memberArrayList,conversion,membername);
                mrecyclerView.setAdapter(madapter);

                String activitmembers=StaticUrl.memberlist+"?society_id="+societyid;

                Log.d("activitmembers",activitmembers);

                if (Connectivity.isConnected(getApplicationContext()))
                {
                    memberArrayList.clear();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            activitmembers,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("response", response);
                                    try {
                                        JSONArray jsonArray= new JSONArray(response);
                                        JSONObject jsonObject= jsonArray.getJSONObject(0);

                                        if (jsonObject.getString("status").equals("1"))
                                        {
                                            JSONArray jsonArray1=jsonObject.getJSONArray("member_name");
                                            length=jsonArray1.length();
                                            for (int i=0; i<jsonArray1.length(); i++)
                                            {
                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                                users_model memlist=new users_model();

                                                if (jsonObject1.getString("flat_status").equals("1"))
                                                    {
                                                        fname = jsonObject1.getString("first_name");
                                                        lname = jsonObject1.getString("last_name");
                                                        /// memberid=jsonObject1.getString("user_id");
                                                        memlist.setFirstname(fname);
                                                        memlist.setLastname(lname);
                                                        memlist.setMemberid((jsonObject1.getString("user_id")));
                                                        memberArrayList.add(memlist);
                                                    }
                                            }
                                            madapter.notifyDataSetChanged();
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
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

    }
/*
    private void memberlist()
    {
        if (Connectivity.isConnected(getApplicationContext()))
        {
            memberArrayList.clear();

            String activitmembers=StaticUrl.memberlist+"?society_id="+societyid;

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    activitmembers,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            try {
                                JSONArray jsonArray= new JSONArray(response);
                                JSONObject jsonObject= jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {
                                    JSONArray jsonArray1=jsonObject.getJSONArray("member_name");
                                    length=jsonArray1.length();
                                    for (int i=0; i<jsonArray1.length(); i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        users_model memlist=new users_model();

                                        if (jsonObject1.getString("flat_status").equals("1"))
                                        {
                                            fname = jsonObject1.getString("first_name");
                                            lname = jsonObject1.getString("last_name");
                                            /// memberid=jsonObject1.getString("user_id");
                                            memlist.setFirstname(fname);
                                            memlist.setLastname(lname);
                                            memlist.setMemberid((jsonObject1.getString("user_id")));
                                            memberArrayList.add(memlist);
                                        }else {
                                            Log.d("Norecords","norecords");
                                        }
                                        ArrayAdapter<users_model> adapter = new ArrayAdapter<users_model>(SocietyActivityFragment.this, android.R.layout.simple_list_item_multiple_choice, memberArrayList);
                                        selectMember.setListAdapter(adapter);
                                    }
                                    madapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

*/
    public void validation()
    {
    if (addactivity.getText().toString().equals("")) {
    Toast.makeText(getApplicationContext(), "Enter Activity Message", Toast.LENGTH_SHORT).show();
    }else if (calenderdate.getText().toString().equals(""))
    {
    Toast.makeText(getApplicationContext(),"Select Date" ,Toast.LENGTH_SHORT).show();
    }else if (select.getText().toString().equals(""))
    {
    Toast.makeText(getApplicationContext(), "Select Member", Toast.LENGTH_SHORT).show();
    }else
    {
    activityadd();
    }
    }

    private void activityadd()
    {
    //URLEncoder.encode(addbulletin.getText().toString(),"utf-8")

    String activityadd=StaticUrl.activityboard;

    Log.d("activityadd",activityadd);
    /* Conversion conversion=new Conversion() {
    @Override
    public String passstraing()
    {
        return null;
    }
    };*/
    //  SocietyActmemberAdapter sadapter=new SocietyActmemberAdapter(conversion);


    if (Connectivity.isConnected(getApplicationContext()))
    {
    spotsDialog.show();
     StringRequest stringRequest=new StringRequest(Request.Method.POST,
            activityadd,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if (jsonObject.getString("status").equals("1"))
                        {
                            spotsDialog.dismiss();
                            Log.d("msg",jsonObject.getString("message"));
                            popup();
                        }else
                        {
                            Log.d("msg",jsonObject.getString("message"));
                            spotsDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            spotsDialog.dismiss();
        }
    }
    ){
         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String,String> params=new HashMap<String,String>();
                 params.put("activity", Html.fromHtml(addactivity.getText().toString()).toString());
                 params.put("publish_date", calenderdate.getText().toString());
                 params.put("society_id",societyid);
                 params.put("sender_id", senderid);
                 params.put("list_of_member", listitems);
             return params;
         }
     };
    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }else {
    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

    }

    }



    public void popup()
    {
    final Dialog dialog = new Dialog(SocietyActivityFragment.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.dialog_memberadd);

      TextView text = (TextView) dialog.findViewById(R.id.skill);
      text.setText("Message Send Successfully");

    Button dialogYes = dialog.findViewById(R.id.donebtn);
    dialogYes.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    dialog.dismiss();
    clear();
    activitylist();
    }
    });


    dialog.show();

    }
    public void activitylist()
    {
    societyactivitymodelArrayList.clear();
    String targeturl= StaticUrl.activitylist+"?society_id="+societyid;
    Log.d("activitylist_targeturl",targeturl );

    if (Connectivity.isConnected(getApplicationContext()))
    {
    spotsDialog.show();
    final StringRequest activity=new StringRequest(Request.Method.GET,
    targeturl,
    new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("activitylist_response", response);

            try {
                JSONArray jsonArray=new JSONArray(response);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                if (jsonObject.getString("status").equals("1"))
                {
                   spotsDialog.dismiss();
                    JSONArray jsonArray1=jsonObject.getJSONArray("Activity_list");

                    for (int i=0; i<jsonArray1.length(); i++)
                    {
                         JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                        societyactivitymodel actlist=new societyactivitymodel();

                        String str= jsonObject1.getString("is_delete");//jsonObject1.getString("is_delete").toString();

                        if (str.equals("0"))
                        {
                            // if (jsonObject1.getString(""))

                            actlist.setActivity(jsonObject1.getString("activity"));
                            actlist.setActivity_id(jsonObject1.getString("activity_id"));
                            actlist.setPublish_date(jsonObject1.getString("publish_date"));
                            actlist.setList_of_member(jsonObject1.getString("list_of_member"));
                            actlist.setList_of_member_lable(jsonObject1.getString("list_of_member_label"));
                            actlist.setCreated_at(jsonObject1.getString("created_at"));
                            societyactivitymodelArrayList.add(actlist);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else
                {
               //     Toast.makeText(getApplicationContext(),"No records found",Toast.LENGTH_SHORT ).show();
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
    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(activity);
    }else
    {
    Toast.makeText(getApplicationContext(),"Check internet Connection!",Toast.LENGTH_SHORT).show();
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
        calenderdate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String firstname;
        String lastname;
        TextView fname,lname;

        fname=view.findViewById(R.id.mname);
        firstname=fname.getText().toString();
        lname=view.findViewById(R.id.lname);
        lastname=lname.getText().toString();
        if (memberArrayList.get(0).getFirstname().equals(firstname)&&memberArrayList.get(0).getLastname().equals(lastname))
        {
            String id = memberArrayList.get(0).getMemberid();
            select.setText(firstname + " " + lastname + ",");
        }

         for(i=0;i<length;i++)
            {
                if (memberArrayList.get(i).getFirstname().equals(firstname)&&memberArrayList.get(i).getLastname().equals(lastname))
                {
                    String id=memberArrayList.get(i).getMemberid();
                    select.setText(firstname +" "+lastname+",");
                }
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void clear()
    {
        userarrayList.clear();
        usernames.clear();
        select.setText("");
        addactivity.setText("");
        calenderdate.setText("");
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
