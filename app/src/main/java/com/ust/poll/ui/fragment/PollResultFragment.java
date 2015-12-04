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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ust.poll.MainActivity;
import com.linedone.poll.R;
import com.ust.poll.activity.LoginActivity;
import com.ust.poll.model.Poll;
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
public class PollResultFragment extends MainActivity.PlaceholderFragment {

    @Bind(R.id.listView)
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_result, container, false);
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
        query.whereLessThan(Poll.END_AT, new Date());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                DialogHelper.fnCloseDialog();
                if (e == null) {
                    for (ParseObject p : objects) {
                        String t = p.get(Poll.TITLE).toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
                        String dt = sdf.format((Date) p.get(Poll.END_AT));
                        String op = p.get(Poll.OPTIONS).toString();
                        list.add(t + "||" + dt+"||"+op);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, android.R.id.text1, list);
                    listView.setAdapter(adapter);
                } else {
                    DialogHelper.getOkAlertDialog(ctx,
                            "Error in connecting server..", e.getMessage())
                            .show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object o = parent.getAdapter().getItem(position);
                Toast.makeText(ctx, o.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
