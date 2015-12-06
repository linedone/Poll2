package com.ust.poll.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ust.poll.MainActivity;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class ActiveEventFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_event, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParseUser user = ParseUser.getCurrentUser();
        String objectId = user.getObjectId();
        String username = user.getUsername();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Event");
        parseQuery.whereContains("EventMembers", username.replace("+852", ""));
        parseQuery.whereGreaterThanOrEqualTo("EventDate", new Date());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    int counter = 0;
                    for (ParseObject parseObject : objects) {
                        String eventId = parseObject.getObjectId();
//    idList.add(eventId);
                        String strTitle = parseObject.get("EventTitle").toString();
                        String strDate = parseObject.get("EventDate").toString();
                        String strTime = parseObject.get("EventTime").toString();
                        String strVenue = parseObject.get("EventVenue").toString();
                        String strRemarkURL = parseObject.get("EventRemarkURL").toString();
//    File f = parseObject.get("EventPhoto").toString();
//    String strMembers = parseObject.get("EventMembers").toString();
//    list.add(strTitle+"||"+strDate+"||"+strTime+"||"+strVenue+"||"+strRemarkURL);
                        counter++;
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
                    Log.e("DBError", "" + e);
                }
            }
        });
        //eventRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemPosition = position;

//        DetailEventFragment fragment = new DetailEventFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("objectID", idList.get(itemPosition));
//        fragment.setArguments(bundle);
//        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }
}