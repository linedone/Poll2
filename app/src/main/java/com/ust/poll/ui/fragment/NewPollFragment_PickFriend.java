package com.ust.poll.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;
import com.ust.poll.model.Poll;
import com.ust.poll.ui.dialog.DialogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ken on 10/7/2015.
 */
public class NewPollFragment_PickFriend extends MainActivity.PlaceholderFragment {
    @Nullable
    @Bind(R.id.txt_title)
    BootstrapEditText txt_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_new_pickfriend, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data



    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ArrayList contactName = PhoneName("name");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewPollFragment_PickFriend.super.getActivity(), android.R.layout.simple_list_item_multiple_choice, contactName);

        ListView friendList = (ListView)getView().findViewById(R.id.friendList);

        // Assign adapter to ListView

        friendList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friendList.setAdapter(adapter);


        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                //view.setSelected(true);
                // ListView Clicked item index
                int itemPosition = position;
                // ListView Clicked item value
                //String itemValue = (String) listView.getItemAtPosition(position);
                // Show Alert
                //Toast.makeText(NewPollFragment_PickFriend.super.getActivity(), "" + itemValue,
               //         Toast.LENGTH_LONG).show();


            }
        });


    }

public ArrayList PhoneName (String contactType){



    final ArrayList contactPhone =new ArrayList();
    final ArrayList contactName =new ArrayList();

    ContentResolver cr =  getActivity().getContentResolver();
    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null);
    if (cur.getCount() > 0) {
        while (cur.moveToNext()) {
            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

            if (Integer.parseInt(cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactName.add(name);
                Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                        new String[]{id}, null);
                while (pCur.moveToNext()) {
                    String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    contactPhone.add(phoneNo);
                    //String name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
                    //Toast.makeText(NewPollFragment_PickFriend.super.getActivity(), "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    //contactPhone.add(name);


                }
                pCur.close();
            }
        }
    }

    if(contactType.equals("name")){
        return contactName;
    }else{
        return contactPhone;
    }

}

    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if(ret==null)
            ret = "Unsaved";
        return ret;
    }


    @OnClick(R.id.btn_new_poll_pickfriend_next)
    public void fnNewPoll(View view) {

        ListView friendList = (ListView)getView().findViewById(R.id.friendList);
        int cntChoice = friendList.getCount();

        String checked = "";
        String unchecked = "";

        SparseBooleanArray sparseBooleanArray = friendList.getCheckedItemPositions();

        for(int i = 0; i < cntChoice; i++)
        {

            if(sparseBooleanArray.get(i) == true)
            {
                checked += friendList.getItemAtPosition(i).toString() + "\n";
                //checked += ""+ i + "\n";
            }
            else  if(sparseBooleanArray.get(i) == false)
            {
                //unchecked+= friendList.getItemAtPosition(i).toString() + "\n";
            }

        }

        Bundle bundle = this.getArguments();
        String title = bundle.getString("title");
        String option1 = bundle.getString("option1");
        String option2 = bundle.getString("option2");
        String option3 = bundle.getString("option3");
        String option4 = bundle.getString("option4");
        Integer day = bundle.getInt("day");
        Integer month = bundle.getInt("month");
        Integer year = bundle.getInt("year");
        Integer hour = bundle.getInt("hour");
        Integer minute = bundle.getInt("minute");

        ArrayList contactPhone = PhoneName("phone");

        String[] positionArray = checked.split("\\n");

        String phone_Friend = "";

        for (int i = 0; i < positionArray.length; i++){
            String tempPhoneno = getPhoneNumber(positionArray[i], NewPollFragment_PickFriend.super.getActivity());
            Toast.makeText(NewPollFragment_PickFriend.super.getActivity(), "" + tempPhoneno.replace("+852", ""), Toast.LENGTH_SHORT).show();
            phone_Friend += ""+ tempPhoneno.replace(" ", "") + ",";
            //Toast.makeText(NewPollFragment_PickFriend.super.getActivity(), "" + contactPhone.get(Integer.parseInt(positionArray[i])), Toast.LENGTH_SHORT).show();
        }

        //Log.d("test", "" + Integer.toString(day));
        Log.d("test", "" + phone_Friend);
        //contactPhone.get(position);


        final Context ctx = this.getContext();

        ParseObject pollObject = new ParseObject(Poll.TABLE_NAME);
        pollObject.put(Poll.TITLE, title);

        pollObject.addAllUnique(Poll.OPTIONS, Arrays.asList(option1,
                option2, option3, option4));

        String date = String.valueOf(day)+ " " +  String.valueOf(month) + " " +  String.valueOf(year) + " " + String.valueOf(hour) + ":" +  String.valueOf(minute);
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy hh:mm");
        try {
            Log.d("test", "" + date);
            Date deadline = format.parse(date);
            pollObject.put(Poll.END_AT,deadline);
            Log.d("test", "" + deadline);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }




        pollObject.put(Poll.FRIEND_PHONE, phone_Friend);

        pollObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                DialogHelper.fnCloseDialog();
                if (e == null) {
                    Toast.makeText(ctx,
                            "Poll Successfully created.",
                            Toast.LENGTH_LONG).show();
                } else {
                    DialogHelper.getOkAlertDialog(ctx,
                            "Error in connecting server..", e.getMessage())
                            .show();
                }
            }
        });
        fnSendPushNotification(phone_Friend);
    }


    //private void fnCreatePoll() {
    //}

    private void fnSendPushNotification(String phoneList) {


        String[] userArray = phoneList.split(",");

        for (int i = 0; i < userArray.length; i++){

        //ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        //installation.deleteEventually();
        // installation.saveEventually();
        //installation.put("username", userArray[i]);
        //installation.saveInBackground();

        ParsePush push = new ParsePush();
        ParseQuery query = ParseInstallation.getQuery();
        query.whereEqualTo("username", userArray[i]);
        push.setQuery(query);
        push.setMessage("New Poll");
        push.sendInBackground();

        }



        ActivePollFragment fragment = new ActivePollFragment();
        Bundle newbundle = new Bundle();
        fragment.setArguments(newbundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

        //installation.deleteEventually();

        //ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        //installation.put(Poll.USERNAME, "+85293483263");
        //installation.saveInBackground();

        //ParsePush push = new ParsePush();
        //ParseQuery query = ParseInstallation.getQuery();
        //query.whereEqualTo(Poll.USERNAME, "+85293483263");

        //push.setQuery(query);
        //push.setMessage("test");
       // push.sendInBackground();

        //Log.d("test", "start");
        //ParsePush parsePush = new ParsePush();
        //ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query
        //pQuery.whereEqualTo(Poll.USERNAME, "+85254990679");
        //parsePush.sendMessageInBackground("aaaaaaaaaaaaaaaaaa", pQuery);


        //ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        //installation.remove("+85254990678");
        //installation.deleteInBackground();
        //installation.saveInBackground();




        //query.whereEqualTo(Poll.USERNAME, "+85254990678");
        //push.sendMessageInBackground("aaaaaaaaaaaaaaaaaa", query);
        //Log.d("test", "end");


        //parsePush.setChannel(Util.PARSE_CHANNEL);
        //parsePush.setMessage("abc");
        //parsePush.setExpirationTime(1424841505);
        //parsePush.sendInBackground();
    }

    }
