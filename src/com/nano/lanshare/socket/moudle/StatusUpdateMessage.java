package com.nano.lanshare.socket.moudle;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusUpdateMessage extends SMessage {
	/*
	 * user status update Message format, example
	 * 
	 * {"message" : 4, "data" : { "type" : 0, for REQ(0)/ACK(1) "name" :
	 * "user name" //nick name "macAddr" : "local MAC address" //user identifier
	 * "userStatus": 0 //user status like offline/online/hide }, "md5" : " "
	 * //md5 digest, optional, for future security requirement }
	 */

	private int mStatus = 0;
	private String mName;

	public void setStatus(int status) {
		mStatus = status;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public int getStatus() {
		return mStatus;
	}

	@Override
	public void parseData() {
		if (mData != null) {
			try {
				JSONObject object = new JSONObject(mData);

				mMacAddress = object.optString("macAddr");
				mMsgDirection = object.optInt("type");
				mName = object.optString("name");
				mMsgDirection = object.optInt("type");
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
			dataObject.put("userStatus", mStatus);

			if (mMacAddress != null) {
				dataObject.put("macAddr", mMacAddress);
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

			obj.put("message", SMessage.MSG_STATUS_UPDATE);

			return obj.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
