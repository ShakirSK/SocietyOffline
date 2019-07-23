package test.soc365.society365.maneger.bulletin;

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

public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.MyViewHolder> {

    private ArrayList<bulletinemodel> bulletinemodelArrayList;
    private Activity context;
    ImageView bdelete;
    TextView bmessage,editdate;


    public BulletinAdapter(Activity context, ArrayList<bulletinemodel> bulletinemodelArrayList){
        this.bulletinemodelArrayList=bulletinemodelArrayList;
        this.context=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_bulletin,parent,false );
        BulletinAdapter.MyViewHolder myViewHolder=new BulletinAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        bmessage=holder.message;
       // TextView bupdateddate=holder.updateddate;
        editdate=holder.editdate;
        final ImageView bdelete=holder.delete;

        bmessage.setText(bulletinemodelArrayList.get(position).getActivity());
       // bupdateddate.setText(bulletinemodelArrayList.get(position).getPublish_date());
        editdate.setText(bulletinemodelArrayList.get(position).getPublish_date());

        final String bulletinid=bulletinemodelArrayList.get(position).getBulletin_id();

        bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popup(bulletinid,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bulletinemodelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
         {
             TextView message, updateddate;
             TextView editdate;
             ImageView delete;

             public MyViewHolder(@NonNull View itemView) {
                 super(itemView);

                 message=itemView.findViewById(R.id.msg);
                 //updateddate=itemView.findViewById(R.id.date);
                 delete=itemView.findViewById(R.id.delete);
                 editdate=itemView.findViewById(R.id.date);
             }
         }

    public void bulletindelete(String bulletinid, final int position)
    {
        String bulletinedelete= StaticUrl.bulletindelete + "?bulletin_id="+bulletinid;

        Log.d("bdelete", bulletinedelete);

        if (Connectivity.isConnected(context))
        {
            final StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    bulletinedelete,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                                Log.d("response", response);
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject =jsonArray.getJSONObject(0);
                                    if (jsonObject.getString("status").equals("1"))
                                    {
                                       Log.d("delete,mesg", jsonObject.getString("message"));

                                        Log.d("msg",  jsonObject.getString("message"));
                                        Log.d("position", String.valueOf(position));
                                        Log.d("arrlistsizetest",""+bulletinemodelArrayList.size());

                                        bulletinemodelArrayList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,bulletinemodelArrayList.size());
                                        //int size = bulletinemodelArrayList.size();
                                       // bulletinemodelArrayList.clear();
                                        //notifyItemRangeRemoved(position, size);

                                        //notifyItemRemoved(position);
                                        Log.d("arrlistsize",""+bulletinemodelArrayList.size());
                                        //notifyItemRangeChanged(position, bulletinemodelArrayList.size());
                                        editdate.setText("");
                                        bmessage.setText("");
                                        //notifyDataSetChanged();
                                        //  popup();
                                       /* LayoutInflater inflater =LayoutInflater.from(context);
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

    private void popup(final String bulletinid, final int position)
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
                bulletindelete(bulletinid,position);
            }
        });
        dialog.show();

    }

}

