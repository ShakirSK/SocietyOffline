package test.soc365.society365.user.audit_report;

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

public class auditAdapter extends RecyclerView.Adapter<auditAdapter.auditviewholder>
{
    ArrayList<auditModel> audirarraylist;
    Activity activity;

    public auditAdapter(FragmentActivity activity, ArrayList<auditModel> auditarray)
    {
        this.activity=activity;
        this.audirarraylist=auditarray;
    }

    @NonNull
    @Override
    public auditviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_auditreport,parent,false);
        auditviewholder auditadapter=new auditviewholder(view);
        return auditadapter;
    }

    @Override
    public void onBindViewHolder(@NonNull auditviewholder holder, int position)
    {
        TextView atitle=holder.title;
        TextView adate=holder.date;
        TextView attach=holder.attach;

        atitle.setText(audirarraylist.get(position).getAudittitle());
        adate.setText(audirarraylist.get(position).getAuditdate());
        String attchurl=audirarraylist.get(position).getAttach();
        /*attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.d("ok","ok");

            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return audirarraylist.size();
    }

    public static class auditviewholder extends RecyclerView.ViewHolder
    {
        TextView title,date,attach;
        public auditviewholder(@NonNull View itemView)
        {
            super(itemView);

            title=itemView.findViewById(R.id.audit);
            date=itemView.findViewById(R.id.date);
            attach=itemView.findViewById(R.id.attach);
        }
    }
}
