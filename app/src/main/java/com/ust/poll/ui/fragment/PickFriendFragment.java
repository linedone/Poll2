// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.TextView;

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
    ArrayList contactPerson = new ArrayList();
    @Bind(R.id.btn_friend_list_submit) BootstrapButton btnSubmitFriendList;
    @Bind(R.id.txt_option1) TextView txtOption1;
    @Bind(R.id.txt_option2) TextView txtOption2;
    @Bind(R.id.txt_option3) TextView txtOption3;
    @Bind(R.id.txt_option4) TextView txtOption4;



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






        ArrayList<PhoneContactInfo> arrContacts = getContactsFromDeviceContactList();
        Iterator iterator = arrContacts.iterator();

        // Traversing elements of ArrayList object
        while (iterator.hasNext()){
            PhoneContactInfo contact = (PhoneContactInfo)iterator.next();
            if (!contactName.contains(contact.getContactName())){
                contactName.add(contact.getContactName());
            }
            if (!contactNumber.contains(contact.getContactNumber())) {
                contactNumber.add(contact.getContactNumber());
            }
            if (!contactPerson.contains(contact.getContactName() + " [" + contact.getContactNumber() + "]")) {
                contactPerson.add(contact.getContactName() + " [" + contact.getContactNumber() + "]");
            }
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(PickFriendFragment.super.getActivity(), android.R.layout.simple_list_item_multiple_choice, contactPerson);
        ListView friendList = (ListView)getView().findViewById(R.id.friendList);

        // Assign adapter to ListView
        friendList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friendList.setAdapter(mAdapter);

        friendList.setOnItemClickListener(this);

        Bundle bundle = this.getArguments();
        String soption1 = bundle.getString("soption1");
        String soption2 = bundle.getString("soption2");
        String soption3 = bundle.getString("soption3");
        String soption4 = bundle.getString("soption4");



        txtOption1.setText(soption1);
        txtOption2.setText(soption2);
        txtOption3.setText(soption3);
        txtOption4.setText(soption4);
        if (bundle!=null && bundle.getString("members")!=null) {



            //Log.d("testing", ""+soption1);

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



    public ArrayList<PhoneContactInfo> getContactsFromDeviceContactList() {
        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<PhoneContactInfo>();
        PhoneContactInfo phoneContactInfo = null;

        Uri contentUri = Phone.CONTENT_URI;
        ContentResolver contentResolver = getActivity().getBaseContext().getContentResolver();
//        Cursor cursor = contentResolver.query(contentUri, new String[]{Phone.NUMBER, Phone.DISPLAY_NAME, Phone._ID}, Phone.TYPE + "=" + Phone.TYPE_MOBILE, null, Phone.DISPLAY_NAME + " ASC");
        Cursor cursor = contentResolver.query(contentUri, new String[]{Phone.NUMBER, Phone.DISPLAY_NAME, Phone._ID}, null, null, Phone.DISPLAY_NAME + " ASC");
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
        String members = "";

        for (int i=0; i<cntChoice; i++) {
            if (sparseBooleanArray.get(i)==true) {
                String contact = friendList.getItemAtPosition(i).toString();
                contact = contact.substring(contact.indexOf("[") + 1, contact.indexOf("]"));
                members = members.concat(contact).concat(",");

                checked += contact + "\n";
                contactPosition = contactPosition.concat(String.valueOf(i));
                contactPosition = contactPosition.concat(",");
            }
        }

        //Bundle bundle = this.getArguments();


        Intent intent = new Intent();
        intent.putExtra("members", members);
        intent.putExtra("soption1", txtOption1.getText().toString());
        Log.d("wahahaha", "" + txtOption1.getText().toString());

        intent.putExtra("soption2", txtOption2.getText().toString());
        intent.putExtra("soption3", txtOption3.getText().toString());
        intent.putExtra("soption4", txtOption4.getText().toString());
        intent.putExtra("contactPosition", contactPosition);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getFragmentManager().popBackStack();
    }



}