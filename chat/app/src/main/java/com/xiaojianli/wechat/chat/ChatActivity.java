package com.xiaojianli.wechat.chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.addfriend.AddFriendActivity;
import com.xiaojianli.wechat.appUtil.Utils;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.chat.ui.FragmentChat;
import com.xiaojianli.wechat.chat.ui.FragmentFriend;
import com.xiaojianli.wechat.chat.ui.ViewPagerFragmentAdapter;
import com.xiaojianli.wechat.database.DataBaseHelper;

public class ChatActivity extends FragmentActivity 
	implements backThread.IServerMessageGet,
	OnClickListener {
	
	
	private static final String TAG = "ChatActivity.";
	
	private LayoutInflater mInflater ;

	private TextView contentTextView;
	ViewPager mViewPager;
	private ImageButton addFriendButton;
	
	private ImageView FirstImageView;
	private ImageView SecondImageView;
	private TextView FirstTextView;
	private TextView SecondTextView;
	
	public backThread mBackThread;
	IDataChange mLisenter;

	public NotificationManager NM;
	String[] mtabId = {"weiyu","pengyou"};
	String[] tab = {"微语","朋友"};
	int[] resource = {R.drawable.tab_weiyu,R.drawable.tab_friend};
	int[] textColor = {R.color.TabHostTextColor,R.color.TabHostTextGrayColor};
	DataBaseHelper db;
	
	FragmentPagerAdapter mAdapter;
	List<Fragment> fragmentList = new ArrayList<Fragment>();
	List<ImageView> ImageViewList = new ArrayList<ImageView>();
	List<TextView> TextViewList = new ArrayList<TextView>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		initFragment();
		setContentView(R.layout.main_layout);
		contentTextView = (TextView) findViewById(R.id.mContent);
		FirstImageView = (ImageView) findViewById(R.id.firstImageView);
		SecondImageView = (ImageView) findViewById(R.id.secondImageView);
		ImageViewList.add(FirstImageView);
		ImageViewList.add(SecondImageView);
		FirstTextView = (TextView) findViewById(R.id.firstTextView);
		SecondTextView = (TextView) findViewById(R.id.secondTextView);
		findViewById(R.id.firstLinearLayout).setOnClickListener(this);
		findViewById(R.id.secondLinearLayout).setOnClickListener(this);
		TextViewList.add(FirstTextView);
		TextViewList.add(SecondTextView);
		mViewPager = (ViewPager)findViewById(R.id.ViewPagerList);
		addFriendButton = (ImageButton) findViewById(R.id.addFriendButton);
		backThread.getInstance().addLisenter(this);
		NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		db = new DataBaseHelper(this);
		initViewPager();
	}
	
	private void initViewPager() {
		mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
		mViewPager.setOnPageChangeListener(new myOnLayoutChangeListener());
		mViewPager.setAdapter(mAdapter);
		updateViewState(0);
	}
	
	private void updateViewState(int index) {
		for (int i = 0; i < ImageViewList.size(); i++) {
			if (i == index) {
				ImageViewList.get(i).setSelected(true);
			} else {
				ImageViewList.get(i).setSelected(false);
			}
		}
		contentTextView.setText(tab[index]);
	}
	
	public class myOnLayoutChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			updateViewState(arg0);
		}
		
	}
	
	public void initFragment() {
		Fragment chat = new FragmentChat();
		Fragment friend = new FragmentFriend();
		fragmentList.add(chat);
		fragmentList.add(friend);
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		addFriendButton.setOnClickListener(new addFriendListener());
		
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
		
	public interface IDataChange {
		public void OnDataChange();
	}
	
	public void addLisenter(IDataChange dc) {
		mLisenter = dc;
	}
	
	class addFriendListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.addFriendButton:
				Intent mIntent = new Intent(ChatActivity.this,AddFriendActivity.class);
				startActivity(mIntent);
				break;
			default:
				break;
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		backThread.getInstance().setFlag(false);
		backThread.getInstance().removeLisenter(this);
	}

	@Override
	public void MessageGet(String message) {
		// TODO Auto-generated method stub
		appLog.e(TAG + "message "+ message + "; IsChatWindowActivityVisiable " + Utils.IsChatWindowActivityVisiable );
		if (Utils.IsChatWindowActivityVisiable) return;
		if (message == null || message.length() <= 0) return;
		if (message.startsWith("#chatContent#")) {
			String content = message.substring(13, message.length());
			String[] contentSplit = content.split("\\|");
			appLog.e(TAG + "length = " + contentSplit.length);
			if (contentSplit.length < 4) return;
			String friendID = contentSplit[0];
			String friendN = contentSplit[1];
			String chatContent = contentSplit[2];
			String time = contentSplit[3];
			
			
			Intent m = new Intent(this,chatWindowActivity.class);
			m.putExtra("friendname", friendN);
			m.putExtra("frienduserId", friendID);
			PendingIntent p = PendingIntent.getActivity(this, 110, m, PendingIntent.FLAG_UPDATE_CURRENT);
			String titleText = (chatContent.length() > 10) ? chatContent.substring(0,10) +"..." : chatContent;
			Notification N = new Notification.Builder(this)
					.setTicker(friendID + ":" + titleText)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentText(friendID + ":" + titleText)
					.setAutoCancel(true)
					.setContentIntent(p)
					.setContentTitle("新消息").build();
			N.defaults = Notification.DEFAULT_SOUND;
			NM.notify(110, N);
			
			
			String[] arg = {friendID};
			Cursor c = db.getReadableDatabase().query(DataBaseHelper.TABLE_CHATLIST, null, "name=?", arg , null, null, null);
			if (!c.moveToNext()) {
				ContentValues cv = new ContentValues();
				cv.put("name", friendID);
				cv.put("realname", friendN);
				if (chatContent.length() <= 12) {
					cv.put("chatcontent", chatContent);
				} else {
					cv.put("chatcontent", chatContent.substring(0, 12) + "...");
				}
				cv.put("chattime", time);
				cv.put("isNewMessage", 1);
				db.insert(DataBaseHelper.TABLE_CHATLIST, cv);
				Utils.mChatListContentChanged = true;
			} else {
				ContentValues cv = new ContentValues();
				if (chatContent.length() <= 12) {
					cv.put("chatcontent", chatContent);
				} else {
					cv.put("chatcontent", chatContent.substring(0, 12) + "...");
				}
				cv.put("chattime",time);
				cv.put("isNewMessage", 1);
				db.update(DataBaseHelper.TABLE_CHATLIST, cv,c.getInt(c.getColumnIndex("id")));
				Utils.mChatListContentChanged = true;
			}
			if (c != null) c.close();
			
			String sql = "create table if not exists " + "chatContent_" + friendID + "(" +
					"id INTEGER primary key AUTOINCREMENT," +
					"isme INTEGER NOT NULL," +
					"chatcontent text," +
					"addtime text NOT NULL);";
			db.getWritableDatabase().execSQL(sql);
			
			ContentValues cv = new ContentValues();
			cv.put("isme", 0);
			cv.put("chatcontent", chatContent);
			cv.put("addtime", time);
			db.insert("chatContent_" + friendID, cv);
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mLisenter != null) mLisenter.OnDataChange();
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.firstLinearLayout:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.secondLinearLayout:
			mViewPager.setCurrentItem(1);
			break;
		}
	}
}
