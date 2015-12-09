package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linedone.poll.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ust.poll.MainActivity;

import butterknife.ButterKnife;

public class DetailFriendListEventFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ProgressDialog progressDialog;
    String strMember;
    ListView firendList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail_friendlist, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        String eventObjectId = bundle.getString("objectId");
        Log.i("Event Object ID", eventObjectId);
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Event");
        parseQuery.getInBackground(eventObjectId, new GetCallback<ParseObject>() {
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    retrieveEventSuccess(parseObject, e);
                }
            }
        });
    }

    public void retrieveEventSuccess(ParseObject parseObject, ParseException e) {
        if (e==null) {
            ImageButton galleryButton = (ImageButton) getActivity().findViewById(R.id.galleryButton);
            TextView titleGroupMember = (TextView) getActivity().findViewById(R.id.txt_efl_title_group_member);
            TextView title = (TextView) getActivity().findViewById(R.id.txt_eflTitle);
            TextView date = (TextView) getActivity().findViewById(R.id.txt_eflDate);
            TextView time = (TextView) getActivity().findViewById(R.id.txt_eflTime);
            TextView venue = (TextView) getActivity().findViewById(R.id.txt_eflVenue);
            TextView remark = (TextView) getActivity().findViewById(R.id.txt_eflRemarkURL);

            // Image Button
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
            galleryButton.setImageBitmap(icon);
            galleryButton.setOnClickListener(this);

            titleGroupMember.setText("Group Members");
            title.setText("Title: " + parseObject.get("EventTitle").toString());
            date.setText("Date: " + parseObject.get("EventDate").toString());
            time.setText("Time: " + parseObject.get("EventTime").toString());
            venue.setText("Venue: " + parseObject.get("EventVenue").toString());
            remark.setText("Remark: " + parseObject.get("EventRemarkURL").toString());

            strMember = parseObject.get("EventMembers").toString();

            // Spilt strMember to an array and find the name by phone number
            String[] arrayMemberPhones = strMember.split(",");
            String[] arrayMemberNames = new String[arrayMemberPhones.length];
            for (int i=0; i<arrayMemberPhones.length; i++){
                arrayMemberNames[i] = getContactName(arrayMemberPhones[i]) + " [" + arrayMemberPhones[i] + "]";
            }

            if (strMember!=null) {  // Construct a ListView
                firendList = (ListView) getActivity().findViewById(R.id.eventfriendListView);
                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayMemberNames);
                firendList.setAdapter(mAdapter);
                firendList.setOnItemClickListener(this);
                progressDialog.dismiss();
            }
            else {
                Log.e("ERROR", "Group member list is equal to null!!!");
            }
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
    }

    public String getContactName(final String phoneNumber) {
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

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(0);
        }

        cursor.close();
        cursor = null;

        return contactName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String contact = (String)this.firendList.getItemAtPosition(position);
        Log.i("Contact", contact);
        String number = contact.substring(contact.indexOf("[") + 1, contact.indexOf("]"));

        try {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        //TODO: Goto Gallery and Upload
    }
}
