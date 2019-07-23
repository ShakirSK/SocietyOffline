package test.soc365.society365.user.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import dmax.dialog.SpotsDialog;

public class Notification extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    SpotsDialog dialog;
    private ArrayList<Notification_model> notificationarray;
    String userid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.notification_recycler, container,false);

        dialog=new SpotsDialog(getActivity(),R.style.Custom);
        recyclerView=view.findViewById(R.id.recycleview);
        /*
        notificationarray=new ArrayList<Notification_model>();
        layoutManager=new LinearLayoutManager(getContext());
        adapter=new Notification_Adapter(notificationarray,getContext());
        recyclerView.setAdapter(adapter);
        */
        sharedPreferences = getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );

        notificationarray=new ArrayList<Notification_model>();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new Notification_Adapter(notificationarray,getContext());
        recyclerView.setAdapter(adapter);

        notificationlist();

        return view;
    }

    private void notificationlist()
    {
        //String notificationurl= StaticUrl.notificationlist;
        String notificationurl= StaticUrl.usernotificationlist+"?reciever_id="+userid;
        Log.d("notification",notificationurl);

        if(Connectivity.isConnected(getContext()))
        {
        final StringRequest request=new StringRequest(Request.Method.GET,
                notificationurl,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.d("Responce:",response);
                dialog.dismiss();

                try
                {
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);

                    if(jsonObject.getString("status").equals("1"))
                    {
                        JSONArray jsonArray1=jsonObject.getJSONArray("notificatoinlist");
                        for(int i=0;i<jsonArray1.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                            Notification_model historyModel = new Notification_model();
                            historyModel.setId(jsonObject1.getString("id"));
                            historyModel.setSender_id(jsonObject1.getString("sender_id"));
                            historyModel.setReciever_id(jsonObject1.getString("reciever_id"));
                            historyModel.setTitle(jsonObject1.getString("request_title"));
                            historyModel.setType(jsonObject1.getString("notification_type"));
                            historyModel.setCreated_on(jsonObject1.getString("created_on"));
                            notificationarray.add(historyModel);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No Record found...",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
        else
    {
        Toast.makeText(getActivity(),"Check Internet connection",Toast.LENGTH_SHORT).show();
    }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Notifications");
    }

}
