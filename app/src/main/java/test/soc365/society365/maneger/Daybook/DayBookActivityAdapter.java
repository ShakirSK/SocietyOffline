package test.soc365.society365.maneger.Daybook;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DayBookActivityAdapter extends RecyclerView.Adapter<DayBookActivityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<partydetailmodel> list;

    private ContactsAdapterListener listener;
    private List<partydetailmodel> contactListFiltered;

    public DayBookActivityAdapter(Context context, List<partydetailmodel> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_daybookactivity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final partydetailmodel movie = contactListFiltered.get(position);

        Date date = null;
        try {
            date = new SimpleDateFormat( "yyyy-MM-dd" , Locale.ENGLISH ).parse(movie.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat( "dd/MMM/yyyy" , Locale.getDefault() );
        String timeperiod1 = formatter.format( date );

        holder.textTitle.setText(movie.getPartyname());
        holder.date.setText(timeperiod1);

        holder.amount.setText("\u20B9"+movie.getAmt_for_receipt());
        holder.vochurtype.setText(movie.getVoucher_type());
        holder.vouchernumber.setText(movie.getVoucher_number());

        holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String type = movie.getVoucher_type();
                String number = movie.getVoucher_number();
                String cqdate =movie.getCqdate();
                String am = String.valueOf(movie.getAmt_for_receipt());

                Intent intent = new Intent(context,PartySummary.class);
                intent.putExtra("vouchertype",type);
                intent.putExtra("vouchernumber",number);
                intent.putExtra("ledgername",movie.getLedger_name());
                intent.putExtra("partyname",movie.getPartyname());
                intent.putExtra("Date",movie.getDate());
                intent.putExtra("ChequeNo",movie.getCqno());
                intent.putExtra("ChequeDate",movie.getCqdate());
                intent.putExtra("BankDate",movie.getBankdate());
                intent.putExtra("Narration",movie.getNarration());
                intent.putExtra("Amount",am);
                if(type.equals("Payment"))
                {
                    intent.putExtra("amtotal",am);
                }
                else
                {
                    intent.putExtra("amtotal","notApp");
                }

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
        public TextView textTitle, vouchernumber,vochurtype,amount,date;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            vouchernumber = itemView.findViewById(R.id.vouchernumber);
            vochurtype = itemView.findViewById(R.id.vochurtype);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);

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
                        if (row.getPartyname().toLowerCase().contains(charString.toLowerCase()) || row.getVoucher_number().contains(charSequence)|| row.getVoucher_type().toLowerCase().contains(charString.toLowerCase())) {
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