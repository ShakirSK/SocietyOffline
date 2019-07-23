package test.soc365.society365.maneger.settings;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import test.soc365.society365.R;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import test.soc365.society365.R;
import test.soc365.society365.maneger.Daybook.DayBookActivity;
import test.soc365.society365.maneger.PaymentReports.PaymentReportActivity;
import test.soc365.society365.maneger.Reciept.RecieptActivity;
import test.soc365.society365.maneger.Sales_Register.SalesRegisterActivity;

import java.util.ArrayList;

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


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reportfragment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
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
        holder.setItemClickListener(new ItemClickListener() {

            @Override
            public void onItemClick(View v, int pos) {

                //INTENT OBJ
     /*           if(position==1)
                {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.container,YOUR_FRAGMENT_NAME,YOUR_FRAGMENT_STRING_TAG);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else*/
                if(position==0)
                {
                   /* Intent intent = new Intent(context,SalesRegisterActivity.class);
                    context.startActivity(intent);*/
                }
                else   if(position==1)
                {
                }
                else if(position==2)
                {
                }

                else if(position==3)
                {
                }
                else if(position==4)
                {

                }
                else if(position==5)
                {

                }
                Log.d("itemcliked", String.valueOf(text1.get(0)) + "    "+ String.valueOf(text3.get(0)));



                //ADD DATA TO OUR INTENT
                // i.putExtra("Name", (Bundle) text1.get(position));
                // i.putExtra("Position", (Bundle) text3.get(position));

                //START DETAIL ACTIVITY
                //context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return text1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public TextView text1,text3;
        private ItemClickListener itemClickListener;
        public ImageView image1,image2;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            image1=(ImageView) itemView.findViewById(R.id.image1);
            //  image2=(ImageView) itemView.findViewById(R.id.image2);

            text1=(TextView)itemView.findViewById(R.id.reportname);
            text3 =(TextView)itemView.findViewById(R.id.reportnameshort);


        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;

        }


    }
}






