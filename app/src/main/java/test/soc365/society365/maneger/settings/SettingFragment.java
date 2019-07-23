package test.soc365.society365.maneger.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.login.Login;
import test.soc365.society365.maneger.Create_billCharges.CreateBill;
import test.soc365.society365.maneger.MainReportTap.ReportFragmentAdapter;
import test.soc365.society365.maneger.ManagerProfile;
import test.soc365.society365.maneger.MomReport.MomFragment;
import test.soc365.society365.maneger.auditreport.AuditFragment;
import test.soc365.society365.user.changepass.change_password;

import java.util.ArrayList;
import java.util.Arrays;

import static test.soc365.society365.maneger.MainActivity.bldid;
import static test.soc365.society365.maneger.MainActivity.editor;
import static test.soc365.society365.maneger.MainActivity.fltno;
import static test.soc365.society365.maneger.MainActivity.joindate;
import static test.soc365.society365.maneger.MainActivity.mno;
import static test.soc365.society365.maneger.MainActivity.societyid;
import static test.soc365.society365.maneger.MainActivity.uemail;
import static test.soc365.society365.maneger.MainActivity.ufname;
import static test.soc365.society365.maneger.MainActivity.ulname;
import static test.soc365.society365.maneger.MainActivity.uprof;
import static test.soc365.society365.maneger.MainActivity.usrid;
import static test.soc365.society365.maneger.MainActivity.utype;


public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclevieww);
        linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        ArrayList text1 = new ArrayList<>(Arrays.asList("Create Bill","MOM Report","Audit Report","Profile","Change Password","Logout"));
        ArrayList text3 = new ArrayList<>(Arrays.asList("WID"));

        int[] images1 = {R.drawable.credit, R.drawable.credit, R.drawable.credit, R.drawable.credit,
                R.drawable.credit, R.drawable.credit};

        int[] images2 = {R.drawable.credit, R.drawable.credit, R.drawable.credit, R.drawable.credit};

        SettingFragmentAdapter adapter = new SettingFragmentAdapter(getActivity(), text1,text3,images1,images2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);


        return view;
    }
    public class SettingFragmentAdapter extends RecyclerView.Adapter<SettingFragmentAdapter.MyViewHolder>
    {
        ArrayList text1;
        ArrayList text3;
        Context context;
        private int[] mResources1;
        private int[] mResources2;


        public SettingFragmentAdapter(Context context,ArrayList text1,ArrayList text3,int[] resources1,int[] resources2) {
            this.text1 = text1;
            this.text3 = text3;
            this.context = context;
            this.mResources1 = resources1;
            this.mResources2 = resources2;
        }



        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reportfragment,parent,false);
            return new SettingFragmentAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
        /*ListItem listItem = name.get(position);
        holder.text1.setText(listItem.getText());*/
            holder.text1.setText((CharSequence) text1.get(position));
            //  holder.text3.setText((CharSequence) text3.get(position));
            holder.image1.setImageResource(mResources1[position]);
            // holder.image2.setImageResource(mResources2[position]);
/*
        Toast.makeText(context,text3.size(),Toast.LENGTH_SHORT).show();

        if(1.equals(text3.size()))
        {
            holder.text3.setVisibility(View.VISIBLE);
           holder.text3.setText((CharSequence) text3.get(position));
        }*/
            holder.fullbody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0)
                    {
                        Intent intentcr=new Intent(getActivity(), CreateBill.class);
                        intentcr.setFlags(intentcr.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intentcr.putExtra("societyid", societyid);
                        intentcr.putExtra("userid", usrid);
                        intentcr.putExtra("usertype", utype);
                        startActivity(intentcr);
                    }
                    else if(position==1)
                    {
                        Intent intentmom=new Intent(getActivity(), MomFragment.class);
                        intentmom.putExtra("societyid", societyid);
                        intentmom.putExtra("userid", usrid);
                        intentmom.putExtra("usertype", utype);
                        startActivity(intentmom);
                    }
                    else if(position==2)
                    {
                        Intent intentau=new Intent(getActivity(), AuditFragment.class);
                        intentau.putExtra("societyid", societyid);
                        intentau.putExtra("userid", usrid);
                        intentau.putExtra("usertype", utype);
                        startActivity(intentau);
                    }

                    else if(position==3)
                    {
                        Intent intentprof=new Intent(getActivity(), ManagerProfile.class);
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
                    }
                    else if(position==4)
                    {
                        Intent changepass=new Intent(getActivity(), change_password.class);
                        changepass.putExtra("societyid", societyid);
                        changepass.putExtra("userid", usrid);
                        changepass.putExtra("usertype", utype);
                        startActivity(changepass);

                    }
                    else if(position==5)
                    {
                        Intent intent=new Intent(getActivity(), Login.class);
                        DatabaseHelper  db = new DatabaseHelper(getContext());
                        db.deleteUsers();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
          
        }

        @Override
        public int getItemCount() {
            return text1.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

            public TextView text1,text3;
            public ImageView image1,image2;
            LinearLayout fullbody;

            public MyViewHolder(View itemView) {
                super(itemView);

                itemView.setOnClickListener(this);
                image1=(ImageView) itemView.findViewById(R.id.image1);
                //  image2=(ImageView) itemView.findViewById(R.id.image2);

                text1=(TextView)itemView.findViewById(R.id.reportname);
                text3 =(TextView)itemView.findViewById(R.id.reportnameshort);
                fullbody=(LinearLayout)itemView.findViewById(R.id.fullbody);

            }


            @Override
            public void onClick(View view) {
                
            }
        }
    }


}


