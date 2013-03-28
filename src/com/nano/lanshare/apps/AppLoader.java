/**
 * 
 */
package com.nano.lanshare.apps;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author King Bright
 * 
 */
public class AppLoader {

	private Context mContext;

	private PackageManager mPkgMgr;

	public AppLoader(Context context) {
		mPkgMgr = context.getPackageManager();
	}

	public void startLoading() {
		// 1.读取xml文件，读出所有游戏的包名

		// 2.读出全部app信息，并按照类别分类
		List<PackageInfo> list = mPkgMgr
				.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		for (PackageInfo info : list) {
			// info.
		}
	}

	public void setAppLoadListener(AppListener listener) {

	}

	/**
	 * 当有任何app信息发生变化时，更新数据，刷新ui
	 * 
	 * @author King Bright
	 * 
	 */
	class AppChangedReceiver extends BroadcastReceiver {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

		}

	}
}
