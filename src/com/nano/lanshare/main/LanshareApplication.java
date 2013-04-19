package com.nano.lanshare.main;

import android.app.Application;

import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.utils.FileUtil;

public class LanshareApplication extends Application {
	private ImageWorker mImageWorker;

	@Override
	public void onCreate() {
		super.onCreate();
		mImageWorker = new ImageWorker(this);

		new Thread() {
			public void run() {
				FileUtil.createInbox(getApplicationContext());
			};
		}.start();
	}

	public ImageWorker getImageWorker() {
		return mImageWorker;
	}
}
