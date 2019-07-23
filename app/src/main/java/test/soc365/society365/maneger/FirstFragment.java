package test.soc365.society365.maneger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rollbar.android.Rollbar;

import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.DashboardFragment.FirstFragmentAdapter;
import test.soc365.society365.maneger.MainReportTap.Reportmodel;
import test.soc365.society365.maneger.Sales_Register.sales_register_model;
import test.soc365.society365.maneger.addmember.AddMember;
import test.soc365.society365.maneger.bulletin.BulletineFragment;
import test.soc365.society365.maneger.paymentsocietymember.Process_society_accounting;
import test.soc365.society365.maneger.process_member_request.ProcessMemberRequest;
import test.soc365.society365.maneger.society_activity.SocietyActivityFragment;
import test.soc365.society365.maneger.viewmember.ViewFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static test.soc365.society365.helper.DatabaseHelper.amount_payment;

public class FirstFragment extends Fragment {

    FloatingActionButton button;
    String usrid,sid,utype,societyname,societyaddress;
    CardView cardview,cardview2,cardview3,cardview4,cardview5,cardview6;
    TextView societynametxt,societyaddresstxt;

    List<Reportmodel> reportlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Gson gson;

    String stallyid,amountTo,amountTo2,amountTo3;

    // Database Helper
    DatabaseHelper db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.firstfragment, container, false);

        cardview=view.findViewById(R.id.cardview);
        cardview2=view.findViewById(R.id.cardview2);
        cardview3=view.findViewById(R.id.cardview3);
        cardview4=view.findViewById(R.id.cardview4);
        cardview5=view.findViewById(R.id.cardview5);
        cardview6=view.findViewById(R.id.cardview6);
        societynametxt=view.findViewById(R.id.societyname);
        societyaddresstxt=view.findViewById(R.id.societyaddress);

        Bundle arg=getArguments();
        usrid=arg.getString("userid");
        sid=arg.getString("societyid");
        utype=arg.getString("usertype");
        societyname=arg.getString("sname");
        societyaddress=arg.getString("saddress");


        societynametxt.setText(societyname);
        societyaddresstxt.setText(societyaddress);

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///Intent intent=new Intent(getActivity(),AddMember.class);
                //startActivity(intent);
             /*   AddMember addMember=new AddMember();
                Intent intent=getActivity().getIntent();
                usrid=intent.getStringExtra("userid");
                utype=intent.getStringExtra("usertype");
                sid=intent.getStringExtra("societyid");
*/
                Intent intent1=new Intent(getActivity(),AddMember.class);
                  //Bundle args = new Bundle();
                intent1.setFlags(intent1.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                 intent1.putExtra("editstatus","1");
                // intent.putExtra("societyid", "1");
                 intent1.putExtra("user_id",usrid);
                 intent1.putExtra("usertype", utype);
                 intent1.putExtra("societyid",sid);
                //args.putString("userid", userid);
                startActivity(intent1);
                /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,addMember);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();*/
            }
        });


        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),ViewFragment.class);
               /* startActivity(intent);
                ViewFragment viewFragment = new ViewFragment();
                Bundle args = new Bundle();
                usrid=args.getString("userid");
                utype=args.getString("usertype");
                sid=args.getString("societyid");
*/
                Intent intent1=new Intent(getActivity(),ViewFragment.class);
                intent1.setFlags(intent1.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                //Bundle args = new Bundle();
                // intent.putExtra("societyid", "1");
                intent1.putExtra("user_id",usrid);
                intent1.putExtra("usertype", utype);
                intent1.putExtra("societyid",sid);

                //args.putString("userid", userid);
                startActivity(intent1);
               // args.putString("shopid",shopid);
                //args.putString("userid", userid);
                //chattingFragment.setArguments(args);
                /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,viewFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();*/
            }
        });

        cardview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Intent intent=new Intent(getActivity(),ProcessMemberRequest.class);
               //startActivity(intent);
           /*  ProcessMemberRequest processMemberRequest =new ProcessMemberRequest();
                  Bundle intent = new Bundle();
                usrid=intent.getString("userid");
                utype=intent.getString("usertype");
                sid=intent.getString("societyid");
*/
                Intent intent1=new Intent(getActivity(),ProcessMemberRequest.class);
                intent1.setFlags(intent1.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent1.putExtra("user_id",usrid);
                intent1.putExtra("usertype", utype);
                intent1.putExtra("societyid",sid);
                startActivity(intent1);
                // args.putString("shopid",shopid);
                //args.putString("userid", userid);
                //chattingFragment.setArguments(args);
                /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,processMemberRequest);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();*/
            }
        });

        cardview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),Process_society_accounting.class);
               /*startActivity(intent);
                Process_society_accounting process_society_accounting =new Process_society_accounting();
                Bundle intent = new Bundle();
                usrid=intent.getString("userid");
                utype=intent.getString("usertype");
                sid=intent.getString("societyid");
                */
                Intent intent1=new Intent(getActivity(),Process_society_accounting.class);
                intent1.putExtra("user_id",usrid);
                intent1.putExtra("usertype", utype);
                intent1.putExtra("societyid",sid);
                startActivity(intent1);

                // args.putString("shopid",shopid);
                //args.putString("userid", userid);
                //chattingFragment.setArguments(args);
                /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,process_society_accounting);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();*/
            }
        });

        cardview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent=new Intent(getActivity(),BulletineFragment.class);
                /*startActivity(intent);
                BulletineFragment bulletineFragment =new BulletineFragment();
                Bundle intent = new Bundle();
                usrid=intent.getString("userid");
                utype=intent.getString("usertype");
                sid=intent.getString("societyid");
*/
                Intent intent1=new Intent(getActivity(),BulletineFragment.class);
                intent1.putExtra("user_id",usrid);
                intent1.putExtra("usertype", utype);
                intent1.putExtra("societyid",sid);
               startActivity(intent1);
                // args.putString("shopid",shopid);
                //args.putString("userid", userid);
                //chattingFragment.setArguments(args);
                /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,bulletineFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();*/
            }
        });

        cardview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),SocietyActivityFragment.class);
               /*startActivity(intent);
                SocietyActivityFragment societyActivityFragment =new SocietyActivityFragment();
                Bundle intent = new Bundle();
                usrid=intent.getString("userid");
                utype=intent.getString("usertype");
                sid=intent.getString("societyid");
*/
                Intent intent1=new Intent(getActivity(),SocietyActivityFragment.class);
                intent1.putExtra("user_id",usrid);
                intent1.putExtra("usertype", utype);
                intent1.putExtra("societyid",sid);
                startActivity(intent1);
                // args.putString("shopid",shopid);
                //args.putString("userid", userid);
                //chattingFragment.setArguments(args);
                /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,societyActivityFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();*/
            }
        });

       /*.. button=view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AddMember.class);
                startActivity(intent);
            }
        });..*/


        gson = new Gson();
        sharedPreferences = getContext().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );

       //recycle view design same as BizAnalytics
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycleview);

        linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        String dsd;
        String dsd2;

        final Calendar calendartp= Calendar.getInstance();
        //setting calender to custom date




        dsd2="01"+"-"+"Apr"+"-"+calendartp.get(Calendar.YEAR);

        calendartp.add(Calendar.YEAR, -1);
        dsd="31"+"-"+"Mar"+"-"+calendartp.get(Calendar.YEAR);
        Log.d("datedate",dsd+" "+dsd2);


     /*   String url=null,url2 = null,url3 = null;
        try {
            url = "http://150.242.14.196:8012/society/service/app/dashboard_totalpayment.php?company_id="
                    + stallyid+ "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8");
            url2 = "http://150.242.14.196:8012/society/service/app/dashboard_totalsales.php?company_id="
                    + stallyid+ "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8");

            url3 = "http://150.242.14.196:8012/society/service/app/dashboard_totalreceipt.php?company_id="
                    + stallyid+ "&filter_date=" +
                    URLEncoder.encode(dsd, "UTF-8") +
                    "&filter_date_end=" + URLEncoder.encode(dsd2, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("onResponse:summaryurl1 ", url);
        Log.d("onResponse:summaryurl2 ", url2);
        Log.d("onResponse:summaryurl3 ", url3);

        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);
                                            amountTo=jsonObject.getString("total_payment");
                                      //      Toast.makeText(getContext(),amountTo,Toast.LENGTH_SHORT).show();


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
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);


        StringRequest jsonArrayRequest2 =
                new StringRequest
                        (url2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);
                                            amountTo2=jsonObject.getString("total_sales");
                                           // Toast.makeText(getContext(),amountTo,Toast.LENGTH_SHORT).show();




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
        jsonArrayRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
        requestQueue2.add(jsonArrayRequest2);




        StringRequest jsonArrayRequest3 =
                new StringRequest
                        (url3,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("onResponse:summary2 ", response);

                                        try {

                                            JSONObject jsonObject= new JSONObject(response);
                                            amountTo3=jsonObject.getString("total_receipt");
                                    //        Toast.makeText(getContext(),amountTo,Toast.LENGTH_SHORT).show();




                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    final ArrayList text1 = new ArrayList<>(Arrays.asList("Payment Report","Sales","Receipt",
                                                            "Cash / Bank balance",
                                                            "Trial balance Ledger","Trial balance Accounts","Outstanding Receivable"));

                                                    final ArrayList text3;

                                                    *//*Locale.getDefault()*//*
                                                    if(amountTo.equals("")||amountTo2.equals("")||amountTo3.equals(""))
                                                    {
                                                         text3 = new ArrayList<>(Arrays.asList("0"
                                                                ,"0",
                                                                "0","0","0","0","0"));

                                                    }
                                                    else if(amountTo.equals(""))
                                                    {
                                                      text3 = new ArrayList<>(Arrays.asList("0"
                                                                ,amountTo2,amountTo3,"0","0","0","0"));

                                                    }
                                                    else if(amountTo2.equals(""))
                                                    {
                                                        text3 = new ArrayList<>(Arrays.asList( amountTo
                                                                ,"0",
                                                                amountTo3,"0","0","0","0"));

                                                    }
                                                    else if(amountTo3.equals(""))
                                                    {
                                                         text3 = new ArrayList<>(Arrays.asList(amountTo
                                                                 ,amountTo2,
                                                                "0","0","0","0","0"));

                                                    }
                                                    else
                                                    {*//*
                                                         text3 = new ArrayList<>(Arrays.asList(String.format("%,d", Double.parseDouble(amountTo))
                                                                ,String.format("%,d", Double.parseDouble(amountTo2)),
                                                                String.format("%,d", Double.parseDouble(amountTo3)),"0","0","0","0"));*//*

                                                        text3 = new ArrayList<>(Arrays.asList(amountTo
                                                                ,amountTo2,
                                                               amountTo3,"0","0","0","0"));

                                                    }

                                                    final int[] images1 = {R.drawable.money, R.drawable.rupee, R.drawable.receipt,
                                                            R.drawable.hand,R.drawable.money, R.drawable.rupee, R.drawable.receipt};
                                                    *//*final ArrayList text3 = new ArrayList<>(Arrays.asList(String.format("%,d", Integer.parseInt(amountTo))
                                                            ,String.format("%,d", Integer.parseInt(amountTo2)),
                                                            String.format("%,d", Integer.parseInt(amountTo3)),"0""0","40,52,222","45,1,122","0"));

                                                    final int[] images1 = {R.drawable.money, R.drawable.rupee, R.drawable.receipt,
                                                            R.drawable.hand, R.drawable.income, R.drawable.hand, R.drawable.money,
                                                            R.drawable.receipt, R.drawable.rupee, R.drawable.trucking, R.drawable.printer};
*//*
                                                    //Do something after 100ms
                                                    FirstFragmentAdapter adapter = new FirstFragmentAdapter(getActivity(), text1,text3,images1);
                                                    recyclerView.setHasFixedSize(true);
                                                    recyclerView.setLayoutManager(linearLayoutManager);
                                                    recyclerView.addItemDecoration(dividerItemDecoration);
                                                    recyclerView.setAdapter(adapter);

                                                }
                                            }, 1000);

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
        jsonArrayRequest3.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue3 = Volley.newRequestQueue(getContext());
        requestQueue3.add(jsonArrayRequest3);*/

        db = new DatabaseHelper(getContext());
/*
        final ArrayList text3 = new ArrayList<>(Arrays.asList(amount_payment,"0","0","0","0","0","0"));
*/

         List<sales_register_model>  Split_ledger_with_flatno =    db.getSplit_ledger_with_flatno();

        String Split_ledger_with_flatno_string = gson.toJson(Split_ledger_with_flatno);

        editor.putString("Split_ledger_with_flatno",Split_ledger_with_flatno_string);
        editor.commit();
        editor.apply();

       final ArrayList text3 = new ArrayList();

        try
        {
            db.getTotalAmount("Payment");
            text3.add(amount_payment);
            db.getTotalAmount("Bill");
            text3.add(amount_payment);
            db.getTotalAmount("Receipt");
            text3.add(amount_payment);
        }
        catch (Exception e)
        {

            Rollbar.init(getContext());
            Rollbar rollbar  = Rollbar.instance();
            rollbar.error(e);
        }
        if(amount_payment==null)
        {
            ArrayList d  = new ArrayList<>(Arrays.asList("0","0","0"));
            text3.addAll(d);
        }

        ArrayList d  = new ArrayList<>(Arrays.asList("0","0","0","0","0"));
        text3.addAll(d);

        final int[] images1 = {R.drawable.money, R.drawable.rupee, R.drawable.receipt,
                R.drawable.hand, R.drawable.rupee, R.drawable.receipt,R.drawable.money, R.drawable.rupee};

        final ArrayList text1 = new ArrayList<>(Arrays.asList("Payment Report","Sales","Receipt",
                "Cash / Bank balance",
                "Trial balance ","Outstanding Receivable","Member Summary","Daybook"));




        FirstFragmentAdapter adapter = new FirstFragmentAdapter(getActivity(), text1,text3,images1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }
}
