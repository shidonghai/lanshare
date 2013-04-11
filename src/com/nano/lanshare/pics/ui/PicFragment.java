package com.nano.lanshare.pics.ui;

import java.util.List;

import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.nano.lanshare.apps.AppListener;
import com.nano.lanshare.apps.AppLoader;
import com.nano.lanshare.components.BasicTabFragment;

public class PicFragment extends BasicTabFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private AppLoader mAppLoader;
	private AppListener mAppListener = new AppListener() {

		@Override
		public void onInstallApp(PackageInfo info) {
		}

		@Override
		public void onUninstallApp(PackageInfo info) {
		}

		@Override
		public void onLoading() {
		}

		@Override
		public void onLoaded(List<PackageInfo> mGames, List<PackageInfo> mApps) {
			Message msg1 = getHandler().obtainMessage();
			msg1.arg1 = LEFT;
			msg1.what = UPDATE_UI;
			msg1.obj = mGames;
			msg1.sendToTarget();

			Message msg2 = getHandler().obtainMessage();
			msg2.arg1 = RIGHT;
			msg2.what = UPDATE_UI;
			msg2.obj = mApps;
			msg2.sendToTarget();
		}

	};

	@Override
	protected void init() {
		mAppLoader = new AppLoader(getActivity());
		mAppLoader.setAppListener(mAppListener);
		mAppLoader.startLoading();

		setAdapter(LEFT, new PicAdapter(getActivity(), null, true,
				getLayoutInflater(null)));
		setAdapter(RIGHT, new PicAdapter(getActivity(), null, true,
				getLayoutInflater(null)));

		getGridView(LEFT).setNumColumns(4);
		getGridView(RIGHT).setNumColumns(4);

		getLoaderManager().initLoader(LEFT, null, this);
		getLoaderManager().initLoader(RIGHT, null, this);
	}

	@Override
	protected void onUpdateData(Message msg) {
		getStore(msg.arg1).setContent(msg.obj);
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
