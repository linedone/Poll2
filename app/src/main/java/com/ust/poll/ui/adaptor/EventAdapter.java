package com.ust.poll.ui.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.linedone.poll.R;
import com.ust.poll.model.EventViewHolder;
import com.ust.poll.util.MediaUtil;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<String>
{
    Context context;

    String[] arrayTitles;
    String[] arrayDates;
    String[] arrayTimes;
    String[] arrayVenues;
    String[] arrayRemarkURLs;
    String[] arrayImages;

    public EventAdapter(Context context, ArrayList<String> strTitles, ArrayList<String> strDates, ArrayList<String> strTimes, ArrayList<String> strVenues, ArrayList<String> strRemarkURLs, ArrayList<String> strImages) {
        super(context, R.layout.fragment_active_event_item, R.id.txt_aeTitle, strTitles);
        this.context = context;

        this.arrayTitles = strTitles.toArray(new String[strTitles.size()]);
        this.arrayDates = strDates.toArray(new String[strDates.size()]);
        this.arrayTimes = strTimes.toArray(new String[strTimes.size()]);
        this.arrayVenues = strVenues.toArray(new String[strVenues.size()]);
        this.arrayRemarkURLs = strRemarkURLs.toArray(new String[strRemarkURLs.size()]);
        this.arrayImages = strImages.toArray(new String[strImages.size()]);
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
        holder.txtDate.setText("Event Date: " + arrayDates[position]);
        holder.txtTime.setText("Event Time: " + arrayTimes[position]);
        holder.txtVenue.setText("Event Venue: " + arrayVenues[position]);
        holder.txtRemarkURL.setText("Event Remarks: " + arrayRemarkURLs[position]);
        holder.imgPhoto.setImageBitmap(MediaUtil.getBitmapFromString(arrayImages[position]));

        return vRow;
    }
}