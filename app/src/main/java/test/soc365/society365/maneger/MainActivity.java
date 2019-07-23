package test.soc365.society365.maneger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.login.Login;
import test.soc365.society365.R;
import test.soc365.society365.maneger.Create_billCharges.CreateBill;
import test.soc365.society365.maneger.Expandablelist.ExpandableListAdapter;
import test.soc365.society365.maneger.MainReportTap.ReportFragment;
import test.soc365.society365.maneger.MomReport.MomFragment;
import test.soc365.society365.maneger.Report.Memberreport;
import test.soc365.society365.maneger.Report.Reports;
import test.soc365.society365.maneger.auditreport.AuditFragment;
import test.soc365.society365.maneger.settings.SettingFragment;
import test.soc365.society365.staticurl.StaticUrl;
import test.soc365.society365.user.changepass.change_password;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener
{
    static public String ufname,ulname,societyid,usrid,uemail,mno,utype,uprof,fltno,bldid,fltstatus,sname,saddress,joindate,buildingname,flatname;
    TextView username;
    ImageView uprofile;
    String baseurlreport;
    SharedPreferences sharedPreferences;
   public static SharedPreferences.Editor editor;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    BottomNavigationView navigation;
    MenuItem menuItem;


    ArrayList<String> partnamearrray,partyflatnoarray;
    EditText edtToolSearch,searchpartyname;
    ImageView searchicon,closeicon;
    static String searchresultdashboard;
    LinearLayout yearspan;
    ImageView leftarrow,rightarrow;

    // Database Helper
    DatabaseHelper db;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);




        /*HashMap<String, String> user = db.getUserDetails();
*/

        // ActionBar actionbar = getSupportActionBar();
        // actionbar.setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);

        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        usrid = sharedPreferences.getString("USERID", "0");
        societyid = sharedPreferences.getString("SOCIETYID", "0");
        utype = sharedPreferences.getString("USERTYPE", "0");
/*

        searchpartyname = (EditText)findViewById(R.id.searchpartyname);
        searchicon = (ImageView)findViewById(R.id.searchicon);
        closeicon= (ImageView)findViewById(R.id.closeicon);
*/

//visible only for First Fragment
        yearspan = (LinearLayout) findViewById(R.id.yearspan);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        final FirstFragment firstFragment = new FirstFragment();
        Bundle arg = new Bundle();
        arg.putString("userid", usrid);
        arg.putString("usertype", utype);
        arg.putString("societyid", societyid);
        arg.putString("sname", sname);
        arg.putString("saddress", saddress);
        arg.putString("joindate", joindate);
        arg.putString("building", bldid);
        arg.putString("buildingname", buildingname);
        arg.putString("flatname", flatname);
        arg.putString("flat", fltno);
        firstFragment.setArguments(arg);

        loadFragment(firstFragment);

        //arrows for date change
        leftarrow = (ImageView) findViewById(R.id.leftarrow);
        rightarrow = (ImageView) findViewById(R.id.rightarrow);
        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loadFragment(firstFragment);
            }
        });

        rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loadFragment(firstFragment);
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
    }

        final DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.mnav_userview);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.name);
        uprofile=header.findViewById(R.id.person);

        // get the listview
        expListView = findViewById(R.id.lvExp);

       ufname = sharedPreferences.getString("firstname","0" );
        ulname = sharedPreferences.getString("lastname","0" );
        uemail= sharedPreferences.getString("email","0");
        mno= sharedPreferences.getString("mobile","0");
        utype= sharedPreferences.getString("usertype","0");
        //uprof= sharedPreferences.getString("profile","0");
        fltno= sharedPreferences.getString("flatno","0");
        bldid= sharedPreferences.getString("buildingno","0");
        //fltstatus= sharedPreferences.getString("flatstatus","0");
        sname= sharedPreferences.getString("sname","0");
        saddress= sharedPreferences.getString("saddress","0");
        joindate= sharedPreferences.getString("joindate","0");
        //buildingname=sharedPreferences.getString("buildingname", "0");
        //flatname=sharedPreferences.getString("flatname", "0");
        /*
        ufname=getIntent().getStringExtra("firstname");
        ulname=getIntent().getStringExtra("lastname");
        societyid=getIntent().getStringExtra("societyid");
        usrid=getIntent().getStringExtra("userid");
        uemail=getIntent().getStringExtra("email");
        mno=getIntent().getStringExtra("mobile");
        utype=getIntent().getStringExtra("usertype");
        uprof=getIntent().getStringExtra("profile");
        fltno=getIntent().getStringExtra("flatno");
        bldid=getIntent().getStringExtra("buildingno");
        fltstatus=getIntent().getStringExtra("flatstatus");
        sname=getIntent().getStringExtra("sname");
        saddress=getIntent().getStringExtra("saddress");
        joindate=getIntent().getStringExtra("joindate");
        */

        getprofile();
        username.setText(ufname+" "+ulname);
        Picasso.with(getApplicationContext()).load(uprof)
                .placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
                .into(uprofile);

        //toolbar name
        getSupportActionBar().setTitle(sname);

        Log.d("snamenew",sname);

        //getprofile();
      /*  FirstFragment firstFragment = new FirstFragment();
        Bundle arg=new Bundle();
        arg.putString("userid", usrid);
        arg.putString("usertype", utype);
        arg.putString("societyid", societyid);
        arg.putString("sname", sname);
        arg.putString("saddress", saddress);
        arg.putString("joindate", joindate);
        arg.putString("building", bldid);
        arg.putString("buildingname", buildingname);
        arg.putString("flatname", flatname);
        arg.putString("flat", fltno);
        firstFragment.setArguments(arg);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, firstFragment).commit();*/

        reportUrl();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d("DEBUG", "heading clicked");

                switch (groupPosition)
                {
                    case 0:

                        drawerLayout.closeDrawers();
                        break;

                    case 1:
                        Intent intentcr=new Intent(MainActivity.this, CreateBill.class);
                        intentcr.setFlags(intentcr.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intentcr.putExtra("societyid", societyid);
                        intentcr.putExtra("userid", usrid);
                        intentcr.putExtra("usertype", utype);
                        startActivity(intentcr);
                        drawerLayout.closeDrawers();
                        break;
                    case 2:
                        Intent intentmom=new Intent(MainActivity.this, MomFragment.class);
                        intentmom.putExtra("societyid", societyid);
                        intentmom.putExtra("userid", usrid);
                        intentmom.putExtra("usertype", utype);
                        startActivity(intentmom);
                        drawerLayout.closeDrawers();
                        break;
                    case 3:
                        Intent intentau=new Intent(MainActivity.this, AuditFragment.class);
                        intentau.putExtra("societyid", societyid);
                        intentau.putExtra("userid", usrid);
                        intentau.putExtra("usertype", utype);
                        startActivity(intentau);
                        drawerLayout.closeDrawers();
                        break;
                    case 4:
                        Intent intentprof=new Intent(MainActivity.this, ManagerProfile.class);
                        intentprof.putExtra("societyid", societyid);
                        intentprof.putExtra("userid", usrid);
                        intentprof.putExtra("usertype", utype);
                        intentprof.putExtra("firstname", ufname);
                        intentprof.putExtra("lastname", ulname);
                        intentprof.putExtra("mobile", mno);
                        Log.d("mobileno", mno);
                        intentprof.putExtra("emailid", uemail);
                        intentprof.putExtra("profilepic", uprof);
                        intentprof.putExtra("joindate", joindate);
                        intentprof.putExtra("building", bldid);
                        intentprof.putExtra("flat", fltno);
                        startActivity(intentprof);
                        drawerLayout.closeDrawers();
                        break;
                    case 5:
                        Intent changepass=new Intent(MainActivity.this, change_password.class);
                        changepass.putExtra("societyid", societyid);
                        changepass.putExtra("userid", usrid);
                        changepass.putExtra("usertype", utype);
                        startActivity(changepass);
                        drawerLayout.closeDrawers();
                        break;
                    case 6:
                        Intent intent=new Intent(MainActivity.this, Login.class);
                        db = new DatabaseHelper(getApplicationContext());
                        db.deleteUsers();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        //end expandable list view
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                final String selected = (String) listAdapter.getChild(groupPosition, childPosition);
                Log.d("submenu item clicked", selected );

                // Handle navigation view item clicks here.
                switch(selected){
                    case "Report":
                        Intent intent1=new Intent(MainActivity.this, Reports.class);
                        intent1.putExtra("societyid", societyid);
                        intent1.putExtra("userid", usrid);
                        intent1.putExtra("usertype", utype);
                        startActivity(intent1);
                        drawerLayout.closeDrawers();
                        break;
                    case "Member Summary":
                        Intent intentmr=new Intent(MainActivity.this, Memberreport.class);
                        intentmr.putExtra("societyid", societyid);
                        intentmr.putExtra("userid", usrid);
                        intentmr.putExtra("usertype", utype);
                        startActivity(intentmr);
                        drawerLayout.closeDrawers();
                        break;
                    case "Party Ledger":
/*
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, partyFragment);
                        ft.commit();*/
                        navigation.setSelectedItemId(R.id.navigation_mycontest);
                        drawerLayout.closeDrawers();
                        callpartyledger_fragment();

                     /*   Intent intent = new Intent(MainActivity.this,PartyFragment.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();*/
                        //String url = "http://150.242.14.196:8012/society/admin/index.php?route=report/partyledger&token=&user_id="+usrid;
                      /*  String url = baseurlreport+"/index.php?route=report/partyledger_mobile&user_id="+usrid;
                        Intent iparty = new Intent(Intent.ACTION_VIEW);
                        iparty.setData(Uri.parse(url));
                        startActivity(iparty);
                        drawerLayout.closeDrawers();*/
                        break;
                    case "Sales Register":
                        //String urlsales = "http://150.242.14.196:8012/society/admin/index.php?route=report/sales_register&token=&user_id="+usrid;
                        String urlsales = baseurlreport+"/index.php?route=report/sales_register_mobile&user_id="+usrid;
                        Intent isales = new Intent(Intent.ACTION_VIEW);
                        isales.setData(Uri.parse(urlsales));
                        startActivity(isales);
                        drawerLayout.closeDrawers();
                        break;
                    case "Daybook":
                        String urlday = baseurlreport+"/index.php?route=report/daybook&token=&user_id="+usrid;
                        Intent iday = new Intent(Intent.ACTION_VIEW);
                        iday.setData(Uri.parse(urlday));
                        startActivity(iday);
                        drawerLayout.closeDrawers();
                        break;
                    case "Trial Balance":
                        String urlbalance = baseurlreport+"/index.php?route=report/trial_balance_ledger&token=&user_id="+usrid;
                        Intent ibalance = new Intent(Intent.ACTION_VIEW);
                        ibalance.setData(Uri.parse(urlbalance));
                        startActivity(ibalance);
                        drawerLayout.closeDrawers();
                        break;

                    case "Receipt Register":
                        String receipt = baseurlreport+"/index.php?route=report/receipt_register_mobile&user_id="+usrid;
                        Intent receiptreg = new Intent(Intent.ACTION_VIEW);
                        receiptreg.setData(Uri.parse(receipt));
                        startActivity(receiptreg);
                        drawerLayout.closeDrawers();
                        break;
                    case "Payment Register":
                        String payment = baseurlreport+"/index.php?route=report/payment_register_mobile&user_id="+usrid;
                        Intent paymentreg = new Intent(Intent.ACTION_VIEW);
                        paymentreg.setData(Uri.parse(payment));
                        startActivity(paymentreg);
                        drawerLayout.closeDrawers();
                        break;
                    case "Receivable Register":
                        String receivable = baseurlreport+"/index.php?route=report/receivable_mobile&user_id="+usrid;
                        Intent receivablereg = new Intent(Intent.ACTION_VIEW);
                        receivablereg.setData(Uri.parse(receivable));
                        startActivity(receivablereg);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });


      /*  searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchpartyname.setVisibility(View.VISIBLE);
                closeicon.setVisibility(View.VISIBLE);
                loadToolBarSearch();
            }
        });*/

/*        String zipFile = Environment.getExternalStorageDirectory() + "/query.zip";
        String unzipLocation = Environment.getExternalStorageDirectory() + "/unzipped/";

        Decompress d = new Decompress(zipFile, unzipLocation);
        d.unzip();*/
    }

    /*
     * Preparing the list data
     */
    private void prepareListData()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //Adding child data
        listDataHeader.add("Home");
       // listDataHeader.add("Reports");
        listDataHeader.add("Create Bill");
        listDataHeader.add("MOM");
        listDataHeader.add("Audit Reports");
        listDataHeader.add("Profile");
        listDataHeader.add("Change Password");
        listDataHeader.add("Logout");

        //Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Report");
        top250.add("Member Summary");
        top250.add("Party Ledger");
        top250.add("Sales Register");
        top250.add("Daybook");
        top250.add("Trial Balance");
        top250.add("Cash Book Balance");
        top250.add("Outstanding list");
        top250.add("Expense Statement");
        top250.add("Payable");
        top250.add("Purchase Register");
        top250.add("P&L");
        top250.add("Payment Report");
        top250.add("Receipt Register");
        top250.add("Payment Register");
        top250.add("Receivable Register");
        //listDataChild.put(listDataHeader.get(1), top250); // Header, Child data


    }

    @Override
    protected void onResume() {
        getprofile();
        super.onResume();
    }

    private void getprofile()
    {
        String profileurl= StaticUrl.getprofile+usrid;

        Log.d("navigation", profileurl);
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
                                    uprof = jsonObject1.getString("profile_pic_url");

                                    ufname=jsonObject1.getString("first_name");
                                    Log.d("firstname", ufname);
                                    ulname=jsonObject1.getString("last_name");
                                    Picasso.with(getApplicationContext()).load(uprof).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.ic_person_black_24dp).into(uprofile);
                                    username.setText(ufname+" "+ulname);
                                } else {
                                   // Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();

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


    private void reportUrl()
    {
        String urlreport=StaticUrl.reportlink;

        Log.d("baseurlreport", urlreport);
        if (Connectivity.isConnected(getApplicationContext()))
        {
            StringRequest stringRequest=new StringRequest(Request.Method.GET,
                    urlreport,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            Log.d("baseurlreport", response);

                            try {
                                JSONObject  jsonObject =new JSONObject(response);
                                baseurlreport = jsonObject.getString("report_base_url");
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    //    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment firstFragment = null;

        switch (item.getItemId()) {

            case R.id.navigation_home:
                //toolbar name
               // yearspan.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(sname);

                firstFragment = new FirstFragment();
                Bundle arg=new Bundle();
                arg.putString("userid", usrid);
                arg.putString("usertype", utype);
                arg.putString("societyid", societyid);
                arg.putString("sname", sname);
                arg.putString("saddress", saddress);
                arg.putString("joindate", joindate);
                arg.putString("building", bldid);
                arg.putString("buildingname", buildingname);
                arg.putString("flatname", flatname);
                arg.putString("flat", fltno);
                firstFragment.setArguments(arg);

                break;

            case R.id.navigation_mycontest:
                //toolbar name
              /*  searchicon.setVisibility(View.VISIBLE);*/
                yearspan.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Party");
                firstFragment = new PartyFragment();
                break;
            case R.id.navigation_me:
                yearspan.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Reports");
                firstFragment = new ReportFragment();
                break;

            case R.id.navigation_more:
                yearspan.setVisibility(View.GONE);
                getSupportActionBar().setTitle("Settings");
                firstFragment = new SettingFragment();
                break;

        }

        return loadFragment(firstFragment);
    }

    private boolean loadFragment(Fragment firstFragment) {
        //switching fragment
        if (firstFragment != null) {

        /*    FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
*/
            Log.d("fragmentlock","ss");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContent, firstFragment)
                    .commit();
            return true;
        }
        return false;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboardmenu, menu);
        menuItem = menu.findItem(R.id.search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      if (id == R.id.search) {
            //searchbar edittext
          searchpartyname.setVisibility(View.VISIBLE);
            loadToolBarSearch();
        }
        return super.onOptionsItemSelected(item);
    }*/

    //searchbar edittext
    public void loadToolBarSearch() {

        StringRequest jsonArrayRequest =
                new StringRequest
                        ("http://society365.in/sms//api/party_ledgers.php?isparty="
                                +1+"&company_id="+10126,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("onResponse:partyledger ", String.valueOf(response));

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                                            if (jsonObject.getString("status").equals("1")) {
                                                JSONArray jsonArray1 = jsonObject.getJSONArray("message");
                                                partnamearrray = new ArrayList<>();
                                                partyflatnoarray = new ArrayList<>();

                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                                    partnamearrray.add(jsonObject1.getString("ledger_name"));
                                                    partyflatnoarray.add(jsonObject1.getString("flat_no"));

                                                }
                                            } else {
                                                // reportarray.clear();
                                                // Toast.makeText(getApplicationContext(),"No Records" , Toast.LENGTH_SHORT).show();
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", error.toString());

                            }
                        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);


    }

    public  void callpartyledger_fragment()
    {

        PartyFragment partyFragment = new PartyFragment();
        loadFragment(partyFragment);
    }
}

