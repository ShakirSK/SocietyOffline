package test.soc365.society365.maneger.society_activity;

import android.app.Activity;
import android.app.Dialog;
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
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SocietyAdapter extends RecyclerView.Adapter<SocietyAdapter.MyViewHolder> {

    private ArrayList<societyactivitymodel> societyactivitymodelArrayList;
    public Activity context;
    ImageView Adelete;

    public SocietyAdapter(Activity context, ArrayList<societyactivitymodel> societyactivitymodelArrayList, Conversion conversion) {
        this.societyactivitymodelArrayList = societyactivitymodelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bulletin,parent,false );
        SocietyAdapter.MyViewHolder myViewHolder=new SocietyAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        TextView Amessage=holder.message;
        TextView Ashowdate=holder.showdate;
        final ImageView Adelete=holder.delete;

        final String activityid=societyactivitymodelArrayList.get(position).getActivity_id();
        Log.d("Activityid", activityid);

        Amessage.setText(societyactivitymodelArrayList.get(position).getActivity());
        Ashowdate.setText(societyactivitymodelArrayList.get(position).getPublish_date());

        Adelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Activityid", activityid);
                popup(activityid,position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return societyactivitymodelArrayList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView message,select_list_of_member,showdate;
        ImageView delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.msg);
            showdate=itemView.findViewById(R.id.date);
            delete=itemView.findViewById(R.id.delete);

        }
    }

    public void activitydelete(final String activityid,final int position)
    {
        String activitydeletestr= StaticUrl.activitydelete+ "?activity_id="+activityid;

        Log.d("Adelete", activitydeletestr);


        if (Connectivity.isConnected(context))
        {

            final StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    activitydeletestr,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject =jsonArray.getJSONObject(0);
                                if (jsonObject.getString("status").equals("1"))
                                {
                                    Toast.makeText(context,"Activity Deleted Successfully",Toast.LENGTH_SHORT).show();
                                    Log.d("msg",  jsonObject.getString("message"));
                                    societyactivitymodelArrayList.remove(position);

                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,societyactivitymodelArrayList.size());


                                  /*  LayoutInflater inflater =LayoutInflater.from(context);
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
                                        //Toast.makeText(getActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show();
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
                                    dialog.show();*/

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
            });
            MySingleton.getInstance(context).addToRequestQueue(stringRequest);

        }else {
            Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void popup(final String activityids, final int position)
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
                activitydelete(activityids,position);
            }
        });
        dialog.show();

    }


}
