package test.soc365.society365.user.society_activity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

/**
 * Created by PC1 on 28-07-2018.
 */

public class Society_Activity_Adpater extends RecyclerView.Adapter<Society_Activity_Adpater.ActivityViewHolder>
{
    ArrayList<Society_Activity_Model> activityarraylist;
    Activity context;

    public Society_Activity_Adpater(FragmentActivity activity, ArrayList<Society_Activity_Model> societyarray)
    {
        context=activity;
        activityarraylist=societyarray;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_user_society_activity,parent,false);
        ActivityViewHolder activityadapter=new ActivityViewHolder(view);
        return activityadapter;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position)
    {
        TextView activity=holder.activity;
        TextView pubdate=holder.pubdate;

        activity.setText(activityarraylist.get(position).getActivity());
        pubdate.setText(activityarraylist.get(position).getPubdate());

    }

    @Override
    public int getItemCount()
    {
        return activityarraylist.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder
    {
        TextView activity;
        TextView pubdate;
        public ActivityViewHolder(@NonNull View itemView)
        {
            super(itemView);

            activity=itemView.findViewById(R.id.requried);
            pubdate=itemView.findViewById(R.id.date);
        }
    }
}
