package com.xiaojianli.wechat.chat.ui;

import java.util.List;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.chat.ChatContentImp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class chatWindowAdapter extends BaseAdapter {
	
	List<ChatContentImp> mList;
	LayoutInflater mInflater;
	
	public chatWindowAdapter(List<ChatContentImp> l, Context context) {
		// TODO Auto-generated constructor stub
		this.mList = l;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChatContentImp cc = mList.get(position);
		if (cc == null) return null;
		boolean isSend = (cc.getIssend() == 1);
		if (!isSend) {
			convertView = mInflater.inflate(R.layout.chat_left, null);
		} else {
			convertView = mInflater.inflate(R.layout.chat_right, null);
		}
		((TextView)convertView.findViewById(R.id.dateTextView)).setText(cc.getSendTime());
		((TextView)convertView.findViewById(R.id.chatContent)).setText(cc.getContent());
		return convertView;

	}
	
}
