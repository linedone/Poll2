package com.ust.poll.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ust.poll.activity.LoginActivity;
import com.ust.poll.MainActivity;
import com.linedone.poll.R;
import com.ust.poll.ui.dialog.DialogHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ken on 10/7/2015.
 */
public class FriendListFragment extends MainActivity.PlaceholderFragment {
    @Bind(R.id.listView)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_frd, container, false);
        ButterKnife.bind(this,rootView);

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

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //query.whereEqualTo("gender", "female");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (ParseUser p : objects) {
                        list.add(p.getUsername());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1, android.R.id.text1, list);
                    listView.setAdapter(adapter);
                }
                else {
                    DialogHelper.getOkAlertDialog(getActivity(),
                            "Error in connecting server..", e.getMessage())
                            .show();
                }
            }
        });
    }
}
