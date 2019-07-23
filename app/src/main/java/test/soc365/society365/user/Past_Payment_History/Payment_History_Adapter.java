package test.soc365.society365.user.Past_Payment_History;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

/**
 * Created by PC1 on 28-07-2018.
 */

public class Payment_History_Adapter extends RecyclerView.Adapter<Payment_History_Adapter.payhistoryviewholder>
{
    ArrayList<Payment_History_Model> historyarraylist;
    Activity context;

    public Payment_History_Adapter(Activity activity, ArrayList<Payment_History_Model> historyarray)
    {
        context=activity;
        historyarraylist=historyarray;
    }

    @NonNull
    @Override
    public payhistoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pasthistory,parent,false);
        payhistoryviewholder payhistoryadapter=new payhistoryviewholder(view);
        return payhistoryadapter;
    }

    @Override
    public void onBindViewHolder(@NonNull payhistoryviewholder holder, int position)
    {
        TextView pid=holder.pid;
        TextView uid=holder.uid;
        TextView bamt=holder.bamt;
        TextView pamt=holder.pamt;
        TextView bacc=holder.bacc;
        TextView pmode=holder.pmode;
        TextView refno=holder.refno;
        TextView payrecieved=holder.payrecieved;
        TextView damt=holder.damt;
        TextView ddate=holder.ddate;

        pid.setText(historyarraylist.get(position).getPaymentid());
        uid.setText(historyarraylist.get(position).getUserid());
        bamt.setText(historyarraylist.get(position).getBillamt());
        //pamt.setText(historyarraylist.get(position).getPaidamt());
        //bacc.setText(historyarraylist.get(position).getBankacc());
        String paymentmode = historyarraylist.get(position).getPaymentmode();

        if(paymentmode.equals("0"))
        {
            pmode.setText("Cash");
        }else  if(paymentmode.equals("1"))
        {
            pmode.setText("Online");
        }else  if(paymentmode.equals("2"))
        {
            pmode.setText("Cheque");
        }else
        {
            pmode.setText("Unpaid");
        }
        refno.setText(historyarraylist.get(position).getRefno());
        ddate.setText(historyarraylist.get(position).getPaymentrecieved());
        //damt.setText(historyarraylist.get(position).getDepositeamt());
        Log.d("depositedate",historyarraylist.get(position).getDepoistedate());
        //ddate.setText(historyarraylist.get(position).getDepoistedate());
    }

    @Override
    public int getItemCount() {
        return historyarraylist.size();
    }

    public static class payhistoryviewholder extends RecyclerView.ViewHolder
    {
        TextView pid,uid,bamt,pamt,bacc,pmode,refno,payrecieved,damt,ddate;
        public payhistoryviewholder(@NonNull View itemView)
        {
            super(itemView);
            pid=itemView.findViewById(R.id.pid);
            uid=itemView.findViewById(R.id.uid);
            bamt=itemView.findViewById(R.id.bamt);
            //pamt=itemView.findViewById(R.id.pamt);
            //bacc=itemView.findViewById(R.id.bacc);
            pmode=itemView.findViewById(R.id.pmode);
            refno=itemView.findViewById(R.id.rno);
            //payrecieved=itemView.findViewById(R.id.dorec);
            //damt=itemView.findViewById(R.id.damt);
            ddate=itemView.findViewById(R.id.ddate);

        }
    }
}
