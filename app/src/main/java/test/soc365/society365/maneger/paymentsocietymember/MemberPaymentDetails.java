package test.soc365.society365.maneger.paymentsocietymember;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import test.soc365.society365.InernetConnection.Connectivity;
import test.soc365.society365.R;
import test.soc365.society365.staticurl.StaticUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MemberPaymentDetails extends DialogFragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner,spinnerbank;
    String item;
    Button submit;
    EditText caldate;
    EditText membername, amount,paidbill, depositedate,depositeamount,refno;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String pid,unm,status,bill,uid,pmode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.activity_member_payment_details, container,false);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().setTitle("Process Society Accounting");

        spinner=view.findViewById(R.id.payment);
        spinnerbank=view.findViewById(R.id.bank);
        caldate=view.findViewById(R.id.date);
        membername=view.findViewById(R.id.lucky);
        amount=view.findViewById(R.id.amount);
        paidbill=view.findViewById(R.id.amount2);
        depositeamount=view.findViewById(R.id.amount4);
        depositedate=view.findViewById(R.id.amount3);
        refno=view.findViewById(R.id.rno);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        depositedate.setInputType(InputType.TYPE_NULL);
        depositedate.setFocusable(false);
        depositedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        Bundle bundle=getArguments();
        pid=bundle.getString("paymentid");
        unm=bundle.getString("username");
        bill=bundle.getString("billamount");
        status=bundle.getString("status");

        sharedPreferences=getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        uid=sharedPreferences.getString("USERID","0");

        membername.setText(unm);
        amount.setText(bill);

        spinnerbank.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Select Bank");
        categories.add("HDFC");
        categories.add("SBI");
        categories.add("BOI");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerbank.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(this);

        List<String> categories2 = new ArrayList<String>();
        categories2.add("Select Payment Mode");
        categories2.add("Online");
        categories2.add("Cheque");
        categories2.add("Cash");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter2);

        if(spinner.getSelectedItem().toString().equals("Online"))
        {
            pmode="1";
        }else if(spinner.getSelectedItem().toString().equals("Cheque"))
        {
            pmode="2";
        }else if(spinner.getSelectedItem().toString().equals("Cash"))
        {
            pmode="0";
        }

        submit=view.findViewById(R.id.submitbtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("name", "ganesh");

                validation();


                /*LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dailog_member_payment_details, null);


                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
               /* alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });**/
                /*alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        // String user = etUsername.getText().toString();
                        // String pass = etEmail.getText().toString();
                        //Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();

                    }

                });
                AlertDialog dialog = alert.create();
                dialog.show();*/

            }
        });

        return view;
    }

    public void setDate()
    {
        //showDialog(999);
        //onCreateDialog();
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), myDateListener, year, month, day);
    }

    // @Override
    protected Dialog onCreateDialog(int id)
    {
        // TODO Auto-generated method stub
        if (id == 999)
        {
            return new DatePickerDialog(getActivity(), myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3)
        {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day)
    {
        depositedate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item =adapterView.getItemAtPosition(i).toString();
        Log.d("select", item);
        Toast.makeText(adapterView.getContext(), "Selected:" + item, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void validation() {
        if (membername.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter Name", Toast.LENGTH_SHORT).show();
        } else if (amount.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Select Bill Amount", Toast.LENGTH_SHORT).show();
        } else if (paidbill.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter Paid Amount", Toast.LENGTH_SHORT).show();
        } else if (depositedate.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter Deposite Date", Toast.LENGTH_SHORT).show();
        } else if (depositeamount.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter Deposite Amount", Toast.LENGTH_SHORT).show();
        } else {
            paymentdetails();

        }
    }


    public void paymentdetails()
    {
        String paymentdetail= null;
            paymentdetail = StaticUrl.memberpaymentdetail+
                                  "?user_id="+uid+
                                  "&bill_amount="+bill+
                                  "&paid_amount=" + Html.fromHtml(paidbill.getText().toString()).toString()+
                                  "&bank_account=" +Html.fromHtml(spinnerbank.getSelectedItem().toString()).toString()+
                                  "&payment_mode=" +pmode+
                                  "&refrence_no=" +Html.fromHtml(refno.getText().toString()).toString()+
                                  "&payment_received_date=" +Html.fromHtml(caldate.getText().toString()).toString()+
                                  "&payment_deposit_amount=" +Html.fromHtml(depositeamount.getText().toString()).toString()+
                                  "&payment_deposit_date=" +Html.fromHtml(depositedate.getText().toString()).toString()+
                                  "&status="+status;

        Log.d("memberpayment",paymentdetail);

       if (Connectivity.isConnected(getContext()))
       {
           StringRequest request=new StringRequest(Request.Method.GET, paymentdetail, new Response.Listener<String>() {
               @Override
               public void onResponse(String response)
               {
                   try
                   {
                       JSONArray jsonArray=new JSONArray(response);
                       JSONObject jsonObject=jsonArray.getJSONObject(0);

                       if(jsonObject.getString("status").equals("1"))
                       {
                           Toast.makeText(getContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Toast.makeText(getContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
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
       }
       else
       {
           Toast.makeText(getContext(),"Check Internet Connection" , Toast.LENGTH_SHORT).show();
       }
    }

}


