package com.xiaojianli.wechat;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.xiaojianli.wechat.appUtil.SigleSocketClinet;
import com.xiaojianli.wechat.appUtil.appLog;

import android.os.Handler;

public class LoginThread extends Thread {
	
	public static final String TAG = "LoginThread.";
	
	BufferedReader br = null;
	BufferedWriter bw = null;
	
	String LoginInfo;
	Handler mHandler;
	public LoginThread(String LoginInfo, Handler m) {
		super("LoginThread");
		this.LoginInfo = LoginInfo;
		this.mHandler = m;
	}
	
	
	@Override
	public void run() {
		if (SigleSocketClinet.getInstance() == null) {
			mHandler.sendEmptyMessage(LoginActivity.REMOVE_DIALOG);
			mHandler.sendEmptyMessage(LoginActivity.CONNECT_ERROR);
			return;
		}
		try {
			br = new BufferedReader(new InputStreamReader(SigleSocketClinet.getInstance().getInputStream(),"UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(SigleSocketClinet.getInstance().getOutputStream()));
			bw.write("#login#" + LoginInfo + "\r\n");
			bw.flush();
			String result = br.readLine();
			mHandler.removeMessages(LoginActivity.REMOVE_DIALOG);
			appLog.e(TAG + "result = " + result);
			if ("loginSuccess".equals(result)) {
				mHandler.sendEmptyMessage(LoginActivity.LOGIN_SUCCESS);
			} else {
				mHandler.sendEmptyMessage(LoginActivity.LOGIN_FAILED);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
//			try {
//				if (br != null) br.close();
//				if (bw != null) bw.close();
//			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		
	}
	
	

}
