package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ust.poll.MainActivity;
import com.ust.poll.ui.adaptor.EventAdapter;
import com.ust.poll.util.MediaUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class ActiveEventFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener {
    private ProgressDialog progressDialog;
    final ArrayList<String> strEventIds = new ArrayList<String>();
    final ArrayList<String> strTitles = new ArrayList<String>();
    final ArrayList<String> strDates = new ArrayList<String>();
    final ArrayList<String> strTimes = new ArrayList<String>();
    final ArrayList<String> strVenues = new ArrayList<String>();
    final ArrayList<String> strRemarkURLs = new ArrayList<String>();
    final ArrayList<String> strMembers = new ArrayList<String>();
    final ArrayList<String> strImages = new ArrayList<String>();
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
        //String userObjectId = user.getObjectId();
        String userPhoneNumber = user.getUsername();

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Event");
        parseQuery.whereContains("EventMembers", userPhoneNumber);
        parseQuery.whereGreaterThanOrEqualTo("EventDate", dFormat.format(new Date()));
        parseQuery.orderByAscending("EventDate");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    retrieveEventSuccess(parseObjects, e);
                }
            }
        });
    }

    public void retrieveEventSuccess(List<ParseObject> parseObjects, ParseException e) {
        if (e==null) {
            int counter = 0;
            for (ParseObject parseObject : parseObjects) {
                strEventIds.add(parseObject.getObjectId().toString());
                strTitles.add(parseObject.get("EventTitle").toString());
                strDates.add(parseObject.get("EventDate").toString());
                strTimes.add(parseObject.get("EventTime").toString());
                strVenues.add(parseObject.get("EventVenue").toString());
                strRemarkURLs.add(parseObject.get("EventRemarkURL").toString());

                ParseFile fileObject = parseObjects.get(counter).getParseFile("EventPhoto");
                if(fileObject!=null){
                    try {
                        strImages.add(Base64.encodeToString(fileObject.getData(), Base64.DEFAULT));
                    }
                    catch (ParseException ePhotoMsg) {
                        Log.e("File", "Error: " + ePhotoMsg.getMessage());
                    }
                }
                else {  //NO Image uploaded
                    strImages.add(MediaUtil.getStringFromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.event, null)));
                }
                strMembers.add(parseObject.get("EventMembers").toString());
                Log.d("Retrieve Database", parseObject.get("EventTitle").toString() + " || " + parseObject.get("EventDate").toString() + " || " + parseObject.get("EventTime").toString() + " || " + parseObject.get("EventVenue").toString() + " || " + parseObject.get("EventRemarkURL").toString());
                counter++;
            }
            Log.d("Database", "Retrieved " + parseObjects.size() + " Event");

            if (strEventIds!=null) {  // Construct a ListView
                eventList = (ListView) getActivity().findViewById(R.id.activeEventListView);
                EventAdapter mAdapter = new EventAdapter(getActivity(), strTitles, strDates, strTimes, strVenues, strRemarkURLs, strImages);
                eventList.setAdapter(mAdapter);
                progressDialog.dismiss();
                eventList.setOnItemClickListener(this);
            }
            else {
                System.out.println("No Event");
                Log.e("ERROR", "Event ID is equal to null!!!");
            }
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        DetailEventFragment fragment = new DetailEventFragment();
        //        Bundle bundle = new Bundle();
        //        bundle.putString("objectID", idList.get(itemPosition));
        //        fragment.setArguments(bundle);
        //        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        Toast.makeText(getActivity().getApplicationContext(), "Item "+position+" clicked.", Toast.LENGTH_LONG).show();
    }
}