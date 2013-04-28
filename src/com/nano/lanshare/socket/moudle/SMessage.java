package com.nano.lanshare.socket.moudle;

import java.io.Serializable;

import android.util.Log;

public abstract class SMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MSG_DISCOVER = 1;
	public static final int MSG_FILE_TRANSFER = 2;
	public static final int MSG_HEART_BEAT = 3;
	public static final int MSG_STATUS_UPDATE = 4;

	public static final int REQ = 0;
	public static final int ACK = 1;

	protected int mMsgType;
	protected int mMsgDirection = REQ;
	protected long mMsgId = 0;
	protected String mName = null;

	// data stored in JSON string
	protected String mData = null;

	// address and port of remote machine
	protected String mRemoteAddress = null;
	protected int mRemotePort = 0;

	// sender's MAC Address
	protected String mMacAddress = null;

	public void setMACAddress(String addr) {
		mMacAddress = addr;
	}

	public String getMACAddress() {
		return mMacAddress;
	}

	public void setMessageID(long id) {
		mMsgId = id;
	}

	public long getMessageID() {
		return mMsgId;
	}

	public void setRemoteAdress(String IP) {
		mRemoteAddress = IP;
	}

	public void setRemotePort(int port) {
		mRemotePort = port;
	}

	public String getRemoteAddress() {
		return mRemoteAddress;
	}

	public int getRemotePort() {
		return mRemotePort;
	}

	public int getMsgDirection() {
		return mMsgDirection;
	}

	public void setMsgDirection(int direction) {
		mMsgDirection = direction;
	}

	public int getMessageType() {
		return mMsgType;
	}

	public String getData() {
		return mData;
	}

	public void setData(String jsonData) {
		mData = jsonData;
	}

	public void dump() {
		Log.w("ShareApp_dump", toJsonString());
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	// parse data to properties
	public abstract void parseData();

	// form all the data properties to a JSON string
	public abstract void formData();

	// convert to a JSON string
	public abstract String toJsonString();
}
