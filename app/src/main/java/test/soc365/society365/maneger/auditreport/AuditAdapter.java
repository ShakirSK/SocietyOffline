package test.soc365.society365.maneger.auditreport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.auditviewholder>
{
    private ArrayList<auditmodel> auditmodelArrayList;
    private Context context;

    public AuditAdapter(Context context,ArrayList<auditmodel> auditmodelArrayList) {
        this.auditmodelArrayList = auditmodelArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public auditviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_mom_report,parent,false );
        AuditAdapter.auditviewholder auditviewholder=new AuditAdapter.auditviewholder(view);
        return auditviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull auditviewholder holder, int position) {

        TextView auditreport=holder.report;
        TextView auditdate=holder.date;
        final ImageView auditdelete=holder.delete;

        auditreport.setText(auditmodelArrayList.get(position).getTitle());
        auditdate.setText(auditmodelArrayList.get(position).getCreated_on());
/*

        auditdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater =LayoutInflater.from(context);
                View alertLayout = inflater.inflate(R.layout.dailog_delete, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        // String user = etUsername.getText().toString();
                        // String pass = etEmail.getText().toString();
                        //Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();

                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return auditmodelArrayList.size();
    }

    public static class auditviewholder extends RecyclerView.ViewHolder
    {
        TextView report,date;
        ImageView delete;
        public auditviewholder(@NonNull View itemView)
        {
            super(itemView);
            report=itemView.findViewById(R.id.report);
            date=itemView.findViewById(R.id.date);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
