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
import android.widget.ListView;

import com.linedone.poll.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ust.poll.MainActivity;
import com.ust.poll.activity.LoginActivity;
import com.ust.poll.model.Poll;
import com.ust.poll.ui.dialog.DialogHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ken on 10/7/2015.
 */
public class SelectPollFragment extends MainActivity.PlaceholderFragment {

    @Bind(R.id.listView)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_poll, container, false);
        ButterKnife.bind(this, rootView);
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
        DialogHelper.fnShowDialog(this.getContext());


        ParseQuery<ParseObject> query = ParseQuery.getQuery(Poll.TABLE_NAME);




    }
}
