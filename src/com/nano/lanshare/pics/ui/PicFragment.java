package com.nano.lanshare.pics.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicTabFragment;
import com.nano.lanshare.components.LongClickListener;
import com.nano.lanshare.main.LanshareApplication;
import com.nano.lanshare.thumbnail.util.ImageWorker;

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
		Log.e("finish loading", "" + cursor.getCount());
		if (loader.getId() == LEFT) {
			mLeftAdapter.setContent(cursor);
		} else {
			mRightAdapter.setContent(cursor);
		}
		notifyFinishLoading();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == LEFT) {
			mLeftAdapter.setContent(null);
		} else {
			mRightAdapter.setContent(null);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
