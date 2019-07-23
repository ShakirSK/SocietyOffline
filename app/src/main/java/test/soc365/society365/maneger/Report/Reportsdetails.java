package test.soc365.society365.maneger.Report;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Reportsdetails extends AppCompatActivity {

    String strdate,strpartyname,strreporttype;
    SpotsDialog spotsDialog;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Report_Model> reportarray;
    Float totalamout=0f;
    TextView totaltxt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportsdetails);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        totaltxt = findViewById(R.id.total);
        spotsDialog = new SpotsDialog(this,R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        reportarray=new ArrayList<Report_Model>();
        recyclerView.setHasFixedSize(true);
        adapter=new Report_adapter_details(getApplicationContext(),reportarray);
        recyclerView.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        strdate = intent.getStringExtra("partydate");
        strpartyname = intent.getStringExtra("partyname");
        strreporttype = intent.getStringExtra("reporttype");

        getdetails();
    }

    private void getdetails()
    {
        String targetUrl = null;
        try {
            targetUrl = StaticUrl.reportdetails
                    +"?date="+strdate
                    +"&party_name="+ URLEncoder.encode(strpartyname,"UTF-8")
                    +"&report_type="+strreporttype;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("reportdetails", targetUrl);

        if(Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();

            StringRequest request=new StringRequest(Request.Method.GET, targetUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if(jsonObject.getString("status").equals("1"))
                        {
                            spotsDialog.dismiss();
                            JSONArray jsonArray1=jsonObject.getJSONArray("message");

                            for (int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                Report_Model model=new Report_Model();
                                //model.setDate(jsonObject1.getString("date"));
                                model.setParty_name(jsonObject1.getString("party_name"));
                                model.setLedgername(jsonObject1.getString("ledger_name"));
                                model.setAmount(jsonObject1.getString("amount"));
                                //model.setVoucher_type(jsonObject1.getString("voucher_type"));
                                //model.setVoucher_number(jsonObject1.getString("voucher_number"));
                                Float amtstr = Float.valueOf(jsonObject1.getString("amount"));
                                Log.d("amtstr", String.valueOf(amtstr));
                                totalamout += amtstr;
                                Log.d("totalamout", String.valueOf(totalamout));
                                reportarray.add(model);
                            }
                            adapter.notifyDataSetChanged();
                            totaltxt.setText(String.valueOf(totalamout));
                        }else
                        {
                            spotsDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"No Records",Toast.LENGTH_SHORT).show();
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
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
        else
        {
            spotsDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Check internet connection" , Toast.LENGTH_SHORT).show();
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
