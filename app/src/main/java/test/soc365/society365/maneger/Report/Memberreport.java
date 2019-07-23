package test.soc365.society365.maneger.Report;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class Memberreport extends AppCompatActivity {

    ArrayList<member_summary_report_model> summaryarray;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    int pagingnopre,pagingnotxt;
    TextView nextpage,prevpage;
    SpotsDialog spotsDialog;

    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberreport);



        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nextpage = findViewById(R.id.next);
        prevpage = findViewById(R.id.prev);
        spotsDialog = new SpotsDialog(this,R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        summaryarray = new ArrayList<>();
        adapter=new Member_summary_report_adapter(summaryarray,getApplicationContext());
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

         summary_report(1);

        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                summary_report(pagingnotxt);
            }
        });

        prevpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pagingno=1;
                  //pagingnotxt = pagingnotxt--;
                 Log.d("pagingno",""+pagingnopre);

                 summary_report(pagingnopre);
            }
        });
    }

    public void summary_report(int pagingno)
    {
        String summaryurl= StaticUrl.memberreport+pagingno+"&c_id="+stallyid;

        Log.d("summulr",summaryurl);
        if(Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();
            summaryarray.clear();

            StringRequest request=new StringRequest(Request.Method.GET, summaryurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if(jsonObject.getString("status").equals("1"))
                        {
                            pagingnotxt= Integer.parseInt(jsonObject.getString("nextpageno"));

                            pagingnopre = pagingnotxt-2;

                            JSONArray jsonArray1=jsonObject.getJSONArray("membersummary");
                            for (int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                member_summary_report_model model=new member_summary_report_model();
                                model.setClosing(jsonObject1.getString("closing_amount"));
                                model.setCredit(jsonObject1.getString("credit_amount"));
                                model.setDebit(jsonObject1.getString("debit_amount"));
                                model.setLeger(jsonObject1.getString("ledger_name"));
                                model.setOpening(jsonObject1.getString("opening_amount"));
                                summaryarray.add(model);
                            }
                            adapter.notifyDataSetChanged();
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
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            spotsDialog.dismiss();
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
