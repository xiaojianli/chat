package com.xiaojianli.wechat.chat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.appUtil.SigleSocketClinet;
import com.xiaojianli.wechat.appUtil.Utils;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.chat.ui.chatWindowAdapter;
import com.xiaojianli.wechat.database.DataBaseHelper;

public class chatWindowActivity extends Activity 

	implements OnClickListener,
	backThread.IServerMessageGet{
	
	private static final String TAG = "chatWindowActivity.";
	
	private static final int INSERT_DATA = 0;
	private static final int SEND_MESSAGE = 1;
	
	private String friendName;
	private String friendUserId;
	TextView backTextView;
	TextView FriendNameTextView;
	EditText editContent;
	Button sendButton;
	ListView chatContentList;
	chatWindowAdapter mchatWindowAdapter;
	DataBaseHelper db ;
	HandlerThread mHandlerThread;
	Handler mHandler;
	BufferedWriter bw = null;
	NotificationManager NM;
	List<ChatContentImp> mList = new ArrayList<ChatContentImp>(); 

	
	View rootView;
	private void initView() {
		rootView = findViewById(R.id.chat_window_layout_root);
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				int diff = Math.abs(rootView.getRootView().getHeight() - rootView.getHeight());
				if (diff > 100 && mchatWindowAdapter != null && mList != null) {
					chatContentList.setSelection(mList.size() - 1);
				} 
			}
		});
		backTextView = (TextView) findViewById(R.id.chatwindowback);
		FriendNameTextView = (TextView) findViewById(R.id.friendNameTextView);
		FriendNameTextView.setText(friendName);
		editContent = (EditText) findViewById(R.id.whatyouwanttosay);
		sendButton = (Button) findViewById(R.id.sendbutton);
		chatContentList = (ListView) findViewById(R.id.chatContentListView);
		backTextView.setOnClickListener(this);
		sendButton.setOnClickListener(this);
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		friendName = getIntent().getStringExtra("friendname");
		friendUserId = getIntent().getStringExtra("frienduserId");
		mHandlerThread = new HandlerThread("updateDatabase");
		mHandlerThread.start();
		mHandler = new MainHandler(mHandlerThread.getLooper());
		db = new DataBaseHelper(this);
		String sql = "create table if not exists " + "chatContent_" + friendUserId + "(" +
				"id INTEGER primary key AUTOINCREMENT," +
				"isme INTEGER NOT NULL," +
				"chatcontent text," +
				"addtime text NOT NULL);";
		db.getWritableDatabase().execSQL(sql);
		setContentView(R.layout.chat_window_layout);
		initView();
		NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	protected void onResume() {
		super.onResume();
		Utils.IsChatWindowActivityVisiable = true;
		if (!"system".equals(friendUserId)) {
			initChatList();
		}
		mchatWindowAdapter = new chatWindowAdapter(mList, this);
		chatContentList.setAdapter(mchatWindowAdapter);
		chatContentList.setSelection(mList.size() - 1);
		backThread.getInstance().addLisenter(this);
		
		if (friendUserId != null && friendUserId.length() > 0) {
			String[] arg = {friendUserId};
			Cursor c = db.getReadableDatabase().query(DataBaseHelper.TABLE_CHATLIST, null, "name=?", arg , null, null, null);
			if (c.moveToNext()) {
				ContentValues cv = new ContentValues();
				cv.put("isNewMessage", 0);
				db.update(DataBaseHelper.TABLE_CHATLIST, cv, c.getInt(c.getColumnIndex("id")));
				Utils.mChatListContentChanged = true;
			}
			if (c != null) c.close();
		}
	};
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Utils.IsChatWindowActivityVisiable = false;
		backThread.getInstance().removeLisenter(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mList.clear();
	}
	private void initChatList() {
		String sql = "select * from " + "chatContent_" + friendUserId;
		Cursor c = db.getReadableDatabase().rawQuery(sql, null);
		while (c.moveToNext()) {
			ChatContentImp cci = new ChatContentImp();
			cci.setIssend(c.getInt(c.getColumnIndex("isme")));
			cci.setContent(c.getString(c.getColumnIndex("chatcontent")));
			cci.setSendTime(c.getString(c.getColumnIndex("addtime")));
			mList.add(cci);
		}
		if (c != null) c.close();
	}
	
	
	class MainHandler extends Handler {
		
		public MainHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			appLog.e(TAG + "msg.what = " + msg.what);
			switch (msg.what) {
			case INSERT_DATA:
				ChatContentImp cci = (ChatContentImp)msg.obj;
				appLog.e(TAG + "INSERT START");
				ContentValues cv = new ContentValues();
				cv.put("isme", cci.getIssend());
				cv.put("chatcontent", cci.getContent());
				cv.put("addtime", cci.getSendTime());
				db.insert("chatContent_" + cci.getUserID(), cv);
				appLog.e(TAG + "INSERT END");
				
				break;
			case SEND_MESSAGE:
				String send = (String)msg.obj;
				try {
					if (SigleSocketClinet.getInstance() == null) break;
					if (bw == null)  bw = new BufferedWriter(new OutputStreamWriter(SigleSocketClinet
							.getInstance().getOutputStream()));
					bw.write(send + "\r\n");
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.chatwindowback:
			
			finish();
			break;
		case R.id.sendbutton:
			appLog.e(TAG + "sendButton");
			String content = editContent.getText().toString().trim();
			if (content.length() <= 0) return;

			if (!"system".equals(friendUserId)) {

				String[] arg = {friendUserId};
				Cursor c = db.getReadableDatabase().query(DataBaseHelper.TABLE_CHATLIST, null, "name=?", arg , null, null, null);
				if (!c.moveToNext()) {
					ContentValues cv = new ContentValues();
					cv.put("name", friendUserId);
					cv.put("realname", friendName);
					if (content.length() <= 12) {
						cv.put("chatcontent", content);
					} else {
						cv.put("chatcontent", content.substring(0, 12) + "...");
					}
					cv.put("chattime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					db.insert(DataBaseHelper.TABLE_CHATLIST, cv);
					Utils.mChatListContentChanged = true;
				} else {
					ContentValues cv = new ContentValues();
					if (content.length() <= 12) {
						cv.put("chatcontent", content);
					} else {
						cv.put("chatcontent", content.substring(0, 12) + "...");
					}
					cv.put("chattime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					db.update(DataBaseHelper.TABLE_CHATLIST, cv,c.getInt(c.getColumnIndex("id")));
					Utils.mChatListContentChanged = true;
				}
				if (c != null) c.close();
				
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

				Message sendMessage = new Message();
				sendMessage.what = SEND_MESSAGE;
				sendMessage.obj = "#chatContent#" + friendUserId + "|" + content + "|" +time; 
				mHandler.sendMessage(sendMessage);
				
				ChatContentImp m = new ChatContentImp();
				m.setContent(content);
				m.setIssend(1);
				m.setSendTime(time);
				m.setUserID(friendUserId);
				mList.add(m);
				
				Message message = new Message();
				message.what = INSERT_DATA;
				message.obj = m;
				mHandler.sendMessage(message);
				editContent.setText("");
				
			} else {
				
				ChatContentImp m = new ChatContentImp();
				m.setContent(content);
				m.setIssend(1);
				m.setSendTime(new SimpleDateFormat().format(new Date()));
				mList.add(m);
				editContent.setText("");
				
				ChatContentImp systemChat = new ChatContentImp();
				systemChat.setContent("别发了，没人回你！！！\n去加好友聊吧！");
				systemChat.setIssend(2);
				systemChat.setSendTime(new SimpleDateFormat().format(new Date()));
				mList.add(systemChat);
			}
			
			mchatWindowAdapter.notifyDataSetChanged();
			chatContentList.setSelection(mList.size() - 1);
			
			break;

		default:
			break;
		}
	}


	@Override
	public void MessageGet(String message) {
		// TODO Auto-generated method stub
		appLog.e(TAG + "message = " + message);
		if (message == null || message.length() <= 0) return;
		if (message.startsWith("#chatContent#")) {
			String content = message.substring(13, message.length());
			String[] contentSplit = content.split("\\|");
			if (contentSplit.length < 4) return;
			String friendID = contentSplit[0];
			String friendN = contentSplit[1];
			String chatContent = contentSplit[2];
			String time = contentSplit[3];
			
			ChatContentImp cci = new ChatContentImp();
			cci.setIssend(0);
			cci.setContent(chatContent);
			cci.setSendTime(time);
			cci.setUserID(friendID);
		
			if (friendID.equals(friendUserId)) {
				mList.add(cci);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mchatWindowAdapter.notifyDataSetChanged();
						chatContentList.setSelection(mList.size() - 1);	
					}
				});

			} else {				
				Intent m = new Intent();
				m.putExtra("friendname", friendN);
				m.putExtra("frienduserId", friendID);
				PendingIntent p = PendingIntent.getActivity(chatWindowActivity.this, 110, m, PendingIntent.FLAG_UPDATE_CURRENT);
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
				
				String sql = "create table if not exists " + friendID + "(" +
						"id INTEGER primary key AUTOINCREMENT," +
						"isme INTEGER NOT NULL," +
						"chatcontent text," +
						"addtime text NOT NULL);";
				db.getWritableDatabase().execSQL(sql);
			}
			
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
				cv.put("isNewMessage", 0);
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
				cv.put("isNewMessage", 0);
				db.update(DataBaseHelper.TABLE_CHATLIST, cv,c.getInt(c.getColumnIndex("id")));
				Utils.mChatListContentChanged = true;
			}
			if (c != null) c.close();
			
			Message m = new Message();
			m.what = INSERT_DATA;
			m.obj = cci;
			mHandler.sendMessage(m);
		}
	}
}
