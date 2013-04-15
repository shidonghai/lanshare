/**
 * 
 */
package com.nano.lanshare.apps;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.nano.lanshare.utils.FileSizeUtil;

/**
 * @author King Bright
 * 
 */
public class AppLoader {
	public static final int TYPE_GAME = 1;
	public static final int TYPE_APP = 2;
	private static final String GAME_NAME_LIST = "appinfo.xml";

	private Context mContext;

	private PackageManager mPkgMgr;

	private List<String> mAllGameList;
	private List<AppInfo> mGames;
	private List<AppInfo> mApps;

	private HashMap<String, String> mLabelCache;

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
		mLabelCache = new HashMap<String, String>();
		// 注册广播接收器
		mAppChangedReceiver = new AppChangedReceiver();

	}

	public void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		mContext.registerReceiver(mAppChangedReceiver, filter);
	}

	public void onStop() {
		mContext.unregisterReceiver(mAppChangedReceiver);
	}

	public PackageManager getPackageManager() {
		return mPkgMgr;
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
		List<PackageInfo> list = mPkgMgr.getInstalledPackages(0);
		Collections.sort(list, new NameComparator(mPkgMgr, mLabelCache));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		AppInfo info;
		for (PackageInfo pkg : list) {

			info = new AppInfo();
			info.pkg = pkg.packageName;
			info.name = mLabelCache.get(info.pkg);
			info.path = pkg.applicationInfo.publicSourceDir;
			File file = new File(info.path);
			info.size = FileSizeUtil.formatFromByte(file.length());
			info.version = pkg.versionName;
			info.date = sdf.format(new Date(pkg.lastUpdateTime));
			// info.icon = pkg.applicationInfo.loadIcon(mPkgMgr);
			info.info = pkg.applicationInfo;
			if (mAllGameList.contains(pkg.packageName)) {
				info.type = TYPE_GAME;
				mGames.add(info);
			} else {
				info.type = TYPE_APP;
				mApps.add(info);
			}
		}
		mAllGameList.clear();
		mLabelCache.clear();
	}

	private void loadXml() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			InputStream is = mContext.getAssets().open(GAME_NAME_LIST);
			mAllGameList = new ArrayList<String>();
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
				String pkgname = new String(ch, start, length).trim();
				if (!TextUtils.isEmpty(pkgname)) {
					list.add(pkgname);
				}
			}
		}
	}

	private class NameComparator implements Comparator<PackageInfo> {
		private Collator mCollator;
		private HashMap<String, String> mLabelCache;
		private PackageManager mPkgMgr;

		NameComparator(PackageManager pkgMgr, HashMap<String, String> labelCache) {
			mCollator = Collator.getInstance();
			mLabelCache = labelCache;
			mPkgMgr = pkgMgr;
		}

		public final int compare(PackageInfo pkgA, PackageInfo pkgB) {
			String labelA;
			String labelB;
			if (mLabelCache.containsKey(pkgA.packageName)) {
				labelA = mLabelCache.get(pkgA.packageName);
			} else {
				labelA = pkgA.applicationInfo.loadLabel(mPkgMgr).toString();
				mLabelCache.put(pkgA.packageName, labelA);
			}

			if (mLabelCache.containsKey(pkgB.packageName)) {
				labelB = mLabelCache.get(pkgB.packageName);
			} else {
				labelB = pkgB.applicationInfo.loadLabel(mPkgMgr).toString();
				mLabelCache.put(pkgB.packageName, labelB);
			}
			return mCollator.compare(labelA, labelB);
		}
	}

	public static class AppInfo {
		public String name;
		public String size;
		public String path;
		public int type;
		public String version;
		public Drawable icon;
		public String pkg;
		public String date;

		public ApplicationInfo info;
	}

	public static ComponentName getComponentNameFromResolveInfo(ResolveInfo info) {
		if (info.activityInfo != null) {
			return new ComponentName(info.activityInfo.packageName,
					info.activityInfo.name);
		} else {
			return new ComponentName(info.serviceInfo.packageName,
					info.serviceInfo.name);
		}
	}
}
