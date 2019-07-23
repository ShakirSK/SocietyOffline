package test.soc365.society365.maneger.editmember;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.maneger.addmember.Building;
import test.soc365.society365.maneger.addmember.flat;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class EditMember extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    Button submit;
    Spinner flatspinner, buildingspinner;
    ArrayList<Building> buildingArrayList;
    ArrayList<flat> flatArrayList;
    String flatid;
    String buildingid;
    EditText firstname, lastname, mobno, emailid, memberstartdate;
    SpotsDialog spotsDialog;
    String user_type,message;
    int i=0,j=0;
    String compareValue;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        submit =findViewById(R.id.submitbtn);
        buildingspinner = findViewById(R.id.bulidingnum);
        flatspinner =findViewById(R.id.flatnum);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        firstname =findViewById(R.id.fname);
        lastname =findViewById(R.id.lname);
        mobno =findViewById(R.id.mobno);
        emailid =findViewById(R.id.emailid);
        memberstartdate = findViewById(R.id.date2);
        spotsDialog = new SpotsDialog(getApplicationContext(), R.style.Custom);


        buildingArrayList = new ArrayList<Building>();
        flatArrayList = new ArrayList<flat>();


        firstname.setText(getIntent().getStringExtra("firstname"));
        lastname.setText(getIntent().getStringExtra("lastname"));
        mobno.setText(getIntent().getStringExtra("mobileno"));

        Log.d("mobileno", "mobileno");

        emailid.setText(getIntent().getStringExtra("emailid"));
        memberstartdate.setText(getIntent().getStringExtra("memberstartdate"));
        String datestr=getIntent().getStringExtra("memberstartdate");
        String bld=getIntent().getStringExtra("buildingno");
        String flt=getIntent().getStringExtra("flatno");

        compareValue=bld;

          //  Log.d("date", datestr);

       // buildingid.setText(getIntent().getStringExtra("firstname"));
       // firstname.setText(getIntent().getStringExtra("firstname"));


        memberstartdate.setInputType(InputType.TYPE_NULL);
        memberstartdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDate();
                }
            }
        });

        getbuildinglist();
        /*for (Building s : buildingArrayList)
        {
            if (!bld.equals(buildingArrayList.get(i).getBuilding_id()))
            {
                j++;
            }
            else
            {
                buildingspinner.setSelection(j);
                break;
            }
            i++;
            Log.d("iValue", String.valueOf(j));
        }*/


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });


        buildingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Building building = buildingArrayList.get(i);

                Toast.makeText(getApplicationContext(), "" + building.getBuilding_no(), Toast.LENGTH_SHORT).show();

                buildingid = building.getBuilding_id();

                //getflatlist(buildingid);

        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
        });

        flatspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                flat flat = flatArrayList.get(position);
                Toast.makeText(getApplicationContext(), "" + flat.getFlat_no(), Toast.LENGTH_SHORT).show();

                flatid = flat.getFlat_no();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void getbuildinglist()
    {
        String getbuildingUrl = StaticUrl.building+"?society_id=1";

        Log.d("building", getbuildingUrl);

        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    getbuildingUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                                if (jsonObject.getString("status").equals("1")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("buildingList");
                                    for (int i = 0; i < jsonArray1.length(); i++) {
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
                                } else {
                                    Log.d("norecord", "No record");
                                }
                                buildingspinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, buildingArrayList));

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
        if (firstname.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter FirstName", Toast.LENGTH_SHORT).show();
        } else if (lastname.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Select LastName", Toast.LENGTH_SHORT).show();
        } else if (mobno.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();

        }

        if (memberstartdate.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Enter Member start date", Toast.LENGTH_SHORT).show();
        } else
        {
            String firstnamestr = firstname.getText().toString();
            editmember();

        }
    }


    private void editmember()
    {
        String editmember=StaticUrl.editmember;

        Log.d("editurl", editmember);

        if (Connectivity.isConnected(getApplicationContext())) {
          //  spotsDialog.show();
            final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    editmember,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);

                            spotsDialog.dismiss();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {
                                   // popup();
                                    Log.d("msg",jsonObject.getString("message") );

                                    LayoutInflater inflater = getLayoutInflater();
                                    View alertLayout = inflater.inflate(R.layout.dialog_memberadd, null);


                                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                                    alert.setTitle("");
                                    // this is set the view from XML inside AlertDialog
                                    alert.setView(alertLayout);
                                    // disallow cancel of AlertDialog on click of back button and outside touch
                                    alert.setCancelable(false);
                /*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });*/
                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {

                                            clear();

                                            // String user = etUsername.getText().toString();
                                            // String pass = etEmail.getText().toString();
                                            //Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    AlertDialog dialog = alert.create();
                                    dialog.show();
                                } else
                                {
                                    Log.d("msg",jsonObject.getString("message") );
                                    Toast.makeText(getApplicationContext(),"Failr" ,Toast.LENGTH_SHORT).show();
                                }
                               // spotsDialog.dismiss();


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
                    try {
                        param.put("first_name", URLEncoder.encode(firstname.getText().toString(), "utf-8"));
                        param.put("last_name", URLEncoder.encode(lastname.getText().toString(), "utf-8"));
                        param.put("mobile_no", URLEncoder.encode(mobno.getText().toString(), "utf-8"));
                        param.put("building_no", buildingid);
                        param.put("flat_no",flatid);
                        param.put("email",URLEncoder.encode(emailid.getText().toString(), "utf-8") );
                        param.put("join_date", URLEncoder.encode(memberstartdate.getText().toString(), "utf-8"));
                        param.put("flat_status","1");
                        param.put("user_type","2");
                        param.put("society_id", "1");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return param;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } else {
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }


    private void popup()
    {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_memberadd, null);


        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
                /*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });*/
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                // String user = etUsername.getText().toString();
                // String pass = etEmail.getText().toString();
                //Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

}
