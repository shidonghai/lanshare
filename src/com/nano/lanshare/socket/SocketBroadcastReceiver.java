package com.nano.lanshare.socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nano.lanshare.R;
import com.nano.lanshare.socket.moudle.FileTransferMessage;
import com.nano.lanshare.socket.moudle.Stranger;
import com.nano.lanshare.utils.FileSizeUtil;

public class SocketBroadcastReceiver extends BroadcastReceiver {
	private static final int NEVER_COULD_BE = -1;
	protected static final int DISMISS_DIALOG = 0;
	private IntentFilter mIntentFilter;
	private Activity mActivity;
	private ProgressDialog mDialog;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DISMISS_DIALOG: {
				Dialog dialog = (Dialog) msg.obj;
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
			}
			}
		}
	};

	public SocketBroadcastReceiver(Activity act) {
		mActivity = act;
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(SocketService.SOCKET_RESULT_ACTION);
		mIntentFilter.addAction(SocketService.SOCKET_TRANSFER_ACTION);
		mIntentFilter.addAction(SocketService.SOCKET_TRANSFER_REQUEST);
		mDialog = new ProgressDialog(mActivity);
		mDialog.setMessage("传输中");
	}

	public void register() {
		mActivity.registerReceiver(this, mIntentFilter);
	}

	public void unregister() {
		mActivity.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SocketService.SOCKET_TRANSFER_REQUEST)) {
			FileTransferMessage transferMessage = (FileTransferMessage) intent
					.getSerializableExtra(SocketService.SOCKET_TRANSFER_MSG);
			// send a notification to the user
			Intent notificationIntent = new Intent(mActivity,
					SocketService.class);
			notificationIntent.putExtra(SocketService.CMD_CODE,
					SocketService.TRANSFER_RESPONSE_ACCEPT);
			notificationIntent.putExtra(SocketService.SOCKET_TRANSFER_MSG,
					transferMessage);
			PendingIntent contentIntent = PendingIntent.getService(mActivity,
					0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			Notification notification = new Notification(R.drawable.avatar1,
					"一个文件传输请求", System.currentTimeMillis());
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(context, "文件发送请求",
					"有一个用户给你发送了一个文件，点击开始接收", contentIntent);
			NotificationManager mNotificationManager = (NotificationManager) mActivity
					.getSystemService(Activity.NOTIFICATION_SERVICE);
			mNotificationManager.notify(SocketService.SOCKET_TRANSFER_REQUEST,
					0, notification);
		} else if (intent.getAction()
				.equals(SocketService.SOCKET_RESULT_ACTION)) {
			int result = intent.getIntExtra(SocketService.RESULT_CODE, -1);

			if (result == NEVER_COULD_BE) {
				Toast.makeText(mActivity, "never could be", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				switch (result) {
				case SocketService.RESULT_DISCOVER: {
					Stranger stranger = (Stranger) intent
							.getSerializableExtra(SocketService.RESULT_DATA);

					AlertDialog.Builder builder = new AlertDialog.Builder(
							mActivity);
					builder.setTitle("新的用户");
					builder.setMessage("发现一个局域网用户:" + stranger.getUserIp());
					Dialog dialog = builder.create();
					dialog.show();

					// dismiss the dialog after 2s
					Message msg = mHandler.obtainMessage();
					msg.obj = dialog;
					msg.what = DISMISS_DIALOG;
					mHandler.sendMessageDelayed(msg, 2000);
					break;
				}
				case SocketService.RESULT_TRANSFER: {
					break;
				}
				case SocketService.REQEUST_TRANSFER: {
					break;
				}
				case SocketService.REQEUST_FRIEND: {
					break;
				}
				}
			}
		} else if (intent.getAction().equals(
				SocketService.SOCKET_TRANSFER_ACTION)) {
			int status = intent.getIntExtra(SocketService.TRANSFER_STATUS, -1);
			switch (status) {
			case SocketService.TRANSFER_STARTED: {
				int end = intent.getIntExtra(SocketService.TRANSFER_END, 0);
				if (end == SocketService.TRANSFER_IN) {
					mDialog.setMessage("接收中...\n"
							+ "0kb/"
							+ FileSizeUtil.formatFromByte(Long.parseLong(intent
									.getStringExtra(SocketService.FILE_LENGTH))));
				} else {
					mDialog.setMessage("发送中...\n"
							+ "0kb/"
							+ FileSizeUtil.formatFromByte(Long.parseLong(intent
									.getStringExtra(SocketService.FILE_LENGTH))));
				}
				mDialog.show();
				break;
			}
			case SocketService.TRANSFER_FINISHED: {
				mDialog.dismiss();
				break;
			}
			case SocketService.TRANSFER_PROGRESS: {
				if (mDialog.isShowing()) {
					int end = intent.getIntExtra(SocketService.TRANSFER_END, 0);
					if (end == SocketService.TRANSFER_IN) {
						mDialog.setMessage("接收中...\n"
								+ FileSizeUtil.formatFromByte(intent
										.getIntExtra(SocketService.SENT_LENGTH,
												0))
								+ "/"
								+ FileSizeUtil.formatFromByte(Long.parseLong(intent
										.getStringExtra(SocketService.FILE_LENGTH))));
					} else {
						mDialog.setMessage("发送中...\n"
								+ FileSizeUtil.formatFromByte(intent
										.getIntExtra(SocketService.SENT_LENGTH,
												0))
								+ "/"
								+ FileSizeUtil.formatFromByte(Long.parseLong(intent
										.getStringExtra(SocketService.FILE_LENGTH))));
					}
				}
			}
			}
		}
	}
}
