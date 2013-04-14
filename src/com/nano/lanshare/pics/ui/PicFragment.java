package com.nano.lanshare.pics.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;

import com.nano.lanshare.components.BasicTabFragment;

public class PicFragment extends BasicTabFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
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
		mLeftGrid.setNumColumns(2);
		getGroup(LEFT).addView(mLeftGrid);
		mLeftAdapter = new PicAdapter(getActivity(), null, true,
				getLayoutInflater(null));
		mLeftGrid.setAdapter(mLeftAdapter);
		// configure right
		mRightGrid = new GridView(getActivity());
		mRightGrid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mRightGrid.setNumColumns(2);
		mRightAdapter = new PicAdapter(getActivity(), null, true,
				getLayoutInflater(null));
		getGroup(RIGHT).addView(mRightGrid);
		mRightGrid.setAdapter(mRightAdapter);

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
		// switch (loadId) {
		// case LEFT:
		// return new CursorLoader(getActivity(),
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mProjection,
		// null, null, null);
		// case RIGHT:
		// return new CursorLoader(getActivity(), mDataUrl, mProjection, null,
		// null, null);
		// }
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

}
