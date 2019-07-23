package test.soc365.society365.maneger.viewmember;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import test.soc365.society365.R;
import com.squareup.picasso.Picasso;


public class MemberDetailsFragment extends AppCompatActivity {
    TextView firstname,lastname,buildingno,flatno,emailid,joindate,enddate,status,phno;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberdetailsfragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firstname=findViewById(R.id.firstname);
        buildingno=findViewById(R.id.buildingname);
        flatno=findViewById(R.id.flat);
        emailid=findViewById(R.id.emailid);
        phno=findViewById(R.id.mobilenumber);
        joindate=findViewById(R.id.joindate2);
        status=findViewById(R.id.active);
        profile=findViewById(R.id.img);



        firstname.setText(getIntent().getStringExtra("fullname"));
        phno.setText(getIntent().getStringExtra("phonenumber"));
        emailid.setText(getIntent().getStringExtra("emailid"));
        buildingno.setText(getIntent().getStringExtra("buldingid"));
        flatno.setText(getIntent().getStringExtra("flatno"));
        joindate.setText(getIntent().getStringExtra("memberjoindate"));
        status.setText(getIntent().getStringExtra("status"));

        String imgurl=getIntent().getStringExtra("profilepic");
        Picasso.with(getApplicationContext())
                .load(imgurl)
                .placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
                .into(profile);
        //enddate.setText(getIntent().getStringExtra("fullname"));

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
