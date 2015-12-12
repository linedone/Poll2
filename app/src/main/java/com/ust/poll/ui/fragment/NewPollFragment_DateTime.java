package com.ust.poll.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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

        FragmentTransaction transaction;
        transaction =  getFragmentManager().beginTransaction();
        hideFragments(transaction);

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


    private void hideFragments(FragmentTransaction transaction) {


        ActiveEventFragment activeEventFragment = (ActiveEventFragment)getFragmentManager().findFragmentByTag("ActiveEventFragment");
        ActivePollFragment activePollFragment = (ActivePollFragment)getFragmentManager().findFragmentByTag("ActivePollFragment");
        DetailFriendListEventFragment detailFriendListEventFragment = (DetailFriendListEventFragment)getFragmentManager().findFragmentByTag("DetailFriendListEventFragment");
        DetailGalleryEventFragment detailGalleryEventFragment = (DetailGalleryEventFragment)getFragmentManager().findFragmentByTag("DetailGalleryEventFragment");
        FriendListFragment friendListFragment = (FriendListFragment)getFragmentManager().findFragmentByTag("FriendListFragment");
        NewEventFragment newEventFragment = (NewEventFragment)getFragmentManager().findFragmentByTag("NewEventFragment");
        NewPollFragment newPollFragment = (NewPollFragment)getFragmentManager().findFragmentByTag("NewPollFragment");
        NewPollFragment_DateTime newPollFragment_DateTime = (NewPollFragment_DateTime)getFragmentManager().findFragmentByTag("NewPollFragment_DateTime");
        NewPollFragment_PickFriend newPollFragment_PickFriend = (NewPollFragment_PickFriend)getFragmentManager().findFragmentByTag("NewPollFragment_PickFriend");
        PickFriendFragment pickFriendFragment = (PickFriendFragment)getFragmentManager().findFragmentByTag("PickFriendFragment");
        PollResultFragment pollResultFragment = (PollResultFragment)getFragmentManager().findFragmentByTag("PollResultFragment");
        SelectPollFragment selectPollFragment = (SelectPollFragment)getFragmentManager().findFragmentByTag("SelectPollFragment");





        if ( activeEventFragment!= null) {
            transaction.hide(activeEventFragment);
        }
        if ( activePollFragment!= null) {
            transaction.hide(activePollFragment);
        }
        if ( detailFriendListEventFragment!= null) {
            transaction.hide(detailFriendListEventFragment);
        }
        if ( detailGalleryEventFragment!= null) {
            transaction.hide(detailGalleryEventFragment);
        }
        if ( friendListFragment!= null) {
            transaction.hide(friendListFragment);
        }
        if ( newEventFragment!= null) {
            transaction.hide(newEventFragment);
        }
        if (newPollFragment != null) {
            transaction.hide(newPollFragment);
        }
        if (newPollFragment_DateTime != null) {
            transaction.hide(newPollFragment_DateTime);
        }
        if (newPollFragment_PickFriend != null) {
            transaction.hide(newPollFragment_PickFriend);
        }
        if (pickFriendFragment != null) {
            transaction.hide(pickFriendFragment);
        }
        if (pollResultFragment != null) {
            transaction.hide(pollResultFragment);
        }
        if (selectPollFragment != null) {
            transaction.hide(selectPollFragment);
        }

    }

    }
