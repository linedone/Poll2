package com.ust.poll.ui.fragment;

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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ust.poll.MainActivity;
import com.ust.poll.ui.adaptor.EventAdapter;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class ActiveEventFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener {

    String[] strTitle = {"TEST1", "TEST2", "TEST3", "TEST4"};
    String[] strDate = {"TEST1", "TEST2", "TEST3", "TEST4"};;
    String[] strTime = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5", "TEST6", "TEST7", "TEST8", "TEST9", "TEST10", "TEST11", "TEST12", "TEST13", "TEST14", "TEST15"};;
    String[] strVenue = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5", "TEST6", "TEST7", "TEST8", "TEST9", "TEST10", "TEST11", "TEST12", "TEST13", "TEST14", "TEST15"};;
    String[] strRemarkURL = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5"};;
    //byte[] imageEvent;

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

        eventList = (ListView)getActivity().findViewById(R.id.activeEventListView);
        EventAdapter mAdapter = new EventAdapter(getActivity(), strTitle, strDate, strTime, strVenue, strRemarkURL);
        eventList.setAdapter(mAdapter);
        eventList.setOnItemClickListener(this);

//        ParseUser user = ParseUser.getCurrentUser();
//        String objectId = user.getObjectId();
//        String username = user.getUsername();
//
//        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Event");
//        parseQuery.whereContains("EventMembers", username.replace("+852", ""));
//        parseQuery.whereGreaterThanOrEqualTo("EventDate", new Date());
//        parseQuery.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null) {
//                    int counter = 0;
//                    for (ParseObject parseObject : objects) {
//                        String eventId = parseObject.getObjectId();
////    idList.add(eventId);
////                        strTitle = parseObject.get("EventTitle").toString();
////                        strDate = parseObject.get("EventDate").toString();
////                        strTime = parseObject.get("EventTime").toString();
////                        strVenue = parseObject.get("EventVenue").toString();
////                        strRemarkURL = parseObject.get("EventRemarkURL").toString();
////    File f = parseObject.get("EventPhoto").toString();
////    String strMembers = parseObject.get("EventMembers").toString();
////    list.add(strTitle+"||"+strDate+"||"+strTime+"||"+strVenue+"||"+strRemarkURL);
//                        counter++;
//                    }
//                } else {
//                    Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
//                    Log.e("DBError", "" + e);
//                }
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemPosition = position;
        Toast.makeText(getActivity().getApplicationContext(), "Item "+position+" selected.", Toast.LENGTH_LONG).show();

//        DetailEventFragment fragment = new DetailEventFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("objectID", idList.get(itemPosition));
//        fragment.setArguments(bundle);
//        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }
}

