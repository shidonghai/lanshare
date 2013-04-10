/**
 * 
 */
package com.nano.lanshare.apps;

import java.util.List;

/**
 * @author King Bright
 * 
 */
public interface AppListener {
	public void onInstallApp();

	public void onUninstallApp();

	public void onLoading();

	public void onLoaded(List<AppInfo> mGames, List<AppInfo> mApps);
}
