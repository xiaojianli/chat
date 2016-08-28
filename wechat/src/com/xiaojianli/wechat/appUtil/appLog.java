package com.xiaojianli.wechat.appUtil;

import android.util.Log;

public class appLog {
	
	public appLog() {
	}
	private static final String  TAG = "lixiaojian";
	private static boolean LOG_OPEN = true;
	
	static public void e(String info) {
		if(LOG_OPEN) {
			Log.e(TAG,info);
		}
		
		
	}
}
