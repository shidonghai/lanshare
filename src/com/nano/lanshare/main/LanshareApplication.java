package com.nano.lanshare.main;

import android.app.Application;

import com.nano.lanshare.socket.logic.SocketController;
import com.nano.lanshare.file.Extentions;
import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.utils.FileUtil;

public class LanshareApplication extends Application {

	private ImageWorker mImageWorker;

	private SocketController mController;

	@Override
	public void onCreate() {
		super.onCreate();
		mImageWorker = new ImageWorker(this);
		mController = new SocketController(this);
		new Thread() {
			public void run() {
				FileUtil.createInbox(getApplicationContext());
			};
		}.start();

		Extentions.getIntance(this).init();
	}

	public ImageWorker getImageWorker() {
		return mImageWorker;
	}

	@Override
	public void onTerminate() {
		mController.destory();
		super.onTerminate();
	}

	public SocketController getSocketController() {
		return mController;
	}

}
