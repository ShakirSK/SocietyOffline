package test.soc365.society365.maneger.Cash_Bank_Balance;


/**
 * Created by Anas on 1/30/2019.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test.soc365.society365.R;
import test.soc365.society365.maneger.partyledger.PartySummary;
import test.soc365.society365.maneger.partyledger.partydetailmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by ankit on 27/10/17.
 */

public class CashBookDetailPageAdapter extends RecyclerView.Adapter<CashBookDetailPageAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<partydetailmodel> list;
    private List<partydetailmodel> trailbal;

    private ContactsAdapterListener listener;
    private List<partydetailmodel> contactListFiltered;
   /* int ob = Integer.parseInt(openbalance);
    int obc = Integer.parseInt(openbalance);*/
    static int cb;

    public CashBookDetailPageAdapter(Context context, List<partydetailmodel> list, List<partydetailmodel> trailbal) {
        this.context = context;
        this.list = list;
        this.trailbal = trailbal;
        this.contactListFiltered = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.partydetailitempage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final partydetailmodel movie = contactListFiltered.get(position);

        partydetailmodel running = trailbal.get(position);

        holder.textTitle.setText(movie.getVoucher_number());
      /*  if(movie.getVoucher_number().equals(""))
        {
            holder.partylayout.setVisibility(View.GONE);
        }*/
        Date date = null;
        try {
            date = new SimpleDateFormat( "yyyy-MM-dd" , Locale.ENGLISH ).parse(movie.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat( "dd/MMM/yyyy" , Locale.getDefault() );
        String t1= formatter.format(date);

        holder.textRating.setText(t1 +" | "+ String.valueOf(movie.getVoucher_type()));

        if("Bill".equals(movie.getVoucher_type())) {
            holder.amount.setText("\u20B9 " + movie.getAmt_for_receipt() + " | " + "Debit");
        }
        else
        {
            holder.amount.setText("\u20B9 " + movie.getAmt_for_receipt() + " | " + String.valueOf(movie.getDr_cr()));
        }
        if(movie.getDr_cr().equals("-"))
        {
            holder.amount.setText("\u20B9 "+movie.getAmt_for_receipt()+" | "+ "Credit");
        }

        holder.preamount.setText("("+"\u20B9 "+running.getPrev_balance()+")");
        holder.preamount.setTextColor(Color.parseColor("#42C0FB"));
       /* if(movie.getDr_cr().equals("Debit"))
        {
*//*
            int a = Integer.parseInt(movie.getAmount());
            int sd = ob - a;

            ob=sd;*//*
            holder.preamount.setText("("+"\u20B9 "+running.getPrev_balance()+")");
            holder.preamount.setTextColor(Color.parseColor("#42C0FB"));
       *//*     cb=sd;*//*

        }
        else
        {*//*
            int a = Integer.parseInt(movie.getAmount());
            int sd = obc + a;

            obc =sd;*//*
            holder.preamount.setText("("+"\u20B9 "+movie.getPrev_balance()+")");
            holder.preamount.setTextColor(Color.parseColor("#42C0FB"));
           // cb=sd;
        }
*/
        ArrayList arrayVT = new ArrayList<>(Arrays.asList("Bill","Receipt","Journal"));
        if("Bill".equals(movie.getVoucher_type()))
        {
            holder.amount.setTextColor(Color.parseColor("#008000"));
        }
        else if("Receipt".equals(movie.getVoucher_type())||"Journal".equals(movie.getVoucher_type()))
        {
            if(movie.getDr_cr().equals("Debit"))
            {
                holder.amount.setTextColor(Color.parseColor("#008000"));
            }
            else
            {
                holder.amount.setTextColor(Color.parseColor("#FF0000"));
            }
        }
        else if(!arrayVT.contains(movie.getVoucher_type()))
        {
            if(movie.getDr_cr().equals("Debit"))
            {
                holder.amount.setTextColor(Color.parseColor("#008000"));
            }
            else
            {
                holder.amount.setTextColor(Color.parseColor("#FF0000"));
            }
        }

        holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = movie.getVoucher_type();
                String number = movie.getVoucher_number();
                String cqdate =movie.getCqdate();
                String am = String.valueOf(movie.getAmt_for_receipt());


                Log.d( "partyledgername1", String.valueOf(movie.getAmt_for_receipt()));
                Intent intent = new Intent(context,PartySummary.class);
                intent.putExtra("vouchertype",type);
                intent.putExtra("vouchernumber",number);
                intent.putExtra("ledgername",movie.getLedger_name());
                intent.putExtra("partyname",movie.getPartyname());
                intent.putExtra("Date",movie.getDate());
                intent.putExtra("ChequeNo",movie.getCqno());
                intent.putExtra("ChequeDate",cqdate);
                intent.putExtra("BankDate",movie.getBankdate());
                intent.putExtra("Narration",movie.getNarration());
                intent.putExtra("Amount",am);
                intent.putExtra("amtotal",am);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating,amount,preamount;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            textRating = itemView.findViewById(R.id.flatno);
            amount = itemView.findViewById(R.id.amount);
            preamount = itemView.findViewById(R.id.preamount);

            partylayout = itemView.findViewById(R.id.partylayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list;
                } else {
                    List<partydetailmodel> filteredList = new ArrayList<>();
                    for (partydetailmodel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getVoucher_type().toLowerCase().contains(charString.toLowerCase()) || row.getVoucher_number().contains(charSequence) ) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<partydetailmodel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(partydetailmodel contact);
    }


}