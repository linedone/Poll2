// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.ust.poll.model.NewsItem;
import com.ust.poll.model.Poll;
import com.ust.poll.ui.adaptor.CustomListAdapter;
import com.ust.poll.util.TelephonyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivePollFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener {
    private ProgressDialog progressDialog;
    @Nullable
    @Bind(R.id.custom_list) ListView lv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_active, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParseUser user = ParseUser.getCurrentUser();
        final String username = user.getUsername();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Poll.TABLE_NAME);
        parseQuery.whereContainedIn(Poll.FRIEND_PHONE, Arrays.asList(username));
        parseQuery.whereGreaterThan(Poll.END_AT, new Date());
        parseQuery.orderByAscending(Poll.END_AT);
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    retrieveActivePollSuccess(parseObjects, e);
                } else {
                    progressDialog.dismiss();
                }
            }
        });
        lv1.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        SelectPollFragment fragment = new SelectPollFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pollID", ((TextView) view.findViewById(R.id.pollid)).getText().toString());
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    public void retrieveActivePollSuccess(List<ParseObject> parseObjects, ParseException e) {
        if (e==null) {
            ArrayList<String> idList = new ArrayList<String>();
            final ArrayList<NewsItem> results = new ArrayList<NewsItem>();
            for (ParseObject parseObject : parseObjects) {
                final String id = parseObject.getObjectId();
                idList.add(id);
                final String t = parseObject.get(Poll.TITLE).toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
                final String dt = sdf.format((Date) parseObject.get(Poll.END_AT));
                final String cph = parseObject.get(Poll.CREATORPHONE).toString();

                NewsItem newsData = new NewsItem();
                newsData.setHeadline("" + t);
                newsData.setReporterName("" + TelephonyUtil.getContactName(getActivity(), cph));
                newsData.setDate("" + dt);
                newsData.setpollID(id);
                results.add(newsData);
            }

            lv1.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), results));
            Log.d("Database", "Retrieved " + parseObjects.size() + " Active");
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
        progressDialog.dismiss();
    }
}