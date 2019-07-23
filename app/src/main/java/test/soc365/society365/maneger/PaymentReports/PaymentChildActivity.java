package test.soc365.society365.maneger.PaymentReports;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import test.soc365.society365.R;

import static test.soc365.society365.maneger.PaymentReports.PaymentReportActivity.movieList;

public class PaymentChildActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private PaymentReportActivityAdapter adapter;
   String snamesales,amount;
    String date1,date2;
    TextView dateendandstart,totalamountgross;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_child);


        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        dateendandstart =(TextView)findViewById(R.id.summaryname);
        totalamountgross =(TextView)findViewById(R.id.totalamountgross);

        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        snamesales = intent.getStringExtra("partyledgername");
        date1 = intent.getStringExtra("date1");
        date2 = intent.getStringExtra("date2");

        //toolbar name
        getSupportActionBar().setTitle(snamesales);

        dateendandstart.setText(date1+" - "+date2);
        totalamountgross.setText(amount);



        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        adapter = new PaymentReportActivityAdapter(getApplicationContext(),movieList);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),PaymentReportActivity.class);
        startActivity(intent);
        finish();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id ==  android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
/*
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.share);
        MenuItem register = menu.findItem(R.id.share);


        register.setVisible(false);

        return true;
    }*/
}
