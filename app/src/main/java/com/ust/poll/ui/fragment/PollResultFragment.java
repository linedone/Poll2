// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ust.poll.MainActivity;
import com.linedone.poll.R;
import com.ust.poll.model.NewsItem;
import com.ust.poll.model.Poll;
import com.ust.poll.model.Polled;
import com.ust.poll.model.Result;
import com.ust.poll.ui.adaptor.CustomListAdapter;
import com.ust.poll.util.TelephonyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PollResultFragment extends MainActivity.PlaceholderFragment {

    private ProgressDialog progressDialog;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a Z");  // 2015-12-13T11:31:00.000Z

    @Nullable
    @Bind(R.id.result_custom_list) ListView lv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_result, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ArrayList<String> list = new ArrayList<String>();
        final Context ctx = this.getActivity();
        //DialogHelper.fnShowDialog(this.getContext());

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);

        // Expired Date
        ParseQuery expiredQuery = new ParseQuery(Poll.TABLE_NAME);
        expiredQuery.whereLessThan(Poll.END_AT, sdf.format(new Date()));  // Deadline_Date < Today(), "END_AT"<TODAY

        // Voted Result, but non-Expired
        ParseQuery votedQuery = new ParseQuery(Poll.TABLE_NAME);
        votedQuery.whereContainedIn(Poll.FRIEND_PHONE, Arrays.asList("null"));  // All members voted, "FRIEND_PHONE"==null

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(expiredQuery);
        queries.add(votedQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    retrievePollResultSuccess(parseObjects, e);
                }
                else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void retrievePollResultSuccess(List<ParseObject> parseObjects, ParseException e) {
        if (e==null) {
            int counter = 0;

            ArrayList<String> idList = new ArrayList<String>();
            ArrayList<NewsItem> results = new ArrayList<NewsItem>();
            for (ParseObject parseObject : parseObjects) {
                //cache
                parseObject.pinInBackground();

                final String id = parseObject.getObjectId();
                idList.add(id);
                final String t = parseObject.get(Poll.TITLE).toString();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
                final String dt = sdf.format((Date) parseObject.get(Poll.END_AT));

                final String op = parseObject.get(Poll.OPTIONS).toString();
                final String cph = parseObject.get(Poll.CREATORPHONE).toString();

                NewsItem newsData = new NewsItem();
                newsData.setHeadline("" + t);
                newsData.setReporterName("" + TelephonyUtil.getContactName(getActivity(), cph));
                newsData.setDate("" + dt);
                newsData.setpollID(id);
                //Log.d("active", "" + t);

                String allOption = "";
                ArrayList sortedResult = new ArrayList();

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Polled.TABLE_NAME);
                parseQuery.whereEqualTo(Polled.POLLID, id);

                try {
                    List<ParseObject> pObject = parseQuery.find();

                    for (ParseObject p : pObject) {
                        allOption += p.get(Polled.OPTION) + ",";
                    }
                    String[] optArray = allOption.split(",");

                    sortedResult = displayDuplicate(optArray);

                    allOption = "";
                    for(int i = 0; i < sortedResult.size(); i++) {
                        allOption += "\n" + sortedResult.get(i).toString();
                    }
                    //Log.d("result------", "" + allOpt);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    progressDialog.dismiss();
                }

                newsData.setallOpt(""+allOption);
                Log.d("result------", "" + allOption);
                results.add(newsData);
            }

            lv1.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), results));
            Log.d("Database", "Retrieved " + parseObjects.size() + " Active");
            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
    }

    public ArrayList displayDuplicate(String[] ar) {
        //String resultStr = "";

        ArrayList<Result> resultList = new ArrayList<Result>();

        boolean[] done = new boolean[ar.length];
        for(int i=0; i<ar.length; i++) {
            if(done[i])
                continue;
            int nb = 0;
            for(int j = i; j < ar.length; j++) {
                if(done[j])
                    continue;
                if(ar[j].equals(ar[i])) {
                    done[j] = true;
                    nb++;
                }
            }
            resultList.add(new Result(nb, ar[i]));
        }

        Collections.sort(resultList);
        for(int i=0; i<resultList.size(); i++) {
            Log.d("result", "" + resultList.get(i).toString());
        }
        return resultList;
    }
}
