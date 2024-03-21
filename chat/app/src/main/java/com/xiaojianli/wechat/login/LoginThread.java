package com.xiaojianli.wechat.login;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.xiaojianli.wechat.appUtil.SigleSocketClinet;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.login.loginmodule.LoginModule;

import android.os.Handler;

public class LoginThread extends Thread {
	
	public static final String TAG = "LoginThread.";
	
	BufferedReader br = null;
	BufferedWriter bw = null;
	
	String LoginInfo;

	LoginModule.LoginResult LL;
	public LoginThread(String LoginInfo, LoginModule.LoginResult LL) {
		super("LoginThread");
		this.LoginInfo = LoginInfo;
		this.LL = LL;
	}
	
	
	@Override
	public void run() {
		if (SigleSocketClinet.getInstance() == null) {
			LL.LoginError();
			return;
		}
		try {
			br = new BufferedReader(new InputStreamReader(SigleSocketClinet.getInstance().getInputStream(),"UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(SigleSocketClinet.getInstance().getOutputStream()));
			bw.write("#login#" + LoginInfo + "\r\n");
			bw.flush();
			String result = br.readLine();
			appLog.e(TAG + "result = " + result);
			if ("loginSuccess".equals(result)) {
				LL.LoginSuccess();
			} else {
				LL.LoginFailed();
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
