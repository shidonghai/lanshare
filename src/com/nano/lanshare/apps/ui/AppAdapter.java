package com.nano.lanshare.apps.ui;

import java.io.File;
import java.util.List;

import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.components.BasicContentStore;
import com.nano.lanshare.util.FileSizeUtil;

public class AppAdapter extends BaseAdapter implements BasicContentStore {
	private List<PackageInfo> mList;
	private LayoutInflater mLayoutInflater;

	public AppAdapter(LayoutInflater layoutInflater) {
		mLayoutInflater = layoutInflater;
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
		PackageInfo info = mList.get(position);
		// ((ImageView) view.findViewById(R.id.icon))
		// .setImageResource(info.applicationInfo.icon);
		// ((TextView) view.findViewById(R.id.title))
		// .setText(info.applicationInfo.labelRes);
		File file = new File(info.applicationInfo.publicSourceDir);
		((TextView) view.findViewById(R.id.size)).setText(FileSizeUtil
				.formatFromByte(file.length()));
		return view;
	}

	@Override
	public void setContent(Object o) {
		mList = (List<PackageInfo>) o;
		notifyDataSetChanged();
	}

	@Override
	public Object getContent() {
		return null;
	}
}
