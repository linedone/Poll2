package com.ust.poll.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ust.poll.MainActivity;
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
public class SelectPollFragment extends MainActivity.PlaceholderFragment {
    @Nullable
    @Bind(R.id.txt_title)
    BootstrapEditText txt_title;


    @Bind(R.id.btn_select_op1)
    BootstrapButton btn_select_op1;

    @Bind(R.id.btn_select_op2)
    BootstrapButton btn_select_op2;

    @Bind(R.id.btn_select_op3)
    BootstrapButton btn_select_op3;

    @Bind(R.id.btn_select_op4)
    BootstrapButton btn_select_op4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_poll, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }



    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //final ArrayList<String> list = new ArrayList<String>();
        final Context ctx = this.getActivity();
        //DialogHelper.fnShowDialog(this.getContext());
        //ParseQuery<ParseObject> query = ParseQuery.getQuery(Poll.TABLE_NAME);


        ParseQuery<ParseObject> query = ParseQuery.getQuery(Poll.TABLE_NAME);
        //Log.d("active", "" + username.replace("+852", ""));


        Bundle bundle = this.getArguments();
        String objectID = bundle.getString("pollID");

        query.whereContains(Poll.OBJECTID, objectID);
        //query.whereGreaterThan(Poll.END_AT, new Date());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                DialogHelper.fnCloseDialog();
                if (e == null) {
                    //int counter = 0;
                    for (ParseObject p : objects) {
                        String id = p.getObjectId();
                        String t = p.get(Poll.TITLE).toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
                        String dt = sdf.format((Date)p.get(Poll.END_AT));
                        ArrayList op = (ArrayList) p.get(Poll.OPTIONS);
                        //counter++;


                        //Log.d("select", "" + t);
                        //Log.d("select", "" + op);
                        //pollTitle.setText(""+t);

                        //String[] optionArray = op.split(",");

                        btn_select_op1.setText(""+op.get(0));
                        btn_select_op2.setText(""+op.get(1));
                        btn_select_op3.setText(""+op.get(2));
                        btn_select_op4.setText(""+op.get(3));
                    }



                }
                else {
                    DialogHelper.getOkAlertDialog(ctx,
                            "Error in connecting server..", e.getMessage())
                            .show();
                }
            }
        });
    }

}
