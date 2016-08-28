package com.xiaojianli.wechat.addfriend;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.xiaojianli.wechat.appUtil.SigleSocketClinet;

import android.os.Handler;

public class SearchThread extends Thread {

	int flag;
	private String searchInfo;
	SearchThread(int flag,String searchInfo,Handler m) {
		super("searchThread");
		this.flag = flag;
		this.searchInfo = searchInfo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (SigleSocketClinet.getInstance() == null) return;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(SigleSocketClinet.getInstance().getOutputStream(),"UTF-8"));
			bw.write("#addfriend#" + searchInfo + "\r\n");
			bw.flush();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
//			try {
//				if (br != null) br.close();
//				if (bw != null) bw.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}
}
