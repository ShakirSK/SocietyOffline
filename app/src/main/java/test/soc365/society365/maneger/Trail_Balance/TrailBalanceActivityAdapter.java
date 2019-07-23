package test.soc365.society365.maneger.Trail_Balance;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test.soc365.society365.R;
import test.soc365.society365.maneger.partyledger.partydetailmodel;

import java.util.ArrayList;
import java.util.List;

public class TrailBalanceActivityAdapter extends RecyclerView.Adapter<TrailBalanceActivityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<partydetailmodel> list;

    private ContactsAdapterListener listener;
    private List<partydetailmodel> contactListFiltered;

    public TrailBalanceActivityAdapter(Context context, List<partydetailmodel> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycleview_trail_balance, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final partydetailmodel movie = contactListFiltered.get(position);

        holder.textTitle.setText(movie.getVoucher_number());
      /*  if(movie.getVoucher_number().equals(""))
        {
            holder.partylayout.setVisibility(View.GONE);
        }*/
        holder.debitamount.setText(String.valueOf(movie.getDate()) +" Dr");
        holder.creditamount.setText( String.valueOf(movie.getVoucher_type())+" Cr");
        holder.debitamount.setTextColor(Color.parseColor("#008000"));
        holder.creditamount.setTextColor(Color.parseColor("#FF0000"));



        holder.amount.setText("OA | "+"\u20B9 "+String.valueOf(movie.getDr_cr()));
        holder.amountop.setText("CA | "+"\u20B9 "+movie.getAmount());
      /*  holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = movie.getVoucher_type();
                String number = movie.getVoucher_number();

                Log.d("Periodlast ",movie.getPeriod());
                Intent intent = new Intent(context,PartySummary.class);
                intent.putExtra("vouchertype",type);
                intent.putExtra("vouchernumber",number);
                intent.putExtra("ledgername","from_sales");
                intent.putExtra("Period",movie.getPeriod());

                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, debitamount,creditamount,amount,preamount,amountop;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            debitamount = itemView.findViewById(R.id.dr);
            creditamount = itemView.findViewById(R.id.cr);
            
            amount = itemView.findViewById(R.id.amount);
            amountop = itemView.findViewById(R.id.amountop);

            partylayout = itemView.findViewById(R.id.partylayout);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });*/
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
                        if (row.getVoucher_number().toLowerCase().contains(charString.toLowerCase())) {
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