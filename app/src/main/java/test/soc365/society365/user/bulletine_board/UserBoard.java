package test.soc365.society365.user.bulletine_board;

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

public class UserBoard extends Fragment
{
    ArrayList<Bulletine_Model> bulletinearray=new ArrayList<Bulletine_Model>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    SpotsDialog dialog;
    String targeturl;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String uid,sid,utype;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.userboard,container,false);

        recyclerView=view.findViewById(R.id.recycleview);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new Bulletine_Adapter(getActivity(),bulletinearray);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        dialog=new SpotsDialog(getActivity(),R.style.Custom);

        uid = sharedPreferences.getString("USERID","0" );
        sid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );

        /*Bundle arg=getArguments();
        //String uid,sid,utype;
        uid=arg.getString("userid");
        sid=arg.getString("societyid");
        utype=arg.getString("usertype");*/

        bulletineBoard();
        return view;
    }

    public void bulletineBoard()
    {
        targeturl= StaticUrl.userbulletineboard+"?society_id="+sid;

        Log.d("bulletineurl",targeturl);

        if(Connectivity.isConnected(getActivity()))
        {
            final StringRequest request=new StringRequest(Request.Method.GET, targeturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    dialog.dismiss();
                    //JSONArray jsonArray= null;
                    try
                    {
                        Log.d("bulletineresponse",response);
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        if(jsonObject.getString("status").equals("1"))
                        {
                            JSONArray jsonArray1=jsonObject.getJSONArray("bulletin list");
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                Bulletine_Model bmodel=new Bulletine_Model();
                                bmodel.setBid(jsonObject1.getString("bulletin_id"));
                                bmodel.setActivity(jsonObject1.getString("activity"));
                                bmodel.setCreatedate(jsonObject1.getString("created_at"));
                                bmodel.setPubdate(jsonObject1.getString("publish_date"));
                                bmodel.setIsdelete(jsonObject1.getString("is_delete"));
                                bulletinearray.add(bmodel);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            //Toast.makeText(getActivity(),"No records found...",Toast.LENGTH_SHORT).show();
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
        getActivity().setTitle("Bulletin Board");
    }
}

