package com.xiawenquan.test.adapter;

import java.util.ArrayList;

import com.xiawenquan.test.asyncqueryhandler.R;
import com.xiawenquan.test.mode.ConteactMode;
import com.xiawenquan.test.widget.PinnedHeaderListView;
import com.xiawenquan.test.widget.PinnedHeaderListView.PinnedHeaderAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter implements PinnedHeaderAdapter, OnScrollListener{

	private Context context;
	private ArrayList<ConteactMode> modes;
	
	public ContactAdapter(Context context){
		this.context = context;
	}
	
	
	public void setData(ArrayList<ConteactMode> modes){
		this.modes = modes;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modes != null && modes.size() > 0 ? modes.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return modes != null && modes.size() > 0 ? modes.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder ;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.alphalistview_item, null);
			
			holder.fistAlphaTextView = (TextView) convertView.findViewById(R.id.first_alpha);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
			holder.phoneTextView = (TextView) convertView.findViewById(R.id.phone);
			
			convertView.setTag(holder);
		}else{
			
			holder = (ViewHolder) convertView.getTag();
			
		}
		
		if(modes != null && modes.size() > 0){
			ConteactMode conteactMode = modes.get(position);
			if(conteactMode != null){
				
				//名称
				String name = conteactMode.getName();
				if(!TextUtils.isEmpty(name)){
					holder.nameTextView.setText(name);
				}
				
				//电话
				String phone = conteactMode.getPhone();
				if(!TextUtils.isEmpty(name)){
					holder.phoneTextView.setText(phone);
				}
				
				
				//首字母(前后两项对比字母是否相同，如果相同则过滤，否则添加进来)
				String currentAlpha = conteactMode.getFirstAlpha();
				ConteactMode mode = (position - 1) >= 0 ? modes.get(position - 1)  : null;
				String previewStr = "";
				if(mode != null){
					previewStr = mode.getFirstAlpha();
				}
				
				if (!previewStr.equals(currentAlpha)) {
					holder.fistAlphaTextView.setVisibility(View.VISIBLE);
					holder.fistAlphaTextView.setText(currentAlpha);
				}else{
					holder.fistAlphaTextView.setVisibility(View.GONE);
				}
			}
		}
		
		
		return convertView;
	}
	
	class ViewHolder{
		TextView fistAlphaTextView;
		TextView nameTextView;
		TextView phoneTextView;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		// TODO Auto-generated method stub
		if (modes != null && modes.size() > 0) {
			ConteactMode conteactMode = modes.get(position);
			if (conteactMode != null) {
				String currentAlpha = conteactMode.getFirstAlpha();
				//判断下一项的首字母是否相同
				ConteactMode mode = (position + 1) < modes.size() ? modes.get(position + 1)  : null;
				String nextStr = "";
				if(mode != null){
					nextStr = mode.getFirstAlpha();
				}
				if (!nextStr.equals(currentAlpha)){
					return PINNED_HEADER_PUSHED_UP;
				}else{
					return PINNED_HEADER_VISIBLE;
				}
			}
		}
		
		return PINNED_HEADER_GONE;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView)view).configureHeaderView(firstVisibleItem);
		}
	}


	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		ConteactMode conteactMode = modes.get(position);
		String currentAlpha = conteactMode.getFirstAlpha();
		((TextView)header).setText(currentAlpha);
	}

}
