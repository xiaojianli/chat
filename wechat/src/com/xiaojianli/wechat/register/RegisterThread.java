package com.xiaojianli.wechat.register;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.os.Handler;

import com.xiaojianli.wechat.appUtil.SigleSocketClinet;
import com.xiaojianli.wechat.appUtil.appLog;

public class RegisterThread extends Thread {
	
	private static final String TAG = "RegisterThread.";
	
	String registerInfo = null;

	BufferedWriter bw = null;
	BufferedReader br = null;
	
	Handler mHandler = null;
	
	public RegisterThread(String info,Handler mHandler) {
		super("RegisterThread");
		this.registerInfo = info;
		this.mHandler = mHandler;
	}
	
	
	@Override
	public void run() {
		
		try {
			if (SigleSocketClinet.getInstance() == null){
				mHandler.sendEmptyMessage(RegisterActivity.REMOVE_DIALOG);
				mHandler.sendEmptyMessage(RegisterActivity.CONNECE_ERROR);
				return;
			}
			bw = new BufferedWriter(
					new OutputStreamWriter(SigleSocketClinet.getInstance().getOutputStream()));
			bw.write("#register#" + registerInfo + "\r\n");
			bw.flush();
			br = new BufferedReader(new InputStreamReader(SigleSocketClinet.getInstance().getInputStream()));
			String result = br.readLine();
			mHandler.removeMessages(RegisterActivity.REMOVE_DIALOG);
			appLog.e(TAG + "result = " + result);
			if ("registerSuccess".equals(result)) {
				mHandler.sendEmptyMessage(RegisterActivity.REGISTER_SUCCESS);
			} else if ("usenameexist".equals(result)){
				mHandler.sendEmptyMessage(RegisterActivity.USER_EXIST);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
	}

	
}
