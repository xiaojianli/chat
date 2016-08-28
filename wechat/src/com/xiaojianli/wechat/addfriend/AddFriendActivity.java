package com.xiaojianli.wechat.addfriend;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.xiaojianli.wechat.R;
import com.xiaojianli.wechat.appUtil.appLog;
import com.xiaojianli.wechat.chat.ui.searchFriendAdapter;
import com.xiaojianli.wechat.database.DataBaseHelper;
import com.xiaojianli.wechat.chat.backThread;

public class AddFriendActivity extends Activity implements backThread.IServerMessageGet{
	
	private final static String TAG = "AddFriendActivity.";
	
	public  final static int CHECK_RESULT = 0;
	private Handler mHandler = new MainHandler();

	Button addButton;
	EditText searchEditText;
	ImageButton searchImageButton;
	ListView searchResultListView;
	DataBaseHelper db;
	searchFriendAdapter searchAdapter;
	List<addFriendImp> mList;
	
	boolean pause = true;
	public int id = 0;
	
	private class MainHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			appLog.e(TAG + "handlerMessage msg = " + msg.what);
			switch (msg.what) {
			case CHECK_RESULT:
				appLog.e(TAG + "obj = " + msg.obj + " id = " + id);
				String result = (String)msg.obj;
				if (result == null) break;
				int index = result.indexOf("#");
				String AllResult = result.substring(index + 14, result.length());
				appLog.e(TAG + AllResult + ":" + AllResult.length());
				if (AllResult != null && AllResult.length() > 0) {
					String[] resultCount = AllResult.split(";");
					mList.clear();
					for (int i = 0;i<resultCount.length;i++){
						appLog.e(TAG + resultCount[i]);
						String[] info = resultCount[i].split(":");
						if(info.length < 3) break;
						addFriendImp add = new addFriendImp();
						add.setUsername(info[0]);
						add.setRealname(info[1]);
						add.setSexual(info[2]);
						mList.add(add);		
					}
					searchAdapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		}
		
	}
		
	public Object idObject = new Object();
	private TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if (searchEditText.getText().toString().trim().length() <= 0) return;
			id++;
			String searchString = searchEditText.getText().toString().trim();
			appLog.e(TAG + "onTextChanged searchString = " + searchString);
//			new SearchThread(id,searchString,mHandler).start();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			appLog.e(TAG + "beforeTextChanged ");

		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			appLog.e(TAG + "afterTextChanged ");
		}
	};
	
	private void initView() {
		searchResultListView = (ListView) findViewById(R.id.searchResultList);
		searchEditText = (EditText) findViewById(R.id.searchEditText);
		searchEditText.addTextChangedListener(mTextWatcher);
		searchEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(50)});
		searchImageButton = (ImageButton) findViewById(R.id.searchButton);
		searchImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (searchEditText.getText().toString().trim().length() <= 0) return;
				id++;
				String searchInfo = searchEditText.getText().toString().trim();
				new SearchThread(id, searchInfo, mHandler).start();
			}
		});
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DataBaseHelper(AddFriendActivity.this);
		setContentView(R.layout.add_friend);
		initView();
		mList = new ArrayList<addFriendImp>();
		mList.clear();
		searchAdapter = new searchFriendAdapter(mList,this,db);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pause = false;
		searchResultListView.setAdapter(searchAdapter);		
		backThread.getInstance().addLisenter(this);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		pause = true;
		id = 0;
		mList.clear();
		backThread.getInstance().removeLisenter(this);
	}
	@Override
	public void MessageGet(String message) {
		// TODO Auto-generated method stub
		if (pause) return;
		if (message == null) return;
		Message msg = new Message();
		msg.what = AddFriendActivity.CHECK_RESULT;
		msg.obj = id +":"+ message;
		mHandler.sendMessage(msg);
	}
}
