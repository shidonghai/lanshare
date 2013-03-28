/**
 * 
 */
package com.nano.lanshare.apps;

/**
 * @author King Bright
 * 
 */
public interface AppListener {
	public void onInstallApp();

	public void onUninstallApp();

	public void onLoading();

	public void onLoaded();
}
