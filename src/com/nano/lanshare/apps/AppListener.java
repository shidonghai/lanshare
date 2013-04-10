/**
 * 
 */
package com.nano.lanshare.apps;

import java.util.List;

import android.content.pm.PackageInfo;

/**
 * @author King Bright
 * 
 */
public interface AppListener {
	public void onInstallApp(PackageInfo info);

	public void onUninstallApp(PackageInfo info);

	public void onLoading();

	public void onLoaded(List<PackageInfo> mGames, List<PackageInfo> mApps);
}
