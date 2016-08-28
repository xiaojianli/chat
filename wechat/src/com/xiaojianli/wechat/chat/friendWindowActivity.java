package com.xiaojianli.wechat.chat;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.appUtil.MapParcelable;
import com.xiaojianli.wechat.appUtil.appLog;

public class friendWindowActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "friendWindowActivity.";
	TextView backTextView;
	TextView friendInfo;
	Button sendButton;
	Map<String,String> infoMap;
	
	private void initView() {
		backTextView = (TextView) findViewById(R.id.friendwindowback);
		backTextView.setOnClickListener(this);
		friendInfo = (TextView) findViewById(R.id.friendInfoTextView);
		sendButton = (Button) findViewById(R.id.sendMessageButton);
		sendButton.setOnClickListener(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent mIntent = getIntent();
		MapParcelable mp = mIntent.getParcelableExtra("info");
		infoMap = mp.getMap();
		appLog.e(TAG + infoMap.get("realname"));
		setContentView(R.layout.friend_window_layout);
		initView();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String addtime = infoMap.get("addtime").split(" ")[0];
		String info = null;
		if (infoMap.get("name").equals("system")) {
			info = "用户名：" + infoMap.get("realname") + "\n"
					+ "添加时间：" + addtime;
			sendButton.setVisibility(View.INVISIBLE);
		} else {
			 info = "用户名：" + infoMap.get("name") + "\n"
					+ "真实姓名：" + infoMap.get("realname") + "\n"
							+ "性别：" + infoMap.get("sexual") + "\n"
									+ "添加时间：" + addtime;
		}
		appLog.e(TAG + info);
		friendInfo.setText(info);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.friendwindowback:
			finish();
			break;
		case R.id.sendMessageButton:
			Intent mIntent = new Intent(this,chatWindowActivity.class);
			mIntent.putExtra("friendname", infoMap.get("realname"));
			mIntent.putExtra("frienduserId", infoMap.get("name"));
			startActivity(mIntent);
			finish();
			break;
		default:
			break;
		}
	}

}
