package com.ust.poll.ui.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.linedone.poll.R;
import com.ust.poll.model.EventPhotoViewHolder;
import com.ust.poll.model.EventViewHolder;
import com.ust.poll.util.MediaUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventPhotoAdapter extends ArrayAdapter<String>
{
    Context context;
    String[] arrayImages;

    public EventPhotoAdapter(Context context, ArrayList<String> strImages) {
        super(context, R.layout.fragment_event_detail_gallery, R.id.imageView, strImages);
        this.context = context;

        this.arrayImages = strImages.toArray(new String[strImages.size()]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventPhotoViewHolder holder = null;
        View vRow = convertView;

        if (vRow==null) {  // Create at 1st time ONLY
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vRow = inflater.inflate(R.layout.fragment_event_detail_gallery, parent, false);
            holder = new EventPhotoViewHolder(vRow);
            vRow.setTag(holder);
        }
        else {  // Recycling
            holder = (EventPhotoViewHolder) vRow.getTag();
        }

        holder.imageView.setImageBitmap(MediaUtil.getBitmapFromString(arrayImages[position]));

        return vRow;
    }
}