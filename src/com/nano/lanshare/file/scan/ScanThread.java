package com.nano.lanshare.file.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.nano.lanshare.file.FileItem;
import com.nano.lanshare.file.FileList;
import com.nano.lanshare.file.ui.FileListAdapter;

public class ScanThread implements Runnable {

	private File mFolder;
	private FileScanListener mListener;
	private FileScanner mScanner;
	private ScanState mState;
	private ScanOption mOption;
	private boolean mRefresh;

	private Thread mThread;

	public enum ScanState {
		RUNNING, STOP
	};

	public ScanThread(File base, ScanOption option) {
		this.mFolder = base;
		this.mOption = option;
		this.mState = ScanState.STOP;
	}

	@Override
	public void run() {
		mState = ScanState.RUNNING;
		FileList fileList = null;
		try {
			File[] files = mFolder.listFiles(mOption.filter);
			Arrays.sort(files, mOption.sorter);
			// List<File> list = Arrays.asList(files);

			fileList = new FileList(getFileItemList(files));
		} catch (Exception e) {
			Log.e("ScanThread", e.toString());
		}
		if (fileList == null || fileList.getFileList() == null) {
			mListener.onScanFailed();
			mState = ScanState.STOP;
			return;
		}

		// if (null != mFolder.getParentFile()) {
		// List<File> listFile = new ArrayList<File>();
		// listFile.add(null);
		// listFile.addAll(fileList.getFileList());
		// fileList.setFileList(listFile);
		// }

		if (mRefresh && !mScanner.isEmpty()) {
			FileList list = mScanner.pop();
			fileList.setPosition(list.getPosition());
		}

		mScanner.push(fileList);
		mListener.onScanSuccessed();

		mState = ScanState.STOP;
	}

	private List<FileItem> getFileItemList(File[] files) {
		List<FileItem> list = null;
		if (null != files) {
			list = new ArrayList<FileItem>();
			FileItem item = null;

			// Add back item.
			if (null != mFolder.getParentFile()) {
				item = new FileItem();
				item.type = FileListAdapter.FILE_TYPE_BACK;
				list.add(item);
			}

			for (int i = 0; i < files.length; i++) {
				item = new FileItem();
				item.file = files[i];
				item.type = getFileType(files[i]);
				list.add(item);
			}
		}
		return list;
	}

	private int getFileType(File file) {
		if (file.isDirectory()) {
			return FileListAdapter.FILE_TYPE_FOLDER;
		} else {
			String type = getMimeType(file.getAbsolutePath());
			if (type != null && type.startsWith("image")) {
				return FileListAdapter.FILE_TYPE_IMAGE;
			}

			return FileListAdapter.FILE_TYPE_FILE;
		}
	}

	public void startScanning(FileScanListener listener,
			FileScanner fileScanner, boolean refresh) {
		mListener = listener;
		mScanner = fileScanner;
		mRefresh = refresh;

		mThread = new Thread(this);
		mListener.onScanStart();
		mThread.start();
	}

	public ScanState getState() {
		return mState;
	}

	public void stop() {
		mThread.interrupt();
	}

	// url = file path or whatever suitable URL you want.
	public String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}

		return type;
	}
}
