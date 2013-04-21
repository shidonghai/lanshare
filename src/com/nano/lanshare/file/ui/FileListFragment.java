package com.nano.lanshare.file.ui;

import java.io.File;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.operation.OperationDialog;
import com.nano.lanshare.components.operation.PopupMenuUtil;
import com.nano.lanshare.file.Extentions;
import com.nano.lanshare.file.FileItem;
import com.nano.lanshare.file.FileList;
import com.nano.lanshare.file.OpenIntent;
import com.nano.lanshare.file.scan.FileScanListener;
import com.nano.lanshare.file.scan.FileScanner;
import com.nano.lanshare.file.scan.FileScanner.ScanMode;
import com.nano.lanshare.utils.FileUtil;

public class FileListFragment extends BasicFileFragment {

	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	protected FileScanner mScanner;
	private int mLastPosition;

	// private Operations.OperationListener mRefreshOperation = new
	// Operations.OperationListener() {
	// public void onOperationDone() {
	// refresh();
	// }
	// };

	// private Operations.OperationListener mInvalidateOptionsMenuListener = new
	// OperationListener() {
	// @Override
	// public void onOperationDone() {
	// getActivity().invalidateOptionsMenu();
	// }
	// };
	private Handler mHandler;

	public FileListFragment(Handler handler) {
		mHandler = handler;
	}

	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case REFRESH_FINISH: {
			FileList list = mScanner.peek();
			if (list == null) {
				return;
			}
			boolean flag = list.getFileList().size() == 0;
			mList.setVisibility(flag ? View.GONE : View.VISIBLE);
			mEmptyView.setVisibility(flag ? View.VISIBLE : View.GONE);

			mAdapter.setFiles(list);
			mList.setSelection(mLastPosition);

			if (null != mHandler) {
				Message message = mHandler.obtainMessage();
				if (mScanner.getScanMode() == ScanMode.INBOX) {
					message.arg1 = LEFT;
					message.obj = String.format(
							getString(R.string.dm_tab_title_zapya),
							getFileSize(list.getFileList()));
				} else {
					message.arg1 = RIGHT;
					message.obj = String.format(
							getString(R.string.dm_tab_title_sdcard),
							getFileSize(list.getFileList()));
				}
				message.sendToTarget();
			}
			return;
		}
		case REFRESH: {
			refresh();
			return;
		}
		}
		super.handleMsg(msg);
	}

	private int getFileSize(List<FileItem> list) {
		if (list != null && list.size() > 0) {
			return FileListAdapter.FILE_TYPE_BACK == list.get(0).type ? list
					.size() - 1 : list.size();
		}
		return 0;
	}

	protected FileScanListener mListener = new FileScanListener() {

		public void onScanStart() {
			mLastPosition = mList.getFirstVisiblePosition();
			mScanner.saveLastPosition(mLastPosition);
		}

		@Override
		public void onScanSuccessed() {
			mLastPosition = mScanner.getLastPosition();
			getHandler().sendEmptyMessage(REFRESH_FINISH);
		}

		@Override
		public void onScanCanceled() {
		}

		@Override
		public void onScanFailed() {
		}

		@Override
		public void onScanRunning() {
		}

	};

	public void onViewCreated(View view, Bundle bundle) {
		super.onViewCreated(view, bundle);
		mScanner = new FileScanner(ScanMode.FILE);
		mScanner.startScanning(null, mListener);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.file_list_menu, menu);
	}

	private void refresh() {
		mScanner.startScanning(mScanner.getCurrentFile(), mListener);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FileItem fileItem = mAdapter.getItem(position);
		switch (fileItem.type) {
		case FileListAdapter.FILE_TYPE_BACK: {
			mScanner.handleBack(mListener);
			break;
		}
		case FileListAdapter.FILE_TYPE_FOLDER: {
			exitQueryMode(false);
			mScanner.startScanning(fileItem.file, mListener);
			break;
		}
		default: {
			showDialog(fileItem, view);
			break;
		}
		}
	}

	private void showDialog(final FileItem item, View view) {
		final OperationDialog operationDialog = new OperationDialog(
				getActivity());

		operationDialog.setContent(PopupMenuUtil.FILE_POPUP_IAMGES,
				getDialogItemNames(item.type),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case PopupMenuUtil.MENU_TRANSPORT:

							break;
						case PopupMenuUtil.MENU_ACTION:
							openAction(item.file);
							break;
						case PopupMenuUtil.MENU_PROPARTY:
							FileUtil.showPropertyDialog(getActivity(),
									item.file.getAbsolutePath());
							break;
						case PopupMenuUtil.MENU_OPERATION:
							FileUtil.showFileOperationDialog(getActivity(),
									item.file.getAbsolutePath());
							break;
						default:
							break;
						}
					}
				});

		operationDialog.showAsDropDown(view);
	}

	private void openAction(File file) {
		String type = Extentions.getIntance(getActivity()).getType(file);
		String mimetype = Extentions.getIntance(getActivity())
				.getMimeType(type);
		Intent intent = OpenIntent.get(type, mimetype, file);
		getActivity().startActivity(Intent.createChooser(intent, ""));
	}

	private int[] getDialogItemNames(int type) {
		int[] names = null;
		switch (type) {
		case FileListAdapter.FILE_TYPE_IMAGE:
			names = PopupMenuUtil.IMAGE_POPUP_TEXT;
			break;
		case FileListAdapter.FILE_TYPE_AUDIO:
		case FileListAdapter.FILE_TYPE_VIDEO:
			names = PopupMenuUtil.AUDIO_POPUP_TEXT;
			break;
		default:
			names = PopupMenuUtil.FILE_POPUP_TEXT;
			break;
		}

		return names;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return true;
	}

	@Override
	public boolean onBackPressed() {
		return mScanner.handleBack(mListener);
	}

	@Override
	protected void setMenuItemVisibility(Menu menu, boolean multiSelectionMode) {

	}

}
