package com.thehackerati.rssreader;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	private ArrayList<FeedItem> listData;
	private Context mContext;
	private LayoutInflater layoutInflater;
	
	public CustomListAdapter(Context context, ArrayList<FeedItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
			holder = new ViewHolder();
			holder.appNameView = (TextView) convertView.findViewById(R.id.textViewAppTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		FeedItem item = (FeedItem) listData.get(position);
		holder.appNameView.setText(item.getAppTitle());
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView appNameView;
	}

}
