package com.nano.lanshare.socket;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.nano.lanshare.conn.logic.UserManager;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.socket.connection.ConnectionUtils;
import com.nano.lanshare.socket.logic.SocketController;
import com.nano.lanshare.socket.moudle.DiscoveryMessage;
import com.nano.lanshare.socket.moudle.FileTransferMessage;
import com.nano.lanshare.socket.moudle.MessageFactory;
import com.nano.lanshare.socket.moudle.SMessage;
import com.nano.lanshare.socket.moudle.Stranger;

public class SocketService extends Service {
	public static final String SOCKET_TRANSFER_ACTION = "transfer";
	public static final String SOCKET_RESULT_ACTION = "result";
	public static final String RESULT_CODE = "result_code";
	public static final String RESULT_DATA = "result_data";
	public static final String CMD_CODE = "cmd_code";
	public static final String DATA = "data";
	public static final String TARGET_USER = "target_user";

	public static final String TRANSFER_STATUS = "transfer_status";

	public static final int RESULT_DISCOVER = 200;
	public static final int RESULT_TRANSFER = 201;

	public static final int CMD_DISCOVER = 100;
	public static final int CMD_TRANSFER = 101;

	public static final int REQEUST_TRANSFER = 300;
	public static final int REQEUST_FRIEND = 301;

	public static final int TRANSFER_STARTED = 400;
	public static final int TRANSFER_FINISHED = 401;

	public SocketController mController;
	private UserManager mManager;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CMD_TRANSFER: {
				Intent intent = (Intent) msg.obj;
				String path = intent.getStringExtra(DATA);
				Stranger target = (Stranger) intent
						.getSerializableExtra(TARGET_USER);
				FileTransferMessage message = (FileTransferMessage) MessageFactory
						.createMessage(SMessage.MSG_FILE_TRANSFER);
				message.setFilePath(path);
				File file = new File(path);
				message.setFileLength(file.length());
				message.setTargetIp(target.getUserIp());
				message.setTargetPort(808);
				message.setRemoteAdress(ConnectionUtils.int2Ip(ConnectionUtils
						.getLocalIp(getApplicationContext())));
				message.setRemotePort(808);
				message.setMsgDirection(SMessage.REQ);
				Log.e("service", "request trasfer");
				Log.e("my ip",
						message.getRemoteAddress() + ",port:"
								+ message.getRemotePort());
				Log.e("target ip",
						message.getTargetIp() + ",port:"
								+ message.getTargetPort());
				mController.requestFileTransfer(message);
				break;
			}
			case CMD_DISCOVER: {
				mController.discover(1000);
				// send a discovery request every 5s
				mHandler.sendEmptyMessageDelayed(msg.what, 5000);
				break;
			}
			case SMessage.MSG_DISCOVER: {
				DiscoveryMessage discoverMsg = (DiscoveryMessage) msg.obj;

				// add this user to the user list
				Stranger stranger = new Stranger();
				stranger.setName(discoverMsg.getName());
				stranger.setUserIdentifier(discoverMsg.getMACAddress());
				stranger.setUserIp(discoverMsg.getRemoteAddress());
				stranger.setUserPhoto(discoverMsg.getPhoto());
				if (mManager.addUser(stranger)) {
					Intent intent = new Intent(SOCKET_RESULT_ACTION);
					intent.putExtra(RESULT_CODE, RESULT_DISCOVER);
					intent.putExtra(RESULT_DATA, stranger);
					sendBroadcast(intent);
				}
				if (discoverMsg.getMsgDirection() == SMessage.REQ) {
					mController.responseForDiscover(discoverMsg);
				}

				break;
			}
			case SMessage.MSG_FILE_TRANSFER: {
				// read this message, and response for it
				FileTransferMessage transferMessage = (FileTransferMessage) msg.obj;
				if (transferMessage.getMsgDirection() == SMessage.REQ) {
					mController.responseForTransfer(transferMessage);
				} else {
					Log.e("service", "start transfer");
					mController.startToSendFile(transferMessage);
				}
				// otherwise, let UserChatAcitivity to handle

				break;
			}
			case TRANSFER_STARTED: {
				Intent intent = new Intent(SOCKET_TRANSFER_ACTION);
				intent.putExtra(TRANSFER_STATUS, TRANSFER_STARTED);
				sendBroadcast(intent);
				break;
			}
			case TRANSFER_FINISHED: {
				Intent intent = new Intent(SOCKET_TRANSFER_ACTION);
				intent.putExtra(TRANSFER_STATUS, TRANSFER_FINISHED);
				sendBroadcast(intent);
				break;
			}
			case SMessage.MSG_STATUS_UPDATE: {
				break;
			}
			default:
				break;
			}
		};
	};

	public SocketService() {
		LanshareApplication app = (LanshareApplication) getApplication();
		mManager = UserManager.getInstance();
		mController = new SocketController(this);
		mController.setHandler(mHandler);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
		switch (intent.getIntExtra(CMD_CODE, 0)) {
		case CMD_DISCOVER: {
			mHandler.sendEmptyMessage(intent.getIntExtra(CMD_CODE, 0));
			break;
		}
		case CMD_TRANSFER: {
			Message msg = mHandler.obtainMessage();
			msg.obj = intent;
			msg.what = intent.getIntExtra(CMD_CODE, 0);
			msg.sendToTarget();

			break;
		}
		}

		// return to ensure the service is always running.
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		mHandler.removeMessages(CMD_DISCOVER);
		super.onDestroy();
	}

}
