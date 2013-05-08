package com.nano.lanshare.socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.nano.lanshare.socket.moudle.Stranger;

public class SocketBroadcastReceiver extends BroadcastReceiver {
	private static final int NEVER_COULD_BE = -1;
	private IntentFilter mIntentFilter;
	private Activity mActivity;
	private ProgressDialog mDialog;

	public SocketBroadcastReceiver(Activity act) {
		mActivity = act;
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(SocketService.SOCKET_RESULT_ACTION);
		mIntentFilter.addAction(SocketService.SOCKET_TRANSFER_ACTION);
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
		if (intent.getAction().equals(SocketService.SOCKET_RESULT_ACTION)) {
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
					builder.create().show();
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
			case SocketService.TRANSFER_STARTED:
				int end = intent.getIntExtra(SocketService.TRANSFER_END, 0);
				if (end == SocketService.TRANSFER_IN) {
					mDialog.setMessage("接收中...");
				} else {
					mDialog.setMessage("发送中...");
				}
				mDialog.show();
				break;
			case SocketService.TRANSFER_FINISHED:
				mDialog.dismiss();
				break;

			}
		}
	}
}
