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
		// 1.��ȡxml�ļ�������������Ϸ�İ���

		// 2.����ȫ��app��Ϣ��������������
		List<PackageInfo> list = mPkgMgr
				.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		for (PackageInfo info : list) {
			// info.
		}
	}

	public void setAppLoadListener(AppListener listener) {

	}

	/**
	 * �����κ�app��Ϣ�����仯ʱ���������ݣ�ˢ��ui
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
