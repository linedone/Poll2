package com.ust.poll.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.linedone.poll.R;
import com.ust.poll.MainActivity;
import com.ust.poll.model.PhoneContactInfo;
import com.ust.poll.util.TelephonyUtil;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.ButterKnife;

public class PickFriendFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener {
    private static int FRAGMENT_CODE = 0;
    ArrayList contactName = new ArrayList();
    ArrayList contactNumber = new ArrayList();
    @Bind(R.id.btn_friend_list_submit) BootstrapButton btnSubmitFriendList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_pickfriend, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemPosition = position;  // ListView Clicked item index
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<PhoneContactInfo> arrContacts = getContactsByPhoneNo();
        Iterator iterator = arrContacts.iterator();

        // Traversing elements of ArrayList object
        while (iterator.hasNext()){
            PhoneContactInfo contact = (PhoneContactInfo)iterator.next();
            if (!contactName.contains(contact.getContactName())){
                contactName.add(contact.getContactName());
            }
            if (!contactNumber.contains(contact.getContactNumber())){
                contactNumber.add(contact.getContactNumber());
            }
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(PickFriendFragment.super.getActivity(), android.R.layout.simple_list_item_multiple_choice, contactName);
        ListView friendList = (ListView)getView().findViewById(R.id.friendList);

        // Assign adapter to ListView
        friendList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friendList.setAdapter(mAdapter);

        friendList.setOnItemClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle!=null && bundle.getString("eventMembers")!=null) {
            String[] contactPosition = bundle.getString("contactPosition").split(",");
            for (int i=0; i<friendList.getCount(); i++) {
                for (int j=0; j<contactPosition.length; j++) {
                    if (String.valueOf(i).compareTo(contactPosition[j])==0) {
                        friendList.setItemChecked(i, true);
                    }
                }
            }
        }
    }

    public ArrayList<PhoneContactInfo> getContactsByPhoneNo() {
        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<PhoneContactInfo>();
        PhoneContactInfo phoneContactInfo = null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getActivity().getBaseContext().getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        String zipCode = TelephonyUtil.GetCountryZipCode(getContext());

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

            contactNumber = contactNumber.replace(" ", "");  // remove spaces
            contactNumber = contactNumber.replace("-", "");  // remove hyphen

            String finalContactNumber;
            if (!contactNumber.contains("+")) {
                finalContactNumber = zipCode+contactNumber;
            }
            else {
                finalContactNumber = contactNumber;
            }

            phoneContactInfo = new PhoneContactInfo();
            phoneContactInfo.setPhoneContactID(phoneContactID);
            phoneContactInfo.setContactName(contactName);
            phoneContactInfo.setContactNumber(finalContactNumber);
            if (phoneContactInfo != null) {
                arrContacts.add(phoneContactInfo);
            }
            phoneContactInfo = null;
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        return arrContacts;
    }

    @OnClick(R.id.btn_friend_list_submit)
    public void fnPickFriendSubmit(View view) {
        ListView friendList = (ListView)getView().findViewById(R.id.friendList);
        int cntChoice = friendList.getCount();
        String checked = "";
        SparseBooleanArray sparseBooleanArray = friendList.getCheckedItemPositions();
        String contactPosition = "";

        for (int i=0; i<cntChoice; i++) {
            if (sparseBooleanArray.get(i)==true) {
                checked += friendList.getItemAtPosition(i).toString()+"\n";
                contactPosition = contactPosition.concat(String.valueOf(i));
                contactPosition = contactPosition.concat(",");
            }
        }

        String[] positionArray = checked.split("\\n");
        String eventMembers = "";
        String zipCode = TelephonyUtil.GetCountryZipCode(getContext());

        for (int i=0; i<positionArray.length; i++){
            String tmpContactNo = TelephonyUtil.getPhoneNumber(positionArray[i], PickFriendFragment.super.getActivity());
            tmpContactNo = tmpContactNo.replace(" ", "");  // remove spaces
            tmpContactNo = tmpContactNo.replace("-", "");  // remove hyphen
            StringBuilder number = new StringBuilder();
            if (!tmpContactNo.contains("+")) {
                number.append(zipCode);
            }
            number.append(tmpContactNo);

            eventMembers = eventMembers.concat(number.toString());

            if (i!=(positionArray.length-1)) {
                eventMembers = eventMembers.concat(",");
            }
        }
        Intent intent = new Intent();
        intent.putExtra("eventMembers", eventMembers);
        intent.putExtra("contactPosition", contactPosition);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getFragmentManager().popBackStack();
    }
}