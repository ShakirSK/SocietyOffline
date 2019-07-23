package test.soc365.society365.maneger;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import test.soc365.society365.R;
import test.soc365.society365.helper.DatabaseHelper;
import test.soc365.society365.maneger.partyledger.PartyAdapter;
import test.soc365.society365.maneger.partyledger.partymodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static test.soc365.society365.helper.DatabaseHelper.partyname2club;
import static test.soc365.society365.helper.DatabaseHelper.todos;

public class PartyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SearchView searchView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<partymodel> movieList;


    private PartyAdapter adapter;

    //get company id from sharedpreference
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String stallyid,usertype;
   static Calendar calendartp2;

    LinearLayout calander;
    AlertDialog.Builder builder;
    ArrayList<String> partynametype;
    JSONArray jsonArray1;
    // Database Helper
    DatabaseHelper db;

    public PartyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_party, container, false);


        calendartp2 = Calendar.getInstance();
        //setting calender to custom date
        calendartp2.set(2019, 31, 1);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);

        //calander to choose custom date
        builder = new AlertDialog.Builder(getActivity());
        db = new DatabaseHelper(getActivity());
        db.getdata(getContext());
        movieList = new ArrayList<>();


/*        adapter = new PartyAdapter(getContext(),todos);*/
        adapter = new PartyAdapter(getContext(),todos);

        linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);



        sharedPreferences = this.getActivity().getSharedPreferences("test.soc365.society365", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        stallyid = sharedPreferences.getString("stallyid","0" );

        usertype = sharedPreferences.getString("usertype","0" );





       // getData();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the fragment menu items.
        inflater.inflate(R.menu.fragment_menu_items, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

    }

    // This method will be invoked when user click the fragment menu item.
    // It also will be invoked when use click activity menu item.
    // So you need to write code to check which menu item trigger this click event.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_search)
        {
            return true;
        }
        else if (itemId == R.id.group)
        {
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_selectable_list_item);
            arrayAdapter.add("All Groups");
            arrayAdapter.addAll(partyname2club);
          /*  arrayAdapter.add("This Year");
            arrayAdapter.add("Last Year");
            arrayAdapter.add("Custom Date");*/

            builder.setTitle("Select Group")
                    .setCancelable(true);



            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  String strName = arrayAdapter.getItem(which);
                 /*     if(strName==partynametype.get(which)){

                    }
                    else if(strName=="This Year")
                    {

                    }
                    else */
                 todos.clear();

                 if(strName=="All Groups")
                    {
                        db.getdata(getContext());
                        adapter = new PartyAdapter(getContext(),todos);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                     db.getdata_wing(getContext(),strName);
                     adapter = new PartyAdapter(getContext(),todos);
                     recyclerView.setAdapter(adapter);
                 }
                    adapter.notifyDataSetChanged();
                 dialog.dismiss();
                }
            });
            builder.show();

        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = null;
        if(usertype.equals("1"))
        {
             url = "http://150.242.14.196:8012/society/service/partyledger_app.php?isparty="
                    +usertype+"&company_id="+stallyid;
        }
        else if(usertype.equals("2"))
        {
             url = "http://150.242.14.196:8012/society/service/partyledger_app.php?isparty="
                    +usertype+"&company_id="+stallyid;
        }

        Log.d("getData:partyash ",url);

        StringRequest jsonArrayRequest =
                new StringRequest
                        (url,
                                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse:partyledger ", String.valueOf(response));

                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);

                        if(jsonObject.getString("status").equals("1"))
                        {
                            JSONArray jsonArray1=jsonObject.getJSONArray("message");
                            //for all group saved

                            String jsonString = jsonArray1.toString();
                            Log.d("partyledger4 ", jsonString);

                             editor.putString("jsonStringforallgroup", jsonString);
                            editor.commit();
                            editor.apply();




                            Log.d("partyledger2 ", String.valueOf(jsonArray1));

                            movieList.clear();
                           partynametype =  new ArrayList<>();

                            for (int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                Log.d("partyledger3 ", String.valueOf(jsonObject1));

                                partymodel movie = new partymodel();
                                movie.setTitle(jsonObject1.getString("ledger_name"));
                                movie.setRating(jsonObject1.getString("flat_no"));
                                movie.setledgerid(jsonObject1.getString("id"));
                                   /*  for all group*/
                                partynametype.add(jsonObject1.getString("parent_name_2"));

                                movieList.add(movie);

                            }
                          /*  for all group*/
                            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(partynametype);
                            partynametype.clear();
                            partynametype.addAll(primesWithoutDuplicates);
                            Collections.sort(partynametype, String.CASE_INSENSITIVE_ORDER);
                            Log.d("partynametype",String.valueOf(partynametype));


                        }else {
                            // reportarray.clear();
                            // Toast.makeText(getApplicationContext(),"No Records" , Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();



                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }





}
