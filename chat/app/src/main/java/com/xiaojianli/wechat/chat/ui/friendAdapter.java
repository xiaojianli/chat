package com.xiaojianli.wechat.chat.ui;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojianli.wechat.R;

public class friendAdapter extends BaseAdapter {
	
	LayoutInflater mInflater;
	List<Map<String, String>> mList;
	public friendAdapter(List<Map<String, String>> list ,Context context) {
		// TODO Auto-generated constructor stub
		mList = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private ViewHolder vh;
	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.friend, null);
			vh = new ViewHolder();
			vh.mFriendName = (TextView) convertView.findViewById(R.id.friendName);
			vh.mIcon = (ImageView) convertView.findViewById(R.id.mTouxiangView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.mFriendName.setText(mList.get(position).get("realname"));
		return convertView;
	}
	
	public final class ViewHolder {
		TextView mFriendName;
		ImageView mIcon;
	}

}
