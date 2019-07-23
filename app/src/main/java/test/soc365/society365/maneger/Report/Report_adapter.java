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

public class Report_adapter extends RecyclerView.Adapter<Report_adapter.ReportViewholder>
{
    ArrayList<Report_Model> reportarray;
    Context context;

    public Report_adapter(Context applicationContext, ArrayList<Report_Model> reportarray)
    {
        context=applicationContext;
        this.reportarray=reportarray;
    }

    @NonNull
    @Override
    public ReportViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_reports, parent,false);
        ReportViewholder viewholder=new ReportViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewholder holder, int position)
    {
        TextView party=holder.party;
        TextView vouchertype=holder.vouchertype;
        TextView voucherno=holder.voucherno;
        TextView amt=holder.amt;

        TextView date=holder.date;



        date.setText(reportarray.get(position).getDate());
        voucherno.setText(reportarray.get(position).getVoucher_number());
        vouchertype.setText(reportarray.get(position).getVoucher_type());
        amt.setText(reportarray.get(position).getAmount());
        party.setText(reportarray.get(position).getParty_name());
    }

    @Override
    public int getItemCount()
    {
        return reportarray.size();
    }

    public static class ReportViewholder extends RecyclerView.ViewHolder
    {
        TextView date,voucherno,vouchertype,amt,party;
        public ReportViewholder(@NonNull View itemView)
        {
            super(itemView);
            date=itemView.findViewById(R.id.sdate);
            voucherno=itemView.findViewById(R.id.vno);
            vouchertype=itemView.findViewById(R.id.vtype);
            party=itemView.findViewById(R.id.partyname);
            amt=itemView.findViewById(R.id.amt);
        }
    }
}
