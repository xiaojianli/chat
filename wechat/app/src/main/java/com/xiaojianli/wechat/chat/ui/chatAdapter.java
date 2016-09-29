package com.xiaojianli.wechat.chat.ui;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaojianli.wechat.R;

public class chatAdapter extends BaseAdapter {
	
	
	private List<Map<String, String>> mchatList;
	private LayoutInflater mInflater;
	public chatAdapter(List<Map<String, String>> chatList, Context context) {
		// TODO Auto-generated constructor stub
		super();
		mInflater = LayoutInflater.from(context);
		mchatList = chatList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mchatList.size();
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
			convertView = mInflater.inflate(R.layout.chat, null);
			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.name);
			vh.chatContent = (TextView) convertView.findViewById(R.id.content);
			vh.newMessage = (TextView) convertView.findViewById(R.id.newMessageTextView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.name.setText(mchatList.get(position).get("name"));
		vh.chatContent.setText(mchatList.get(position).get("chatcontent"));
		if ("1".equals(mchatList.get(position).get("isNewMessage"))) {
			vh.newMessage.setVisibility(View.VISIBLE);
		} else {
			vh.newMessage.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public final class ViewHolder{
		TextView name;
		TextView chatContent;
		TextView newMessage;
	}

}
