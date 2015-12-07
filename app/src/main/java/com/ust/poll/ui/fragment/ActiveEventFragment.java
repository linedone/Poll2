package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ust.poll.MainActivity;
import com.ust.poll.ui.adaptor.EventAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class ActiveEventFragment extends MainActivity.PlaceholderFragment {
    private ProgressDialog progressDialog;
    final ArrayList<String> strEventIds = new ArrayList<String>();
    final ArrayList<String> strTitles = new ArrayList<String>();
    final ArrayList<String> strDates = new ArrayList<String>();
    final ArrayList<String> strTimes = new ArrayList<String>();
    final ArrayList<String> strVenues = new ArrayList<String>();
    final ArrayList<String> strRemarkURLs = new ArrayList<String>();
    //byte[] imageEvent;
    final ArrayList<String> strMembers = new ArrayList<String>();
    ListView eventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_event, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.UK);
        ParseUser user = ParseUser.getCurrentUser();
        String userObjectId = user.getObjectId();
        String username = user.getUsername();

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading record...", true);
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Event");
        parseQuery.whereContains("EventMembers", username);
        parseQuery.whereGreaterThanOrEqualTo("EventDate", dFormat.format(new Date()));
        parseQuery.orderByAscending("EventDate");

        /* The query first tries to load from the network,
         * but if that fails, it loads results from the cache. */
        parseQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    int counter = 0;
                    for (ParseObject parseObject : parseObjects) {
                        strEventIds.add(parseObject.getObjectId().toString());
                        strTitles.add(parseObject.get("EventTitle").toString());
                        strDates.add(parseObject.get("EventDate").toString());
                        strTimes.add(parseObject.get("EventTime").toString());
                        strVenues.add(parseObject.get("EventVenue").toString());
                        strRemarkURLs.add(parseObject.get("EventRemarkURL").toString());
//                        File f = parseObject.get("EventPhoto").toString();
                        strMembers.add(parseObject.get("EventMembers").toString());
                        Log.d("Retrieve Database", parseObject.get("EventTitle").toString() + " || " + parseObject.get("EventDate").toString() + " || " + parseObject.get("EventTime").toString() + " || " + parseObject.get("EventVenue").toString() + " || " + parseObject.get("EventRemarkURL").toString());
                        counter++;
                    }
                    Log.d("Database", "Retrieved " + parseObjects.size() + " Event");
                    
                    if (strEventIds != null) {  // Construct a ListView
                        eventList = (ListView) getActivity().findViewById(R.id.activeEventListView);
                        EventAdapter mAdapter = new EventAdapter(getActivity(), strTitles, strDates, strTimes, strVenues, strRemarkURLs);
                        eventList.setAdapter(mAdapter);
                        progressDialog.dismiss();
                        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //        DetailEventFragment fragment = new DetailEventFragment();
                                //        Bundle bundle = new Bundle();
                                //        bundle.putString("objectID", idList.get(itemPosition));
                                //        fragment.setArguments(bundle);
                                //        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                            }
                        });
                    } else {
                        System.out.println("No Event");
                        Log.e("ERROR", "Event ID is equal to null!!!");
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
                    Log.e("Database", "Error: " + e.getMessage());
                }
            }
        });
    }
}

