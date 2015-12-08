package com.ust.poll.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseException;
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
import com.ust.poll.ui.dialog.DialogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ken on 10/7/2015.
 */
public class ActivePollFragment extends MainActivity.PlaceholderFragment {

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
        new getActlistTask().execute();

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                //view.setSelected(true);
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                //String itemValue = (String) listView.getItemAtPosition(position);
                // Show Alert
                //Toast.makeText(ActivePollFragment.super.getActivity(), "" + idList.get(itemPosition),
                //         Toast.LENGTH_LONG).show();
                //idList.get(itemPosition);


                SelectPollFragment fragment = new SelectPollFragment();
                Bundle bundle = new Bundle();
                //bundle.putString("pollID", s.get(itemPosition));
                bundle.putString("pollID", ((TextView) view.findViewById(R.id.pollid)).getText().toString());
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();


            }
        });
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






    private ArrayList<NewsItem> getactPoll() {

        // query search with username subsets
        ArrayList<NewsItem> results = new ArrayList<NewsItem>();
        ParseUser user = ParseUser.getCurrentUser();
        final String username = user.getUsername();
        final String userid = user.getObjectId();
        final ArrayList<String> idList = new ArrayList<String>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Poll.TABLE_NAME);
        query.whereContains(Poll.FRIEND_PHONE, username.replace("+852", ""));
        query.whereGreaterThan(Poll.END_AT, new Date());



        List<ParseObject> pollObject;
        //String[] myFriends = new String[0];
        //Object[] myFriendsObjects = null;



        try {
            pollObject = query.find();
            for (ParseObject p : pollObject) {

                final String id = p.getObjectId();
                idList.add(id);
                final String t = p.get(Poll.TITLE).toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
                final String dt = sdf.format((Date) p.get(Poll.END_AT));
                final String op = p.get(Poll.OPTIONS).toString();
                final String cph = p.get(Poll.CREATORPHONE).toString();


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
                }

            }

            //myFriends = Arrays.copyOf(myFriendsObjects, myFriendsObjects.length, String[].class);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return results;
    }


    private class getActlistTask extends AsyncTask<ArrayList<NewsItem>, Integer, ArrayList<NewsItem>> {
        protected ArrayList<NewsItem> doInBackground(ArrayList<NewsItem>... activeList) {
            //int count = urls.length;
            //long totalSize = 0;
            //for (int i = 0; i < count; i++) {
                //totalSize += Downloader.downloadFile(urls[i]);
                //publishProgress((int) ((i / (float) count) * 100));
            //}
            ArrayList<NewsItem> poll_details = getactPoll();
            Log.d("active", "testing123");
            return poll_details;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(ArrayList<NewsItem> result) {
            //showDialog("Downloaded " + result + " bytes");

            lv1.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), result));
        }


    }


/*



    private ArrayList<NewsItem> getListData() {
        final ArrayList<NewsItem> results = new ArrayList<NewsItem>();



        final Context ctx = this.getActivity();
        DialogHelper.fnShowDialog(this.getContext());

        ParseUser user = ParseUser.getCurrentUser();
        final String username = user.getUsername();
        final String userid = user.getObjectId();
        final ArrayList<String> list2 = new ArrayList<String>();
        final ArrayList<String> idList = new ArrayList<String>();


        ParseQuery<ParseObject> query = ParseQuery.getQuery(Poll.TABLE_NAME);
        //Log.d("active", "" + username.replace("+852", ""));
        query.whereContains(Poll.FRIEND_PHONE, username.replace("+852", ""));
        query.whereGreaterThan(Poll.END_AT, new Date());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                NewsItem newsData = new NewsItem();
                DialogHelper.fnCloseDialog();
                if (e == null) {
                    int counter = 0;

                    for (ParseObject p : objects) {
                        final String id = p.getObjectId();
                        idList.add(id);
                        final String t = p.get(Poll.TITLE).toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
                        final String dt = sdf.format((Date) p.get(Poll.END_AT));
                        final String op = p.get(Poll.OPTIONS).toString();


                        ParseQuery<ParseObject> polledquery = ParseQuery.getQuery(Polled.TABLE_NAME);
                        polledquery.whereEqualTo(Polled.POLLID, id);
                        polledquery.whereEqualTo(Polled.USERID, userid);

                        try {
                            int test = polledquery.count();
                            if (!(test > 0)) {

                                //list2.add(t + "||" + dt + "||" + op);
                                //objectsWereRetrievedSuccessfully(objects);
                                newsData = new NewsItem();
                                newsData.setHeadline("" + t);
                                newsData.setReporterName("Ken");
                                newsData.setDate("" + dt);
                                results.add(newsData);

                                Log.d("active", "" + t);
                            }

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                        //Log.d("active", "" + list2.size());
                        //NewPoll pollList = new NewPoll();
                        //list2.addAll(pollList.getactiveList());
                        counter++;
                    }

                    //Log.d("active", "" + list2.size());
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1, android.R.id.text1, list2);
                    //listView.setAdapter(adapter);



/*
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                            //view.setSelected(true);
                            // ListView Clicked item index
                            int itemPosition = position;
                            // ListView Clicked item value
                            //String itemValue = (String) listView.getItemAtPosition(position);
                            // Show Alert
                            //Toast.makeText(ActivePollFragment.super.getActivity(), "" + idList.get(itemPosition),
                            //         Toast.LENGTH_LONG).show();
                            //idList.get(itemPosition);


                            SelectPollFragment fragment = new SelectPollFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("pollID", idList.get(itemPosition));
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();



                        }
                    });

} else {
        DialogHelper.getOkAlertDialog(ctx,
        "Error in connecting server..", e.getMessage())
        .show();
        }



        }


        });





        return results;
        }

 */



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
