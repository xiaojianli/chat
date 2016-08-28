package com.xiaojianli.wechat.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaojianli.wechat.R;

public class ToastShow {
	

	public static ToastShow instance = new ToastShow();
	
	Toast mToast;
	LayoutInflater mInflater;
	private ToastShow() {
		
	}
	
	public static ToastShow getInstance() {
		if (instance == null) {
			instance = new ToastShow();
		}
		return instance;
	}
	
	public void showToast(Context context,String text) {
		 mToast = new Toast(context);
		 View v = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null);
		 TextView tv = (TextView) v.findViewById(R.id.mTextView);
		 tv.setText(text);
		 mToast.setView(v);
		 mToast.setDuration(Toast.LENGTH_SHORT);
		 mToast.show();	
	}

}
