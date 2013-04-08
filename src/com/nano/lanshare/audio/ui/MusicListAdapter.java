package com.nano.lanshare.audio.ui;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nano.lanshare.R;
import com.nano.lanshare.audio.bean.MusicInfo;

public class MusicListAdapter extends BaseAdapter implements
		OnItemClickListener {

	// public MusicListAdapter(Context context, Cursor c) {
	// super(context, c);
	// }

	private List<MusicInfo> mPlayList;

	private LayoutInflater mInflater;

	public MusicListAdapter(Context context, List<MusicInfo> list) {
		mPlayList = list;
		mInflater = LayoutInflater.from(context);
	}

	// Change the file size as KB/MB.
	private String readFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size
				/ Math.pow(1024, digitGroups))
				+ " " + units[digitGroups];
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public int getCount() {
		return mPlayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mPlayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (null == contentView) {
			holder = new ViewHolder();

			contentView = mInflater.inflate(R.layout.music_list_item, null);
			holder.artist = (TextView) contentView
					.findViewById(R.id.music_artist);
			holder.title = (TextView) contentView
					.findViewById(R.id.music_title);
			holder.size = (TextView) contentView.findViewById(R.id.music_size);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		holder.artist.setText(mPlayList.get(position).artist);
		holder.title.setText(mPlayList.get(position).title);
		holder.size.setText(readFileSize(mPlayList.get(position).size));

		return contentView;
	}

	private class ViewHolder {
		TextView title;
		TextView artist;
		TextView size;
	}
}
