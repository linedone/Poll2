package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;
import com.linedone.poll.R;
import com.ust.poll.activity.LoginActivity;
import com.ust.poll.model.NewsItem;
import com.ust.poll.model.Poll;
import com.ust.poll.model.Polled;
import com.ust.poll.model.Result;
import com.ust.poll.ui.adaptor.CustomListAdapter;
import com.ust.poll.ui.dialog.DialogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ken on 10/7/2015.
 */
public class PollResultFragment extends MainActivity.PlaceholderFragment {


    private ProgressDialog progressDialog;

    @Nullable
    @Bind(R.id.result_custom_list)
    ListView lv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_result, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_example) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ArrayList<String> list = new ArrayList<String>();
        final Context ctx = this.getActivity();
        //DialogHelper.fnShowDialog(this.getContext());



        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                //view.setSelected(true);
                // ListView Clicked item index
                int itemPosition = position;

                //SelectPollFragment fragment = new SelectPollFragment();
                //Bundle bundle = new Bundle();
                //bundle.putString("pollID", ((TextView) view.findViewById(R.id.pollid)).getText().toString());
                //fragment.setArguments(bundle);
                //getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();


            }
        });

        ParseQuery myQuery1 = new ParseQuery(Poll.TABLE_NAME);
        myQuery1.whereLessThan(Poll.END_AT, new Date());

        ParseQuery myQuery2 = new ParseQuery(Poll.TABLE_NAME);
        //myQuery2.whereEqualTo(Poll.FRIEND_PHONE, "[]");
        myQuery2.whereContainedIn(Poll.FRIEND_PHONE, Arrays.asList("null"));
        //myQuery2.whereEqualTo(Poll.FRIEND_PHONE, Arrays.asList(""));

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(myQuery1);
        queries.add(myQuery2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);


        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    retrieveEventSuccess(parseObjects, e);


                } else {
                    progressDialog.dismiss();
                }
            }
        });


    }



    public void retrieveEventSuccess(List<ParseObject> parseObjects, ParseException e) {

        if (e==null) {
            int counter = 0;

            ArrayList<String> idList = new ArrayList<String>();
            ArrayList<NewsItem> results = new ArrayList<NewsItem>();
            for (ParseObject parseObject : parseObjects) {

                ParseUser user = ParseUser.getCurrentUser();
                final String username = user.getUsername();
                final String userid = user.getObjectId();
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
                newsData.setReporterName("" + getContactName(cph));
                newsData.setDate("" + dt);
                newsData.setpollID(id);
                //Log.d("active", "" + t);


                String allOpt = "";
                ArrayList sortedResult = new ArrayList();
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Polled.TABLE_NAME);
                parseQuery.whereEqualTo(Polled.POLLID, id);

                try {
                    List<ParseObject> pObject = parseQuery.find();

                    for (ParseObject p : pObject) {
                        //allOpt += p.get(Polled.OPTION) + "," + p.get(Polled.USERID)+ ",";
                        allOpt += p.get(Polled.OPTION) + ",";
                    }
                    String[] optArray = allOpt.split(",");

                    sortedResult = displayDuplicate(optArray);

                    allOpt = "";
                    for(int i = 0; i < sortedResult.size(); i++) {
                        allOpt += "\n" + sortedResult.get(i).toString();
                    }
                    //Log.d("result------", "" + allOpt);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                    progressDialog.dismiss();
                }

                newsData.setallOpt(""+allOpt);
                Log.d("result------", "" + allOpt);
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


    public String getContactName(final String phoneNumber)
    {
        Uri uri;
        String[] projection;
        Uri mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
        projection = new String[] { android.provider.Contacts.People.NAME };
        try {
            Class<?> c =Class.forName("android.provider.ContactsContract$PhoneLookup");
            mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
            projection = new String[] { "display_name" };
        }
        catch (Exception e) {
        }


        uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber));
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        String contactName = "";

        if (cursor.moveToFirst())
        {
            contactName = cursor.getString(0);
        }

        cursor.close();
        cursor = null;

        return contactName;
    }


    public ArrayList displayDuplicate(String[] ar) {
        //String resultStr = "";

        ArrayList<Result> resultList = new ArrayList<Result>();

        boolean[] done = new boolean[ar.length];
        for(int i = 0; i < ar.length; i++) {
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
        for(int i = 0; i < resultList.size(); i++) {

            Log.d("result", "" + resultList.get(i).toString());
        }
        return resultList;
    }

}
