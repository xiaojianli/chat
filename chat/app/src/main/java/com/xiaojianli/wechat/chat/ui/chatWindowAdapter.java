package com.xiaojianli.wechat.chat.ui;

import java.util.List;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.chat.ChatContentImp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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

	private final class ViewHolder {
		public TextView mDateTextView;
		public TextView mRightTextViewContent;
		public TextView mLeftTextViewContent;
		public ImageView iconLeftImageView;
		public ImageView iconRightImageView;
		public ViewGroup mRightViewGroup;
		public ViewGroup mLeftViewGroup;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChatContentImp cc = mList.get(position);
		ViewHolder vh;
		if (cc == null) return null;
		boolean isSend = (cc.getIssend() == 1);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.chat_right,null);
			vh = new ViewHolder();
			vh.mDateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
			vh.mLeftTextViewContent = (TextView)convertView.findViewById(R.id.chatContent_left);
			vh.mLeftViewGroup = (RelativeLayout) convertView.findViewById(R.id.LayoutLeft);
			vh.iconLeftImageView = (ImageView)convertView.findViewById(R.id.usericon_left);
			vh.mRightTextViewContent = (TextView)convertView.findViewById(R.id.chatContent_right);
			vh.mRightViewGroup = (RelativeLayout)convertView.findViewById(R.id.LayoutRight);
			vh.iconRightImageView = (ImageView)convertView.findViewById(R.id.usericon_right);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (!isSend) {
			vh.mLeftViewGroup.setVisibility(View.VISIBLE);
			vh.mRightViewGroup.setVisibility(View.GONE);
			vh.mLeftTextViewContent.setText(cc.getContent());
		} else {
			vh.mRightViewGroup.setVisibility(View.VISIBLE);
			vh.mLeftViewGroup.setVisibility(View.GONE);
			vh.mRightTextViewContent.setText(cc.getContent());
		}
		vh.mDateTextView.setText(cc.getSendTime());
		return convertView;

	}
	
}
