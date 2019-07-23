package test.soc365.society365.maneger.partyledger;


/**
 * Created by Anas on 1/30/2019.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.List;



/**
 * Created by ankit on 27/10/17.
 */

public class partySummaryAdapter extends RecyclerView.Adapter<partySummaryAdapter.ViewHolder> {

    private Context context;
    private List<partydetailmodel> list;

    public partySummaryAdapter(Context context, List<partydetailmodel> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.partydetailitempage, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final partydetailmodel movie = list.get(position);

        if(movie.getVoucher_type().equals("Journal"))
        {
            if(position==0)
            {
                holder.textTitle.setText(movie.getLedger_name());
                holder.textTitle.setTextColor(Color.parseColor("#46a5ce"));

                holder.amount.setText("\u20B9 "+movie.getAmount());
                holder.amount.setTextColor(Color.parseColor("#46a5ce"));

                holder.textRating.setVisibility(View.GONE);
                holder.preamount.setVisibility(View.GONE);
            }
            else
            {
                holder.textTitle.setText(movie.getLedger_name());
                holder.textRating.setVisibility(View.GONE);

                holder.amount.setText("\u20B9 "+movie.getAmount());
                holder.preamount.setVisibility(View.GONE);
            }
        }
        else
        {

            holder.textTitle.setText(movie.getLedger_name());
            holder.textRating.setVisibility(View.GONE);

            holder.amount.setText("\u20B9 "+movie.getAmount());
            holder.preamount.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
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

        }
    }

}