package test.soc365.society365.maneger.MainReportTap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import test.soc365.society365.R;
import test.soc365.society365.maneger.Cash_Bank_Balance.CashBankBalanceActivity;
import test.soc365.society365.maneger.Daybook.DayBookActivity;
import test.soc365.society365.maneger.Main2Activity;
import test.soc365.society365.maneger.Outstanding_Receivable.ReceivableActivity;
import test.soc365.society365.maneger.PartyFragment;
import test.soc365.society365.maneger.PaymentReports.PaymentReportActivity;
import test.soc365.society365.maneger.Report.Memberreport;
import test.soc365.society365.maneger.Sales_Register.SalesChildActivity;
import test.soc365.society365.maneger.Trail_Balance.TrailBalance;
import test.soc365.society365.maneger.Trail_Balance.TrailBalanceActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Reportmodel> reportlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    ArrayList text1;
    int[] images1;
    ReportFragmentAdapter adapter;
    String societyid,usrid,utype;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    public ReportFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclevieww);
     /*   String[] strs = { "abc","123","zzz" };
        reportlist= new ArrayList<>();
        for(int i =  0; i < strs.length; i++){
            reportlist.add(strs[i]);  //something like this?
        }
*/



        initializeCarItemList();
        linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

         text1 = new ArrayList<>(Arrays.asList("Member Summary","Party Ledger","Sales Register","Daybook",
                 "Payment Report","Receipt Register","Trial balance ","Cash Book Balance","Outstanding Receivable"));
         images1 = new int[]{R.drawable.credit, R.drawable.credit, R.drawable.credit,
                 R.drawable.credit, R.drawable.credit, R.drawable.credit, R.drawable.credit,
                 R.drawable.credit, R.drawable.credit, R.drawable.credit,
                 R.drawable.credit, R.drawable.credit, R.drawable.credit};

         adapter = new ReportFragmentAdapter(getActivity(), text1,images1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);


        sharedPreferences = getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        usrid = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );



        return view;
    }

    

    /* Initialise car items in list. */
    private void initializeCarItemList()
    {
        if(reportlist == null)
        {
            reportlist = new ArrayList<Reportmodel>();
            reportlist.add(new Reportmodel("Audi", R.drawable.credit));
            reportlist.add(new Reportmodel("BMW", R.drawable.credit));
            reportlist.add(new Reportmodel("Benz", R.drawable.credit));
            reportlist.add(new Reportmodel("Jeep", R.drawable.credit));
            reportlist.add(new Reportmodel("Land Rover", R.drawable.credit));
            reportlist.add(new Reportmodel("Future", R.drawable.credit));
        }
    }


    public class ReportFragmentAdapter extends RecyclerView.Adapter<ReportFragmentAdapter.MyViewHolder>
    {
        ArrayList text1;
        Context context;
        private int[] mResources1;

        public ReportFragmentAdapter(Context context,ArrayList text1,int[] resources1) {
            this.text1 = text1;
            this.context = context;
            this.mResources1 = resources1;
        }


        @NonNull
        @Override
        public ReportFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reportfragment,parent,false);
            return new ReportFragmentAdapter.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ReportFragmentAdapter.MyViewHolder holder, final int position) {
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
                        Intent intentmr=new Intent(context, Memberreport.class);
                      /*  Intent intentmr=new Intent(context, Main2Activity.class);*/
                        intentmr.putExtra("societyid", societyid);
                        intentmr.putExtra("userid", usrid);
                        intentmr.putExtra("usertype", utype);
                        context.startActivity(intentmr);
                 }else if(position==1)
                {
                    adapter.notifyDataSetChanged();
                    text1.clear();
                    images1=new int[]{};

                   PartyFragment fragment = new PartyFragment();
                    replaceFragment(fragment);
                }else if(position==2)
                    {
                        Intent intent = new Intent(context,SalesChildActivity.class);
                        intent.putExtra("type","from_sales");
                        context.startActivity(intent);
                    }
                    else if(position==3)
                    {

                        Intent intent = new Intent(context,DayBookActivity.class);
                        context.startActivity(intent);

                   /* MainActivity mainActivity = new MainActivity();
                    mainActivity.callpartyledger_fragment();*/
                    }
                    else if(position==4)
                    {
                        Intent intent = new Intent(context,PaymentReportActivity.class);
                        context.startActivity(intent);
                    }
                    else   if(position==5)
                    {/*
                        RecieptActivity*/
                        Intent intent = new Intent(context,SalesChildActivity.class);
                        intent.putExtra("type","from_receipt");
                        context.startActivity(intent);
                    }
                    else   if(position==6)
                    {
                        Intent intent = new Intent(context,TrailBalance.class);
                        context.startActivity(intent);
                    }
                    else   if(position==7)
                    {
                        Intent intent = new Intent(context,CashBankBalanceActivity.class);
                        context.startActivity(intent);
                    }
                    else   if(position==8)
                    {
                       /* Intent intent = new Intent(context,ReceivableActivity.class);
                       */
                        Intent intent = new Intent(context,SalesChildActivity.class);
                        intent.putExtra("type","from_ReceivableActivity");
                        context.startActivity(intent);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return text1.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder  {

            public TextView text1,text3;
            public ImageView image1,image2;
            LinearLayout fullbody;

            public MyViewHolder(View itemView) {
                super(itemView);

                image1=(ImageView) itemView.findViewById(R.id.image1);
                //  image2=(ImageView) itemView.findViewById(R.id.image2);

                text1=(TextView)itemView.findViewById(R.id.reportname);
                text3 =(TextView)itemView.findViewById(R.id.reportnameshort);

                fullbody = (LinearLayout)itemView.findViewById(R.id.fullbody);

            }


        }
    }

    public   void replaceFragment(Fragment fragment) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }











}
