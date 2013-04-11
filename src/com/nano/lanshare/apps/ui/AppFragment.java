package com.nano.lanshare.apps.ui;

import java.util.List;

import android.content.pm.PackageInfo;
import android.os.Message;

import com.nano.lanshare.apps.AppListener;
import com.nano.lanshare.apps.AppLoader;
import com.nano.lanshare.components.BasicTabFragment;

public class AppFragment extends BasicTabFragment {
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

		setAdapter(LEFT, new AppAdapter(getLayoutInflater(null)));
		setAdapter(RIGHT, new AppAdapter(getLayoutInflater(null)));

		getGridView(LEFT).setNumColumns(4);
		getGridView(RIGHT).setNumColumns(4);
	}

	@Override
	public void onResume() {
		super.onResume();
		mAppLoader.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		mAppLoader.onStop();
	}

	@Override
	protected void onUpdateData(Message msg) {
		getStore(msg.arg1).setContent(msg.obj);
	}

}
