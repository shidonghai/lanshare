package com.nano.lanshare.socket.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nano.lanshare.socket.connection.ConnectionManager;
import com.nano.lanshare.socket.connection.ConnectionUtils;
import com.nano.lanshare.socket.moudle.DiscoveryMessage;
import com.nano.lanshare.socket.moudle.FileTransferMessage;
import com.nano.lanshare.socket.moudle.MessageFactory;
import com.nano.lanshare.socket.moudle.SMessage;
import com.nano.lanshare.socket.moudle.StatusUpdateMessage;
import com.nano.lanshare.socket.moudle.User;
import com.nano.lanshare.socket.net.SocketUtils;
import com.nano.lanshare.socket.net.TCPSocketServer;

public class SocketController {
	private ArrayList<User> mUserList = new ArrayList<User>();
	private MessageCenter mMsgCenter;
	private ConnectionManager mConnMgr;
	private Handler mHandler;
	private Context mContext;

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public SocketController(Context context) {
		mContext = context;
		mMsgCenter = MessageCenter.getInstance();
		mConnMgr = ConnectionManager.getInstance();

		// register to listen incoming message
		MessageListener discoverListener = new MessageListener() {
			@Override
			public void onEvent(int type, SMessage msg) {
				Log.w("ShareApp", "message comes: " + type);
				Log.w("ShareApp", "Message : " + msg.toJsonString());

				android.os.Message message = new android.os.Message();
				message.what = SMessage.MSG_DISCOVER;
				message.obj = msg;
				if (mHandler != null)
					mHandler.sendMessage(message);
			}
		};

		discoverListener.addFilterType(MessageListener.MSG_USER_DISCOVER_ACK
				| MessageListener.MSG_USER_DISCOVER_REQ);
		mMsgCenter.addMessageListener(discoverListener);

		// register to listening File transfer Message
		MessageListener transferListener = new MessageListener() {
			@Override
			public void onEvent(int type, SMessage msg) {
				Log.w("ShareApp", "message comes: " + type);
				Log.w("ShareApp", "Message : " + msg.toJsonString());

				android.os.Message message = new android.os.Message();
				message.what = SMessage.MSG_FILE_TRANSFER;
				message.obj = msg;
				if (mHandler != null)
					mHandler.sendMessage(message);
			}
		};

		transferListener.addFilterType(MessageListener.MSG_TRANSFER_REQUEST
				| MessageListener.MSG_TRANSFER_REQUEST);
		mMsgCenter.addMessageListener(transferListener);

		// register to listen user status update message
		MessageListener statusListener = new MessageListener() {
			@Override
			public void onEvent(int type, SMessage msg) {
				Log.w("ShareApp", "message comes: " + type);
				Log.w("ShareApp", "Message : " + msg.toJsonString());

				Message message = new Message();
				message.what = SMessage.MSG_STATUS_UPDATE;
				message.obj = msg;
				if (mHandler != null) {
					mHandler.sendMessage(message);
				}
			}
		};

		statusListener.addFilterType(MessageListener.MSG_USER_STATUS_UPDATE);
		mMsgCenter.addMessageListener(statusListener);

	}

	public void discover(long reqId) {
		// compose a discovery message
		DiscoveryMessage msg = (DiscoveryMessage) MessageFactory
				.createMessage(SMessage.MSG_DISCOVER);

		// this is a request message
		msg.setMsgDirection(SMessage.REQ);

		// NOTE: do not use broadcast address like "192.168.1.255", some AP
		// disabled the broadcast features for security sake, instead, we scan
		// all the machine in the LAN at port SIGNAL_PORT

		int ip = ConnectionUtils.getLocalIp(mContext);
		int mask = ConnectionUtils.getLocalNetmask(mContext);

		int subDeviceNum = ((mask ^ 0xFFFFFFFF));
		int subNetIp = ip & mask;

		String ipAddr = null;// ConnectionUtils.int2Ip(subNetIp | subDeviceNum);

		Log.w("ClientActivity",
				"subNetIp = " + ConnectionUtils.int2Ip(subNetIp));
		Log.w("ClientActivity",
				"broadcastIp = "
						+ ConnectionUtils.int2Ip(subNetIp | subDeviceNum));

		for (int i = 1; i < subDeviceNum; i++) {
			// don't need to discovery myself
			if (ip == subNetIp + i) {
				continue;
			}

			ipAddr = ConnectionUtils.int2Ip(subNetIp + i);

			msg.setMessageID(reqId);

			// set my local MAC address
			msg.setMACAddress(ConnectionUtils.getLocalMacAddress(mContext));

			// set my Name and photo
			msg.setName(Build.MODEL);

			// set recipient address, port
			msg.setRemotePort(ConnectionManager.SIGNAL_PORT);
			msg.setRemoteAdress(ipAddr);

			// Log.w("ShareApp", "send Message : " + msg.toJsonString() + " to "
			// + ipAddr);

			try {
				mConnMgr.sendMessage(1000, ipAddr,
						msg.toJsonString().getBytes("utf8"));

				// add 15 tick sleep to avoid socket buffer over follow
				Thread.sleep(2);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateMyStatus(int userStatus) {

		StatusUpdateMessage msg = (StatusUpdateMessage) MessageFactory
				.createMessage(SMessage.MSG_STATUS_UPDATE);

		msg.setStatus(userStatus);
		msg.setRemotePort(ConnectionManager.SIGNAL_PORT);

		// set my local MAC address
		msg.setMACAddress(ConnectionUtils.getLocalMacAddress(mContext));

		// set my Name
		msg.setName(Build.MODEL);

		for (User user : mUserList) {
			msg.setRemoteAdress(user.getUserIp());

			try {
				mConnMgr.sendMessage(1000, user.getUserIp(), msg.toJsonString()
						.getBytes("utf8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

	public void responseForTransfer(FileTransferMessage message) {

		// find a free TCP port to for file transferring
		int port = SocketUtils.getFreeTCPPort();

		// compose ACK message
		FileTransferMessage msg = (FileTransferMessage) MessageFactory
				.createMessage(SMessage.MSG_FILE_TRANSFER);

		msg.setMsgDirection(SMessage.ACK);
		msg.setFilePath(msg.getFilePath());
		msg.setMACAddress(ConnectionUtils.getLocalMacAddress(mContext));
		msg.setMessageID(message.getMessageID());
		msg.setRemoteAdress(message.getRemoteAddress());
		msg.setRemotePort(message.getRemotePort());

		// target IP to receive the file
		msg.setTargetIp(ConnectionUtils.int2Ip(ConnectionUtils
				.getLocalIp(mContext)));
		// target port to receive the file
		msg.setTargetPort(port);

		final String fileName = msg.getFilePath().substring(
				msg.getFilePath().lastIndexOf("/"));

		// start TCP server to wait file transfer, 80s timeout
		try {
			TCPSocketServer fileServer = new TCPSocketServer(port);
			fileServer.setTimeout(30000);
			fileServer.setCallBack(new TCPSocketServer.ServerCallback() {

				@Override
				public void onResponse(InputStream inStream) {
					// save the file to
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {

						File sdCardDir = Environment
								.getExternalStorageDirectory();
						File saveFile = new File(sdCardDir, fileName);
						FileOutputStream outStream = null;

						try {
							outStream = new FileOutputStream(saveFile);

							int b = 0;
							while ((b = inStream.read()) != -1) {
								outStream.write((char) b);
							}

							inStream.close();
							outStream.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			fileServer.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void responseForDiscover(SMessage message) {
		// compose a discovery message
		DiscoveryMessage msg = (DiscoveryMessage) MessageFactory
				.createMessage(SMessage.MSG_DISCOVER);

		msg.setMsgDirection(SMessage.ACK);
		msg.setMessageID(message.getMessageID());

		// set my local MAC address
		msg.setMACAddress(ConnectionUtils.getLocalMacAddress(mContext));

		// set my Name and photo
		msg.setName(Build.MODEL);

		// set recipient address, port
		msg.setRemotePort(ConnectionManager.SIGNAL_PORT);
		msg.setRemoteAdress(message.getRemoteAddress());

		try {
			mConnMgr.sendMessage(msg.getMessageID(), msg.getRemoteAddress(),
					msg.toJsonString().getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public void startToSendFile(FileTransferMessage message) {
		mConnMgr.StartFileTransfer(message.getFilePath(),
				message.getRemoteAddress(), message.getRemotePort(),
				message.getMessageID());
	}

	public void destory() {
		mMsgCenter.destory();
		mMsgCenter = null;
		mConnMgr.release();
		mConnMgr = null;
	}
}
