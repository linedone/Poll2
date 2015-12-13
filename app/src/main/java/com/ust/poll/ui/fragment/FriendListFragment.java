// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ust.poll.MainActivity;
import com.linedone.poll.R;
import com.ust.poll.ui.dialog.DialogHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ust.poll.util.TelephonyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendListFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.listView) ListView listView;
    ArrayList<String> contactList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_frd, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> parseObjects, ParseException e) {
                retrieveSuccess(parseObjects, e);
            }
        });
    }

    private void retrieveSuccess(List<ParseUser> parseObjects, ParseException e) {
        if (e == null) {
            for (ParseUser parseItem : parseObjects) {
                String contactName = TelephonyUtil.getContactName(getActivity(), parseItem.getUsername());
                if (contactName.compareTo("")!=0) {
                    contactList.add(contactName + "[" + parseItem.getUsername().toString() + "]");
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, contactList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }
        else {
            DialogHelper.getOkAlertDialog(getActivity(), "Error in connecting server..", e.getMessage()).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String contact = (String)this.listView.getItemAtPosition(position);
        Log.i("Contact", contact);
        String number = contact.substring(contact.indexOf("[") + 1, contact.indexOf("]"));

        try {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
