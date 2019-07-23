package test.soc365.society365.maneger.paymentsocietymember;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

public class ProcessAcoountingAdapter extends RecyclerView.Adapter<ProcessAcoountingAdapter.MyViewholder>
{

    private ArrayList<ProcessAcouutingModel> processAcouutingModelArrayList;
    private Context context;

    public ProcessAcoountingAdapter(Context context, ArrayList<ProcessAcouutingModel> processAcouutingModelArrayList){
        this.processAcouutingModelArrayList= processAcouutingModelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.process_society_accounting, parent, false);
        ProcessAcoountingAdapter.MyViewholder MyViewholder = new ProcessAcoountingAdapter.MyViewholder(view);
        return MyViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position)
    {
        TextView billamt,paymode,refno,bank,status,paidamt,name;
        billamt=holder.billamt;
        paymode=holder.paymode;
        refno=holder.refno;
        bank=holder.bank;
        status=holder.status;
        paidamt=holder.paidamt;
        name=holder.name;

        billamt.setText(processAcouutingModelArrayList.get(position).getBill_amount());
        refno.setText(processAcouutingModelArrayList.get(position).getRefrence_no());
        bank.setText(processAcouutingModelArrayList.get(position).getBank_account());
        paidamt.setText(processAcouutingModelArrayList.get(position).getPaid_amount());
        name.setText(processAcouutingModelArrayList.get(position).getUsername());

        String paymentmode = processAcouutingModelArrayList.get(position).getPayment_mode();
        //0-cash	1-online	2-cheque
        if(paymentmode.equals("0"))
        {
            paymode.setText("Cash");
        }else  if(paymentmode.equals("1"))
        {
            paymode.setText("Online");
        }else  if(paymentmode.equals("2"))
        {
            paymode.setText("Cheque");
        }else
            {
            paymode.setText("Unpaid");
        }



        if(processAcouutingModelArrayList.get(position).getStatus().equals("0"))
        {
            status.setText("Unpaid");
        }
        else if(processAcouutingModelArrayList.get(position).getStatus().equals("1"))
        {
            status.setText("Paid");
        }
    }

    @Override
    public int getItemCount()
    {
        return processAcouutingModelArrayList.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder
{
    TextView billamt,paymode,refno,bank,status,paidamt,name;

    public MyViewholder(@NonNull View itemView)
    {
        super(itemView);
        billamt=itemView.findViewById(R.id.billamt);
        paymode=itemView.findViewById(R.id.pmode);
        refno=itemView.findViewById(R.id.rno);
        bank=itemView.findViewById(R.id.bankacc);
        status=itemView.findViewById(R.id.status);
        paidamt=itemView.findViewById(R.id.paidamt);
        name=itemView.findViewById(R.id.lastname);

    }
}
}
