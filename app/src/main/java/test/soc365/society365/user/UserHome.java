package test.soc365.society365.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.soc365.society365.R;
import test.soc365.society365.maneger.PartyFragment;
import test.soc365.society365.user.audit_report.UserAuditReport;
import test.soc365.society365.user.billpayment.BillPayment;
import test.soc365.society365.user.bulletine_board.UserBoard;
import test.soc365.society365.user.list_request_society.Request_Society;
import test.soc365.society365.user.mom.UserMom;
import test.soc365.society365.user.society_activity.UserActivity;

public class UserHome extends Fragment
{

    CardView cardview, cardview2,cardview3,cardview4,cardview5,cardview6,cardviewpl;

    String useremailid,usermobile,usrid,utype,sid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.userhome, container, false);

        cardview=view.findViewById(R.id.cardview);
        cardview2=view.findViewById(R.id.cardview2);
        cardview3=view.findViewById(R.id.cardview3);
        cardview4=view.findViewById(R.id.cardview4);
        cardview5=view.findViewById(R.id.cardview5);
        cardview6=view.findViewById(R.id.cardview6);
        cardviewpl = view.findViewById(R.id.cardview7);


        Bundle arg=getArguments();
        usrid=arg.getString("userid");
        sid=arg.getString("societyid");
        utype=arg.getString("usertype");
        usermobile=arg.getString("usermobile");
        useremailid=arg.getString("useremailid");

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),UserPayBill.class);
                // startActivity(intent);
                BillPayment billPayment = new BillPayment();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", sid);
                args.putString("usertype", utype);
                args.putString("usermobile", usermobile);
                args.putString("useremailid", useremailid);
                billPayment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent,billPayment);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),RequestBox.class);
                //startActivity(intent);
                Request_Society request_society=new Request_Society();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", sid);
                args.putString("usertype", utype);
                request_society.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,request_society);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

            }
        });
        cardview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //Intent intent=new Intent(getActivity(),UserAuditReport.class);
                //startActivity(intent);
                UserAuditReport userAuditReport=new UserAuditReport();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", sid);
                args.putString("usertype", utype);
                userAuditReport.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,userAuditReport);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        cardview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),UserBoard.class);
                //startActivity(intent);
                UserBoard userBoard=new UserBoard();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", sid);
                args.putString("usertype", utype);
                userBoard.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,userBoard);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        cardview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),U_Ledger.class);
                //startActivity(intent);
                UserActivity activity=new UserActivity();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", sid);
                args.putString("usertype", utype);
                activity.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,activity);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        cardview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),UserMom.class);
                //startActivity(intent);
                UserMom userMom=new UserMom();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", sid);
                args.putString("usertype", utype);
                userMom.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,userMom);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        cardviewpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(getActivity(),UserMom.class);
                //startActivity(intent);
                PartyFragment userMom=new PartyFragment();
                Bundle args = new Bundle();
                args.putString("userid",usrid);
                args.putString("societyid", sid);
                args.putString("usertype", utype);
                userMom.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent
                        ,userMom);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }
}
