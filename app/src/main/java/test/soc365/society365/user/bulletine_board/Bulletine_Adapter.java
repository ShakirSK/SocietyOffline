package test.soc365.society365.user.bulletine_board;

import android.content.Context;
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

public class Bulletine_Adapter extends RecyclerView.Adapter<Bulletine_Adapter.Bulletineviewholder>
{
    ArrayList<Bulletine_Model> bulletinearraylist=new ArrayList<Bulletine_Model>();
    Context context;

    public Bulletine_Adapter(FragmentActivity activity, ArrayList<Bulletine_Model> bulletinearray)
    {
        context=activity;
        bulletinearraylist=bulletinearray;
    }

    @NonNull
    @Override
    public Bulletineviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_userboard,parent,false);
        Bulletineviewholder bulletineadapter=new Bulletineviewholder(view);
        return bulletineadapter;
    }

    @Override
    public void onBindViewHolder(@NonNull Bulletineviewholder holder, int position)
    {
        TextView activity=holder.activity;
        TextView pubdate=holder.pubdate;

        activity.setText(bulletinearraylist.get(position).getActivity());
        pubdate.setText(bulletinearraylist.get(position).getPubdate());
    }

    @Override
    public int getItemCount() {
        return bulletinearraylist.size();
    }

    public static class Bulletineviewholder extends RecyclerView.ViewHolder
    {
        TextView activity;
        TextView pubdate;
        public Bulletineviewholder(@NonNull View itemView)
        {
            super(itemView);
            activity=itemView.findViewById(R.id.requried);
            pubdate=itemView.findViewById(R.id.date);
        }
    }
}
