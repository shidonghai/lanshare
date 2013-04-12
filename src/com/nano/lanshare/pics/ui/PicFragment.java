package com.nano.lanshare.pics.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.nano.lanshare.components.BasicTabFragment;

public class PicFragment extends BasicTabFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	@Override
	protected void init() {

//		setAdapter(LEFT, new PicAdapter(getActivity(), null, true,
//				getLayoutInflater(null)));
//		setAdapter(RIGHT, new PicAdapter(getActivity(), null, true,
//				getLayoutInflater(null)));
//
//		getGridView(LEFT).setNumColumns(4);
//		getGridView(RIGHT).setNumColumns(4);

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
		// return new CursorLoader(getActivity(), mDataUrl, mProjection, null,
		// null, null);
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
