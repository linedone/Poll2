package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.util.Base64;
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

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ust.poll.MainActivity;
import com.ust.poll.activity.LoginActivity;
import com.ust.poll.model.NewPoll;
import com.ust.poll.model.NewsItem;
import com.ust.poll.model.Poll;
import com.ust.poll.model.Polled;
import com.ust.poll.ui.adaptor.CustomListAdapter;
import com.ust.poll.ui.adaptor.EventAdapter;
import com.ust.poll.ui.dialog.DialogHelper;
import com.ust.poll.util.MediaUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ken on 10/7/2015.
 */
public class ActivePollFragment extends MainActivity.PlaceholderFragment {

    private ProgressDialog progressDialog;

    @Nullable
    @Bind(R.id.custom_list)
    ListView lv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_active, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;


    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Log.d("active", "testing12345");
        //new getActlistTask().execute();


        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);


        ParseUser user = ParseUser.getCurrentUser();
        final String username = user.getUsername();
        final String userid = user.getObjectId();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Poll.TABLE_NAME);
        parseQuery.whereContainedIn(Poll.FRIEND_PHONE, Arrays.asList(username.replace("+852", "")));
        parseQuery.whereGreaterThan(Poll.END_AT, new Date());
        parseQuery.orderByAscending(Poll.END_AT);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    retrieveEventSuccess(parseObjects, e);
                }
                else{
                    progressDialog.dismiss();
                }
            }
        });




        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                //view.setSelected(true);
                // ListView Clicked item index
                int itemPosition = position;

                SelectPollFragment fragment = new SelectPollFragment();
                Bundle bundle = new Bundle();
                //bundle.putString("pollID", s.get(itemPosition));
                bundle.putString("pollID", ((TextView) view.findViewById(R.id.pollid)).getText().toString());
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();


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


                ParseQuery<ParseObject> polledquery = ParseQuery.getQuery(Polled.TABLE_NAME);
                polledquery.whereEqualTo(Polled.POLLID, id);
                polledquery.whereEqualTo(Polled.USERID, userid);

                try {
                    int test = polledquery.count();
                    if (!(test > 0)) {

                        //list2.add(t + "||" + dt + "||" + op);
                        //objectsWereRetrievedSuccessfully(objects);
                        NewsItem newsData = new NewsItem();
                        newsData.setHeadline("" + t);
                        newsData.setReporterName("" + getContactName(cph));
                        newsData.setDate("" + dt);
                        newsData.setpollID(id);
                        results.add(newsData);
                        Log.d("active", "" + t);
                    }

                } catch (ParseException e1) {
                    e1.printStackTrace();
                    progressDialog.dismiss();
                }


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



}
