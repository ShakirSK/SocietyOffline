package test.soc365.society365.maneger.Report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Reports extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    Spinner catagory;
    EditText to,from;
    ImageView submitrpt;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Report_Model> reportarray;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String type="0";
    List<String> categories;

    SpotsDialog spotsDialog;
    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        sharedPreferences = getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );

        Log.d("stallyid",stallyid);

        catagory=findViewById(R.id.catagory);
        from=findViewById(R.id.fromdate);
        to=findViewById(R.id.todate);
        submitrpt = findViewById(R.id.submitrpt);

        spotsDialog = new SpotsDialog(this,R.style.Custom);
        recyclerView=findViewById(R.id.recycleview);
        reportarray=new ArrayList<Report_Model>();
        adapter=new Report_adapter(getApplicationContext(),reportarray);
        recyclerView.setAdapter(adapter);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar c = Calendar.getInstance();
        int dayi = c.get(Calendar.DAY_OF_MONTH);
        int monthi = c.get(Calendar.MONTH);
        int yeari = c.get(Calendar.YEAR);
        String datei = dayi + "-" + (monthi+1) + "-" + (yeari-1);
        from.setText(datei);
        Log.d("checkdate_test2",datei);
        Calendar t= Calendar.getInstance();
        int dayt = t.get(Calendar.DAY_OF_MONTH);
        int montht = t.get(Calendar.MONTH);
        int yeart = t.get(Calendar.YEAR);
        String datet = dayt + "-" + (montht+1) + "-" + yeart;
        to.setText(datet);

        from.setInputType(InputType.TYPE_NULL);
        from.setFocusable(false);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setDate(999);
            }
        });
        to.setInputType(InputType.TYPE_NULL);
        to.setFocusable(false);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setDate(666);
            }
        });

        catagory.setOnItemSelectedListener(this);
        categories = new ArrayList<String>();
        categories.add("Payment");
        categories.add("Reciepts");
        categories.add("Recievable");
        categories.add("Payable");
        //categories.add("Member Summary");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.item_spiner, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        catagory.setAdapter(dataAdapter);

        submitrpt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(type.equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Select Type", Toast.LENGTH_SHORT).show();
                }
                else
                if(to.getText().toString().equals("") && from.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Select Start & End Date", Toast.LENGTH_SHORT).show();
                }else {
                    String reporturl= StaticUrl.reporturl+"?start_date="+from.getText()+ "&end_date="+to.getText()+ "&report_type="+type+"&tally_id="+stallyid;
                    getReport(reporturl);
                }
            }
        });


        //String defaulturl = StaticUrl.reporturl+"?report_type=1";
        //getReport(defaulturl);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {
                        String partyname = reportarray.get(position).getParty_name();
                        String partydate = reportarray.get(position).getDate();
                        String reporttype = type;

                        Intent intent=new Intent(Reports.this,Reportsdetails.class);
                        intent.putExtra("partyname",partyname);
                        intent.putExtra("partydate",partydate);
                        intent.putExtra("reporttype",reporttype);
                        startActivity(intent);
                    }
                    @Override
                    public void onLongClick(View view, int position)
                    {

                    }
                }));

        String reporturl= StaticUrl.reporturl+"?start_date="+from.getText()+ "&end_date="+to.getText()+ "&report_type="+type+"&tally_id="+stallyid;
        getReport(reporturl);


    }

    public void setDate(int id)
    {
        showDialog(id);
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
        if (id == 666)
        {
            return new DatePickerDialog(this, myDateListener1, year, month, day);
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
            showDate(from,arg1, arg2+1, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListener1 = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
        {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(to,arg1, arg2+1, arg3);
        }
    };

    private void showDate(TextView txt,int year, int month, int day)
    {
        txt.setText(new StringBuilder().append(day).append("-").append(month).append("-").append(year));

        if(type.equals("0"))
        {
            Toast.makeText(getApplicationContext(),"Select Type", Toast.LENGTH_SHORT).show();
        }
        else
        if(to.getText().toString().equals("") && from.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Select Start & End Date", Toast.LENGTH_SHORT).show();
        }else {
            String reporturl= StaticUrl.reporturl+"?start_date="+from.getText()+ "&end_date="+to.getText()+ "&report_type="+type+"&tally_id="+stallyid;
            getReport(reporturl);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        String ch=catagory.getSelectedItem().toString();
        switch(ch)
        {
            case "Payment":
                type="1";
                String defaulturlp = StaticUrl.reporturl+"?report_type=1"+"&tally_id="+stallyid;
                getReport(defaulturlp);
                break;
            case "Reciepts":
                type="2";
                String defaulturlr = StaticUrl.reporturl+"?report_type=2"+"&tally_id="+stallyid;
                getReport(defaulturlr);
                break;
            case "Recievable":
                type="3";
                String defaulturlre = StaticUrl.reporturl+"?report_type=3"+"&tally_id="+stallyid;
                getReport(defaulturlre);
                break;
            case "Payable":
                type="4";
                String defaulturlpa = StaticUrl.reporturl+"?report_type=4"+"&tally_id="+stallyid;
                getReport(defaulturlpa);
                break;
           /* case "Member Summary":
                type="5";
                break;
                */
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getReport(String reporturl)
    {
        Log.d("reporturl",reporturl);
        if(Connectivity.isConnected(getApplicationContext()))
        {
            spotsDialog.show();

            reportarray.clear();

            StringRequest request=new StringRequest
                    (Request.Method.GET, reporturl,
                            new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if(jsonObject.getString("status").equals("1"))
                        {
                            JSONArray jsonArray1=jsonObject.getJSONArray("message");


                            for (int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                Report_Model model=new Report_Model();
                                model.setDate(jsonObject1.getString("date"));
                                model.setParty_name(jsonObject1.getString("party_name"));
                                model.setVoucher_type(jsonObject1.getString("voucher_type"));
                                model.setVoucher_number(jsonObject1.getString("voucher_number"));
                                model.setAmount(jsonObject1.getString("amount"));
                                reportarray.add(model);
                            }
                            //adapter.notifyDataSetChanged();
                            spotsDialog.dismiss();
                        }else {
                            spotsDialog.dismiss();
                           // reportarray.clear();
                           // Toast.makeText(getApplicationContext(),"No Records" , Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
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

    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {

        private GestureDetector gestureDetector;
        private Reports.RecyclerTouchListener.ClickListener clickListener;

        public RecyclerTouchListener(Context applicationContext, final RecyclerView recyclerView, final Reports.RecyclerTouchListener.ClickListener clickListener)
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
