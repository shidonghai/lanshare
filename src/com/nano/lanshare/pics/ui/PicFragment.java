package com.nano.lanshare.pics.ui;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.components.LongClickListener;
import com.nano.lanshare.components.operation.OperationDialog;
import com.nano.lanshare.components.operation.PopupMenuUtil;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.utils.FileUtil;

public class PicFragment extends BasicTabFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {
	GridView mLeftGrid;
	GridView mRightGrid;
	PicAdapter mLeftAdapter;
	PicAdapter mRightAdapter;

	@Override
	protected void init() {
		// configure left
		mLeftGrid = new GridView(getActivity());
		mLeftGrid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mLeftGrid.setNumColumns(3);
		mLeftGrid.setVerticalSpacing(14);
		mLeftGrid.setHorizontalSpacing(14);
		getGroup(LEFT).addView(mLeftGrid);
		mLeftAdapter = new PicAdapter(getActivity(), null, true,
				getLayoutInflater(null));
		mLeftGrid.setAdapter(mLeftAdapter);
		// configure right
		mRightGrid = new GridView(getActivity());
		mRightGrid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mRightGrid.setNumColumns(3);
		mRightGrid.setVerticalSpacing(14);
		mRightGrid.setHorizontalSpacing(14);
		mRightAdapter = new PicAdapter(getActivity(), null, true,
				getLayoutInflater(null));
		getGroup(RIGHT).addView(mRightGrid);
		mRightGrid.setAdapter(mRightAdapter);
		// set title
		setTitle(LEFT, getString(R.string.dm_tab_title_camera, 0));
		setTitle(RIGHT, getString(R.string.dm_tab_title_photos, 0));
		// set listeners
		ImageWorker worker = ((LanshareApplication) getActivity()
				.getApplication()).getImageWorker();
		mLeftGrid.setOnScrollListener(worker.getScrollerListener());
		mRightGrid.setOnScrollListener(worker.getScrollerListener());
		LongClickListener mLongClickListener = new LongClickListener(
				getActivity(), R.id.icon);
		mLeftGrid.setOnItemLongClickListener(mLongClickListener);
		mRightGrid.setOnItemLongClickListener(mLongClickListener);
		mLeftGrid.setOnItemClickListener(this);
		mRightGrid.setOnItemClickListener(this);
		// start loading data
		notifyStartLoading();
		getLoaderManager().initLoader(LEFT, null, this);
		getLoaderManager().initLoader(RIGHT, null, this);
	}

	@Override
	protected void onUpdateData(Message msg) {
		// getStore(msg.arg1).setContent(msg.obj);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loadId, Bundle bundle) {
		switch (loadId) {
		case LEFT:
			return new CursorLoader(getActivity(),
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
							MediaStore.Images.Media._ID,
							MediaStore.Images.Media.DATA,
							MediaStore.Images.Media.DATE_MODIFIED,
							MediaStore.Images.Media.DISPLAY_NAME,
							MediaStore.Images.Media.SIZE },
					MediaStore.Images.Media.BUCKET_DISPLAY_NAME
							+ " = 'Camera' AND " + MediaStore.Images.Media.SIZE
							+ " > 20000", null, null);
		case RIGHT:
			return new CursorLoader(getActivity(),
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
							MediaStore.Images.Media._ID,
							MediaStore.Images.Media.DATA,
							MediaStore.Images.Media.DATE_MODIFIED,
							MediaStore.Images.Media.DISPLAY_NAME,
							MediaStore.Images.Media.SIZE },
					MediaStore.Images.Media.BUCKET_DISPLAY_NAME
							+ " != 'Camera' AND "
							+ MediaStore.Images.Media.SIZE + " > 20000", null,
					null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor == null) {
			return;
		}
		if (loader.getId() == LEFT) {
			mLeftAdapter.setContent(cursor);
			setTitle(LEFT,
					getString(R.string.dm_tab_title_camera, cursor.getCount()));
		} else {
			mRightAdapter.setContent(cursor);
			setTitle(RIGHT,
					getString(R.string.dm_tab_title_photos, cursor.getCount()));
		}
		notifyFinishLoading();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == LEFT) {
			mLeftAdapter.setContent(null);
			setTitle(LEFT, getString(R.string.dm_tab_title_camera, 0));
		} else {
			mRightAdapter.setContent(null);
			setTitle(RIGHT, getString(R.string.dm_tab_title_photos, 0));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
			long arg3) {
		final OperationDialog operationDialog = new OperationDialog(
				getActivity());

		operationDialog.setContent(PopupMenuUtil.FILE_POPUP_IAMGES,
				PopupMenuUtil.IMAGE_POPUP_TEXT,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case PopupMenuUtil.MENU_TRANSPORT:
							String path = String.valueOf(arg1.getTag());
							if (!TextUtils.isEmpty(path)) {
								startTransfer(path);
							}
							break;
						case PopupMenuUtil.MENU_ACTION:
							break;
						case PopupMenuUtil.MENU_PROPARTY:
							FileUtil.showPropertyDialog(getActivity(),
									String.valueOf(arg1.getTag()));
							break;
						case PopupMenuUtil.MENU_OPERATION:
							FileUtil.showFileOperationDialog(getActivity(),
									String.valueOf(arg1.getTag()));
							break;
						default:
							break;
						}
					}
				});

		operationDialog.showAsDropDown(arg1);
	}

}
