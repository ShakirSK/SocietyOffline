package test.soc365.society365.user.audit_report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

/**
 * Created by PC1 on 28-07-2018.
 */

public class UserAuditReport extends Fragment
{
    ArrayList<auditModel> auditarray;
    View view;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    String targeturl;
    SpotsDialog spotsDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String uid,sid,utype;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        view= inflater.inflate(R.layout.userauditreport,container,false);

        spotsDialog=new SpotsDialog(getContext(), R.style.Custom);
        recyclerView=view.findViewById(R.id.recycleview);
        layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        auditarray=new ArrayList<auditModel>();
        adapter =new auditAdapter(getActivity(),auditarray);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        uid = sharedPreferences.getString("USERID","0" );
        sid = sharedPreferences.getString("SOCIETYID","0" );
        utype = sharedPreferences.getString("USERTYPE","0" );

        /*Bundle arg=getArguments();
        //String uid,sid,utype;
        uid=arg.getString("userid");
        sid=arg.getString("societyid");
        utype=arg.getString("usertype");*/
        //http://www.androidsnippets.com/open-any-type-of-file-with-default-intent.html
        recyclerView.addOnItemTouchListener(new UserAuditReport.RecyclerTouchListener(getActivity(), recyclerView,
                new UserAuditReport.RecyclerTouchListener.ClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {
                        String url = auditarray.get(position).getAttach();
                        Log.d("pdfurl", url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.parse(url),"application/pdf");
                        startActivity(i);

                    }
                    @Override
                    public void onLongClick(View view, int position)
                    {

                    }
                }));

        report();
       // return super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
    {

        private GestureDetector gestureDetector;
        private UserAuditReport.RecyclerTouchListener.ClickListener clickListener;

        public RecyclerTouchListener(Context applicationContext, final RecyclerView recyclerView, final UserAuditReport.RecyclerTouchListener.ClickListener clickListener)
        {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(applicationContext, new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e)
                {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null)
                    {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
        {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e)
        {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
        {
        }
        public interface ClickListener
        {
            void onClick(View view, int position);
            void onLongClick(View view, int position);
        }
    }


    public void report()
    {
        targeturl=StaticUrl.userauditreport+"?society_id="+sid;

        Log.d("Target",targeturl);

        if (Connectivity.isConnected(getActivity()))
        {
            StringRequest request = new StringRequest(Request.Method.GET, targeturl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Responce",response);
                            spotsDialog.dismiss();


                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (jsonObject.getString("status").equals("1"))
                                {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("auditList");
                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        auditModel model = new auditModel();
                                        model.setAuditid(jsonObject1.getString("audit_id"));
                                        model.setAudittitle(jsonObject1.getString("title"));
                                        Log.d("title",jsonObject1.getString("title"));
                                        model.setAuditdate(jsonObject1.getString("created_on"));
                                        model.setAttach(jsonObject1.getString("upload_url"));
                                        auditarray.add(model);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else
                                {
                                  //  Toast.makeText(getActivity(),"No Records Found...",Toast.LENGTH_SHORT).show();
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
        getActivity().setTitle("Audit Report");
    }
}



