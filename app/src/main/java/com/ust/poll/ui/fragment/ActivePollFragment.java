// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.ust.poll.activity.LoginActivity;
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

        //getActivity().setTheme(R.style.AppTheme2);
        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //new getActlistTask().execute();
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);

        ParseUser user = ParseUser.getCurrentUser();
        final String username = user.getUsername();
        final String userid = user.getObjectId();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Poll.TABLE_NAME);
        parseQuery.whereContainedIn(Poll.FRIEND_PHONE, Arrays.asList(username));
        Log.d("active+++", username);
        //parseQuery.whereContainedIn(Poll.FRIEND_ID, Arrays.asList(userid));
        parseQuery.whereGreaterThan(Poll.END_AT, new Date());
        parseQuery.orderByAscending(Poll.END_AT);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    retrieveEventSuccess(parseObjects, e);
                } else {
                    progressDialog.dismiss();
                }
            }
        });
        lv1.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        int itemPosition = position;

        SelectPollFragment fragment = new SelectPollFragment();
        Bundle bundle = new Bundle();
        //bundle.putString("pollID", s.get(itemPosition));
        bundle.putString("pollID", ((TextView) view.findViewById(R.id.pollid)).getText().toString());
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    public void retrieveEventSuccess(List<ParseObject> parseObjects, ParseException e) {
        if (e==null) {
            int counter = 0;
            ArrayList<String> idList = new ArrayList<String>();
            final ArrayList<NewsItem> results = new ArrayList<NewsItem>();
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
                newsData.setReporterName("" + TelephonyUtil.getContactName(getActivity(), cph));
                newsData.setDate("" + dt);
                newsData.setpollID(id);
                results.add(newsData);
//                Log.d("active------", "" + t);
/*
                ParseQuery<ParseObject> polledquery = ParseQuery.getQuery(Polled.TABLE_NAME);
                polledquery.whereEqualTo(Polled.POLLID, id);
                polledquery.whereEqualTo(Polled.USERID, userid);

                polledquery.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {

                            NewsItem newsData = new NewsItem();
                            newsData.setHeadline("" + t);
                            newsData.setReporterName("" + getContactName(cph));
                            newsData.setDate("" + dt);
                            newsData.setpollID(id);
                            results.add(newsData);
                            Log.d("active------", "" + t);

                            //retrieveEventSuccess(parseObjects, e);
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                });
/*
                /*
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
                */

            }

            lv1.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), results));
            Log.d("Database", "Retrieved " + parseObjects.size() + " Active");

            FragmentTransaction transaction;
            transaction =  getFragmentManager().beginTransaction();
            hideFragments(transaction);
            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_example) {
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            startActivityForResult(intent, 1);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    public String getContactName(final String phoneNumber)
//    {
//        Uri uri;
//        String[] projection;
//        Uri mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
//        projection = new String[] { android.provider.Contacts.People.NAME };
//        try {
//            Class<?> c =Class.forName("android.provider.ContactsContract$PhoneLookup");
//            mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
//            projection = new String[] { "display_name" };
//        }
//        catch (Exception e) {
//        }
//
//        uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber));
//        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
//
//        String contactName = "";
//
//        if (cursor.moveToFirst())
//        {
//            contactName = cursor.getString(0);
//        }
//
//        cursor.close();
//        cursor = null;
//
//        return contactName;
//    }

    private void hideFragments(FragmentTransaction transaction) {
        ActiveEventFragment activeEventFragment = (ActiveEventFragment)getFragmentManager().findFragmentByTag("ActiveEventFragment");
        ActivePollFragment activePollFragment = (ActivePollFragment)getFragmentManager().findFragmentByTag("ActivePollFragment");
        DetailFriendListEventFragment detailFriendListEventFragment = (DetailFriendListEventFragment)getFragmentManager().findFragmentByTag("DetailFriendListEventFragment");
        DetailGalleryEventFragment detailGalleryEventFragment = (DetailGalleryEventFragment)getFragmentManager().findFragmentByTag("DetailGalleryEventFragment");
        FriendListFragment friendListFragment = (FriendListFragment)getFragmentManager().findFragmentByTag("FriendListFragment");
        NewEventFragment newEventFragment = (NewEventFragment)getFragmentManager().findFragmentByTag("NewEventFragment");
        NewPollFragment newPollFragment = (NewPollFragment)getFragmentManager().findFragmentByTag("NewPollFragment");
        PickFriendFragment pickFriendFragment = (PickFriendFragment)getFragmentManager().findFragmentByTag("PickFriendFragment");
        PollResultFragment pollResultFragment = (PollResultFragment)getFragmentManager().findFragmentByTag("PollResultFragment");
        SelectPollFragment selectPollFragment = (SelectPollFragment)getFragmentManager().findFragmentByTag("SelectPollFragment");

        if ( activeEventFragment!= null) {
            transaction.hide(activeEventFragment);
        }
        if ( activePollFragment!= null) {
            transaction.hide(activePollFragment);
        }
        if ( detailFriendListEventFragment!= null) {
            transaction.hide(detailFriendListEventFragment);
        }
        if ( detailGalleryEventFragment!= null) {
            transaction.hide(detailGalleryEventFragment);
        }
        if ( friendListFragment!= null) {
            transaction.hide(friendListFragment);
        }
        if ( newEventFragment!= null) {
            transaction.hide(newEventFragment);
        }
        if (newPollFragment != null) {
            transaction.hide(newPollFragment);
        }
        if (pickFriendFragment != null) {
            transaction.hide(pickFriendFragment);
        }
        if (pollResultFragment != null) {
            transaction.hide(pollResultFragment);
        }
        if (selectPollFragment != null) {
            transaction.hide(selectPollFragment);
        }
    }
}
