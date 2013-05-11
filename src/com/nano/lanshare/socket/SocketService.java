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
	protected static final String TAG = "socket service";

	public static final String SOCKET_TRANSFER_REQUEST = "transfer_request";
	public static final String SOCKET_TRANSFER_ACTION = "transfer";
	public static final String SOCKET_TRANSFER_MSG = "transfer_message";
	public static final String SOCKET_RESULT_ACTION = "result";
	public static final String RESULT_CODE = "result_code";
	public static final String RESULT_DATA = "result_data";
	public static final String CMD_CODE = "cmd_code";
	public static final String DATA = "data";
	public static final String TARGET_USER = "target_user";
	public static final String TRANSFER_END = "transfer_end";
	public static final String TRANSFER_STATUS = "transfer_status";
	public static final String FILE_LENGTH = "file_length";
	public static final String SENT_LENGTH = "sent_length";

	public static final int RESULT_DISCOVER = 200;
	public static final int RESULT_TRANSFER = 201;

	public static final int CMD_DISCOVER = 100;
	public static final int CMD_TRANSFER = 101;

	public static final int REQEUST_TRANSFER = 300;
	public static final int REQEUST_FRIEND = 301;

	public static final int TRANSFER_STARTED = 400;
	public static final int TRANSFER_FINISHED = 401;
	public static final int TRANSFER_PROGRESS = 402;
	public static final int TRANSFER_RESPONSE_REFUSED = 403;
	public static final int TRANSFER_RESPONSE_ACCEPT = 404;

	public static final int TRANSFER_OUT = 501;
	public static final int TRANSFER_IN = 500;

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
				Log.e(TAG, "request to transfer a file");
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
				FileTransferMessage transferMessage = (FileTransferMessage) msg.obj;
				if (transferMessage.getMsgDirection() == SMessage.REQ) {
					Log.e(TAG, "respond for file transfer request");
					Intent intent = new Intent(SOCKET_TRANSFER_REQUEST);
					intent.putExtra(SOCKET_TRANSFER_MSG, transferMessage);
					sendBroadcast(intent);
				} else {
					Log.e(TAG, "get response and start to transfer file");
					mController.startToSendFile(transferMessage);
				}
				break;
			}
			case TRANSFER_RESPONSE_REFUSED: {
				FileTransferMessage transferMessage = (FileTransferMessage) msg.obj;
				mController.responseForTransfer(transferMessage);
				break;
			}
			case TRANSFER_RESPONSE_ACCEPT: {
				FileTransferMessage transferMessage = (FileTransferMessage) msg.obj;
				mController.responseForTransfer(transferMessage);
				break;
			}
			case TRANSFER_STARTED: {
				Intent intent = new Intent(SOCKET_TRANSFER_ACTION);
				intent.putExtra(TRANSFER_END, msg.arg1);
				intent.putExtra(FILE_LENGTH, ((Long) msg.obj).toString());
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
			case TRANSFER_PROGRESS: {
				Intent intent = new Intent(SOCKET_TRANSFER_ACTION);
				intent.putExtra(TRANSFER_END, msg.arg1);
				intent.putExtra(TRANSFER_STATUS, TRANSFER_PROGRESS);
				intent.putExtra(FILE_LENGTH, ((Long) msg.obj).toString());
				intent.putExtra(SENT_LENGTH, msg.arg2);
				sendBroadcast(intent);
				break;
			}
			case SMessage.MSG_STATUS_UPDATE: {
				break;
			}
			default:
				break;
			}
		}

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
		case TRANSFER_RESPONSE_ACCEPT: {
			Message msg = mHandler.obtainMessage();
			msg.obj = intent.getSerializableExtra(SOCKET_TRANSFER_MSG);
			msg.what = TRANSFER_RESPONSE_ACCEPT;
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
