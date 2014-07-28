package com.et.TestViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	Context context;

	public ListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.list_items, null);
			viewHolder = new ViewHolder();
			viewHolder.textView1 = (TextView) convertView.findViewById(R.id.ItemTitle);
			viewHolder.textView2 = (TextView) convertView.findViewById(R.id.ItemText);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView1.setText("Level" + position);
		viewHolder.textView2.setText("Finished in 1 Min 54 Secs, 70 Moves! ");
		return convertView;
	}

	private class ViewHolder {
		TextView textView1;
		TextView textView2;
	}
}
