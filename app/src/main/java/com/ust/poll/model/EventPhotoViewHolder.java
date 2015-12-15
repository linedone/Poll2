// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.model;

import android.view.View;
import android.widget.ImageView;

import com.linedone.poll.R;

public class EventPhotoViewHolder {
    public ImageView imageView;

    public EventPhotoViewHolder(View vRow) {
        imageView = (ImageView) vRow.findViewById(R.id.imageView);
    }
}
