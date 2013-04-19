package com.nano.lanshare.file.ui;

import android.os.Bundle;
import android.view.View;

import com.nano.lanshare.file.scan.FileScanner;
import com.nano.lanshare.file.scan.FileScanner.ScanMode;

public class InboxFileFragment extends FileListFragment {
	@Override
	public void onViewCreated(View view, Bundle bundle) {
		super.onViewCreated(view, bundle);
		mScanner = new FileScanner(ScanMode.INBOX);
		mScanner.startScanning(null, mListener);
	}
}
