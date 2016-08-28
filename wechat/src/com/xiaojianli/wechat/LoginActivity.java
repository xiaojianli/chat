package com.xiaojianli.wechat;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.chat.ChatActivity;
import com.xiaojianli.wechat.database.DataBaseHelper;
import com.xiaojianli.wechat.register.RegisterActivity;
import com.xiaojianli.wechat.ui.ToastShow;

public class LoginActivity extends Activity implements OnClickListener{

	private static final String TAG = "LoginActivity.";
	
	public static final int REMOVE_DIALOG = 1;
	public static final int CONNECT_ERROR = 2;
	public static final int LOGIN_FAILED = 3;
	public static final int LOGIN_SUCCESS = 4;
	EditText userNameEditText;
	EditText passWordEditText;
	CheckBox passWordCheckBox;
	Button mLoginButton;
	Button mRegisterButton;
	SharedPreferences mSharedPreferences ;
	Handler mHanlder = new myHandler();
	Dialog mDialog;
	
	class myHandler extends Handler {

		@SuppressWarnings("null")
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case REMOVE_DIALOG:
				if (mDialog != null) {
					mDialog.cancel();
				}
				ToastShow.getInstance().showToast(LoginActivity.this, "登录超时。。。");
				break;
			case CONNECT_ERROR:
				ToastShow.getInstance().showToast(LoginActivity.this, "连接错误。。.");
				break;

			case LOGIN_FAILED:
				if (mDialog != null) {
					mDialog.cancel();
				}
				ToastShow.getInstance().showToast(LoginActivity.this, "登录失败，用户名或密码错误");
				break;
			case LOGIN_SUCCESS:
				if (mDialog != null) {
					mDialog.cancel();
				}
				appLog.e("Login_success userName = " + userName);
				DataBaseHelper.TABLE_CHATLIST = "chat_list_" + userName;
				DataBaseHelper.TABLE_FRIEND = "friend_" + userName;
				DataBaseHelper.DATABASE_NAME = DataBaseHelper.DATABASE_NAME + "." + userName;
				
				
				DataBaseHelper db = new DataBaseHelper(LoginActivity.this);
				String query = "select * from " + DataBaseHelper.TABLE_FRIEND + " where name = 'system'";
				Cursor c = db.getReadableDatabase().rawQuery(query, null);
				if (c == null || !c.moveToNext()) {
					ContentValues friend = new ContentValues();
					friend.put("name", "system");
					friend.put("realname", "系统消息");
					db.insert(DataBaseHelper.TABLE_FRIEND,friend);
					ContentValues chat = new ContentValues();
					chat.put("name", "system");
					chat.put("realname", "系统消息");
					chat.put("chatcontent", "欢迎使用微语!");
					db.insert(DataBaseHelper.TABLE_CHATLIST,chat);
				}
				saveLoginInfo();
				Intent m = new Intent(LoginActivity.this,ChatActivity.class);
				startActivity(m);
				finish();
				break;
			default:
				break;
			}
		}
		
	}
	
	public void PrepareView() {
		userNameEditText = (EditText)findViewById(R.id.mInputUserName);
		passWordEditText = (EditText)findViewById(R.id.mInputPassword);
		passWordCheckBox = (CheckBox)findViewById(R.id.checkbox);
		mLoginButton = (Button)findViewById(R.id.LoginButton);
		mRegisterButton = (Button)findViewById(R.id.registerButton);
		mLoginButton.setOnClickListener(this);
		mRegisterButton.setOnClickListener(this);
		passWordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor e = mSharedPreferences.edit();
				e.putBoolean("remPassword", isChecked);
				e.commit();
			}
		});
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.login_in);
		PrepareView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		appLog.e(TAG + "onResume");
		lookCheckBox();
	}

	@Override
	protected void onPause() {
		super.onPause();
		appLog.e(TAG + "onPause");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		appLog.e(TAG + " onDestroy");
//		SigleSocketClinet.getInstance().closeSocket();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	public void lookCheckBox() {
			String username = mSharedPreferences.getString("username", "");
			String password = mSharedPreferences.getString("password", "");
			boolean rebPassword = mSharedPreferences.getBoolean("remPassword", false);
			userNameEditText.setText(username);
			if (rebPassword) {
				passWordCheckBox.setChecked(true);
				passWordEditText.setText(password);
			} 
	}
	
	public void saveLoginInfo() {
		Editor e = mSharedPreferences.edit();
		e.putString("username", userNameEditText.getText().toString());
		if (passWordCheckBox.isChecked()) {
			e.putString("password", passWordEditText.getText().toString());
			e.putBoolean("remPassword", true);
		} else {
			e.putString("password", "");
			e.putBoolean("remPassword", false);
		}
		e.commit();
	}
	
	private String userName;
	private String passWord;
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.LoginButton:
			userName = userNameEditText.getText().toString();
			passWord = passWordEditText.getText().toString();
			
			/**
			 * just for test
			 */
//			if (mSharedPreferences.getBoolean("firstLogin", true)) {
//				DataBaseHelper db = new DataBaseHelper(LoginActivity.this);
//				ContentValues friend = new ContentValues();
//				friend.put("name", "system");
//				friend.put("realname", "系统消息");
//				db.insert(DataBaseHelper.TABLE_FRIEND,friend);
//				
//				ContentValues chat = new ContentValues();
//				chat.put("name", "system");
//				chat.put("realname", "系统消息");
//				chat.put("chatcontent", "欢迎使用微语!");
//				db.insert(DataBaseHelper.TABLE_CHATLIST,chat);
//				
//				Editor e = mSharedPreferences.edit();
//				e.putBoolean("firstLogin", false);
//				e.commit();
//			}
//			
//			saveLoginInfo();
//			Intent m = new Intent(LoginActivity.this,ChatActivity.class);
//			startActivity(m);
//			finish();
//			if (true) return;
			/**
			 * 
			 */
			
			if (userName.trim().isEmpty()) {
				ToastShow.getInstance().showToast(LoginActivity.this, "请输入用户名");
				return;
			}
			if (passWord.trim().equals("")) {
				ToastShow.getInstance().showToast(LoginActivity.this, "请输入密码");
				return;
			}

			mDialog = ProgressDialog.show(LoginActivity.this, "", "正在登录。。。");
			mHanlder.sendEmptyMessageDelayed(REMOVE_DIALOG, 1000*10);
			String LoginInfo = userName + "|" + passWord;
			new LoginThread(LoginInfo, mHanlder).start();
			break;
		case R.id.registerButton:
			Intent mIntent = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(mIntent);
			break;
			default :
				break;
		}
	}
}
