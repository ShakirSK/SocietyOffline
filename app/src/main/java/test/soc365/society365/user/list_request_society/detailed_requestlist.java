package test.soc365.society365.user.list_request_society;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import test.soc365.society365.R;

public class detailed_requestlist extends AppCompatActivity
{

    public String id,date,reqmsg,respmsg,status;//,type fromid,,createdat sfromid,,stype
    public TextView sid,sdate,sreqmsg,srespmsg,sstatus;


    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detailed_requestlist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Log.d("ok","ok");
        sid=findViewById(R.id.blank);
        //sfromid=findViewById(R.id.fromid);
        sreqmsg=findViewById(R.id.reqmsg);
        srespmsg=findViewById(R.id.responce);
        sstatus=findViewById(R.id.status2);
       // stype=findViewById(R.id.type);
        sdate=findViewById(R.id.blank2);


        id=getIntent().getStringExtra("id");
        date=getIntent().getStringExtra("req_date");
        //fromid=getIntent().getStringExtra("from_id");
        reqmsg=getIntent().getStringExtra("user_msg");
        respmsg=getIntent().getStringExtra("responce_msg");
        //createdat=getIntent().getStringExtra("created_at");
        //type=getIntent().getStringExtra("req_type");
        status=getIntent().getStringExtra("status");

        detailedlist();

    }

    public void detailedlist()
    {
        sid.setText(id);
        //sfromid.setText(fromid);
        sreqmsg.setText(reqmsg);
        srespmsg.setText(respmsg);
        sdate.setText(date);

        if(status.equals("1"))
        {
            //sstatus.setTextSize(15);
            sstatus.setText("Open");
        }
        else
        {
           // sstatus.setTextSize(15);
            sstatus.setText("Close");
        }
       /* if(type.equals("1"))
        {
            stype.setTextSize(15);
            stype.setText("Complaint");
        }
        else
        {
            stype.setTextSize(15);
            stype.setText("Request");
        }
        */
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
