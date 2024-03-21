package com.xiaojianli.wechat.register;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.ui.ToastShow;

public class RegisterActivity extends Activity implements OnClickListener{
	
	
	private final static String TAG = "RegisterActivity.";
	
	public static final int REMOVE_DIALOG = 1;
	public static final int CONNECE_ERROR = 2;
	public static final int REGISTER_SUCCESS = 3;
	public static final int REGISTER_FAILED = 4;
	public static final int USER_EXIST = 5;

	Handler mHandler = new MyHandler();
	EditText userNameEdit;
	EditText passwordEdit;
	EditText passwordAgainEdit;
	EditText realNameEdit;
	RadioGroup sexChoose;
	RadioButton maleRadio;
	RadioButton femaleRadio;
	RadioButton femalemaleRadio;
	Button completeButton;
	Dialog mDialog;
	
	
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			appLog.e(TAG + "handleMessage what = " + msg.what);
			switch (msg.what) {
			case REMOVE_DIALOG:
				if (mDialog != null) {
					mDialog.cancel();
				}
				ToastShow.getInstance().showToast(RegisterActivity.this, "注册失败");
				break;
			case CONNECE_ERROR:
				ToastShow.getInstance().showToast(RegisterActivity.this, "连接错误");
				break;
			case REGISTER_SUCCESS:
				if (mDialog != null) {
					mDialog.cancel();
				}
				ToastShow.getInstance().showToast(RegisterActivity.this, "注册成功");
				break;
			case REGISTER_FAILED:
				if (mDialog != null) {
					mDialog.cancel();
				}
				ToastShow.getInstance().showToast(RegisterActivity.this,"注册失败");
				break;
			case USER_EXIST:
				if (mDialog != null) {
					mDialog.cancel();
				}
				ToastShow.getInstance().showToast(RegisterActivity.this,"注册失败,用户已存在");
				break;
			default:
				break;
			}
		}
		
	}
	
	public void prepareView() {
		userNameEdit = (EditText) findViewById(R.id.nameEditText);
		passwordEdit = (EditText) findViewById(R.id.passwordEditText);
		passwordAgainEdit = (EditText) findViewById(R.id.passwordagainEditText);
		realNameEdit = (EditText) findViewById(R.id.realNameEditText);
		sexChoose = (RadioGroup) findViewById(R.id.sexChoose);
		maleRadio = (RadioButton) findViewById(R.id.male);
		femalemaleRadio = (RadioButton) findViewById(R.id.female);
		femalemaleRadio = (RadioButton) findViewById(R.id.malefemale);
		sexChoose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.male:
					sex = "男";
					break;
				case R.id.female:
					sex = "女";
					break;
				case R.id.malefemale:
					sex = "中性";
					break;
				default:
					break;
				}
			}
		});
		completeButton = (Button) findViewById(R.id.completeButton);
		completeButton.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		
		prepareView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	String sex = "男";
	@Override
	public void onClick(View v) {
		appLog.e(TAG+"onClick id = " + v.getId());
		switch (v.getId()) {
		case R.id.completeButton:
			String username = userNameEdit.getText().toString();
			String password = passwordEdit.getText().toString();
			String passwordAgain = passwordAgainEdit.getText().toString();
			String realname = realNameEdit.getText().toString();
			if (username.trim().isEmpty()) {
				ToastShow.getInstance().showToast(RegisterActivity.this, "请输入用户名");
				return;
			}
			if (password.trim().isEmpty()) {
				ToastShow.getInstance().showToast(RegisterActivity.this, "请输入密码");
				return;
			}
			if (passwordAgain.trim().isEmpty()) {
				ToastShow.getInstance().showToast(RegisterActivity.this, "请确认密码");
				return;
			}
			if (realname.trim().isEmpty()) {
				ToastShow.getInstance().showToast(RegisterActivity.this, "请输入真实姓名");
				return;
			}
			if (sex.trim().isEmpty()) {
				ToastShow.getInstance().showToast(RegisterActivity.this, "请选择性别");
				return;
			}
			if (!password.trim().equals(passwordAgain.trim())) {
				ToastShow.getInstance().showToast(RegisterActivity.this, "两次密码输入不一致");
				return;
			}
			mDialog = ProgressDialog.show(RegisterActivity.this, "", "注册中");
			mHandler.sendEmptyMessageDelayed(REMOVE_DIALOG, 1000 * 10);
			String info = username + "|" + password + "|" + realname + "|" + sex;
			new RegisterThread(info, mHandler).start();
			break;

		default:
			break;
		}
	}

	
}
