package com.xiaojianli.wechat.chat;

public class ChatContentImp {
	
	private String content;
	private String sendTime;
	private int Issend;
	private String userID;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public int getIssend() {
		return Issend;
	}
	public void setIssend(int issend) {
		Issend = issend;
	}

	
}
