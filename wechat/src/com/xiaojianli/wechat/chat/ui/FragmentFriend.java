package com.xiaojianli.wechat.chat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.appUtil.MapParcelable;
import com.xiaojianli.wechat.appUtil.Utils;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.chat.friendWindowActivity;
import com.xiaojianli.wechat.database.DataBaseHelper;


public class FragmentFriend extends Fragment {

	private static final String TAG = "FragmentFriend.";
	private View v;
	private ListView mListView;
	private List<Map<String, String>> dataList;
	private friendAdapter fa;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (v == null) {
			v = inflater.inflate(R.layout.fragmentfriend, null);
		}
		if (mListView == null) {
			mListView = (ListView) v.findViewById(R.id.friendlist);
		}
		
		ViewGroup vg = (ViewGroup) v.getParent();
		if (vg != null) vg.removeView(v);
		
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		appLog.e(TAG + "onActivityCreateed");
		dataList = getData();
		Utils.mDatabaseChanged = false;
		initAdapter();
	}
	
	private void initAdapter() {
		fa = new friendAdapter(dataList, getActivity());
		mListView.setAdapter(fa);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				appLog.e(TAG + "onItemClick position:" + position);
				Intent mIntent = new Intent(getActivity(),friendWindowActivity.class);
				MapParcelable mp = new MapParcelable();
				mp.setMap(dataList.get(position));
				mIntent.putExtra("info", mp);
				startActivity(mIntent);
			}
			
		});
	}
	List<Map<String, String>> getData() {
		List<Map<String, String>> mList = new ArrayList<Map<String,String>>();
		DataBaseHelper db = new DataBaseHelper(getActivity());
		String sql = "select * from " + DataBaseHelper.TABLE_FRIEND + " order by addtime desc";
		appLog.e(TAG + "QUERY START");
		Cursor c = db.getReadableDatabase().rawQuery(sql, null);
		appLog.e(TAG + "QUERY END");
		while(c.moveToNext()) {
			appLog.e(TAG + "name = " + c.getString(c.getColumnIndex("realname")));
			Map<String, String> m = new HashMap<String, String>();
			m.put("realname",c.getString(c.getColumnIndex("realname")));
			m.put("name", c.getString(c.getColumnIndex("name")));
			m.put("sexual", c.getString(c.getColumnIndex("sex")));
			m.put("addtime", c.getString(c.getColumnIndex("addtime")));
			mList.add(m);
		}
		if (c != null) c.close();
		return mList;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		appLog.e(TAG + "onResume databaseChanged = " + Utils.mDatabaseChanged);
		if (Utils.mDatabaseChanged) {
			dataList = getData();
			initAdapter();
			Utils.mDatabaseChanged = false;
		}
		
	}

	
	
}
