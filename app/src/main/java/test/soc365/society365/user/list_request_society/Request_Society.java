package test.soc365.society365.user.list_request_society;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
//import test.soc365.society365.maneger.FirstFragment;

public class Request_Society extends Fragment
{
    ArrayList<request_society_model> requestSocietyArray;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SpotsDialog dialog;
    String targeturl,Manager_id;
    FloatingActionButton addrequest;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

   // Button addrequest;
    EditText id,request;
    Button submit;
    // SpotsDialog dialog;
    String userid,societyid,usertype,usermesg,resmessage;

    View view2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.request_society,container,false);
        // view2=inflater.inflate(R.layout.activity_request_box,container,false)
        recyclerView=view.findViewById(R.id.recycleview);
        addrequest=view.findViewById(R.id.addrequest);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        requestSocietyArray=new ArrayList<request_society_model>();
        adapter=new request_society_adapter(getActivity(),requestSocietyArray);
        recyclerView.setAdapter(adapter);
        dialog=new SpotsDialog(getActivity(),R.style.Custom);

        sharedPreferences = getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        userid = sharedPreferences.getString("USERID","0" );
        societyid = sharedPreferences.getString("SOCIETYID","0" );
        usertype = sharedPreferences.getString("USERTYPE","0" );

        /*Bundle arg=getArguments();
        userid=arg.getString("userid");
        societyid=arg.getString("societyid");
        usertype=arg.getString("usertype");*/

        requestSociety();

        addrequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("in","1st onclick");

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.activity_request_box, null);
                request=alertLayout.findViewById(R.id.water);

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("");
                alert.setView(alertLayout);
                alert.setCancelable(true);

                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which)
                    {
                        // showing();
                        if (request.getText().toString().equals(""))
                        {
                            Toast.makeText(getActivity(),"Enter Request",Toast.LENGTH_SHORT).show();
                        }
                        else {
                                usermesg = Html.fromHtml(request.getText().toString()).toString();
                            addRequest(usermesg);
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }

        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView,
                new RecyclerTouchListener.ClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {
                        Intent intent = new Intent(getActivity(),detailed_requestlist.class);
                        intent.putExtra("id",requestSocietyArray.get(position).getId());
                        intent.putExtra("from_id",requestSocietyArray.get(position).getFromid());
                        intent.putExtra("req_date",requestSocietyArray.get(position).getDate());
                        intent.putExtra("req_type",requestSocietyArray.get(position).getType());
                        intent.putExtra("user_msg",requestSocietyArray.get(position).getMsg());
                        intent.putExtra("responce_msg",requestSocietyArray.get(position).getRespmsg());
                        intent.putExtra("status",requestSocietyArray.get(position).getStatus());
                        intent.putExtra("created_at",requestSocietyArray.get(position).getCreated_at());
                        startActivity(intent);
                    }
                    @Override
                    public void onLongClick(View view, int position)
                    {

                    }
                }));

        return view;
    }


    public void requestSociety()
    {
        requestSocietyArray.clear();

        targeturl= StaticUrl.societyrequestlist+"?request_from_id="+userid;
        Log.d("target",targeturl);
        dialog.show();

        if(Connectivity.isConnected(getActivity()))
        {
            StringRequest request=new StringRequest(Request.Method.GET, targeturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    Log.d("Responce:",response);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        if(jsonObject.getString("status").equals("1"))
                        {
                            JSONArray jsonArray1=jsonObject.getJSONArray("requestlist");
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                request_society_model reqmodel=new request_society_model();
                                reqmodel.setId(jsonObject1.getString("request_id"));
                                reqmodel.setMsg(jsonObject1.getString("user_message"));
                                reqmodel.setDate(jsonObject1.getString("request_date"));
                                reqmodel.setFromid(jsonObject1.getString("request_from_id"));
                                reqmodel.setRespmsg(jsonObject1.getString("response_message"));
                                reqmodel.setStatus(jsonObject1.getString("status"));
                                reqmodel.setManager_id(jsonObject1.getString("manager_id"));
                                Manager_id = jsonObject1.getString("manager_id");
                                Log.d("Manager_id",Manager_id);
                                reqmodel.setType(jsonObject1.getString("request_type"));
                                requestSocietyArray.add(reqmodel);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                           // Toast.makeText(getActivity(),"No Records found...",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } catch (JSONException e) {
                        dialog.dismiss();
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
            dialog.dismiss();
            Toast.makeText(getActivity(),"Check Internet Connection...",Toast.LENGTH_SHORT).show();
        }
    }

    private void addRequest(final String usermesg)
    {
        String targeturl=  StaticUrl.requestSociety;

        Log.d("requestSociety",targeturl);
        Log.d("requestSend",userid+usermesg+Manager_id+societyid);

        if(Connectivity.isConnected(getActivity()))
        {
            final StringRequest request = new StringRequest(Request.Method.POST,
                    targeturl,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            try {
                                Log.d("requestresponse",response);

                                JSONArray jsonArray = new JSONArray(response);

                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if(jsonObject.getString("status").equals("1"))
                                {
                                    popup();
                                    resmessage =jsonObject.getString("message");
                                    //Toast.makeText(getActivity(),resmessage,Toast.LENGTH_LONG).show();
                                    //dialog.dismiss();
                                }
                                else
                                {
                                    resmessage =jsonObject.getString("message");
                                    Toast.makeText(getActivity(), Request_Society.this.resmessage,Toast.LENGTH_SHORT).show();
                                    // dialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String,String> map= new HashMap<String,String>();
                        map.put("request_from_id",userid);
                        map.put("user_message",usermesg);
                       //map.put("request_type","1");
                       //map.put("manager_id",Manager_id);
                        map.put("society_id",societyid);
                        Log.d("request",userid+usermesg+societyid);
                    return map;
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }
        else
        {
            Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void popup()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_memberadd);

        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        // text.setText(msg);
        TextView mesgtxt = dialog.findViewById(R.id.skill);
        mesgtxt.setText("Your Request Sent Successfully");
        Button dialogButton = dialog.findViewById(R.id.donebtn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestSociety();
            }
        });
       /*
        Button dialogYes = dialog.findViewById(R.id.deleteyes);
        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
         */
        dialog.show();
    }

   private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
   {
       private GestureDetector gestureDetector;
       private Request_Society.RecyclerTouchListener.ClickListener clickListener;

       public RecyclerTouchListener(Context applicationContext, final RecyclerView recyclerView, final Request_Society.RecyclerTouchListener.ClickListener clickListener)
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Request");
    }
}