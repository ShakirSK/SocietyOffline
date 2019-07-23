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

public class Report_adapter_details extends RecyclerView.Adapter<Report_adapter_details.ReportViewholder>
{
    ArrayList<Report_Model> reportarray;
    Context context;

    public Report_adapter_details(Context applicationContext, ArrayList<Report_Model> reportarray)
    {
        context=applicationContext;
        this.reportarray=reportarray;
    }

    @NonNull
    @Override
    public ReportViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_report_details, parent,false);
        ReportViewholder viewholder=new ReportViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewholder holder, int position)
    {
        TextView date=holder.datestr;
        TextView amt=holder.amt;
        TextView party=holder.party;
        TextView ledger=holder.ledgername;

        date.setText(reportarray.get(position).getDate());
        ledger.setText(reportarray.get(position).getLedgername());
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
        TextView datestr,amt,party,ledgername;
        public ReportViewholder(@NonNull View itemView)
        {
            super(itemView);
            datestr=itemView.findViewById(R.id.sdate);
            party=itemView.findViewById(R.id.partyname);
            amt=itemView.findViewById(R.id.amt);
            ledgername=itemView.findViewById(R.id.ledgername);
        }
    }
}
