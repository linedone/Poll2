package com.ust.poll.ui.adaptor;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.linedone.poll.R;
import com.ust.poll.model.EventViewHolder;

public class EventAdapter extends ArrayAdapter<String>
{
    Context context;

    String[] arrayTitles;
    String[] arrayDates;
    String[] arrayTimes;
    String[] arrayVenues;
    String[] arrayRemarkURLs;

    public EventAdapter(Context context, String[] strTitle, String[] strDate, String[] strTime, String[] strVenue, String[] strRemarkURL) {
        super(context, R.layout.fragment_active_event_item, R.id.txt_aeTitle, strTitle);
        this.context = context;

        this.arrayTitles = strTitle;
        this.arrayDates = strDate;
        this.arrayTimes = strTime;
        this.arrayVenues = strVenue;
        this.arrayRemarkURLs = strRemarkURL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventViewHolder holder = null;
        View vRow = convertView;

        if (vRow==null) {  // Create at 1st time ONLY
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vRow = inflater.inflate(R.layout.fragment_active_event_item, parent, false);
            holder = new EventViewHolder(vRow);
            vRow.setTag(holder);
        }
        else {  // Recycling
            holder = (EventViewHolder) vRow.getTag();
        }

        holder.txtTitle.setText(arrayTitles[position]);
        holder.txtDate.setText(arrayDates[position]);
        holder.txtTime.setText(arrayTimes[position]);
        holder.txtVenue.setText(arrayVenues[position]);
        holder.txtRemarkURL.setText(arrayRemarkURLs[position]);
        //holder.imgPhoto.setImageBitmap(arrayImages[position]);

        return vRow;
    }
}