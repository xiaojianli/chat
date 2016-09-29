package com.xiaojianli.wechat.chat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.appUtil.Utils;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.chat.ChatActivity;
import com.xiaojianli.wechat.chat.chatWindowActivity;
import com.xiaojianli.wechat.database.DataBaseHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class FragmentChat extends Fragment 

		implements ChatActivity.IDataChange{
	
	private static final String TAG = "FragmentChat.";

	private ListView mListView;
	private View v;
	private List<Map<String, String>> dataList;
	DataBaseHelper db;
	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		appLog.e(TAG + "onCreateView");
		initLisenter();
		if (v == null) {
			v = inflater.inflate(R.layout.fragmentchat, null);
		}
		if (mListView == null) {
			mListView = (ListView) v.findViewById(R.id.chatScrollView);
		}
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null) {
			parent.removeView(v);
		}
		return v;
	}
	
	public void initLisenter() {
		((ChatActivity)getActivity()).addLisenter(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		appLog.e(TAG + "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		dataList = getData();
		Utils.mChatListContentChanged = false;
		initAdapter();
	}
	
	public void initAdapter() {
		chatAdapter cA = new chatAdapter(dataList, getActivity());
		mListView.setAdapter(cA);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				appLog.e(TAG + "onItemClick position:" + position);
				Intent mIntent = new Intent(getActivity(),chatWindowActivity.class);
				mIntent.putExtra("friendname", dataList.get(position).get("name"));
				mIntent.putExtra("frienduserId", dataList.get(position).get("userIdname"));
				startActivity(mIntent);
				
			}
			
		});
	}
	
	private List<Map<String, String>> getData() {
		List<Map<String, String>> mList = new ArrayList<Map<String,String>>();
		
		String sql = "select * from " + DataBaseHelper.TABLE_CHATLIST + " order by chattime desc ";
		Cursor c = db.getReadableDatabase().rawQuery(sql, null);
		while (c.moveToNext()) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("userIdname", c.getString(c.getColumnIndex("name")));
			m.put("name", c.getString(c.getColumnIndex("realname")));
			m.put("chatcontent", c.getString(c.getColumnIndex("chatcontent")));
			appLog.e(TAG + c.getString(c.getColumnIndex("chattime")));
			m.put("time", c.getString(c.getColumnIndex("chattime")));
			m.put("isNewMessage", String.valueOf(c.getInt(c.getColumnIndex("isNewMessage"))));
			mList.add(m);
		}
		if (c != null) c.close();
		return mList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appLog.e(TAG + "onCreate");
		db = new DataBaseHelper(getActivity());
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		appLog.e(TAG + "onCreateAnimation");

		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		appLog.e(TAG + "onDestroy");

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		appLog.e(TAG + "onDestroyView");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		appLog.e(TAG + "onPause");

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		appLog.e(TAG + "onResume");
		doDataChange();
	}
	
	private void doDataChange() {
		if (Utils.mChatListContentChanged) {
			dataList = getData();
			initAdapter();
			Utils.mChatListContentChanged = false;
		}
	}

	@Override
	public void OnDataChange() {
		// TODO Auto-generated method stub
		appLog.e(TAG + "OnDataChange");
		doDataChange();
	}

	
}
