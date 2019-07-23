package test.soc365.society365.maneger.partyledger;

/**
 * Created by Anas on 1/30/2019.
 */

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

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ankit on 27/10/17.
 */

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<partymodel> list;
    private List<partymodel> contactListFiltered;
    private ContactsAdapterListener listener;

    public PartyAdapter(Context context, List<partymodel> list) {
        this.context = context;
        this.list = list;
        this.contactListFiltered = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.partyitemlayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*List<partymodel> allToDos = db.getdata();
        for (partymodel todo : allToDos) {
            Log.d("ToDo", todo.getTitle());
        }*/
        final partymodel movie = contactListFiltered.get(position);
        holder.textTitle.setText(movie.getTitle());
        holder.textRating.setText(String.valueOf(movie.getRating()));


        holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String partname = movie.getTitle();
                Intent intent = new Intent(context,PartyLedgerDetailPage.class);
                intent.putExtra("partyledgername",partname);
                intent.putExtra("showpdfstatus","0");
                intent.putExtra("yearforReceivable","2018");
                intent.putExtra("isparty",movie.getIsparty());
                intent.putExtra("flatno",movie.getRating());
                intent.putExtra("ledgerid",movie.getledgerid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textRating;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            textRating = itemView.findViewById(R.id.flatno);
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
                    List<partymodel> filteredList = new ArrayList<>();
                    for (partymodel row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())
                                || row.getFlatnoremoved_SC().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<partymodel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(partymodel contact);
    }


}