package test.soc365.society365.user.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.Notificationviewholder>
{

    ArrayList<Notification_model> notificationarray=new ArrayList<Notification_model>();
    Context context;

    public Notification_Adapter(ArrayList<Notification_model> notificationarray, Context context)
    {
        this.notificationarray = notificationarray;
        this.context = context;
    }

    @NonNull
    @Override
    public Notificationviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification,parent,false );
        Notificationviewholder notificationviewholder=new Notificationviewholder(view);
        return notificationviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Notificationviewholder holder, int position)
    {
        TextView title=holder.title;
        TextView date=holder.date;

        title.setText(notificationarray.get(position).getTitle());
        date.setText(notificationarray.get(position).getCreated_on());
    }

    @Override
    public int getItemCount()
    {
        return notificationarray.size();
    }

    public static class Notificationviewholder extends RecyclerView.ViewHolder
    {
        TextView title,date;
        public Notificationviewholder(@NonNull View itemView)
        {
            super(itemView);
            title=itemView.findViewById(R.id.notification);
            date=itemView.findViewById(R.id.date);
        }
    }

}
