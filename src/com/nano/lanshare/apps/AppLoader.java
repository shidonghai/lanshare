/**
 * 
 */
package com.nano.lanshare.apps;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author King Bright
 * 
 */
public class AppLoader {
	private static final int TYPE_GAME = 1;
	private static final int TYPE_APP = 2;
	private static final String GAME_NAME_LIST = "appinfo.xml";

	private Context mContext;

	private PackageManager mPkgMgr;

	private List<String> mAllGameList;
	private List<AppInfo> mGames;
	private List<AppInfo> mApps;

	private AppChangedReceiver mAppChangedReceiver;

	private AppListener mListener;

	public AppLoader(Context context) {
		init(context);
		mContext = context;
	}

	/**
	 * 初始化AppLoader
	 */
	private void init(Context context) {
		mPkgMgr = context.getPackageManager();
		mGames = new ArrayList<AppInfo>();
		mApps = new ArrayList<AppInfo>();

		// 注册广播接收器
		mAppChangedReceiver = new AppChangedReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);

		context.registerReceiver(mAppChangedReceiver, filter);

	}

	public void setAppListener(AppListener listener) {
		this.mListener = listener;
	}

	public void startLoading() {
		new Thread() {
			public void run() {
				if (mListener != null) {
					mListener.onLoading();
				}
				// 1.读取xml
				loadXml();
				// 2.对比包名，进行分类
				loadPkg();

				if (mListener != null) {
					mListener.onLoaded(mGames, mApps);
				}
			}
		}.start();
	}

	private void loadPkg() {
		List<PackageInfo> list = mPkgMgr
				.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		for (PackageInfo info : list) {
			AppInfo appInfo = new AppInfo();

			appInfo.name = info.applicationInfo.name;
			appInfo.position = info.applicationInfo.sourceDir;
			appInfo.pkg = info.packageName;
			appInfo.modify = info.lastUpdateTime + "";
			appInfo.version = info.versionName;
			// TODO 获取应用大小需要用到反射。。
			// appInfo.size=info.applicationInfo.
			// TODO 获取图标
			// appInfo.icon=
			if (list.contains(info.packageName)) {
				appInfo.type = TYPE_GAME;
				mGames.add(appInfo);
			} else {
				appInfo.type = TYPE_APP;
				mApps.add(appInfo);
			}
		}
		mAllGameList.clear();
	}

	private void loadXml() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			InputStream is = mContext.getAssets().open(GAME_NAME_LIST);
			parser.parse(is, new GameAppInfoHandler(mAllGameList));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 应用变更时收到通知更新ui. Intent.ACTION_PACKAGE_ADDED Intent.ACTION_PACKAGE_REPLACED
	 * Intent.ACTION_PACKAGE_REMOVED
	 * 
	 * @author King Bright
	 * 
	 */
	class AppChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
				if (mListener != null) {
					mListener.onInstallApp(null);
				}
			} else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
				if (mListener != null) {
					mListener.onInstallApp(null);
				}
			} else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
				if (mListener != null) {
					mListener.onUninstallApp(null);
				}
			} else {
				// something wrong happened
			}
		}
	}

	/**
	 * 解析xml
	 * 
	 * @author King Bright
	 * 
	 */
	class GameAppInfoHandler extends DefaultHandler {

		final String ITEM_TAG = "name";
		String tag;
		List<String> list;

		public GameAppInfoHandler(List<String> mAllGameList) {
			list = mAllGameList;
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			tag = localName;
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (tag.equals(ITEM_TAG)) {
				String pkgname = new String(ch, start, length);
				list.add(pkgname);
			}
		}
	}
}
