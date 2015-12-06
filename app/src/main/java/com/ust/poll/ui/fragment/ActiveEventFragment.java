package com.ust.poll.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

    String[] strTitle;
    String[] strDate;
    String[] strTime;
    String[] strVenue;
    String[] strRemarkURL;
    byte[] imageEvent;

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
        EventAdapter mAdapter = new EventAdapter(getActivity().getApplicationContext(), strTitle, strDate, strTime, strVenue, strRemarkURL, imageEvent);
        eventList.setAdapter(mAdapter);

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
//                        strTitle = parseObject.get("EventTitle").toString();
//                        strDate = parseObject.get("EventDate").toString();
//                        strTime = parseObject.get("EventTime").toString();
//                        strVenue = parseObject.get("EventVenue").toString();
//                        strRemarkURL = parseObject.get("EventRemarkURL").toString();
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

class EventAdapter extends ArrayAdapter<String>
{
    Context context;
    //byte[] images;
    String[] arrayTitle;
    String[] arrayDate;
    String[] arrayTime;
    String[] arrayVenue;
    String[] arrayRemarkURL;


    EventAdapter(Context context, String[] strTitle, String[] strDate, String[] strTime, String[] strVenue, String[] strRemarkURL, byte[] imageEvent) {
        super(context, R.layout.fragment_active_event_item, R.id.txt_aeTitle, strTitle); //, R.id.txt_aeDate, R.id.txt_aeTime, R.id.txt_aeVenue, R.id.txt_aeRemarkURL, R.id.img_eaPhoto);
        this.context = context;
        this.arrayTitle = strTitle;
        this.arrayDate = strDate;
        this.arrayTime = strTime;
        this.arrayVenue = strVenue;
        this.arrayRemarkURL = strRemarkURL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vRow = convertView;
        EventViewHolder holder = null;
        if (vRow==null) {  // Create at 1st time ONLY
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vRow = inflater.inflate(R.layout.fragment_active_event_item, parent, false);
            holder = new EventViewHolder(vRow);
            vRow.setTag(holder);
        }
        else {  // Recycling
            holder = (EventViewHolder) vRow.getTag();
            holder.txtTitle.setText(arrayTitle[position]);
            holder.txtDate.setText(arrayDate[position]);
            holder.txtTime.setText(arrayTime[position]);
            holder.txtVenue.setText(arrayVenue[position]);
            holder.txtRemarkURL.setText(arrayRemarkURL[position]);
            //holder.imgPhoto.setImageBitmap(images[position]);
        }

        return vRow;
    }
}

class EventViewHolder
{
    TextView txtTitle;
    TextView txtDate;
    TextView txtTime;
    TextView txtVenue;
    TextView txtRemarkURL;
    ImageView imagePhoto;

    EventViewHolder(View vRow)
    {
        txtTitle = (TextView) vRow.findViewById(R.id.txt_aeTitle);
        txtDate = (TextView) vRow.findViewById(R.id.txt_aeDate);
        txtTime = (TextView) vRow.findViewById(R.id.txt_aeTime);
        txtVenue = (TextView) vRow.findViewById(R.id.txt_aeVenue);
        txtRemarkURL = (TextView) vRow.findViewById(R.id.txt_aeRemarkURL);
        //ImageView imgPhoto = (ImageView) vRow.findViewById(R.id.img_eaPhoto);
    }
}