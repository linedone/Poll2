// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;
import com.ust.poll.model.Poll;
import com.ust.poll.ui.dialog.DialogHelper;
import com.ust.poll.util.TelephonyUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPollFragment extends MainActivity.PlaceholderFragment {
    @Bind(R.id.txt_title)
    BootstrapEditText txt_title;
    @Bind(R.id.option1)
    BootstrapButton option1;
    @Bind(R.id.option2)
    BootstrapButton option2;
    @Bind(R.id.option3)
    BootstrapButton option3;
    @Bind(R.id.option4)
    BootstrapButton option4;

    @Bind(R.id.txt_deadlineDate)
    BootstrapEditText txt_deadlineDate;
    @Bind(R.id.txt_deadlineTime)
    BootstrapEditText txt_deadlineTime;
    private static int FRAGMENT_CODE = 0;

    String members;
    String contactPosition;
    ArrayList<String> contactList = new ArrayList<String>();

    String soption1 = "";
    String soption2 = "";
    String soption3 = "";
    String soption4 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_new, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> parseObjects, com.parse.ParseException e) {
                retrieveSuccess(parseObjects, e);
            }
        });

        if (!soption1.isEmpty())
            option1.setText(soption1);

        if (!soption2.isEmpty())
            option2.setText(soption2);

        if (!soption3.isEmpty())
            option3.setText(soption3);

        if (!soption4.isEmpty())
            option4.setText(soption4);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        soption1 = data.getStringExtra("soption1");
        soption2 = data.getStringExtra("soption2");
        soption3 = data.getStringExtra("soption3");
        soption4 = data.getStringExtra("soption4");
        if (requestCode == FRAGMENT_CODE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                if (data.getStringExtra("members") != null) {
                    members = data.getStringExtra("members");
                    contactPosition = data.getStringExtra("contactPosition");
                    Log.d("Poll PickFriend", "Data passed from PickFriend Fragment = " + members);
                }
            }
        }
    }

    @OnClick(R.id.txt_deadlineDate)
    public void fnPickDate(View view) {
        final Calendar eventDate = Calendar.getInstance();
        final Calendar todayDate = Calendar.getInstance();
        int mYear = eventDate.get(Calendar.YEAR);
        int mMonth = eventDate.get(Calendar.MONTH);
        int mDay = eventDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                eventDate.set(Calendar.YEAR, year);
                eventDate.set(Calendar.MONTH, monthOfYear);
                eventDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                todayDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                todayDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
                todayDate.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                if (eventDate.getTime().compareTo(todayDate.getTime()) >= 0) {
                    txt_deadlineDate.setText(sdFormat.format(eventDate.getTime()));
                } else {
                    txt_deadlineDate.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a suitable event date.", Toast.LENGTH_LONG).show();
                }
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }


    @OnClick(R.id.txt_deadlineTime)
    public void fnPickTime(View view) {
        final Calendar eventTime = Calendar.getInstance();
        int hour = eventTime.get(Calendar.HOUR_OF_DAY);
        int minute = eventTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                if (hourOfDay < 10 && minute < 10) {
                    txt_deadlineTime.setText("0" + hourOfDay + ":0" + minute);
                } else if (hourOfDay >= 10 && minute < 10) {
                    txt_deadlineTime.setText(hourOfDay + ":0" + minute);
                } else if (hourOfDay < 10 && minute >= 10) {
                    txt_deadlineTime.setText("0" + hourOfDay + ":" + minute);
                } else {
                    txt_deadlineTime.setText(hourOfDay + ":" + minute);
                }
            }
        }, hour, minute, true);  // true for 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @OnClick(R.id.btn_new_poll_friend)
    public void fnPickFriends(View view) {
        PickFriendFragment fragment = new PickFriendFragment();
        fragment.setTargetFragment(this, FRAGMENT_CODE);
        Bundle bundle = new Bundle();
        if (members != null) {
            bundle.putString("members", members);
            bundle.putString("contactPosition", contactPosition);
        }
        bundle.putStringArrayList("contactList", contactList);
        bundle.putString("soption1", option1.getText().toString());
        bundle.putString("soption2", option2.getText().toString());
        bundle.putString("soption3", option3.getText().toString());
        bundle.putString("soption4", option4.getText().toString());
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    @OnClick({R.id.option1, R.id.option2, R.id.option3, R.id.option4})
    public void fnOption(View view) {
        final BootstrapButton btn = (BootstrapButton) view;

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Option");

        // Set up the input
        final EditText input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn.setText(input.getText().toString());
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

    @OnClick(R.id.btn_new_poll_next)
    public void fnNewPoll(View view) {
        boolean nextChecking = true;

        if (txt_deadlineDate.getText().toString().length() == 0) {
            txt_deadlineDate.setError("Poll deadline date is required!");
            nextChecking = false;
        }
        if (txt_deadlineTime.getText().toString().length() == 0) {
            txt_deadlineTime.setError("Poll deadline time is required!");
            nextChecking = false;
        }
        if (txt_title.getText().toString().length() == 0) {
            txt_title.setError("Poll title is required!");
            nextChecking = false;
        }
        int counter = 0;
        if (option1.getText().toString().equals("OPTION 1") || option1.getText().toString().equals("")) {
            counter++;
        }
        if (option2.getText().toString().equals("OPTION 2") || option2.getText().toString().equals("")) {
            counter++;
        }
        if (option3.getText().toString().equals("OPTION 3") || option3.getText().toString().equals("")) {
            counter++;
        }
        if (option4.getText().toString().equals("OPTION 4") || option4.getText().toString().equals("")) {
            counter++;
        }

        if (counter >= 3) {
            Toast.makeText(NewPollFragment.super.getActivity(), "Option must be more than one!", Toast.LENGTH_LONG).show();
            nextChecking = false;
        }

        if (members != null) {
            String[] pollmemberArray = members.split(",");
            if (pollmemberArray.length <= 0 || pollmemberArray[0].equals("")) {
                Toast.makeText(NewPollFragment.super.getActivity(), "Friend must be more than one!", Toast.LENGTH_LONG).show();
                nextChecking = false;
            }
        } else {
            Toast.makeText(NewPollFragment.super.getActivity(), "Friend must be more than one!", Toast.LENGTH_LONG).show();
            nextChecking = false;
        }

        String testdeadDate = txt_deadlineDate.getText().toString() + " " + txt_deadlineTime.getText().toString();
        SimpleDateFormat testFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = new Date();
        testFormat.format(date);

        try {
            Date testDeadline = testFormat.parse(testdeadDate);
            if (date.after(testDeadline)) {
                // In between
                Toast.makeText(NewPollFragment.super.getActivity(), "Deadline Date must be in future!", Toast.LENGTH_LONG).show();
                nextChecking = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            nextChecking = false;
        }
        if (nextChecking) {
            final Context ctx = this.getContext();
            ParseObject pollObject = new ParseObject(Poll.TABLE_NAME);
            pollObject.put(Poll.TITLE, txt_title.getText().toString());
            pollObject.addAllUnique(Poll.OPTIONS, Arrays.asList(option1.getText().toString(),
                    option2.getText().toString(), option3.getText().toString(), option4.getText().toString()));
            String deadDate = txt_deadlineDate.getText().toString() + " " + txt_deadlineTime.getText().toString();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                //Log.d("test", "" + txt_deadlineDate.getText().toString());
                Date deadline = format.parse(deadDate);
                pollObject.put(Poll.END_AT, deadline);
                //Log.d("test", "" + deadline);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            if (members != null) {
                String[] pollmemberArray = members.split(",");
                ParseUser user = ParseUser.getCurrentUser();
                final String username = user.getUsername();

                pollObject.put(Poll.FRIEND_PHONE, Arrays.asList(pollmemberArray));
                pollObject.put(Poll.CREATORPHONE, username);
                pollObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        DialogHelper.fnCloseDialog();
                        if (e == null) {
                            Toast.makeText(ctx,
                                    "Poll Successfully created.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("Save Error", e.toString());
                        }
                    }
                });
                fnSendPushNotification(pollmemberArray, txt_title.getText().toString(), TelephonyUtil.getContactName(getActivity(), username));
            }
        }
    }

    private void fnSendPushNotification(String[] phoneList, String titleOpt, String contactName) {
        String[] userArray = phoneList;
        for (int i = 0; i < userArray.length; i++) {
            ParsePush push = new ParsePush();
            ParseQuery query = ParseInstallation.getQuery();
            query.whereEqualTo("username", userArray[i]);
            Log.d("NewPollFragment", userArray[i]);

            push.setQuery(query);
            push.setMessage(contactName + " just sent you a poll, Title: " + titleOpt);
            push.sendInBackground();
        }

        ActivePollFragment fragment = new ActivePollFragment();
        Bundle newbundle = new Bundle();
        fragment.setArguments(newbundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void retrieveSuccess(List<ParseUser> parseObjects, com.parse.ParseException e) {
        if (e == null) {
            for (ParseUser parseItem : parseObjects) {
                String contactName = TelephonyUtil.getContactName(getActivity(), parseItem.getUsername());
                if (contactName.compareTo("") != 0) {
                    contactList.add(contactName + " [" + parseItem.getUsername().toString() + "]");
                }
            }
        }
    }
}
