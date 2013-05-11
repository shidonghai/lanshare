package com.nano.lanshare.socket.moudle;

import org.json.JSONException;
import org.json.JSONObject;

public class FileTransferMessage extends SMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * transfer Message format, example
	 * 
	 * {"message" : 2, "data" : { "type" : 0; //0 for REQ, 1 for ACK
	 * "messageId": "001" //identify the request "name" : "user name" //nick
	 * name "macAddr" : "local MAC address" //user identifier "MimeType":
	 * "text/plain" //mime type of the file "FileLength": 1024 //file length of
	 * the file "FilePath": "/user/beijing.txt" //local file path "targetIP":
	 * "19.168.1.1" //target IP address "targetPort": 808 //target port }, "md5"
	 * : " " //md5 digest, optional, for future security requirement }
	 */
	public FileTransferMessage() {
		mMsgType = MSG_FILE_TRANSFER;
	}

	private String mName = null;
	private String mMIMEType = null;
	private long mFileLen = 0;
	private String mFilePath = null;
	private int mTargetPort;
	private String mTargetIp = null;

	public long getFileLength() {
		return mFileLen;
	}

	public void setFileLength(long length) {
		mFileLen = length;
	}

	public String getMimeType() {
		return mMIMEType;
	}

	public void setMimeType(String type) {
		mMIMEType = type;
	}

	public String getTargetIp() {
		return mTargetIp;
	}

	public void setTargetIp(String targetIp) {
		mTargetIp = targetIp;
	}

	public void setTargetPort(int port) {
		mTargetPort = port;
	}

	public int getTargetPort() {
		return mTargetPort;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public void setFilePath(String filePath) {
		mFilePath = filePath;
	}

	@Override
	public void parseData() {
		if (mData != null) {
			try {
				JSONObject object = new JSONObject(mData);

				mMsgDirection = object.optInt("type");
				mMsgId = object.optLong("messageId");
				mName = object.optString("name");
				mMacAddress = object.optString("macAddrject");
				mMIMEType = object.optString("MimeType");
				mFileLen = object.optLong("FileLength");
				mFilePath = object.optString("FilePath");
				mTargetIp = object.optString("targetIP");
				mTargetPort = object.optInt("targetPort");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String toString() {
		return "filepath:" + mFilePath;
	}

	@Override
	public void formData() {
		JSONObject dataObject = new JSONObject();

		try {
			dataObject.put("type", mMsgDirection);
			dataObject.put("messageId", mMsgId);
			dataObject.put("FileLength", mFileLen);
			dataObject.put("targetPort", mTargetPort);

			if (mName != null) {
				dataObject.put("name", mName);
			}

			if (mMIMEType != null) {
				dataObject.put("MimeType", mMIMEType);
			}

			if (mMacAddress != null) {
				dataObject.put("macAddr", mMacAddress);
			}

			if (mFilePath != null) {
				dataObject.put("FilePath", mFilePath);
			}

			if (mTargetIp != null) {
				dataObject.put("targetIP", mTargetIp);
			}

			mData = dataObject.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String toJsonString() {
		formData();
		JSONObject obj = new JSONObject();

		try {
			obj.put("message", SMessage.MSG_FILE_TRANSFER);

			if (mData != null) {
				obj.put("data", mData);
			}

			// reserve for MD5
			obj.put("md5", "");

			return obj.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
