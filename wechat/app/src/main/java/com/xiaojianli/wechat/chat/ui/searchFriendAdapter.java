package com.xiaojianli.wechat.chat.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.addfriend.addFriendImp;
import com.xiaojianli.wechat.appUtil.Utils;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.database.DataBaseHelper;
import com.xiaojianli.wechat.ui.ToastShow;

public class searchFriendAdapter extends BaseAdapter {
	
	public static final String TAG = "searchFriendAdapter.";
	List<addFriendImp> mList = new ArrayList<addFriendImp>();
	LayoutInflater mInflater;
	DataBaseHelper db;
	Context context;
	public searchFriendAdapter(List<addFriendImp> list ,Context context,DataBaseHelper d) {
		// TODO Auto-generated constructor stub
		this.mList = list;
		this.mInflater = LayoutInflater.from(context);
		this.db = d;
		this.context = context;
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

	ViewHolder vh;
	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int p = position;
		addFriendImp afi = mList.get(p);
		if (afi == null) return null;
		convertView = mInflater.inflate(R.layout.search_friend_list_layout, null);
		vh = new ViewHolder();
		vh.username = (TextView) convertView.findViewById(R.id.searchUsername);
		vh.realname = (TextView) convertView.findViewById(R.id.searchrealname);
		vh.sexual = (TextView) convertView.findViewById(R.id.searchSexual);
		vh.addButton = (Button) convertView.findViewById(R.id.addFriendButton);
		convertView.setTag(vh);
		vh.username.setText("用户名：" + afi.getUsername());
		vh.realname.setText(afi.getRealname());
		vh.sexual.setText(afi.getSexual());
		vh.addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContentValues cv = new ContentValues();
				cv.put("name",mList.get(p).getUsername());
				cv.put("realname", mList.get(p).getRealname());
				cv.put("sex", mList.get(p).getSexual());
				String[] arg = {cv.getAsString("name")};
				Cursor c = db.getReadableDatabase().query(DataBaseHelper.TABLE_FRIEND, null, "name=?", arg , null, null, null);
				if (c.moveToNext()) {
					appLog.e(TAG + "已在列表中");
					ToastShow.getInstance().showToast(context, "已在朋友列表中");
					return;
				}
				if (c != null) c.close();
				Utils.mDatabaseChanged = true;
				db.insert(DataBaseHelper.TABLE_FRIEND, cv);
			}
		});
		
		return convertView;
	}

	public final class ViewHolder{
		TextView username;
		TextView realname;
		TextView sexual;
		Button addButton;
	}
	
}
