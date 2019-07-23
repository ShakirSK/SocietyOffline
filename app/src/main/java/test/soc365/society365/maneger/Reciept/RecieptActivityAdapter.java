package test.soc365.society365.maneger.Reciept;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anas on 2/11/2019.
 */


import test.soc365.society365.maneger.Sales_Register.SalesRegisterActivity;
import test.soc365.society365.maneger.Sales_Register.sales_register_model;

/*
import static test.soc365.society365.maneger.Sales_Register.SalesChildActivity.type;*/

public class RecieptActivityAdapter extends RecyclerView.Adapter<RecieptActivityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<sales_register_model> list;

    private ContactsAdapterListener listener;
    private List<sales_register_model> contactListFiltered;

    public RecieptActivityAdapter(Context context, List<sales_register_model> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_reportfragment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final sales_register_model movie = contactListFiltered.get(position);

        holder.text1.setText(movie.getName());
        holder.text3.setVisibility(View.VISIBLE);
        holder.text3.setText("\u20B9"+movie.getAmt_for_receipt());
        holder.image1.setVisibility(View.GONE);

/*
        holder.flatno.setText(movie.getFlatno());*/
       // holder.flatno.setVisibility(View.GONE);

        holder.fullbody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String partname = movie.getName();
                Intent intent = new Intent(context,SalesRegisterActivity.class);

                    intent.putExtra("type","from_sales_account");
                intent.putExtra("banktype","from_sales_account");
                  intent.putExtra("partyledgertype",partname);
                intent.putExtra("date1",movie.getDate1());
                intent.putExtra("date2",movie.getDate2());
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
        public TextView text1,text3;
        public ImageView image1,image2;
        LinearLayout fullbody;

        public ViewHolder(View itemView) {
            super(itemView);

            image1=(ImageView) itemView.findViewById(R.id.image1);
            //  image2=(ImageView) itemView.findViewById(R.id.image2);

            text1=(TextView)itemView.findViewById(R.id.reportname);
            text3 =(TextView)itemView.findViewById(R.id.amountwingwise);

            fullbody = (LinearLayout)itemView.findViewById(R.id.fullbody);

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
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())) {
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
