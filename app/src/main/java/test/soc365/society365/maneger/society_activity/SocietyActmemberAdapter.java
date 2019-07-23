package test.soc365.society365.maneger.society_activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import test.soc365.society365.R;

import java.util.ArrayList;

import static test.soc365.society365.maneger.society_activity.SocietyActivityFragment.userarrayList;
import static test.soc365.society365.maneger.society_activity.SocietyActivityFragment.usernames;


public class SocietyActmemberAdapter extends RecyclerView.Adapter<SocietyActmemberAdapter.MyViewHolder> {

    Conversion conversion;
    private ArrayList<users_model> memberArrayList;
    private ArrayList<users_model> membernames;
    private Context context;

    ArrayList<String> checked=new ArrayList<String>();
    ArrayList<String> unchecked=new ArrayList<String>();
    StringBuilder sb;

    public SocietyActmemberAdapter (Context context, ArrayList<users_model> memberArrayList,Conversion conversion,ArrayList<users_model> membernames)
    {
        this.memberArrayList = memberArrayList;
        this.membernames=membernames;
        this.context = context;
        this.conversion=conversion;
    }

    // String[] Array=new String[2];

    //ArrayList<String> arraystring=new ArrayList<String>();
    //String userid=null;



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dailog_select_member,parent,false );
        SocietyActmemberAdapter.MyViewHolder MyViewHolder=new SocietyActmemberAdapter.MyViewHolder(view);
        return MyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        TextView Smembername=holder.membername;
        TextView Slastname=holder.lastname;
        final TextView Sselect=holder.select;
        final CheckBox Scheckbox=holder.checkBox;
        final ArrayList<String> id=new ArrayList<String>();

        Smembername.setText(memberArrayList.get(position).getFirstname());
        // Sselect.setText(memberArrayList.get(position).getNo_of_members());

        Slastname.setText(memberArrayList.get(position).getLastname());
       /* Scheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memberid=(memberArrayList.get(position).getMemberid());
               // memberidstr[position]=memberid;
                Iterator<String> itr = arraystr.iterator();
                Iterator<String> itrator=arraystring.iterator();
                if (Scheckbox.isChecked())
                {
                    arraystr.add(memberid);
                   String str= arraystr.toString();

                   Log.d("str",str);

                   // userid=userid+" "+memberid+",";
                   // Sselect.setText(userid);

                    //Log.d("result",Array[position]);
                    Log.d("memberid",memberid+arraystr.toString());

                    Log.d("membername",memberid+arraystring.toString());

                }else //if(!Scheckbox.isChecked())
                {
                    arraystr.remove(arraystr.size()-1);
                    Log.d("memberid",memberid+arraystr.toString());
                    Log.d("membername",memberid+arraystring.toString());
                }

            }
        });*/

       final users_model model=memberArrayList.get(position);
       //final users_model model1=membernames.get(position);
       holder.setItemClickListener(new ItemClickListener() {
           @Override
           public void onItemClick(View v, int pos)
           {
               String userid = memberArrayList.get(position).getMemberid();
               //String username=membernames.get(position).getFirstname()+" "+membernames.get(position).getLastname();
               Log.d("Userid", userid);

               model.setIsselected(!model.isIsselected());
               if (model.isIsselected())
               {
                   model.setIsselected(!model.isIsselected());
                   userarrayList.add(model.getMemberid());
                   model.setIndex(pos);
                   Log.d("indexadd", String.valueOf(pos));
                   usernames.add(model.getFirstname() + " " + model.getLastname());
                   //Scheckbox.setChecked(true);
               } else
                   {
                   model.setIsselected(model.isIsselected());
                   model.setRemoveindex(pos);
                   Log.d("indexremove", String.valueOf(pos));
                   userarrayList.remove(position);
                   usernames.remove(position);
                   //Scheckbox.setChecked(false);
               }
           }
               //userarrayList.clear();
       });
      /* for(int i=0;i<memberArrayList.size();i++)
       {
           if(checked.get(i).equals(unchecked.get(i)))
           {
               checked.remove(i);
               Log.d("Removed", checked.get(i));
           }
       }
       Log.d("arraylist_now", checked.toString());*/



    }


    @Override
    public int getItemCount() {
        return memberArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView membername,lastname,select;
        CheckBox checkBox;

        ItemClickListener itemClickListener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            membername=itemView.findViewById(R.id.mname);
            lastname=itemView.findViewById(R.id.lname);
            checkBox=itemView.findViewById(R.id.checkbox);
            select=itemView.findViewById(R.id.select);

            checkBox.setOnClickListener(this);
        }
        public  void setItemClickListener(ItemClickListener ick)
        {
            this.itemClickListener=ick;
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view,getLayoutPosition());

        }
    }
}
