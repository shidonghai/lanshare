package com.nano.lanshare.file.ui;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;

import com.nano.lanshare.components.operation.FileOperationContentView;
import com.nano.lanshare.components.operation.OperationDialog;
import com.nano.lanshare.file.FileItem;
import com.nano.lanshare.file.FileList;
import com.nano.lanshare.file.scan.FileScanListener;
import com.nano.lanshare.file.scan.FileScanner;

public class FileListFragment extends BasicFragment {
	private FileScanner mScanner;
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
			// if
			// (ClipBoard.getInstance().needRefresh(mScanner.getCurrentFile()))
			// {
			// Log.e("refresh", "position " + mLastPosition);
			// getHandler().sendEmptyMessage(REFRESH);
			// }
			return;
		}
		case REFRESH: {
			refresh();
			return;
		}
		}
		super.handleMsg(msg);
	}

	private FileScanListener mListener = new FileScanListener() {

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
		mScanner = FileScanner.getInstance();
		mScanner.startScanning(null, mListener);
		Log.d("zxh", "FileListFragment onViewCreated");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.file_list_menu, menu);
	}

	private void refresh() {
		mScanner.startScanning(mScanner.getCurrentFile(), mListener);
	}

	// private List<File> getSelectedFiles() {
	// List<File> list = mAdapter.getFiles().getFileList();
	// List<File> selectedFiles = new ArrayList<File>();
	// for (int index : mAdapter.getSelected()) {
	// selectedFiles.add(list.get(index));
	// }
	// mAdapter.getSelected().clear();
	// return selectedFiles;
	// }

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

	private void showDialog(FileItem item, View view) {
		final OperationDialog operationDialog = new OperationDialog(
				getActivity());

		FileOperationContentView fileOperationContentView = new FileOperationContentView(
				OperationDialog.TYPE_FILE, item.file.getAbsolutePath(),
				operationDialog);

		fileOperationContentView
				.setActionClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						operationDialog.dismiss();
					}
				});

		operationDialog.setContentView(fileOperationContentView
				.createContentView(getActivity()));
		operationDialog.showAsDropDown(view);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// FileItem file = mAdapter.getItem(position);
		// PopupMenu menu = getPopupMenu(view, getHandler(), file,
		// mRefreshOperation);
		// menu.getMenu().findItem(R.id.menu_remove_favourite).setVisible(false);
		// menu.getMenu().findItem(R.id.menu_share).setVisible(file.isFile() &
		// file.canRead());
		// menu.show();
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
