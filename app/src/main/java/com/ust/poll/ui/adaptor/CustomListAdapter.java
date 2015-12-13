// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linedone.poll.R;
import com.ust.poll.model.NewsItem;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<NewsItem> listData;

	private LayoutInflater layoutInflater;

	public CustomListAdapter(Context context, ArrayList<NewsItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView.findViewById(R.id.title);
			holder.resultView= (TextView) convertView.findViewById(R.id.result);
			holder.reporterNameView = (TextView) convertView.findViewById(R.id.reporter);
			holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
			holder.reportedIdView = (TextView) convertView.findViewById(R.id.pollid);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.headlineView.setText("Poll Title : " + listData.get(position).getHeadline());

		if(listData.get(position).getallOpt() == null) {

			holder.resultView.setVisibility(View.GONE);
		}else{

			holder.resultView.setVisibility(View.VISIBLE);
			holder.resultView.setText("Poll Result : " + listData.get(position).getallOpt());
		}

		holder.reporterNameView.setText("Created By, " + listData.get(position).getReporterName());
		holder.reportedDateView.setText("Poll Deadline : " + listData.get(position).getDate());
		holder.reportedIdView.setText(listData.get(position).getpollID());

		return convertView;
	}

	static class ViewHolder {
		TextView headlineView;
		TextView reporterNameView;
		TextView reportedDateView;
		TextView reportedIdView;
		TextView resultView;
	}

}
