package test.soc365.society365.maneger.editmember;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Updatemember extends AppCompatActivity
{
    Button submit;
    Spinner flatspinner, buildingspinner;
    ArrayList<Building> buildingArrayList;
    ArrayList<flat> flatArrayList;
    String flatid;
    String buildingid;
    EditText firstname, lastname, mobno, emailid, memberstartdate;
    SpotsDialog spotsDialog;
    String user_type,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatemember);
        submit = findViewById(R.id.submitbtn);
        buildingspinner = findViewById(R.id.bulidingnum);
        flatspinner =findViewById(R.id.flatnum);

        firstname = findViewById(R.id.fname);
        lastname = findViewById(R.id.lname);
        mobno =findViewById(R.id.mobnum);
        emailid = findViewById(R.id.email);
        memberstartdate = findViewById(R.id.date2);
        spotsDialog = new SpotsDialog(getApplicationContext(), R.style.Custom);

        buildingArrayList = new ArrayList<Building>();
        flatArrayList = new ArrayList<flat>();

        /*memberstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment=new DatePickerFragment();
                dialogFragment.show(getFragmentManager(), "Date Picker");
                Log.d("setdata","test" );
            }
        });*/


        getbuildinglist();

        buildingspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Building building = buildingArrayList.get(position);
                Toast.makeText(getApplicationContext(), "" + building.getBuilding_no(), Toast.LENGTH_SHORT).show();

                buildingid = building.getBuilding_id();

                getflatlist(buildingid);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("name", "ganesh");
                //validation();
            }
        });
    }



    public void getflatlist(String buildingid) {
        String getflatUrl = StaticUrl.flat + "&building_id=" + buildingid;

        Log.d("flat", getflatUrl);

        if (Connectivity.isConnected(getApplicationContext())) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    getflatUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray1 = new JSONArray(response);
                                Log.d("response", response);
                                JSONObject jsonObject = (JSONObject) jsonArray1.get(0);
                                if (jsonObject.getString("status").equals("1")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("flat_list");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        flat flat = new flat();
                                        flat.setFlat_id(jsonObject1.getString("flat_id"));
                                        Log.d("flat_id", jsonObject1.getString("flat_id"));
                                        flat.setFlat_no(jsonObject1.getString("flat_no"));
                                        flat.setCreated_on(jsonObject1.getString("created_on"));
                                        flat.setBuilding_id(jsonObject1.getString("building_id"));
                                        flat.setStatus(jsonObject1.getString("status"));
                                        flatArrayList.add(flat);
                                    }
                                } else {
                                    Log.d("norecord", "No record");
                                }
                                flatspinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, flatArrayList));

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


   /* private void validation()
    {
        if (firstname.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Enter FirstName", Toast.LENGTH_SHORT).show();
        } else if (lastname.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Select LastName", Toast.LENGTH_SHORT).show();
        } else if (mobno.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (emailid.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Enter Email-Id", Toast.LENGTH_SHORT).show();
        } else

        if (memberstartdate.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(), "Enter Member start date", Toast.LENGTH_SHORT).show();
        } else
        {
            addmember();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Member");

    }




    */
/*private void popup()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_memberadd, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
        /*alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
}*/
}
