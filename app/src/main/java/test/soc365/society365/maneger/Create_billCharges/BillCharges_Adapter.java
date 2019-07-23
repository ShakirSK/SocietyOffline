package test.soc365.society365.maneger.Create_billCharges;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import test.soc365.society365.R;
import test.soc365.society365.maneger.viewmember.member;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BillCharges_Adapter extends RecyclerView.Adapter<BillCharges_Adapter.billcharge> {
    private ArrayList<member>memberArrayList;
    private Context context;

    public BillCharges_Adapter(ArrayList<member> memberArrayList, Context context) {
        this.memberArrayList = memberArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public billcharge onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create_bill, parent,false);
        billcharge charge=new billcharge(view);
        return charge;
    }

    @Override
    public void onBindViewHolder(@NonNull billcharge holder, int position) {

        TextView first=holder.firstname;
        TextView contact=holder.contactno;
        TextView flatno=holder.flat;
        TextView buildingno=holder.buildingno;
        ImageView imgview = holder.userimg;

        first.setText(memberArrayList.get(position).getFirstname()+" "+memberArrayList.get(position).getLastname());
        contact.setText(memberArrayList.get(position).getPhonno());
        flatno.setText(memberArrayList.get(position).getFlatno());
        buildingno.setText(memberArrayList.get(position).getBuildingno());
        String imageurl =memberArrayList.get(position).getImage();

        Picasso.with(context)
                .load(imageurl)
                .placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
                .into(imgview);
    }

    @Override
    public int getItemCount() {
        return memberArrayList.size();
    }

    public class billcharge extends RecyclerView.ViewHolder{

        TextView firstname,lastname,contactno,flat,buildingno;
        ImageView userimg;

        public billcharge(@NonNull View itemView) {
            super(itemView);
            firstname=itemView.findViewById(R.id.firstname);
            lastname=itemView.findViewById(R.id.lastname);
            contactno=itemView.findViewById(R.id.number);
            flat=itemView.findViewById(R.id.flat);
            buildingno=itemView.findViewById(R.id.buildingno);
            userimg = itemView.findViewById(R.id.img);
        }
    }
}
