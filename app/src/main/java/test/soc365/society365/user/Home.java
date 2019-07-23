package test.soc365.society365.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.login.Login;
import test.soc365.society365.R;
import test.soc365.society365.staticurl.StaticUrl;
import test.soc365.society365.user.Past_Payment_History.user_payment_history;
import test.soc365.society365.user.changepass.change_password;
import test.soc365.society365.user.notification.Notification;
import test.soc365.society365.user.userprofile.u_MyProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String useremailid,usermobile,ufname,ulname,societyid,usrid,uemail,mno,utype,uprof,fltno,bldid,fltstatus,joindate,building,flat,societyname,societyaddress;
    TextView username;
    ImageView uprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        // ActionBar actionbar = getSupportActionBar();
        // actionbar.setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);
        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.mnav_userview);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.name);
        uprofile=header.findViewById(R.id.person);

        usrid = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );

        ufname = sharedPreferences.getString("firstname","0" );
        ulname = sharedPreferences.getString("lastname","0" );
        uemail= sharedPreferences.getString("email","0");
        mno= sharedPreferences.getString("mobile","0");
        utype= sharedPreferences.getString("usertype","0");
        //uprof= sharedPreferences.getString("profile","0");
        fltno= sharedPreferences.getString("flatno","0");
        bldid= sharedPreferences.getString("buildingno","0");
        flat=sharedPreferences.getString("flatname", "0");
        building=sharedPreferences.getString("buildingname", "0");
        fltstatus= sharedPreferences.getString("flatstatus","0");
        //sname= sharedPreferences.getString("sname","0");
        //saddress= sharedPreferences.getString("saddress","0");
        joindate= sharedPreferences.getString("joindate","0");
        societyname=sharedPreferences.getString("sname", "0");
        societyaddress=sharedPreferences.getString("saddress", "0");
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
        joindate=getIntent().getStringExtra("joindate");
*/
        username.setText(ufname+" "+ulname);
        getprofile();
        Picasso.with(getApplicationContext()).load(uprof)
                .placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
                .into(uprofile);



        UserHome userHome=new UserHome();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle arg=new Bundle();
        arg.putString("userid", usrid);
        arg.putString("usertype", utype);
        arg.putString("usermobile", usermobile);
        arg.putString("useremailid", useremailid);
        userHome.setArguments(arg);
        fragmentManager.beginTransaction().replace(R.id.flContent, userHome).addToBackStack(null).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {  //Handle navigation view item clicks here.
        int id = item.getItemId();
        //Fragment fragment=null;
        //Class fragmentClass = null;

        switch (id)
        {
            case R.id.nav_fragment_home:

              /*  Bundle arg=new Bundle();
                arg.putString("userid", usrid);
                arg.putString("usertype", utype);
                arg.putString("societyid", societyid);
                fragment.setArguments(arg);
                fragment=new UserHome();
            */
                UserHome userHome = new UserHome();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", societyid);
                args.putString("usertype", utype);
                userHome.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent, userHome);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case R.id.nav_fragment_myprofile:
                u_MyProfile profile=new u_MyProfile();
                //u_MyProfile profile=new u_MyProfile();
                //FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle arg1=new Bundle();
                arg1.putString("userid", usrid);
                arg1.putString("usertype", utype);
                arg1.putString("societyid", societyid);
                arg1.putString("firstname", ufname);
                arg1.putString("lastname", ulname);
                arg1.putString("mobile", mno);
                arg1.putString("emailid", uemail);
                arg1.putString("profilepic", uprof);
                arg1.putString("flat", fltno);
                arg1.putString("building", bldid);
                arg1.putString("flatstatus", fltstatus);
                arg1.putString("joindate", joindate);
                arg1.putString("buildingname", building);
                arg1.putString("flatname", flat);
                arg1.putString("societyname", societyname);
                profile.setArguments(arg1);
                FragmentManager fragmentManagera = getSupportFragmentManager();
                FragmentTransaction fragmentTransactiona = fragmentManagera.beginTransaction();
                fragmentTransactiona.replace(R.id.flContent, profile);
                fragmentTransactiona.addToBackStack(null);
                fragmentTransactiona.commit();
                break;
            case R.id.notification:
               /* Intent notification=new Intent(Home.this,Notification.class);
                startActivity(notification);*/
               Notification notification=new Notification();
                FragmentManager fragmentManagers = getSupportFragmentManager();
                FragmentTransaction fragmentTransactions = fragmentManagers.beginTransaction();
                fragmentTransactions.replace(R.id.flContent, notification);
                fragmentTransactions.addToBackStack(null);
                fragmentTransactions.commit();
                break;
            case R.id.nav_fragment_changepass:
                Intent changepass=new Intent(Home.this,change_password.class);
                changepass.setFlags(changepass.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                changepass.putExtra("userid", usrid);
                changepass.putExtra("usertype", utype);
                changepass.putExtra("societyid", societyid);
                startActivity(changepass);
                break;
            case R.id.pastpyment:
                user_payment_history history=new user_payment_history();
                Bundle argp=new Bundle();
                argp.putString("userid", usrid);
                argp.putString("usertype", utype);
                argp.putString("societyid", societyid);
                history.setArguments(argp);
                FragmentManager fragmentManagerp = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionp = fragmentManagerp.beginTransaction();
                fragmentTransactionp.replace(R.id.flContent, history);
                fragmentTransactionp.addToBackStack(null);
                fragmentTransactionp.commit();
                break;
            case R.id.logout:
                Intent intent=new Intent(Home.this, Login.class);
                editor.clear();
                editor.apply();
                editor.commit();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                UserHome userHomes = new UserHome();
                Bundle argss = new Bundle();
                argss.putString("userid",usrid);
                argss.putString("societyid", societyid);
                argss.putString("usertype", utype);
                userHomes.setArguments(argss);
                FragmentManager fragmentManagerss = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionss = fragmentManagerss.beginTransaction();
                fragmentTransactionss.replace(R.id.flContent, userHomes);
                fragmentTransactionss.addToBackStack(null);
                fragmentTransactionss.commit();
        }

/*
        FragmentManager fragmentManager = getSupportFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

*/
       /* FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction.addToBackStack(null);
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Home.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    */

    @Override
    protected void onResume() {
        getprofile();
        super.onResume();
    }

    private void getprofile()
    {
        String profileurl= StaticUrl.getprofile+usrid;
        Log.d("profileurl", profileurl);
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
                                    String profurl = jsonObject1.getString("profile_pic_url");
                                    ufname=jsonObject1.getString("first_name");
                                    ulname=jsonObject1.getString("last_name");
                                    usermobile=jsonObject1.getString("mobile_no");
                                    useremailid=jsonObject1.getString("email");
                                    username.setText(ufname+" "+ulname);
                                    Picasso.with(getApplicationContext()).load(profurl).placeholder(R.drawable.ic_person_black_24dp).error(R.drawable.ic_person_black_24dp).into(uprofile);
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
}
