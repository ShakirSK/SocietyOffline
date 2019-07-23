package test.soc365.society365.maneger.Sales_Register;


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
        import android.widget.Toast;

        import test.soc365.society365.R;
        import test.soc365.society365.helper.DatabaseHelper;
        import test.soc365.society365.maneger.partyledger.PartySummary;
        import test.soc365.society365.maneger.partyledger.partydetailmodel;

        import java.util.ArrayList;
        import java.util.List;

        import static test.soc365.society365.maneger.Sales_Register.SalesRegisterDetailPage.ledgerid;


/**
 * Created by ankit on 27/10/17.
 */

public class SalesRegisterDetailPageAdapter extends RecyclerView.Adapter<SalesRegisterDetailPageAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<partydetailmodel> list;

    private ContactsAdapterListener listener;
    private List<partydetailmodel> contactListFiltered;


    String narr,bd,cd,cn,pn,ln;

  String amtotal;
    public SalesRegisterDetailPageAdapter(Context context, List<partydetailmodel> list) {
        this.context = context;
        this.list = list;
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

        holder.textTitle.setText(movie.getVoucher_number());
      /*  if(movie.getVoucher_number().equals(""))
        {
            holder.partylayout.setVisibility(View.GONE);
        }*/
        holder.textRating.setText(String.valueOf(movie.getDate()) +" | "+ String.valueOf(movie.getVoucher_type()));



        holder.amount.setText("\u20B9 "+movie.getAmount()+" | "+ String.valueOf(movie.getDr_cr()));
        if(movie.getDr_cr().equals("-"))
        {
            holder.amount.setText("\u20B9 "+movie.getAmount()+" | "+ "Cr");
        }

        if(ledgerid.equals("Payment"))
        {
            holder.preamount.setVisibility(View.GONE);
        }
        else {
            holder.preamount.setText("(" + "\u20B9 " + movie.getPrev_balance() + ")");
        }

        if(movie.getDr_cr().equals("Dr"))
        {
            holder.amount.setTextColor(Color.parseColor("#008000"));
        }
        else
        {
            holder.amount.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.partylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> allToDos3 = null;
                if( movie.getNarration().contains(":"))
                {
                    String[] arrOfStr = movie.getNarration().split("\\:");
                    String[] arrOfStr2 = arrOfStr[1].split("\\s+");
                    Log.d("salesnarr",arrOfStr2[0]);

                    Toast.makeText(context,"1  " +arrOfStr2[0]+"   2  "+arrOfStr2[1],Toast.LENGTH_LONG).show();
                    DatabaseHelper db = new DatabaseHelper(context);
                    allToDos3 =   db.getPartyledger_ReceiptDebit(movie.getDate(),arrOfStr2[1],movie.getAmount());

                }
                else
                {
                  /*  Toast.makeText(context,"else",Toast.LENGTH_LONG).show();
*/
                    DatabaseHelper db = new DatabaseHelper(context);
                    allToDos3 =   db.getPartyledger_ReceiptDebit(movie.getDate(),"Paymentincash",movie.getAmount());
                }
              /* String[] arrOfStr = movie.getNarration().split("\\:");
                String[] arrOfStr2 = arrOfStr[1].split("\\s+");
                Log.d("salesnarr",arrOfStr2[0]);

                Toast.makeText(context,"1  " +arrOfStr2[0]+"   2  "+arrOfStr2[1],Toast.LENGTH_LONG).show();
                DatabaseHelper db = new DatabaseHelper(context);
                ArrayList<String> allToDos3 =   db.getPartyledger_ReceiptDebit(movie.getDate(),arrOfStr2[1],movie.getAmount());*/
              if(allToDos3.isEmpty())
              {
                  Toast.makeText(context,"Double entry in Cash Payment ",Toast.LENGTH_LONG).show();
              }
              else{
                  narr= allToDos3.get(0);
                  cn=allToDos3.get(1);
                  cd=allToDos3.get(2);
                  bd=allToDos3.get(3);
                  ln=allToDos3.get(4);
                  pn=allToDos3.get(5);
                  amtotal=allToDos3.get(6);
                  String am = String.valueOf(movie.getAmount());

                  // Log.d("Periodlast ",movie.getPeriod());
                  Intent intent = new Intent(context,PartySummary.class);
                  intent.putExtra("vouchertype",movie.getVoucher_type());
                  intent.putExtra("vouchernumber",movie.getVoucher_number());
                  intent.putExtra("ledgername",ln);
                  intent.putExtra("partyname",pn);
                  intent.putExtra("Date",movie.getDate());
                  intent.putExtra("ChequeNo",cn);
                  intent.putExtra("ChequeDate",cd);
                  intent.putExtra("BankDate",bd);
                  intent.putExtra("Narration",narr);
                  intent.putExtra("Amount",am);
                  intent.putExtra("amtotal",amtotal);

                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(intent);
              }

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
                        if (row.getVoucher_type().toLowerCase().contains(charString.toLowerCase()) || row.getVoucher_number().contains(charSequence)) {
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