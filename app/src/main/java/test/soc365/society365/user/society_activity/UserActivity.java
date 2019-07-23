package test.soc365.society365.user.society_activity;

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

public class UserActivity extends Fragment
{
    ArrayList<Society_Activity_Model> societyarray;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    SpotsDialog dialog;

    String uid,sid,utype;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.user_activity,container,false);
        dialog=new SpotsDialog(getContext(),R.style.Custom);
        recyclerView=view.findViewById(R.id.recycleview);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        societyarray=new ArrayList<Society_Activity_Model>();
        adapter=new Society_Activity_Adpater(getActivity(),societyarray);
        recyclerView.setAdapter(adapter);
        Log.d("test","oncreate");

        Bundle arg=getArguments();
        //String uid,sid,utype;
        uid=arg.getString("userid");
        sid=arg.getString("societyid");
        utype=arg.getString("usertype");
        societyActivity();
        return view;
    }

    public void societyActivity()
    {
        String targeturl= StaticUrl.societyactivity+"?society_id="+sid;
        Log.d("targeturl:",targeturl);
        if(Connectivity.isConnected(getActivity()))
        {
            StringRequest request=new StringRequest(Request.Method.GET, targeturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    Log.d("Activityresponse",response);
                    dialog.dismiss();
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        if(jsonObject.getString("status").equals("1"))
                        {
                            JSONArray jsonArray1=jsonObject.getJSONArray("Activity_list");
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                                Society_Activity_Model model=new Society_Activity_Model();
                                model.setActid(jsonObject1.getString("activity_id"));
                                model.setActivity(jsonObject1.getString("activity"));
                                model.setCreatedat(jsonObject1.getString("created_at"));
                                model.setListmember(jsonObject1.getString("list_of_member_label"));
                                model.setMemberlabel(jsonObject1.getString("list_of_member"));
                                model.setPubdate(jsonObject1.getString("publish_date"));
                                societyarray.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                           // Toast.makeText(getActivity(),"No Records Found...",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(),"Check Internet Connection...",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Activities");
    }
}
