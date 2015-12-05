package com.ust.poll.ui.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
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

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.ButterKnife;

public class PickFriendFragment extends MainActivity.PlaceholderFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private static int FRAGMENT_CODE = 0;
    @Bind(R.id.btn_friend_list_submit) BootstrapButton btnSubmitFriendList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pickfriend, container, false);
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

        ArrayList contactName = contactList("name");
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(PickFriendFragment.super.getActivity(), android.R.layout.simple_list_item_multiple_choice, contactName);
        ListView friendList = (ListView)getView().findViewById(R.id.friendList);

        // Assign adapter to ListView
        friendList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        friendList.setAdapter(mAdapter);

        friendList.setOnItemClickListener(this);

        // Prepare the loader. Either re-connect with an existing one, or start a new one.
        getLoaderManager().initLoader(0, null, (LoaderManager.LoaderCallbacks<Cursor>) this);
    }

    public ArrayList contactList (String contactType) {
        final ArrayList contactNo = new ArrayList();
        final ArrayList contactName = new ArrayList();

        ContentResolver contentResolver =  getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contactName.add(name);
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactNo.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }

        if (contactType.equals("name")) {
            return contactName;
        }
        else {
            return contactNo;
        }
    }

    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if (ret==null) {
            ret = "Unsaved";
        }
        return ret;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @OnClick(R.id.btn_friend_list_submit)
    public void fnPickFriendSubmit(View view) {
        ListView friendList = (ListView)getView().findViewById(R.id.friendList);
        int cntChoice = friendList.getCount();
        String checked = "";
        SparseBooleanArray sparseBooleanArray = friendList.getCheckedItemPositions();
        for (int i=0; i<cntChoice; i++) {
            if (sparseBooleanArray.get(i)==true) {
                checked += friendList.getItemAtPosition(i).toString()+"\n";
            }
        }

        ArrayList contactNo = contactList("phone");
        String[] positionArray = checked.split("\\n");
        String eventMembers = "";
        for (int i=0; i<positionArray.length; i++){
            String tmpContactNo = getPhoneNumber(positionArray[i],PickFriendFragment.super.getActivity());
            tmpContactNo = tmpContactNo.replace(" ", "");  // remove spaces
            tmpContactNo = tmpContactNo.replace("+852", "");  // remove +852
            eventMembers = eventMembers.concat(tmpContactNo);
            if (i!=(positionArray.length-1)) {
                eventMembers = eventMembers.concat(",");
            }
        }
        Log.i("Event Members", eventMembers);

        Intent intent = new Intent();
        intent.putExtra("eventMembers", eventMembers);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getFragmentManager().popBackStack();
    }
}