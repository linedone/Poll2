// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.linedone.poll.R;
import com.ust.poll.model.EventViewHolder;
import com.ust.poll.util.MediaUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventAdapter extends ArrayAdapter<String>
{
    Context context;

    String[] arrayTitles;
    String[] arrayDates;
    String[] arrayTimes;
    String[] arrayVenues;
    String[] arrayRemarkURLs;
    String[] arrayImages;

    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

    public EventAdapter(Context context, ArrayList<String> strTitles, ArrayList<String> strDates, ArrayList<String> strTimes, ArrayList<String> strVenues, ArrayList<String> strRemarkURLs, ArrayList<String> strImages) {
        super(context, R.layout.fragment_event_active_item, R.id.txt_aeTitle, strTitles);
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
            vRow = inflater.inflate(R.layout.fragment_event_active_item, parent, false);
            holder = new EventViewHolder(vRow);
            vRow.setTag(holder);
        }
        else {  // Recycling
            holder = (EventViewHolder) vRow.getTag();
        }

        holder.txtTitle.setText(arrayTitles[position]);
        holder.txtDate.setText("Event Date: " + arrayDates[position]);
        if (arrayDates[position].toString().compareTo(dFormat.format(new Date()).toString())==0) {
            holder.toolbar.setBackground(new ColorDrawable(Color.parseColor("#ec407a")));
        }
        else if (position%2==0) {
            holder.toolbar.setBackground(new ColorDrawable(Color.parseColor("#01bcd5")));
        }
        else {
            holder.toolbar.setBackground(new ColorDrawable(Color.parseColor("#2d5d82")));
        }
        holder.txtTime.setText("Event Time: " + arrayTimes[position]);
        holder.txtVenue.setText("Event Venue: " + arrayVenues[position]);
        holder.txtRemarkURL.setText("Event Remarks: " + arrayRemarkURLs[position]);
        holder.imgPhoto.setImageBitmap(MediaUtil.getBitmapFromString(arrayImages[position]));
        return vRow;
    }
}