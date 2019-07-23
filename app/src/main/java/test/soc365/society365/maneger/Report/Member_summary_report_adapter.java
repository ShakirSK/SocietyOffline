package test.soc365.society365.maneger.Report;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

/**
 * Created by PC1 on 10-08-2018.
 */

public class Member_summary_report_adapter extends RecyclerView.Adapter<Member_summary_report_adapter.Myviewholder>
{
    ArrayList<member_summary_report_model> summaryarray;
    Context context;

    public Member_summary_report_adapter(ArrayList<member_summary_report_model> summaryarray, Context context)
    {
        this.summaryarray = summaryarray;
        this.context = context;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_member__summary__report,parent,false);
        Myviewholder myviewholder=new Myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position)
    {
        TextView leger=holder.leger;
        TextView opening=holder.opening;
        TextView credit=holder.credit;
        TextView debit=holder.debit;
        TextView closing=holder.closing;

        leger.setText(summaryarray.get(position).getLeger());
        opening.setText(summaryarray.get(position).getOpening());
        credit.setText(summaryarray.get(position).getCredit());
        debit.setText(summaryarray.get(position).getDebit());
        closing.setText(summaryarray.get(position).getClosing());
    }

    @Override
    public int getItemCount()
    {
        return summaryarray.size();
    }

    public static class Myviewholder extends RecyclerView.ViewHolder
    {
        TextView leger,opening,credit,debit,closing;
        public Myviewholder(@NonNull View itemView)
        {
            super(itemView);
            leger=itemView.findViewById(R.id.party);
            opening=itemView.findViewById(R.id.voucher);
            credit=itemView.findViewById(R.id.trantype);
            debit=itemView.findViewById(R.id.voucherno);
            closing=itemView.findViewById(R.id.date);

        }
    }
}
