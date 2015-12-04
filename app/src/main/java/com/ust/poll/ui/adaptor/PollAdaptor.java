package com.ust.poll.ui.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linedone.poll.R;
import com.parse.ParseObject;

/**
 * Created by Ken on 10/7/2015.
 */

public class PollAdaptor extends ArrayAdapter<ParseObject> {
    private final Context context;
    private final String[] values;

    public PollAdaptor(Context context, String[] values) {
        super(context, android.R.layout.simple_list_item_1);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return parent;
    }
}