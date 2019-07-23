package test.soc365.society365.maneger.viewmember;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.SingleTonRequest.MySingleton;
import test.soc365.society365.maneger.addmember.AddMember;
import test.soc365.society365.staticurl.StaticUrl;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

    private ArrayList<member>memberArrayList;
    private Activity context;
    ImageView mdelete;

    public MemberAdapter(Activity context,ArrayList<member>memberArrayList){
        this.memberArrayList = memberArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_memberlist, parent, false);
        MemberAdapter.MyViewHolder myViewHolder = new MemberAdapter.MyViewHolder(view);
        return myViewHolder ;
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position){

        TextView mfirstname=holder.firstname;
        TextView mlastname=holder.lastname;
        TextView mphoneno=holder.phoneno;
        TextView mflatno=holder.flatno;
        ImageView muserimg=holder.userimg;
        ImageView medit=holder.edit;
        final ImageView mdelete=holder.delete;
        //ImageView mphon=holder.phon;
        ImageView mprofile=holder.profile;

        mfirstname.setText(memberArrayList.get(position).getFirstname());
        mlastname.setText(memberArrayList.get(position).getLastname());
        mphoneno.setText(memberArrayList.get(position).getPhonno());

        mflatno.setText("Building No: "+memberArrayList.get(position).getBuildingno()+" Flat No: "+ memberArrayList.get(position).getFlatno());

        String memberid=memberArrayList.get(position).getMemberid();

        String imageurl =memberArrayList.get(position).getImage();
        Log.d("user_img", imageurl);
        Picasso.with(context)
                .load(imageurl)
                .placeholder(R.drawable.ic_person_black_24dp)
                .error(R.drawable.ic_person_black_24dp)
                .into(muserimg);

       /* mprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MemberDetailsFragment.class);
                context.startActivity(intent);
            }
        });*/

        muserimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MemberDetailsFragment.class);

                intent.putExtra("fullname", memberArrayList.get(position).getFirstname()+" "+memberArrayList.get(position).getLastname());
                intent.putExtra("phonenumber", memberArrayList.get(position).getPhonno());
                intent.putExtra("emailid", memberArrayList.get(position).getEmailid());
                intent.putExtra("buldingid",memberArrayList.get(position).getBuildingno());
                intent.putExtra("flatno", memberArrayList.get(position).getFlatno());
                intent.putExtra("memberjoindate", memberArrayList.get(position).getJoindate());
                intent.putExtra("profilepic", memberArrayList.get(position).getImage());
                //intent.putExtra("memberebddate",memberArrayList.get(position).)
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

       // final String userid=memberArrayList.get(position).getUsertype();
        final String userid=memberArrayList.get(position).getMemberid();

        mdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 popup(userid,position);

               //mdelete(userid);
            }
        });

        medit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // AddMember addMember=new AddMember();
                Intent intent = new Intent(context,AddMember.class);
                intent.putExtra("editstatus", "2");
                intent.putExtra("firstname",memberArrayList.get(position).getFirstname());
                intent.putExtra("lastname",memberArrayList.get(position).getLastname());
                intent.putExtra("mobileno",memberArrayList.get(position).getPhonno());
                intent.putExtra("emailid",memberArrayList.get(position).getEmailid());
                intent.putExtra("buildingno",memberArrayList.get(position).getBuildingno());
                intent.putExtra("flatno",memberArrayList.get(position).getFlatno());
                Log.d("flatnoconfirm", memberArrayList.get(position).getFlatno());
                intent.putExtra("memberstartdate", memberArrayList.get(position).getJoindate());
                intent.putExtra("status", memberArrayList.get(position).getStatus());
                intent.putExtra("memberuserid",memberArrayList.get(position).getMemberid() );
                intent.setFlags(intent.getFlags()|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                //args.putString("userid", userid);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return memberArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView firstname,lastname,phoneno,flatno;
        ImageView userimg,edit,delete,profile;
        //ImageView phno;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            firstname=itemView.findViewById(R.id.firstname);
            lastname=itemView.findViewById(R.id.lastname);
            phoneno=itemView.findViewById(R.id.number);
            flatno=itemView.findViewById(R.id.flat);
            userimg=itemView.findViewById(R.id.img);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
           // phon=itemView.findViewById(R.id.phn);
           // profile=itemView.findViewById(R.id.img);

        }
    }

    private void mdelete(String userid,final int position)
    {
        String mdeletstr= StaticUrl.changestatus + "?user_id="+userid;

        Log.d("mdelete", mdeletstr);

        if (Connectivity.isConnected(context))
        {
            final StringRequest stringRequest=new StringRequest(Request.Method.GET,
                    mdeletstr,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                           try {
                               JSONArray jsonArray = new JSONArray(response);
                               JSONObject jsonObject =jsonArray.getJSONObject(0);
                               if (jsonObject.getString("status").equals("1"))
                               {

                                   memberArrayList.remove(position);
                                   notifyItemRemoved(position);
                                   notifyItemRangeChanged(position,memberArrayList.size());


                               /* Log.d("msg",  jsonObject.getString("message"));

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
                                       // Toast.makeText(g(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                   alert.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int which) {

                                           // String user = etUsername.getText().toString();
                                           // String pass = etEmail.getText().toString();
                                          Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();

                                       }
                                   });
                                   AlertDialog dialog = alert.create();
                                   dialog.show();*/

                                   Toast.makeText(context,"Member Deleted Successfully", Toast.LENGTH_LONG).show();

                               }else {

                                   Log.d("message",  jsonObject.getString("message"));
                               }

                        }catch (JSONException e) {
                               e.printStackTrace();
                           }
                           }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            );
            MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }else {
            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void popup(final String userid,final int position)
    {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailog_delete);

        Button dialogButton = dialog.findViewById(R.id.nodelete);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialogYes = dialog.findViewById(R.id.deleteyes);
        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mdelete(userid,position);
            }
        });
        dialog.show();

    }
}
