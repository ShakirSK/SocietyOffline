package test.soc365.society365.maneger.PaymentReports;

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
import test.soc365.society365.maneger.Sales_Register.SalesRegisterDetailPage;
import test.soc365.society365.maneger.Sales_Register.sales_register_model;

import java.util.ArrayList;
import java.util.List;

public class PaymentReportActivityAdapter extends RecyclerView.Adapter<PaymentReportActivityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<sales_register_model> list;

    private ContactsAdapterListener listener;
    private List<sales_register_model> contactListFiltered;

    public PaymentReportActivityAdapter(Context context, List<sales_register_model> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_salesregister, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final sales_register_model movie = contactListFiltered.get(position);

        holder.textTitle.setText(movie.getName());

        holder.amount.setText("\u20B9"+movie.getAmount());

        holder.flatno.setVisibility(View.GONE);

        holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String partname = movie.getName();
                Intent intent = new Intent(context,SalesRegisterDetailPage.class);
                intent.putExtra("partyledgername",partname);
                intent.putExtra("ledgerid","from_payment");
                intent.putExtra("showpdfstatus","0");
                intent.putExtra("date1",movie.getDate1());
                intent.putExtra("date2",movie.getDate2());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle,amount,flatno;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);
            flatno = itemView.findViewById(R.id.flatno);

            textTitle = itemView.findViewById(R.id.partyname);
            amount = itemView.findViewById(R.id.amount);


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
                    List<sales_register_model> filteredList = new ArrayList<>();
                    for (sales_register_model row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())||row.getAmount().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<sales_register_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(sales_register_model contact);
    }


}