package com.ust.poll.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.ust.poll.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ken on 10/7/2015.
 */
public class NewPollFragment_DateTime extends MainActivity.PlaceholderFragment {
    @Nullable
    @Bind(R.id.txt_title)
    BootstrapEditText txt_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_new_datetime, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;


    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatePicker datePicker = (DatePicker) getView().findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);



        //Toast.makeText(NewPollFragment_DateTime.super.getActivity(), ""+title,
        //        Toast.LENGTH_LONG).show();


    }



    @OnClick(R.id.btn_new_poll_datetime_next)
    public void fnNewPoll(View view) {

        Bundle bundle = this.getArguments();
        String title = bundle.getString("title");
        String option1 = bundle.getString("option1");
        String option2 = bundle.getString("option2");
        String option3 = bundle.getString("option3");
        String option4 = bundle.getString("option4");

        DatePicker datePicker = (DatePicker) getView().findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();


        TimePicker timePicker = (TimePicker) getView().findViewById(R.id.timePicker);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        NewPollFragment_PickFriend fragment = new NewPollFragment_PickFriend();
        Bundle newbundle = new Bundle();
        newbundle.putString("title", title);
        newbundle.putString("option1", option1);
        newbundle.putString("option2", option2);
        newbundle.putString("option3", option3);
        newbundle.putString("option4", option4);
        newbundle.putInt("day", day);
        newbundle.putInt("month", month);
        newbundle.putInt("year", year);
        newbundle.putInt("hour", hour);
        newbundle.putInt("minute", minute);
        fragment.setArguments(newbundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }



    }
