package com.nano.lanshare.main;

import android.app.Application;

import com.nano.lanshare.thumbnail.util.ImageWorker;

public class LanshareApplication extends Application {
	private ImageWorker mImageWorker;

	@Override
	public void onCreate() {
		super.onCreate();
		mImageWorker = new ImageWorker(this);
	}

	public ImageWorker getImageWorker() {
		return mImageWorker;
	}
}
