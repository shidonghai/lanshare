package com.nano.lanshare.apps.ui;

import java.util.List;

import android.os.Message;

import com.nano.lanshare.apps.AppInfo;
import com.nano.lanshare.apps.AppListener;
import com.nano.lanshare.apps.AppLoader;
import com.nano.lanshare.components.BasicTabFragment;

public class AppFragment extends BasicTabFragment {
	private AppLoader mAppLoader;
	private AppListener mAppListener = new AppListener() {

		@Override
		public void onInstallApp(AppInfo info) {
		}

		@Override
		public void onUninstallApp(AppInfo info) {
		}

		@Override
		public void onLoading() {
		}

		@Override
		public void onLoaded(List<AppInfo> mGames, List<AppInfo> mApps) {
		}

	};

	@Override
	protected void init() {
		mAppLoader = new AppLoader(getActivity());
		mAppLoader.setAppListener(mAppListener);
	}

	@Override
	protected void onUpdateData(Message msg) {
	}

}
