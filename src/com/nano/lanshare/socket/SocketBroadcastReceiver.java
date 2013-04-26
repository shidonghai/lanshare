package com.nano.lanshare.socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.nano.lanshare.conn.logic.UserManager;
import com.nano.lanshare.socket.moudle.Stranger;

public class SocketBroadcastReceiver extends BroadcastReceiver {
	private static final int NEVER_COULD_BE = -1;
	private IntentFilter mIntentFilter;
	private Activity mActivity;

	private UserManager mManager;

	public SocketBroadcastReceiver(Activity act) {
		mActivity = act;
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(SocketService.SOCKET_RESULT_ACTION);
		mManager = UserManager.getInstance();
	}

	public void register() {
		mActivity.registerReceiver(this, mIntentFilter);
	}

	public void unregister() {
		mActivity.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int result = intent.getIntExtra(SocketService.RESULT_CODE, -1);
		if (result == NEVER_COULD_BE) {
			Toast.makeText(mActivity, "never could be", Toast.LENGTH_SHORT)
					.show();
			return;
		} else {
			switch (result) {
			case SocketService.RESULT_DISCOVER: {
				Stranger stranger = (Stranger) intent
						.getSerializableExtra(SocketService.RESULT_DATA) ;
				if (mManager.addUser((Stranger) intent
						.getSerializableExtra(SocketService.RESULT_DATA))) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mActivity);
					builder.setTitle("新的用户");
					builder.setMessage("发现一个局域网用户:"+stranger.getUserIp());
					builder.create().show();
				}
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
	}
}
