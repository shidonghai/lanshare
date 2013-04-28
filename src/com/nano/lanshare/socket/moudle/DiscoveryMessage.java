package com.nano.lanshare.socket.moudle;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class DiscoveryMessage extends SMessage {
	/*
	 * discovery Message format, example
	 * 
	 * {"message" : 1, "data" : { "type" : 0; //0 for REQ, 1 for ACK "name" :
	 * "user name" //nick name "photo" : "base64 data" //photo thumb nail
	 * "macAddr" : "local MAC address" //user identifier }, "md5" : " " //md5
	 * digest, optional, for future security requirement }
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] mPhoto;

	public void setphot(byte[] photo) {
		mPhoto = photo;
	}

	public byte[] getPhoto() {
		return mPhoto;
	}

	public DiscoveryMessage() {
		mMsgType = MSG_DISCOVER;
	}

	@Override
	public void parseData() {
		if (mData != null) {
			try {
				JSONObject object = new JSONObject(mData);

				mMsgDirection = object.optInt("type");
				mName = object.optString("name");
				mMacAddress = object.optString("macAddr");

				String photo = object.optString("photo");
				if (photo.trim().length() != 0) {
					mPhoto = Base64.decode(photo, Base64.DEFAULT);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void formData() {
		JSONObject dataObject = new JSONObject();

		try {
			if (mMacAddress != null) {
				dataObject.put("macAddr", mMacAddress);
			}

			if (mPhoto != null) {
				String photo = Base64.encodeToString(mPhoto, Base64.DEFAULT);
				dataObject.put("photo", photo);
			}

			if (mName != null) {
				dataObject.put("name", mName);
			}

			dataObject.put("type", mMsgDirection);

			mData = dataObject.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String toJsonString() {
		JSONObject obj = new JSONObject();

		try {
			// reserve for MD5
			obj.put("md5", "");

			formData();

			if (mData != null) {
				obj.put("data", mData);
			}

			obj.put("message", SMessage.MSG_DISCOVER);

			return obj.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
