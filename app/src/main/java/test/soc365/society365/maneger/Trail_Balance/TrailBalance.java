package test.soc365.society365.maneger.Trail_Balance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import test.soc365.society365.R;

public class TrailBalance extends AppCompatActivity {

    RelativeLayout ledger,account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_balance2);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Trail Balance");

        ledger = (RelativeLayout) findViewById(R.id.Trail_Balance_Ledger_layout);
        account = (RelativeLayout) findViewById(R.id.Trail_Balance_Account_layout);


        ledger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TrailBalanceActivity.class);
                intent.putExtra("type","ledger");
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TrailBalanceActivity.class);
                intent.putExtra("type","accounts");
                startActivity(intent);
            }
        });
    }

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

}
