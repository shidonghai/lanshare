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
		}

	};

	@Override
	protected void init() {
		mAppLoader = new AppLoader(getActivity());
		mAppLoader.setAppListener(mAppListener);
		mAppLoader.startLoading();
	}

	@Override
	protected void onUpdateData(Message msg) {
	}

}
