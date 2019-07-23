package test.soc365.society365.maneger.MomReport;

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

public class MomAdapter extends RecyclerView.Adapter<MomAdapter.MyViewHolder> {

    private ArrayList<MomModel> momModelArrayList;
    private Context context;

    public MomAdapter(Context context, ArrayList<MomModel> momModelArrayList) {
        this.momModelArrayList =momModelArrayList ;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_mom_report,parent,false );
        MomAdapter.MyViewHolder MyViewHolder=new MomAdapter.MyViewHolder(view);
        return MyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView momreport=holder.report;
        TextView momdate=holder.date;
        final ImageView momdelete=holder.delete;

        momreport.setText(momModelArrayList.get(position).getTitle());
        momdate.setText(momModelArrayList.get(position).getCreated_on());

    /*   momdelete.setOnClickListener(new View.OnClickListener()
        {
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
                alert.setPositiveButton("Ok",  new DialogInterface.OnClickListener()
                {
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
        return momModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView report,date;
        ImageView delete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            report=itemView.findViewById(R.id.report);
            date=itemView.findViewById(R.id.date);
            delete=itemView.findViewById(R.id.delete);
        }
    }



}
