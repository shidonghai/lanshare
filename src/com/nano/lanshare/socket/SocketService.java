package com.nano.lanshare.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.socket.logic.SocketController;
import com.nano.lanshare.socket.moudle.DiscoveryMessage;
import com.nano.lanshare.socket.moudle.FileTransferMessage;
import com.nano.lanshare.socket.moudle.SMessage;
import com.nano.lanshare.socket.moudle.Stranger;

public class SocketService extends Service {
	public static final String SOCKET_RESULT_ACTION = "result";
	public static final String RESULT_CODE = "result_code";
	public static final String RESULT_DATA = "result_data";
	public static final String CMD_CODE = "cmd_code";

	public static final int RESULT_DISCOVER = 200;
	public static final int RESULT_TRANSFER = 201;

	public static final int CMD_DISCOVER = 100;
	public static final int CMD_TRANSFER = 101;

	public static final int REQEUST_TRANSFER = 300;
	public static final int REQEUST_FRIEND = 301;

	public SocketController mController;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CMD_TRANSFER: {
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

				Intent intent = new Intent(SOCKET_RESULT_ACTION);
				intent.putExtra(RESULT_CODE, RESULT_DISCOVER);
				intent.putExtra(RESULT_DATA, stranger);
				sendBroadcast(intent);

				if (discoverMsg.getMsgDirection() == SMessage.REQ) {
					mController.responseForDiscover(discoverMsg);
				}

				break;
			}
			case SMessage.MSG_FILE_TRANSFER: {
				// read this message, and response for it
				FileTransferMessage transferMessage = (FileTransferMessage) msg.obj;

				// otherwise, let UserChatAcitivity to handle

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
		mHandler.sendEmptyMessage(intent.getIntExtra(CMD_CODE, 0));

		// return to ensure the service is always running.
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		mHandler.removeMessages(CMD_DISCOVER);
		super.onDestroy();
	}

}
