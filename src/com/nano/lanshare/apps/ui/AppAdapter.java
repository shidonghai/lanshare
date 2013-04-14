package com.nano.lanshare.apps.ui;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.apps.AppLoader.AppInfo;
import com.nano.lanshare.components.BasicContentStore;
import com.nano.lanshare.thumbnail.util.ImageWorker;
import com.nano.lanshare.thumbnail.util.ImageWorker.LoadMethod;

public class AppAdapter extends BaseAdapter implements BasicContentStore {
	private List<AppInfo> mList;
	private LayoutInflater mLayoutInflater;
	private ImageWorker mWorker;
	private Bitmap mLoadingBitmap;
	private Context mContext;

	private LoadMethod mAppIconLoader = new LoadMethod() {

		@Override
		public Bitmap processBitmap(Object obj, Context context) {
			ApplicationInfo info = (ApplicationInfo) obj;
			Drawable drawable = info.loadIcon(context.getPackageManager());
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}

	};

	public AppAdapter(Context context, ImageWorker worker,
			LayoutInflater layoutInflater) {
		mContext = context;
		mLayoutInflater = layoutInflater;
		mWorker = worker;
		mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.app_default_icon);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.app_item, null);
		}
		AppInfo info = mList.get(position);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		mWorker.loadImage(info.info, icon, mLoadingBitmap, mAppIconLoader);
		((TextView) view.findViewById(R.id.title)).setText(info.name);
		((TextView) view.findViewById(R.id.size)).setText(info.size);
		view.setDrawingCacheEnabled(true);
		return view;
	}

	@Override
	public void setContent(Object o) {
		mList = (List<AppInfo>) o;
		notifyDataSetChanged();
	}

	@Override
	public Object getContent() {
		return null;
	}
}
