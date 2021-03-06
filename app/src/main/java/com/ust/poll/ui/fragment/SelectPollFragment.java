// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ust.poll.model.Poll;
import com.ust.poll.model.Polled;
import com.ust.poll.ui.dialog.DialogHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_poll, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Context ctx = this.getActivity();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Poll.TABLE_NAME);
        Bundle bundle = this.getArguments();
        final String objectID = bundle.getString("pollID");

        query.whereContains(Poll.OBJECTID, objectID);
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading record...", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                DialogHelper.fnCloseDialog();
                if (e == null) {
                    for (ParseObject p : objects) {
//                        String id = p.getObjectId();
                        String t = p.get(Poll.TITLE).toString();
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
//                        String dt = sdf.format((Date) p.get(Poll.END_AT));
                        ArrayList op = (ArrayList) p.get(Poll.OPTIONS);

                        TextView pollTitle = (TextView) getView().findViewById(R.id.pollTitle);
                        pollTitle.setText(t);
                        pollTitle.setVisibility(View.VISIBLE);

                        if (op.get(0).equals("OPTION 1")) {
                            btn_select_op1.setVisibility(View.GONE);
                        } else {
                            btn_select_op1.setVisibility(View.VISIBLE);
                            btn_select_op1.setText("" + op.get(0));
                        }

                        if (op.get(1).equals("OPTION 2")) {
                            btn_select_op2.setVisibility(View.GONE);
                        } else {
                            btn_select_op2.setVisibility(View.VISIBLE);
                            btn_select_op2.setText("" + op.get(1));
                        }

                        if (op.get(2).equals("OPTION 3")) {
                            btn_select_op3.setVisibility(View.GONE);
                        } else {
                            btn_select_op3.setVisibility(View.VISIBLE);
                            btn_select_op3.setText("" + op.get(2));
                        }

                        if (op.get(3).equals("OPTION 4")) {
                            btn_select_op4.setVisibility(View.GONE);
                        } else {
                            btn_select_op4.setVisibility(View.VISIBLE);
                            btn_select_op4.setText("" + op.get(3));
                        }
                    }
                } else {
                    DialogHelper.getOkAlertDialog(ctx,
                            "Error in connecting server..", e.getMessage())
                            .show();
                }
                progressDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.btn_select_op1, R.id.btn_select_op2, R.id.btn_select_op3, R.id.btn_select_op4})
    public void onClick(View v) {
        String option = "";
        Bundle bundle = this.getArguments();
        final String pollID = bundle.getString("pollID");

        ParseUser user = ParseUser.getCurrentUser();
        final String userid = user.getObjectId();
        final String username = user.getUsername();

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
                ParseObject polledObject = new ParseObject(Polled.TABLE_NAME);
                polledObject.put(Polled.OPTION, finalOption);
                polledObject.put(Polled.POLLID, pollID);
                polledObject.put(Polled.USERID, userid);
                progressDialog = ProgressDialog.show(getActivity(), "", "Saving record...", true);
                polledObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        DialogHelper.fnCloseDialog();
                        if (e != null) {
                            Log.e("Save", e.toString());
                        }
                    }
                });
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Poll.TABLE_NAME);
                parseQuery.whereEqualTo(Poll.OBJECTID, pollID);
                parseQuery.whereContainedIn(Poll.FRIEND_PHONE, Arrays.asList(username));
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {
                            Log.d("active---", "" + userid);
                            Log.d("active---", "" + username);
                            Log.d("active---", "" + pollID);

                            List<String> list11 = new ArrayList<String>();
                            for (ParseObject p : parseObjects) {
                                ParseObject point = ParseObject.createWithoutData(Poll.TABLE_NAME, p.getObjectId());
                                list11 = p.getList(Poll.FRIEND_PHONE);
                                list11.remove(username);
                                String[] tempPhone = list11.toArray(new String[0]);

                                if (tempPhone.length == 0) {
                                    point.put(Poll.FRIEND_PHONE, Arrays.asList("null"));
                                } else {
                                    point.put(Poll.FRIEND_PHONE, Arrays.asList(tempPhone));
                                }
                                point.addAll(Poll.FRIEND_ID, Arrays.asList(userid));
                                // Save
                                point.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            // Saved successfully.
                                            Toast.makeText(getActivity().getBaseContext(), "Poll has been submitted.", Toast.LENGTH_LONG).show();
                                            ActivePollFragment fragment = new ActivePollFragment();
                                            Bundle newbundle = new Bundle();
                                            fragment.setArguments(newbundle);
                                            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                                        }
                                    }
                                });
                            }
                            //retrieveEventSuccess(parseObjects, e);
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