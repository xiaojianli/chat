package com.xiaojianli.wechat.database;

import com.xiaojianli.wechat.appUtil.appLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	public static String userName = "";
	private static final String TAG = "DataBaseHelper.";
	public static String DATABASE_NAME = "wei_yu.db";
	public static String TABLE_FRIEND = "";
	public static String TABLE_CHATLIST = "";
	private static final int VERSION = 1;

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	public DataBaseHelper(Context context, int version) {
		
		super(context, DATABASE_NAME, null, version);
		// TODO Auto-generated constructor stub
	}
	
	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		appLog.e(TAG + "onCreate");

		String createFriendTable = "create table "+ TABLE_FRIEND +"("
				+ "id INTEGER primary key AUTOINCREMENT,"
				+ "name text NOT NULL,"
				+ "realname text,"
				+ "sex text,"
				+ "addtime TIMESTAMP default (datetime('now', 'localtime')));";
		db.execSQL(createFriendTable);
		String createChatListTable = "create table " + TABLE_CHATLIST + "("
				+ "id INTEGER primary key AUTOINCREMENT,"
				+ "name text NOT NULL,"
				+ "realname text,"
				+ "chatcontent text,"
				+ "isNewMessage INTEGER default 0,"
				+ "chattime TIMESTAMP default (datetime('now','localtime')));";
		db.execSQL(createChatListTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		appLog.e(TAG + "onUpgrade");
	}
	
	public void insert(String tableName,ContentValues cv) {
		appLog.e(TAG + "insert " + tableName);
		SQLiteDatabase sd = this.getWritableDatabase();
		long result = sd.insert(tableName, null, cv);
		appLog.e(TAG + "RESULT = " + result);
		
	}
	
	public void update(String tableName, ContentValues cv, int id) {
		appLog.e(TAG + "update " + tableName + "id = " + id);
		SQLiteDatabase sq = this.getWritableDatabase();
		sq.update(tableName, cv, "id=" + id, null);
	}

	
}
