package com.nano.lanshare.file.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.nano.lanshare.file.scan.FileScanner;
import com.nano.lanshare.file.scan.FileScanner.ScanMode;

public class InboxFileFragment extends FileListFragment {
	public InboxFileFragment(Handler handler) {
		super(handler);
	}

	public InboxFileFragment() {
	}

	@Override
	public void onViewCreated(View view, Bundle bundle) {
		super.onViewCreated(view, bundle);
		mScanner = new FileScanner(ScanMode.INBOX);
		mScanner.startScanning(null, mListener);
		mAdapter.setScanMode(ScanMode.INBOX);
	}
}
