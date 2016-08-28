package com.xiaojianli.wechat.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.xiaojianli.wechat.appUtil.SigleSocketClinet;
import com.xiaojianli.wechat.appUtil.appLog;

public class backThread extends Thread {
	
	private static backThread instance = null;
	private static final String TAG = "backThread.";
	private static boolean flag = true;
	
	List<IServerMessageGet> LisenterList = new ArrayList<backThread.IServerMessageGet>();
	
	public static backThread getInstance() {
		if (instance == null) {
			instance = new backThread();
			flag = true;
			instance.start();
		}
		return instance;
	}
	
	@SuppressWarnings("static-access")
	public void setFlag(boolean flag) {
		this.flag = flag;
		instance = null;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
					
			BufferedReader br = null;	
			
			try {
				while(flag) {
					if (SigleSocketClinet.getInstance() == null) { flag = false; break; }

					br = new BufferedReader(new InputStreamReader(SigleSocketClinet.getInstance()
							.getInputStream()));
					String serverResult = br.readLine();
					appLog.e(TAG + " = " + serverResult);
					if (serverResult == null) {
						setFlag(false);
						break;
					}
					for (IServerMessageGet ism : LisenterList) {
						ism.MessageGet(serverResult);
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					appLog.e(TAG + "finally ");
					if (br != null) br.close();
					if (SigleSocketClinet.getInstance() != null) SigleSocketClinet.getInstance().closeSocket();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	public interface IServerMessageGet {
		public void MessageGet(String message);
	}
	
	public void addLisenter(IServerMessageGet ISM) {
		if (LisenterList != null) {
			LisenterList.add(ISM);
		}
	}
	
	public void removeLisenter(IServerMessageGet ism) {
		if (LisenterList != null) {
			LisenterList.remove(ism);
		}
	}

}
