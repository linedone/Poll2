package com.ust.poll.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.linedone.poll.R;

public class EventViewHolder
{
    public TextView txtTitle;
    public TextView txtDate;
    public TextView txtTime;
    public TextView txtVenue;
    public TextView txtRemarkURL;
    public ImageView imgPhoto;
    public android.support.v7.widget.Toolbar toolbar;

    public EventViewHolder(View vRow)
    {
        txtTitle = (TextView) vRow.findViewById(R.id.txt_aeTitle);
        txtDate = (TextView) vRow.findViewById(R.id.txt_aeDate);
        txtTime = (TextView) vRow.findViewById(R.id.txt_aeTime);
        txtVenue = (TextView) vRow.findViewById(R.id.txt_aeVenue);
        txtRemarkURL = (TextView) vRow.findViewById(R.id.txt_aeRemarkURL);
        imgPhoto = (ImageView) vRow.findViewById(R.id.img_eaPhoto);
        toolbar = (android.support.v7.widget.Toolbar) vRow.findViewById(R.id.toolbar);
    }
}
