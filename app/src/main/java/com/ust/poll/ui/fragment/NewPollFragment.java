package com.ust.poll.ui.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
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
import com.ust.poll.MainActivity;
import com.ust.poll.model.Poll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ken on 10/7/2015.
 */
public class NewPollFragment extends MainActivity.PlaceholderFragment {
    @Bind(R.id.txt_title) BootstrapEditText txt_title;
    @Bind(R.id.option1) BootstrapButton option1;
    @Bind(R.id.option2) BootstrapButton option2;
    @Bind(R.id.option3) BootstrapButton option3;
    @Bind(R.id.option4) BootstrapButton option4;

    @Bind(R.id.txt_deadlineDate) BootstrapEditText txt_deadlineDate;
    @Bind(R.id.txt_deadlineTime) BootstrapEditText txt_deadlineTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_new, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                }
                else {
                    txt_deadlineDate.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a suitable event date.", Toast.LENGTH_LONG).show();
                }
            }
        },mYear, mMonth, mDay);
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
                if (hourOfDay<10 && minute<10) {
                    txt_deadlineTime.setText("0" + hourOfDay + ":0" + minute);
                }
                else if (hourOfDay>=10 && minute<10){
                    txt_deadlineTime.setText(hourOfDay + ":0" + minute);
                }
                else if (hourOfDay<10 && minute>=10){
                    txt_deadlineTime.setText("0" + hourOfDay + ":" + minute);
                }
                else {
                    txt_deadlineTime.setText(hourOfDay + ":" + minute);
                }
            }
        }, hour, minute, true);  // true for 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }





    @OnClick({ R.id.option1, R.id.option2, R.id.option3, R.id.option4 })
    public void fnOption(View view) {
        final BootstrapButton btn = (BootstrapButton)view;

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

        HideFragment hideFrag = new HideFragment();
        hideFrag.HideFragment();


        //Fragment fragment = new NewPollFragment_DateTime();
        //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.container, fragment);
        // fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.commit();

        boolean nextChecking = true;

        if(txt_deadlineDate.getText().toString().length() == 0 ){
            txt_deadlineDate.setError("Poll deadline date is required!");
            nextChecking = false;
        }

        if(txt_deadlineTime.getText().toString().length() == 0 ){
            txt_deadlineTime.setError("Poll deadline time is required!");
            nextChecking = false;
        }

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //Date today = new Date();


        //try {
        //    Date pollDate = sdf.parse(txt_deadlineDate.getText().toString());


        //    if(today.compareTo(pollDate) > 0){


        //        txt_deadlineDate.setError("Poll date invalid");

        //        nextChecking = false;
        //    }

        //} catch (ParseException e) {
        //    e.printStackTrace();
        //}



        //Log.d("test", "" + );


        if( txt_title.getText().toString().length() == 0 ) {
            txt_title.setError("Poll title is required!");
            nextChecking = false;
        }
        //Log.d("test", ""+option1.getText());

        int counter = 0;
        if (option1.getText().toString().equals( "OPTION 1")) {
            counter++;
        }
        if (option2.getText().toString().equals( "OPTION 2")) {
            counter++;
        }
        if (option3.getText().toString().equals( "OPTION 3")) {
            counter++;
        }
        if (option4.getText().toString().equals( "OPTION 4")) {
            counter++;
        }

        if (counter >=3){
            Toast.makeText(NewPollFragment.super.getActivity(), "Option must be more than one!", Toast.LENGTH_LONG).show();
            nextChecking = false;
        }

        if(nextChecking) {
            NewPollFragment_PickFriend fragment = new NewPollFragment_PickFriend();
            Bundle bundle = new Bundle();
            bundle.putString("title", txt_title.getText().toString());
            bundle.putString("option1", option1.getText().toString());
            bundle.putString("option2", option2.getText().toString());
            bundle.putString("option3", option3.getText().toString());
            bundle.putString("option4", option4.getText().toString());
            bundle.putString("date", txt_deadlineDate.getText().toString());
            bundle.putString("time", txt_deadlineTime.getText().toString());
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

            //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            //fragmentManager.beginTransaction()
            //        .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance(5))
            //        .addToBackStack(null).commit();
        }
        // DialogHelper.fnShowDialog(this.getContext());
        //fnCreatePoll();


        //Intent intent = new Intent(getActivity().getBaseContext(),
        //        MainActivity.class);
        //intent.putExtra("title", txt_title.getText().toString());
        //intent.putExtra("option1", option1.getText().toString());
        //intent.putExtra("option2", option2.getText().toString());
        //intent.putExtra("option3", option3.getText().toString());
        //intent.putExtra("option4", option4.getText().toString());
        //getActivity().startActivity(intent);


        //YEAH YEAH YEAH
    }


}
