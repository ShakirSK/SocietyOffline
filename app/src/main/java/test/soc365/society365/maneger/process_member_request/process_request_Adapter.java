package test.soc365.society365.maneger.process_member_request;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

public class process_request_Adapter extends RecyclerView.Adapter<process_request_Adapter.MyViewHolder> {

    private ArrayList<processrequestmodel> processrequestmodelArrayList;
    private Activity context;

    public process_request_Adapter(Activity context,ArrayList<processrequestmodel>processrequestmodelArrayList){
      this.processrequestmodelArrayList=processrequestmodelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_process_member_request, parent, false);
        process_request_Adapter.MyViewHolder myViewHolder = new process_request_Adapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        TextView prequest_id=holder.request_id;
        TextView prequest_date=holder.request_date;
        TextView prequest_from_id=holder.request_from_id;
        TextView pstatus=holder.status;
        ImageView pact=holder.act;

        prequest_id.setText(processrequestmodelArrayList.get(position).getRequest_id());
        prequest_date.setText(processrequestmodelArrayList.get(position).getRequest_date());
        prequest_from_id.setText(processrequestmodelArrayList.get(position).getRequest_from_id());

        String strstatus=processrequestmodelArrayList.get(position).getStatus();
        if(strstatus.equals("1"))
        {
            pstatus.setText("Open");
        }
        else
        {
            pstatus.setText("Close");
        }


        pact.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, MemberRequestWrite.class);
               intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
               intent.putExtra("request_id", processrequestmodelArrayList.get(position).getRequest_id());
               intent.putExtra("request_date", processrequestmodelArrayList.get(position).getRequest_date());
               intent.putExtra("Member_name", processrequestmodelArrayList.get(position).getRequest_from_id());
               intent.putExtra("status", processrequestmodelArrayList.get(position).getStatus());
               intent.putExtra("reqmesg", processrequestmodelArrayList.get(position).getUser_message());
               intent.putExtra("respmesg", processrequestmodelArrayList.get(position).getResponse_message());
               //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
           }
       });
    }



    @Override
    public int getItemCount() {
        return processrequestmodelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView request_id,request_from_id,request_date,request_type,user_message,response_message,status,created_on;
        ImageView act;

     public MyViewHolder(@NonNull View itemView) {
         super(itemView);
         request_id=itemView.findViewById(R.id.membid);
        request_date=itemView.findViewById(R.id.reqdate);
        request_from_id=itemView.findViewById(R.id.memname);
        status=itemView.findViewById(R.id.here);
        act=itemView.findViewById(R.id.actbtn);


     }
 }
}
