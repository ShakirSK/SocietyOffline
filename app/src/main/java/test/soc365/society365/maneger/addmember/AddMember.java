package test.soc365.society365.maneger.addmember;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import test.soc365.society365.maneger.viewmember.ViewFragment;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;


public class AddMember extends AppCompatActivity {

    Button submit;
    Spinner flatspinner, buildingspinner;
    ArrayList<Building> buildingArrayList;
    ArrayList<flat> flatArrayList;
    ArrayList<flat> flatAllottedArraylist;
    String flatid="",stat,societyid;
    String buildingid;
    String editstat;
    int count = 0;
    String memberuserid;
    EditText firstname, lastname, mobno, emailid, memberstartdate;
    SpotsDialog spotsDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String user_type,message,fname,lname,mno,email,startdate,building,flat,status,profilepic,uid;

    String userid,sid,utype;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        submit =findViewById(R.id.submitbtn);
        buildingspinner = findViewById(R.id.bulidingnum);
        flatspinner = findViewById(R.id.flatnum);

        firstname = findViewById(R.id.fname);
        lastname = findViewById(R.id.lname);
        mobno = findViewById(R.id.mobnum);
        emailid = findViewById(R.id.email);
        memberstartdate = findViewById(R.id.date2);
        spotsDialog = new SpotsDialog(this, R.style.Custom);

        buildingArrayList = new ArrayList<Building>();
        flatArrayList = new ArrayList<flat>();
        flatAllottedArraylist = new ArrayList<flat>();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        //utype = sharedPreferences.getString("USERTYPE","0" );

        Intent intent=getIntent();
        editstat=intent.getStringExtra("editstatus");

        Log.d("editstat", editstat);

        if(editstat.equals("1"))
        {
            getSupportActionBar().setTitle("Add Member");

            //userid=intent.getStringExtra("user_id");
            utype=intent.getStringExtra("usertype");
            //societyid=intent.getStringExtra("societyid");

            memberstartdate.setInputType(InputType.TYPE_NULL);
            memberstartdate.setFocusable(false);
            memberstartdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDate();
                }
            });
        }
        else
        if(editstat.equals("2"))
        {
            getSupportActionBar().setTitle("Update Member");

            fname=intent.getStringExtra("firstname");
            lname=intent.getStringExtra("lastname");
            mno=intent.getStringExtra("mobileno");
            email=intent.getStringExtra("emailid");
            startdate=intent.getStringExtra("memberstartdate");
            building=intent.getStringExtra("buildingno");
            flat=intent.getStringExtra("flatno");
            status=intent.getStringExtra("status");
            profilepic=intent.getStringExtra("profilepic");

            memberuserid=intent.getStringExtra("memberuserid");
            utype=intent.getStringExtra("usertype");
           // societyid=intent.getStringExtra("societyid");

            firstname.setText(fname);
            lastname.setText(lname);
            mobno.setText(mno);
            emailid.setText(email);
            memberstartdate.setText(startdate);

            emailid.setInputType(InputType.TYPE_NULL);
            emailid.setClickable(false);
            emailid.setFocusable(false);

            //memberstartdate.setInputType(InputType.TYPE_NULL);
            memberstartdate.setClickable(false);
            memberstartdate.setInputType(InputType.TYPE_NULL);
            memberstartdate.setFocusable(false);
            memberstartdate.setOnClickListener(null);

        }

        buildingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                flatid="";
                flatspinner.setAdapter(null);
                Building building = buildingArrayList.get(position);
               //Toast.makeText(getApplicationContext(), "" + building.getBuilding_no(), Toast.LENGTH_SHORT).show();
                    //do nothing.
                    buildingid = building.getBuilding_id();
                    Log.d("buildingid", buildingid +" "+ building);

                    getflatlist(buildingid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        flatspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {

                if(editstat.equals("1"))
                {
                    flat flat = flatArrayList.get(position);
                    //Toast.makeText(getApplicationContext(), "" + flat.getFlat_no(), Toast.LENGTH_SHORT).show();

                    String flatstatus = flatArrayList.get(position).getStatus();

                    if(flatstatus.equals("0"))
                    {
                        flatid = flat.getFlat_id();
                    }else
                    if(flatstatus.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(),"Flat Already Allotted" ,Toast.LENGTH_LONG ).show();
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Select Flat" ,Toast.LENGTH_LONG ).show();
                    }
                }
                else if (editstat.equals("2"))
                {
                    flat flat = flatAllottedArraylist.get(position);
                    //Toast.makeText(getApplicationContext(), "" + flat.getFlat_no(), Toast.LENGTH_SHORT).show();

                    String flatstatus = flatAllottedArraylist.get(position).getStatus();

                    flatid = flat.getFlat_id();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("nothingisselected", "Nothing is selected");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("test","submit");
                validation();

            }
        });


        getbuildinglist();

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
        memberstartdate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }


    private void getflatlist(String buildingid)
    {
        String getflatUrl = StaticUrl.flat + "?building_id=" + buildingid;

        Log.d("flat", getflatUrl);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            flatArrayList.clear();
            flatAllottedArraylist.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    getflatUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            try
                            {
                                JSONArray jsonArray1 = new JSONArray(response);
                                Log.d("response", response);
                                JSONObject jsonObject = (JSONObject) jsonArray1.get(0);
                                if (jsonObject.getString("status").equals("1"))
                                {
                                    JSONArray jsonArray = jsonObject.getJSONArray("flat_list");
                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        flat flatm = new flat();
                                        if(jsonObject1.getString("status").equals("0"))
                                        {
                                            flatm.setFlat_id(jsonObject1.getString("flat_id"));
                                            Log.d("flat_id", jsonObject1.getString("flat_id"));
                                            flatm.setFlat_no(jsonObject1.getString("flat_no"));
                                            String flatstr = jsonObject1.getString("flat_no");

                                            flatm.setCreated_on(jsonObject1.getString("created_on"));
                                            flatm.setBuilding_id(jsonObject1.getString("building_id"));
                                            flatm.setStatus(jsonObject1.getString("status"));
                                            flatArrayList.add(flatm);
                                            flatAllottedArraylist.add(flatm);
                                        }

                                        if(jsonObject1.getString("status").equals("1") && jsonObject1.getString("flat_no").equals(flat))
                                        {
                                            flatm.setFlat_id(jsonObject1.getString("flat_id"));
                                            Log.d("flat_id", jsonObject1.getString("flat_id"));
                                            flatm.setFlat_no(jsonObject1.getString("flat_no"));
                                            String flatstr = jsonObject1.getString("flat_no");

                                            flatm.setCreated_on(jsonObject1.getString("created_on"));
                                            flatm.setBuilding_id(jsonObject1.getString("building_id"));
                                            flatm.setStatus(jsonObject1.getString("status"));
                                            flatAllottedArraylist.add(flatm);
                                        }

                                    }

                                    Log.d("flatArrayList", flatArrayList.toString());
                                    Log.d("flatAllottedArraylist", flatAllottedArraylist.toString());

                                    if(editstat.equals("1"))
                                    {
                                        ArrayAdapter<flat> adapterf = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, flatArrayList);
                                        flatspinner.setAdapter(adapterf);
                                    }else if(editstat.equals("2"))
                                    {
                                        ArrayAdapter<flat> adapterf = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, flatAllottedArraylist);
                                        flatspinner.setAdapter(adapterf);
                                        //adapterf.notifyDataSetChanged();
                                        for (int pos =0; pos<adapterf.getCount();pos++)
                                        {
                                            if(flatAllottedArraylist.get(pos).getFlat_no().equals(flat))
                                            {
                                                flatspinner.setSelection(pos);
                                                Log.d("flatno", flatAllottedArraylist.get(pos).getFlat_no());
                                            }
                                        }
                                    }

                                } else {
                                    Log.d("norecord", "No record");
                                }
                               // flatspinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, flatArrayList));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getbuildinglist()
    {
        String getbuildingUrl = StaticUrl.building+"?society_id="+societyid;//+societyid;
        Log.d("building", getbuildingUrl);
        buildingArrayList.clear();
        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    getbuildingUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            Log.d("buildingresonse",response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                                if (jsonObject.getString("status").equals("1")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("buildingList");
                                    for (int i = 0; i < jsonArray1.length(); i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        Building mylist = new Building();
                                        mylist.setBuilding_id(jsonObject1.getString("building_id"));
                                        mylist.setBuilding_no(jsonObject1.getString("building_no"));


                                        mylist.setSociety_id(jsonObject1.getString("society_id"));
                                        mylist.setCreated_on(jsonObject1.getString("created_on"));
                                        mylist.setNo_of_flats(jsonObject1.getString("no_of_flats"));
                                        mylist.setStatus(jsonObject1.getString("status"));

                                        buildingArrayList.add(mylist);
                                    }
                                    ArrayAdapter<Building> adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, buildingArrayList);
                                    buildingspinner.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    for (int pos =0; pos<adapter.getCount();pos++)
                                    {
                                        if(buildingArrayList.get(pos).getBuilding_no().equals(building))
                                        {
                                            buildingspinner.setSelection(pos);
                                            Log.d("buldno", buildingArrayList.get(pos).getBuilding_no());
                                        }
                                    }
                                } else {
                                    Log.d("norecord", "No record");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void validation()
    {
        Log.d("test", "validation");
        if (firstname.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Enter FirstName", Toast.LENGTH_SHORT).show();
        } else if (lastname.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Enter LastName", Toast.LENGTH_SHORT).show();
        } else if (mobno.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        }
        else if (emailid.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Enter Email-Id", Toast.LENGTH_SHORT).show();
        }
        else if (memberstartdate.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Select Membership Date", Toast.LENGTH_SHORT).show();
        }/*else if (flatid.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Select Flat No", Toast.LENGTH_SHORT).show();
        }*/
        else if ((isValidMail(emailid.getText().toString())) && (isValidMobile(mobno.getText().toString())))
        {
            if(editstat.equals("1"))
            {

                String addmember =StaticUrl.addmember;
                Log.d("test", addmember);
                addmember(addmember);
            }
            else if(editstat.equals("2"))
            {
                //String editmember =StaticUrl.editmember;
                Updatemember();
            }
        }
    }

    private void addmember(String addmember)
    {
        Log.d("addmember", addmember);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();
           StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    addmember,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("addresponse", response);

                            spotsDialog.dismiss();
                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {

                                    Log.d("msg",jsonObject.getString("message") );
                                    message = jsonObject.getString("message");
                                    uid=jsonObject.getString("user");
                                    //popup();
                                    popup("Member Added Successfully");
                                }else
                                {
                                    Log.d("msg",jsonObject.getString("message") );
                                    message = jsonObject.getString("message");
                                    Toast.makeText(getApplicationContext(),message ,Toast.LENGTH_SHORT).show();
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
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param=new HashMap<String, String>();
                    param.put("first_name", Html.fromHtml(firstname.getText().toString()).toString());
                    param.put("last_name", Html.fromHtml(lastname.getText().toString()).toString());
                    param.put("mobile_no", Html.fromHtml(mobno.getText().toString()).toString());
                    param.put("building_no", buildingid);
                 //   param.put("flat_no",flatid);
                    param.put("email",emailid.getText().toString());
                    param.put("join_date", Html.fromHtml(memberstartdate.getText().toString()).toString());
                    param.put("flat_status","1");
                    param.put("user_type","2");//utype
                    param.put("society_id",societyid);
                    return param;
                   /* Map<String,String> param=new HashMap<String, String>();
                    try {
                        param.put("first_name", URLEncoder.encode(firstname.getText().toString(), "utf-8"));
                        param.put("last_name", URLEncoder.encode(lastname.getText().toString(), "utf-8"));
                        param.put("mobile_no", URLEncoder.encode(mobno.getText().toString(), "utf-8"));
                        param.put("building_no", buildingid);
                        param.put("flat_no",flatid);
                        param.put("email",emailid.getText().toString());
                        param.put("join_date", URLEncoder.encode(memberstartdate.getText().toString(), "utf-8"));
                        param.put("flat_status","1");
                        param.put("user_type","2");//utype
                        param.put("society_id",societyid);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return param;
                    */
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            //URLEncoder.encode(emailid.getText().toString(), "utf-8")
        }
    }

    private void Updatemember()
    {
        String editmember =StaticUrl.editmember;

        Log.d("editmember", editmember);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    editmember,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("editresponse", response);

                            //spotsDialog.dismiss();
                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {

                                    Log.d("msg",jsonObject.getString("message") );
                                    message = jsonObject.getString("message");
                                    //popup();
                                    popup("Member Updated Successfully");
                                }else
                                {
                                    Log.d("msg",jsonObject.getString("message") );
                                    message = jsonObject.getString("message");
                                    Toast.makeText(getApplicationContext(),message ,Toast.LENGTH_SHORT).show();
                                }
                                spotsDialog.dismiss();


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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param=new HashMap<String, String>();
                    param.put("first_name",Html.fromHtml(firstname.getText().toString()).toString());
                    param.put("last_name", Html.fromHtml(lastname.getText().toString()).toString());
                    param.put("mobile_no",Html.fromHtml(mobno.getText().toString()).toString());
                    param.put("building_no", buildingid);
                    param.put("flat_no",flatid);
                    // param.put("email",emailid.getText().toString());
                    // param.put("join_date", URLEncoder.encode(memberstartdate.getText().toString(), "utf-8"));
                    //param.put("flat_status","1");
                    //param.put("user_type","2");
                    //param.put("society_id",societyid);
                    param.put("user_id",memberuserid );//"?society_id=1";
                    return param;
                    /*Map<String,String> param=new HashMap<String, String>();
                    try {
                        param.put("first_name", URLEncoder.encode(firstname.getText().toString(), "utf-8"));
                        param.put("last_name", URLEncoder.encode(lastname.getText().toString(), "utf-8"));
                        param.put("mobile_no", URLEncoder.encode(mobno.getText().toString(), "utf-8"));
                        param.put("building_no", buildingid);
                        param.put("flat_no",flatid);
                        // param.put("email",emailid.getText().toString());
                        // param.put("join_date", URLEncoder.encode(memberstartdate.getText().toString(), "utf-8"));
                        //param.put("flat_status","1");
                        //param.put("user_type","2");
                        //param.put("society_id",societyid);
                        param.put("user_id",memberuserid );//"?society_id=1";
                    } catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    return param;
                    */
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
            //URLEncoder.encode(emailid.getText().toString(), "utf-8")
        }
    }

    private void popup(String message)
    {
        final Dialog dialog = new Dialog(AddMember.this);
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
                clear();
                Intent intent = new Intent(AddMember.this, ViewFragment.class);
                intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
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

    private void clear()
    {
        firstname.setText("");
        lastname.setText("");
        emailid.setText("");
        mobno.setText("");
        memberstartdate.setText("");
        flatspinner.setSelection(0);
        buildingspinner.setSelection(0);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(editstat.equals("2")) {
                    Intent intent = new Intent(AddMember.this, ViewFragment.class);
                    startActivity(intent);
                }/*else {
                    Intent intent = new Intent(AddMember.this, ViewFragment.class);
                    startActivity(intent);
                }*/
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(editstat.equals("2"))
        {
            Intent intent = new Intent(AddMember.this, ViewFragment.class);
            startActivity(intent);
        }
        super.onBackPressed();
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
    private boolean isValidMail(String email)
    {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check) {
            Toast.makeText(getApplicationContext(),"Not Valid Email",Toast.LENGTH_SHORT).show();
            // txtEmail.setError("Not Valid Email");
        }
        return check;
    }
}



