package com.ust.poll.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;
import com.ust.poll.activity.LoginActivity;
import com.ust.poll.model.Poll;
import com.ust.poll.model.Polled;
import com.ust.poll.ui.dialog.DialogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                        String dt = sdf.format((Date) p.get(Poll.END_AT));
                        ArrayList op = (ArrayList) p.get(Poll.OPTIONS);
                        //counter++;


                        //Log.d("select", "" + t);
                        //Log.d("select", "" + op);
                        TextView pollTitle = (TextView) getView().findViewById(R.id.pollTitle);
                        pollTitle.setText(t);

                        //String[] optionArray = op.split(",");

                        if (op.get(0).equals("OPTION 1")) {
                            btn_select_op1.setVisibility(View.GONE);
                        } else {
                            btn_select_op1.setText("" + op.get(0));
                        }

                        if (op.get(1).equals("OPTION 2")) {
                            btn_select_op2.setVisibility(View.GONE);
                        } else {
                            btn_select_op2.setText("" + op.get(1));
                        }

                        if (op.get(2).equals("OPTION 3")) {
                            btn_select_op3.setVisibility(View.GONE);
                        } else {
                            btn_select_op3.setText("" + op.get(2));
                        }

                        if (op.get(3).equals("OPTION 4")) {
                            btn_select_op4.setVisibility(View.GONE);
                        } else {
                            btn_select_op4.setText("" + op.get(3));
                        }

                    }


                } else {
                    DialogHelper.getOkAlertDialog(ctx,
                            "Error in connecting server..", e.getMessage())
                            .show();
                }


            }


        });





    }


    @OnClick({ R.id.btn_select_op1, R.id.btn_select_op2, R.id.btn_select_op3, R.id.btn_select_op4 })
    public void onClick(View v){

        String option = "";
        Bundle bundle = this.getArguments();
        final String pollID = bundle.getString("pollID");

        ParseUser user = ParseUser.getCurrentUser();
        final String userid = user.getObjectId();

        switch (v.getId()) {
            case R.id.btn_select_op1:
                // it was the first button
                option = btn_select_op1.getText().toString();
                Log.d("select", "clicked 1");
                break;
            case R.id.btn_select_op2:
                // it was the second button
                option = btn_select_op2.getText().toString();
                Log.d("select", "clicked 2");
                break;
            case R.id.btn_select_op3:
                // it was the third button
                option = btn_select_op3.getText().toString();
                Log.d("select", "clicked 3");
                break;
            case R.id.btn_select_op4:
                // it was the fourth button
                option = btn_select_op4.getText().toString();
                Log.d("select", "clicked 4");
                break;
        }

        final String finalOption = option;


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Confirm this option ?");

        builder.setMessage(finalOption);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //btn.setText(input.getText().toString());

                ParseObject pollObject = new ParseObject(Polled.TABLE_NAME);
                pollObject.put(Polled.OPTION, finalOption);
                pollObject.put(Polled.POLLID, pollID);
                pollObject.put(Polled.USERID, userid);

                pollObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        DialogHelper.fnCloseDialog();
                        if (e == null) {
                            Toast.makeText(getActivity().getBaseContext(),
                                    "Poll has been submitted.",
                                    Toast.LENGTH_LONG).show();

                            ActivePollFragment fragment = new ActivePollFragment();
                            Bundle newbundle = new Bundle();
                            fragment.setArguments(newbundle);
                            getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

                        } else {
                            DialogHelper.getOkAlertDialog(getActivity().getBaseContext(),
                                    "Error in connecting server..", e.getMessage())
                                    .show();
                        }
                    }
                });




            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

}
