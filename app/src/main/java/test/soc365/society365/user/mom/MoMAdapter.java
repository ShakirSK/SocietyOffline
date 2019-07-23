package test.soc365.society365.user.mom;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

/**
 * Created by PC1 on 28-07-2018.
 */

public class MoMAdapter extends RecyclerView.Adapter<MoMAdapter.Myviewholder>
{
    ArrayList<MoMModel> moMarraylist;
    Activity activity;


    public MoMAdapter(FragmentActivity activity, ArrayList<MoMModel> momarray)
    {
        this.activity=activity;
        moMarraylist=momarray;
    }

    @NonNull
    @Override

    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_mom,parent,false);
        Myviewholder momadapter=new Myviewholder(view);
        return momadapter;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position)
    {
        TextView umom=holder.momtitle;
        TextView udate=holder.datetitle;

        umom.setText(moMarraylist.get(position).getMomtitle());
        udate.setText(moMarraylist.get(position).getMomdate());

    }

    @Override
    public int getItemCount()
    {
        return moMarraylist.size();
    }


    public static class Myviewholder extends RecyclerView.ViewHolder
    {
        private TextView momtitle;
        private TextView datetitle;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            momtitle=itemView.findViewById(R.id.mom);
            datetitle=itemView.findViewById(R.id.date);
        }
    }
}
