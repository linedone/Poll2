package com.ust.poll.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linedone.poll.R;

public class EventPhotoViewHolder
{
    public ImageView imageView;

    public EventPhotoViewHolder(View vRow)
    {
        imageView = (ImageView) vRow.findViewById(R.id.imageView);
    }
}
