package test.soc365.society365.user.list_request_society;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

/**
 * Created by PC1 on 28-07-2018.
 */

public class request_society_adapter extends RecyclerView.Adapter<request_society_adapter.reqviewholder>
{
    ArrayList<request_society_model> reqarraylist;
    Activity context;

    public request_society_adapter(FragmentActivity activity, ArrayList<request_society_model> requestSocietyArray)
    {
        context=activity;
        reqarraylist=requestSocietyArray;
    }

    @NonNull
    @Override
    public reqviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_request_society,parent,false);
        reqviewholder requestadapter=new reqviewholder(view);
        return requestadapter;
    }

    @Override
    public void onBindViewHolder(@NonNull reqviewholder holder, final int position)
    {
        TextView uid=holder.uid;
        TextView udate=holder.udate;
        TextView reqmsg=holder.reqmsg;
        TextView response=holder.response;
        TextView status=holder.status;
        //Button details=holder.details;

        uid.setText(reqarraylist.get(position).getId());
        udate.setText(reqarraylist.get(position).getDate());
        reqmsg.setText("Request: "+reqarraylist.get(position).getMsg());
        response.setText("Response: "+reqarraylist.get(position).getRespmsg());
        //status.setText(reqarraylist.get(position).getStatus());
        String statusstr =  reqarraylist.get(position).getStatus();

        if(statusstr.equals("1"))
        {
            status.setText("Open");
        }else{
            status.setText("Close");
        }

      /*  details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context,detailed_requestlist.class);
                intent.putExtra("id",reqarraylist.get(position).getId());
                intent.putExtra("from_id",reqarraylist.get(position).getFromid());
                intent.putExtra("req_date",reqarraylist.get(position).getDate());
                intent.putExtra("req_type",reqarraylist.get(position).getType());
                intent.putExtra("user_msg",reqarraylist.get(position).getMsg());
                intent.putExtra("responce_msg",reqarraylist.get(position).getRespmsg());
                intent.putExtra("status",reqarraylist.get(position).getStatus());
                intent.putExtra("created_at",reqarraylist.get(position).getCreated_at());
                context.startActivity(intent);

            }
        });
        */
    }

    @Override
    public int getItemCount()
    {
        return reqarraylist.size();
    }

    public static class reqviewholder extends RecyclerView.ViewHolder
    {
        TextView uid,udate,reqmsg,response,status;
        Button details;
        public reqviewholder(@NonNull View itemView) {
            super(itemView);

            uid=itemView.findViewById(R.id.blank);
            udate=itemView.findViewById(R.id.blank2);
            reqmsg=itemView.findViewById(R.id.reqmsg);
            response=itemView.findViewById(R.id.responce);
            status=itemView.findViewById(R.id.status);
            details=itemView.findViewById(R.id.details);
        }
    }
}
