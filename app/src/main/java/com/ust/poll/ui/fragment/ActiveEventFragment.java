package com.ust.poll.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linedone.poll.R;
import com.ust.poll.MainActivity;

import butterknife.ButterKnife;

public class ActiveEventFragment extends MainActivity.PlaceholderFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_event, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }
}