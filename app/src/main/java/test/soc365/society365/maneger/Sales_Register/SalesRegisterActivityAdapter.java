package test.soc365.society365.maneger.Sales_Register;

/**
 * Created by Anas on 2/7/2019.
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
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.partyledger.PartySummary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static test.soc365.society365.maneger.Sales_Register.SalesRegisterActivity.ledgertype;


public class SalesRegisterActivityAdapter extends RecyclerView.Adapter<SalesRegisterActivityAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<sales_register_model> list;

    private ContactsAdapterListener listener;
    private List<sales_register_model> contactListFiltered;
    String narr,bd,cd,cn,pn,ln;

    public SalesRegisterActivityAdapter(Context context, List<sales_register_model> list) {
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
        holder.flatno.setText(movie.getFlatno());
        holder.amount.setText("\u20B9"+movie.getAmt_for_receipt());
        holder.textTitle.setText(movie.getName());


        Date date = null;
        try {
            date = new SimpleDateFormat( "yyyy-MM-dd" , Locale.ENGLISH ).parse(movie.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat( "dd/MMM/yyyy" , Locale.getDefault() );
        String t1= formatter.format(date);

         if (ledgertype.equals("from_receipt"))
        {
            if(movie.getLed_name().equals("Cash")||movie.getLed_name().equals("T.J.S.B A/c"))
            {
                holder.textTitle.setText(movie.getName());
                holder.vno.setVisibility(View.VISIBLE);
                holder.date.setVisibility(View.VISIBLE);
                holder.vno.setText(movie.getVn());
                holder.date.setText(" | "+t1);
            }
            else {
                holder.textTitle.setText(movie.getLed_name());
                holder.vno.setVisibility(View.VISIBLE);
                holder.date.setVisibility(View.VISIBLE);
                holder.vno.setText(movie.getVn());
                holder.date.setText(" | "+t1);
            }

        }

/*

        if(movie.getName().contains("(")) {
            holder.flatno.setVisibility(View.GONE);
        }

*/


if (ledgertype.equals("from_sales_wingwise_summary")||ledgertype.equals("from_sales_account"))
{

}
else   if (ledgertype.equals("from_receipt"))
{
    holder.partylayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context,PartySummary.class);
            intent.putExtra("vouchertype",movie.getType());
            intent.putExtra("vouchernumber",movie.getVn());
            intent.putExtra("partyname",movie.getName());
            intent.putExtra("ledgername",movie.getLed_name());
            String am = String.valueOf(movie.getAmt_for_receipt());
            intent.putExtra("Date",movie.getDate());
            intent.putExtra("ChequeNo",movie.getCqno());
            intent.putExtra("ChequeDate",movie.getCqdate());
            intent.putExtra("BankDate",movie.getBankdate());
            intent.putExtra("Narration",movie.getNarration());
            intent.putExtra("Amount",am);
            intent.putExtra("amtotal","notApp");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

           /* if(movie.getLed_name().equals("Cash")||movie.getLed_name().equals("T.J.S.B A/c")) {
                Intent intent = new Intent(context,PartySummary.class);
                intent.putExtra("vouchertype",movie.getType());
                intent.putExtra("vouchernumber",movie.getVn());
                intent.putExtra("partyname",movie.getName());
                intent.putExtra("ledgername",movie.getLed_name());
                String am = String.valueOf(movie.getAmt_for_receipt());
                intent.putExtra("Date",movie.getDate());
                intent.putExtra("ChequeNo",movie.getCqno());
                intent.putExtra("ChequeDate",movie.getCqdate());
                intent.putExtra("BankDate",movie.getBankdate());
                intent.putExtra("Narration",movie.getNarration());
                intent.putExtra("Amount",am);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else
            {

                DatabaseHelper db = new DatabaseHelper(context);
                ArrayList<String> allToDos3 =   db.getPartyledger_ReceiptDebit(movie.getLed_name(),"Receipt",movie.getVn());

                narr= allToDos3.get(0);
                cn=allToDos3.get(1);
                cd=allToDos3.get(2);
                bd=allToDos3.get(3);
                ln=allToDos3.get(4);
                pn=allToDos3.get(5);

                Intent intent = new Intent(context,PartySummary.class);
                intent.putExtra("vouchertype",movie.getType());
                intent.putExtra("vouchernumber",movie.getVn());
                intent.putExtra("ledgername",ln);
                intent.putExtra("partyname",pn);
                intent.putExtra("Date",movie.getDate());
                String am = String.valueOf(movie.getAmt_for_receipt());


                intent.putExtra("ChequeNo",cn);
                intent.putExtra("ChequeDate",cd);
                intent.putExtra("BankDate",bd);
                intent.putExtra("Narration",narr);
                intent.putExtra("Amount",am);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

*/
        }
    });

}
else
{
    holder.partylayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String partname = movie.getName();
            Intent intent = new Intent(context,SalesRegisterDetailPage.class);
            intent.putExtra("partyledgername",partname);
            intent.putExtra("showpdfstatus","0");
            intent.putExtra("ledgerid",ledgertype);
            intent.putExtra("date1",movie.getDate1());
            intent.putExtra("date2",movie.getDate2());
            context.startActivity(intent);
        }
    });
}

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle,amount,flatno,date,vno;
        RelativeLayout partylayout;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.partyname);
            amount = itemView.findViewById(R.id.amount);

            flatno = itemView.findViewById(R.id.flatno);
            date = itemView.findViewById(R.id.date);
            vno = itemView.findViewById(R.id.vno);


            partylayout = itemView.findViewById(R.id.partylayout);
            if (ledgertype.equals("from_sales_wingwise_summary")||ledgertype.equals("from_sales_account"))
            {

            }
            else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // send selected contact in callback
                        listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                    }
                });
            }
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

                        if (ledgertype.equals("from_receipt"))
                        {

                            if(row.getFlatno()==null)
                            {
                                if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getCqno().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }

                            }
                            else
                            {
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getCqno().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }

                            }



                          /*  //if comes from Others that does not have flat name
                            if(row.getFlatno()==null)     /* ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())
                            {
                                if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getCqno().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getVn().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                            }
                            else
                            {  // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getCqno().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getVn().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }

                            }*/

                        }
                        else {
                            //if comes from Others that does not have flat name
                            if(row.getFlatno()==null)
                            {
                                if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                        ) {
                                    filteredList.add(row);
                                }
                            }
                            else
                            {
                                // name match condition. this might differ depending on your requirement
                                // here we are looking for name or phone number match
                                if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                        ||row.getFlatno().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                            }


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